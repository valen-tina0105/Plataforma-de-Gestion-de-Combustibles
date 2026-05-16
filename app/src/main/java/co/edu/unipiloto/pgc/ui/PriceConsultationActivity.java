package co.edu.unipiloto.pgc.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.StationDAO;
import co.edu.unipiloto.pgc.model.Station;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.StationsAdapter;

public class PriceConsultationActivity extends BaseActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private StationDAO stationDAO;
    private ArrayList<Station> stations;
    private StationsAdapter adapterEstaciones;
    private User user;
    private RecyclerView listaEstaciones;
    private LocationManager locationManager;
    private Button btnMostrarMapa;
    
    // Coordenadas por defecto (Bogotá) como respaldo
    private double currentLat = 4.6097; 
    private double currentLon = -74.0817;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_consultation);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        setupToolbar(findViewById(R.id.toolbar), user);

        listaEstaciones = findViewById(R.id.listaEstaciones);
        listaEstaciones.setLayoutManager(new LinearLayoutManager(this));

        btnMostrarMapa = findViewById(R.id.btnMostrarMapa);
        btnMostrarMapa.setOnClickListener(v -> openGoogleMapsForGasStations());

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        stationDAO = new StationDAO(this);

        // Carga inicial con ubicación por defecto
        updateStationsList(currentLat, currentLon);
        
        // Intentar obtener la ubicación actual real
        checkLocationPermissionAndGetLocation();
    }

    private void openGoogleMapsForGasStations() {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=estaciones+de+servicio");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        
        mapIntent.setPackage("com.google.android.apps.maps");

        try {
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Intent genericMapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                if (genericMapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(genericMapIntent);
                } else {
                    Toast.makeText(this, "No se encontró una aplicación de mapas instalada", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al abrir el mapa", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLocationPermissionAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fetchCurrentLocation();
        }
    }

    private void fetchCurrentLocation() {
        try {
            Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location bestLocation = null;
            if (locationGps != null && locationNetwork != null) {
                bestLocation = (locationGps.getAccuracy() < locationNetwork.getAccuracy()) ? locationGps : locationNetwork;
            } else {
                bestLocation = (locationGps != null) ? locationGps : locationNetwork;
            }

            if (bestLocation != null) {
                currentLat = bestLocation.getLatitude();
                currentLon = bestLocation.getLongitude();
                updateStationsList(currentLat, currentLon);
            }
            
            requestSingleUpdate();
            
        } catch (SecurityException e) {
            Log.e("LOCATION_ERROR", "Error al obtener ubicación", e);
        }
    }

    private void requestSingleUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    currentLat = location.getLatitude();
                    currentLon = location.getLongitude();
                    updateStationsList(currentLat, currentLon);
                }
                @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override public void onProviderEnabled(@NonNull String provider) {}
                @Override public void onProviderDisabled(@NonNull String provider) {}
            }, null);
        }
    }

    private void updateStationsList(double latUser, double lonUser) {
        stationDAO.getNearbyStations(latUser, lonUser, new StationDAO.StationsCallback() {
            @Override
            public void onSuccess(ArrayList<Station> stationsList) {
                stations = stationsList;
                adapterEstaciones = new StationsAdapter(PriceConsultationActivity.this, stations);
                listaEstaciones.setAdapter(adapterEstaciones);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(PriceConsultationActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
