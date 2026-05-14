package co.edu.unipiloto.pgc.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

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

    private StationDAO stationDAO;
    private ArrayList<Station> stations;
    private StationsAdapter adapterEstaciones;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_consultation);
        
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        setupToolbar(findViewById(R.id.toolbar), user);

        RecyclerView listaEstaciones = findViewById(R.id.listaEstaciones);
        listaEstaciones.setLayoutManager(new LinearLayoutManager(this));

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            double latUser = location.getLatitude();
            double lonUser = location.getLongitude();

            stationDAO = new StationDAO(this);
            stations = stationDAO.getAllStations(latUser, lonUser);

            adapterEstaciones = new StationsAdapter(this, stations);
            listaEstaciones.setAdapter(adapterEstaciones);

        } else {
            Log.d("DEBUG", "No se pudo obtener ubicación");
        }
    }
}
