package co.edu.unipiloto.pgc.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.service.OdometerBoundService;

public class OdometerActivity extends BaseActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2002;

    private User user;
    private DeliveryDAO deliveryDAO;
    private ArrayList<Delivery> deliveries;

    private Spinner spinnerEntrega;
    private TextView textLocation;
    private TextView textDistance;
    private Button btnStart;
    private Button btnStop;
    private Button btnShowRecords;

    private OdometerBoundService odometerService;
    private boolean bound;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final DecimalFormat distanceFormat = new DecimalFormat("0.0");

    private final Runnable uiUpdater = new Runnable() {
        @Override
        public void run() {
            if (bound && odometerService != null) {
                updateStatus();
            }
            uiHandler.postDelayed(this, 1000);
        }
    };

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OdometerBoundService.LocalBinder binder = (OdometerBoundService.LocalBinder) service;
            odometerService = binder.getService();
            bound = true;
            updateStatus();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            odometerService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odometer);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        user = (User) getIntent().getSerializableExtra("user");
        deliveryDAO = new DeliveryDAO(this);

        spinnerEntrega = findViewById(R.id.spinnerEntrega);
        textLocation = findViewById(R.id.textLocation);
        textDistance = findViewById(R.id.textDistance);
        btnStart = findViewById(R.id.btnStartTracking);
        btnStop = findViewById(R.id.btnStopTracking);
        btnShowRecords = findViewById(R.id.btnShowRecords);
        btnStart.setEnabled(false);
        btnStop.setEnabled(false);

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);

        setupBottomNavigation(user);
        loadDeliveries();

        btnStart.setOnClickListener(v -> onStartTracking());
        btnStop.setOnClickListener(v -> onStopTracking());
        btnShowRecords.setOnClickListener(v -> openReport());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, OdometerBoundService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        uiHandler.post(uiUpdater);
    }

    @Override
    protected void onStop() {
        super.onStop();
        uiHandler.removeCallbacks(uiUpdater);
        if (bound) {
            unbindService(serviceConnection);
            bound = false;
        }
    }

    private void loadDeliveries() {
        deliveryDAO.getDeliveriesByState(user, "PENDIENTE", new DeliveryDAO.ApiCallback<ArrayList<Delivery>>() {
            @Override
            public void onSuccess(ArrayList<Delivery> result) {
                runOnUiThread(() -> {
                    deliveries = result;
                    ArrayList<String> labels = new ArrayList<>();
                    if (deliveries != null) {
                        for (Delivery delivery : deliveries) {
                            labels.add("Entrega #" + delivery.getId());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(OdometerActivity.this,
                            android.R.layout.simple_spinner_item, labels);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEntrega.setAdapter(adapter);
                    boolean hasDeliveries = deliveries != null && !deliveries.isEmpty();
                    btnStart.setEnabled(hasDeliveries);
                    btnStop.setEnabled(false);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(OdometerActivity.this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void onStartTracking() {
        if (!ensureLocationPermission()) {
            return;
        }
        if (!bound || odometerService == null) {
            Toast.makeText(this, "Servicio no disponible", Toast.LENGTH_SHORT).show();
            return;
        }
        int deliveryId = getSelectedDeliveryId();
        if (deliveryId <= 0) {
            Toast.makeText(this, "Seleccione una entrega", Toast.LENGTH_SHORT).show();
            return;
        }
        odometerService.configure(deliveryId);
        odometerService.startTracking();
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        Toast.makeText(this, "Seguimiento iniciado", Toast.LENGTH_SHORT).show();
    }

    private void onStopTracking() {
        if (bound && odometerService != null) {
            odometerService.stopTracking();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            Toast.makeText(this, "Seguimiento detenido", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStatus() {
        boolean tracking = odometerService.isTracking();
        boolean hasDeliveries = deliveries != null && !deliveries.isEmpty();
        btnStart.setEnabled(hasDeliveries && !tracking);
        btnStop.setEnabled(tracking);

        double total = odometerService.getTotalDistanceMeters();
        textDistance.setText(getString(R.string.odometer_distance_value, distanceFormat.format(total)));

        Location location = odometerService.getLastLocation();
        if (location != null) {
            String coords = location.getLatitude() + ", " + location.getLongitude();
            textLocation.setText(getString(R.string.odometer_location_value, coords));
        } else {
            textLocation.setText(getString(R.string.odometer_location_value, "-"));
        }
    }

    private int getSelectedDeliveryId() {
        if (deliveries == null || deliveries.isEmpty()) {
            return -1;
        }
        int index = spinnerEntrega.getSelectedItemPosition();
        if (index < 0 || index >= deliveries.size()) {
            return -1;
        }
        return deliveries.get(index).getId();
    }

    private void openReport() {
        Intent intent = new Intent(this, OdometerReportActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private boolean ensureLocationPermission() {
        boolean fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (fine || coarse) {
            return true;
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
