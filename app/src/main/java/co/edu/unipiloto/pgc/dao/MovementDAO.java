package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Inventory;
import co.edu.unipiloto.pgc.model.Movement;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovementDAO {
    private ApiService apiService;

    public interface MovementCallback {
        void onSuccess(ArrayList<Movement> movements);

        void onError(String message);
    }

    public MovementDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllMovements(User userId, MovementCallback callback) {
        apiService.getAllMovements(userId.getId()).enqueue(new Callback<ArrayList<Movement>>() {
            @Override
            public void onResponse(Call<ArrayList<Movement>> call, Response<ArrayList<Movement>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener movimientos");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Movement>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }

    public void getMovementsOrderByType(User userId, MovementCallback callback) {
        apiService.getMovementsOrderByType(userId.getId()).enqueue(new Callback<ArrayList<Movement>>() {
            @Override
            public void onResponse(Call<ArrayList<Movement>> call, Response<ArrayList<Movement>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener movimientos");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Movement>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });

    }

    public void getMovementsByDate(User userId, MovementCallback callback) {
        apiService.getMovementsByDate(userId.getId()).enqueue(new Callback<ArrayList<Movement>>() {
            @Override
            public void onResponse(Call<ArrayList<Movement>> call, Response<ArrayList<Movement>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener movimientos");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Movement>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }


}
