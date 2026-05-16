package co.edu.unipiloto.pgc.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RolDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;

public class SignUpActivity extends AppCompatActivity {

    private ArrayList<User> users;
    private ArrayList<Rol> roles;
    private final ArrayList<Rol> rolesDisponibles = new ArrayList<>();
    private UserDAO userDAO;
    private RolDAO rolDAO;
    private Spinner spinnerRoles;
    private double latitud = 0.0;
    private double longitud = 0.0;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Handler timeoutHandler = new Handler(Looper.getMainLooper());
    private static final int LOCATION_TIMEOUT_MS = 15000;
    private Button btnUbicacion;
    private TextView txtDireccion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_blue));
        userDAO = new UserDAO(this);
        rolDAO = new RolDAO(this);
        roles = new ArrayList<>();
        spinnerRoles = findViewById(R.id.roles);
        rolDAO.getAllRoles(new RolDAO.RolesCallbacK() {
            @Override
            public void onSuccess(ArrayList<Rol> roles) {
                SignUpActivity.this.roles = roles;

                ArrayList<String> rolesTexto = new ArrayList<>();
                rolesDisponibles.clear();

                for (Rol rol : roles) {
                    if (!rol.getNombre().contains("Administrador")) {
                        rolesDisponibles.add(rol);
                        rolesTexto.add(rol.getNombre());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        SignUpActivity.this,
                        R.layout.spinner_item,
                        rolesTexto
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRoles.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(SignUpActivity.this,
                                message,
                                Toast.LENGTH_SHORT).show()
                );
            }
        });


        txtDireccion = findViewById(R.id.direccion);
        btnUbicacion = findViewById(R.id.btnUbicacion);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnUbicacion.setOnClickListener(v -> obtenerUbicacion());

        EditText fecha = findViewById(R.id.fechaNacimiento);

        fecha.setOnClickListener(v -> {

            Calendar calendario = Calendar.getInstance();

            int year = calendario.get(Calendar.YEAR);
            int month = calendario.get(Calendar.MONTH);
            int day = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        @SuppressLint("DefaultLocale") String fechaBD = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);

                        @SuppressLint("DefaultLocale") String fechaUI = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear);

                        fecha.setText(fechaUI);
                        fecha.setTag(fechaBD);

                    }, year, month, day);
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, -18);

            datePicker.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePicker.show();
        });

        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(this::onSignUp);
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        btnUbicacion.setEnabled(false);
        Toast.makeText(this, "Obteniendo ubicación...", Toast.LENGTH_SHORT).show();

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null && esUbicacionReciente(location)) {
                usarUbicacion(location);
            } else {
                solicitarNuevaUbicacion();
            }
        }).addOnFailureListener(e -> solicitarNuevaUbicacion());
    }

    private void solicitarNuevaUbicacion() {
        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdates(1)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                timeoutHandler.removeCallbacksAndMessages(null);
                Location location = result.getLastLocation();
                if (location != null) {
                    usarUbicacion(location);
                }
                limpiarLocationCallback();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper());

            timeoutHandler.postDelayed(() -> {
                limpiarLocationCallback();
                btnUbicacion.setEnabled(true);
                Toast.makeText(this,
                        "No se pudo obtener la ubicación. Verifica el GPS.",
                        Toast.LENGTH_LONG).show();
            }, LOCATION_TIMEOUT_MS);
        }
    }

    private void usarUbicacion(Location location) {
        latitud = location.getLatitude();
        longitud = location.getLongitude();
        obtenerDireccion(latitud, longitud, txtDireccion);
        btnUbicacion.setEnabled(true);
        Toast.makeText(this, "Ubicación obtenida correctamente", Toast.LENGTH_SHORT).show();
    }

    private void limpiarLocationCallback() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            locationCallback = null;
        }
    }

    private boolean esUbicacionReciente(Location location) {
        return (SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos())
                < TimeUnit.MINUTES.toNanos(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeoutHandler.removeCallbacksAndMessages(null);
        limpiarLocationCallback();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == 1) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this,
                        "Permiso concedido. Presione nuevamente obtener ubicación",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerDireccion(double lat, double lng, TextView txtDireccion) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> direcciones = geocoder.getFromLocation(lat, lng, 1);

            if (direcciones != null && !direcciones.isEmpty()) {
                Address direccion = direcciones.get(0);

                String direccionTexto = direccion.getAddressLine(0);

                txtDireccion.setText(direccionTexto);
            }

        } catch (IOException e) {
            e.printStackTrace();
            txtDireccion.setText("Error obteniendo dirección");
        }
    }

    public void onSignUp(View view) {

        EditText nombre = findViewById(R.id.textoNombreCompleto);
        EditText usuario = findViewById(R.id.textoUsuario);
        EditText email = findViewById(R.id.textoEmail);
        EditText password = findViewById(R.id.textoContrasenia);
        EditText confirmPassword = findViewById(R.id.textoConfirmarContrasenia);

        TextView direccion = findViewById(R.id.direccion);
        Spinner spinnerRoles = findViewById(R.id.roles);
        RadioGroup radioGenero = findViewById(R.id.radioGenero);
        EditText fecha = findViewById(R.id.fechaNacimiento);

        if (nombre.getText().toString().isEmpty() ||
                usuario.getText().toString().isEmpty() ||
                email.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                confirmPassword.getText().toString().isEmpty() ||
                direccion.getText().toString().isEmpty() ||
                fecha.getText().toString().isEmpty()) {

            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        userDAO.verificarUsername(usuario.getText().toString(), new UserDAO.UsernameCallback() {
            @Override
            public void onSuccess(boolean exists) {

                if (exists) {
                    runOnUiThread(() -> {
                        Toast.makeText(SignUpActivity.this,
                                "Username ya existente",
                                Toast.LENGTH_SHORT).show();
                    });
                    return;
                }


                int selectedId = radioGenero.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(SignUpActivity.this, "Seleccione género", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (latitud == 0.0 || longitud == 0.0) {
                    Toast.makeText(SignUpActivity.this, "Debe obtener la ubicación", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rolesDisponibles.isEmpty()) {
                    Toast.makeText(SignUpActivity.this,
                            "No hay roles disponibles",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int rolPosition = spinnerRoles.getSelectedItemPosition();
                if (rolPosition < 0 || rolPosition >= rolesDisponibles.size()) {
                    Toast.makeText(SignUpActivity.this,
                            "Seleccione un rol valido",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedRadio = findViewById(selectedId);
                String genero = selectedRadio.getText().toString();
                Rol rolSeleccionado = rolesDisponibles.get(rolPosition);
                User user = new User();
                user.setNombreCompleto(nombre.getText().toString());
                user.setUsername(usuario.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                user.setGenero(genero);
                user.setDireccion(direccion.getText().toString());
                user.setLatitud(latitud);
                user.setLongitud(longitud);
                String fechaBD = (String) fecha.getTag();
                user.setFechaNacimiento(fechaBD);
                user.setRol(rolSeleccionado);

                userDAO.insertarUsuario(user, new UserDAO.RegisterCallback() {
                    @Override
                    public void onSuccess(User user) {
                        runOnUiThread(() -> {
                            Toast.makeText(SignUpActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show());
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(SignUpActivity.this,
                                message,
                                Toast.LENGTH_SHORT).show()
                );
            }
        });

    }
}
