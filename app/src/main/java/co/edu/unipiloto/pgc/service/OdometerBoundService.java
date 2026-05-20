package co.edu.unipiloto.pgc.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import co.edu.unipiloto.pgc.dao.OdometerLogDAO;

public class OdometerBoundService extends Service implements LocationListener {

    public static final int DEFAULT_PRECISION_METERS = 5;
    public static final int DEFAULT_UPDATE_SECONDS = 2;

    private final IBinder binder = new LocalBinder();
    private LocationManager locationManager;
    private OdometerLogDAO odometerLogDAO;

    private int precisionMeters = DEFAULT_PRECISION_METERS;
    private int updateSeconds = DEFAULT_UPDATE_SECONDS;
    private int deliveryId;
    private boolean tracking;
    private double totalDistanceMeters;
    private Location lastLocation;

    public class LocalBinder extends Binder {
        public OdometerBoundService getService() {
            return OdometerBoundService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        odometerLogDAO = new OdometerLogDAO(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void configure(int deliveryId) {
        this.deliveryId = deliveryId;
        this.precisionMeters = DEFAULT_PRECISION_METERS;
        this.updateSeconds = DEFAULT_UPDATE_SECONDS;
        totalDistanceMeters = odometerLogDAO.getTotalDistanceByDelivery(deliveryId);
    }

    public void startTracking() {
        if (tracking) {
            return;
        }
        tracking = true;
        lastLocation = null;
        requestLocationUpdates();
    }

    public void stopTracking() {
        tracking = false;
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public boolean isTracking() {
        return tracking;
    }

    public double getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    private void requestLocationUpdates() {
        if (locationManager == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        long minTimeMs = updateSeconds * 1000L;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeMs, precisionMeters, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeMs, precisionMeters, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (!tracking || location == null) {
            return;
        }
        if (lastLocation != null) {
            double delta = lastLocation.distanceTo(location);
            if (delta >= precisionMeters) {
                totalDistanceMeters += delta;
                odometerLogDAO.insertLog(deliveryId, System.currentTimeMillis(),
                        location.getLatitude(), location.getLongitude(), totalDistanceMeters);
            }
        } else {
            odometerLogDAO.insertLog(deliveryId, System.currentTimeMillis(),
                    location.getLatitude(), location.getLongitude(), totalDistanceMeters);
        }
        lastLocation = location;
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    @Override
    public void onDestroy() {
        stopTracking();
        super.onDestroy();
    }
}
