package co.edu.unipiloto.pgc.dao;

import android.content.Context;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.Station;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationDAO {
    private ApiService apiService;

    public interface StationsCallback {
        void onSuccess(ArrayList<Station> stations);
        void onError(String message);
    }

    public StationDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getNearbyStations(double lat, double lon, StationsCallback callback) {
        Call<ArrayList<Station>> call = apiService.getNearbyStations(lat, lon);
        call.enqueue(new Callback<ArrayList<Station>>() {
            @Override
            public void onResponse(Call<ArrayList<Station>> call, Response<ArrayList<Station>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener estaciones cercanas");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Station>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
