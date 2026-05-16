package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Price;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceDAO {
    private ApiService apiService;

    public interface PricesCallback {
        void onSuccess(ArrayList<Price> prices);

        void onError(String message);
    }

    public PriceDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllPrices(User estacionId, PricesCallback callback) {
        Call<ArrayList<Price>> call = apiService.getAllPrices(estacionId.getId());
        call.enqueue(new Callback<ArrayList<Price>>() {
            @Override
            public void onResponse(Call<ArrayList<Price>> call, Response<ArrayList<Price>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener precios");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Price>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
