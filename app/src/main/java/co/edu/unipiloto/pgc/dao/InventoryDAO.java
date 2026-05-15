package co.edu.unipiloto.pgc.dao;

import android.content.Context;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.Inventory;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryDAO {
    private ApiService apiService;

    public interface InventoriesCallback {
        void onSuccess(ArrayList<Inventory> inventories);
        void onError(String message);
    }

    public interface UpdateCallback {
        void onSuccess();
        void onError(String message);
    }

    public InventoryDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllInventories(User station, InventoriesCallback callback) {
        Call<ArrayList<Inventory>> call = apiService.getInventoriesByStation(station.getId());
        call.enqueue(new Callback<ArrayList<Inventory>>() {
            @Override
            public void onResponse(Call<ArrayList<Inventory>> call, Response<ArrayList<Inventory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener inventarios");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Inventory>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }

    public void actualizarCantidad(int inventoryId, double nuevaCantidad, UpdateCallback callback) {
        Call<Void> call = apiService.updateInventoryQuantity(inventoryId, nuevaCantidad);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Error al actualizar inventario");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
