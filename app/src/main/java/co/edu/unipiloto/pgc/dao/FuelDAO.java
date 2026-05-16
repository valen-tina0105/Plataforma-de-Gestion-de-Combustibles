package co.edu.unipiloto.pgc.dao;

import android.content.Context;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelDAO {
    private ApiService apiService;

    public interface FuelsCallback {
        void onSuccess(ArrayList<Fuel> fuels);
        void onError(String message);
    }

    public FuelDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllFuels(FuelsCallback callback) {
        Call<ArrayList<Fuel>> call = apiService.getAllFuels();
        call.enqueue(new Callback<ArrayList<Fuel>>() {
            @Override
            public void onResponse(Call<ArrayList<Fuel>> call, Response<ArrayList<Fuel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener combustibles");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Fuel>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
