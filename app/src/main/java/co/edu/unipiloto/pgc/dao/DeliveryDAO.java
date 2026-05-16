package co.edu.unipiloto.pgc.dao;

import android.content.Context;

import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class DeliveryDAO {

    private ApiService apiService;

    public interface ApiCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }

    public DeliveryDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private String getErrorMessage(Response<?> response, String defaultMsg) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                JSONObject jsonObject = new JSONObject(errorBody);
                return jsonObject.optString("error", defaultMsg + ": " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultMsg + ": " + response.code();
    }

    public void insertDelivery(Delivery delivery, ApiCallback<Delivery> callback) {
        apiService.createDelivery(delivery).enqueue(new Callback<Delivery>() {
            @Override
            public void onResponse(Call<Delivery> call, Response<Delivery> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(getErrorMessage(response, "Error al crear entrega"));
                }
            }

            @Override
            public void onFailure(Call<Delivery> call, Throwable t) {
                callback.onError(t.getMessage());

            }
        });
    }

    public void getDeliveriesByState(User user, String estado, ApiCallback<ArrayList<Delivery>> callback) {
        apiService.getDeliveriesByState(user.getId(), estado).enqueue(new Callback<ArrayList<Delivery>>() {
            @Override
            public void onResponse(Call<ArrayList<Delivery>> call, Response<ArrayList<Delivery>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(getErrorMessage(response, "Error al obtener entregas"));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Delivery>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void markAsDelivered(int deliveryId, String placa, ApiCallback<Void> callback) {
        apiService.markAsDelivered(deliveryId, placa).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(getErrorMessage(response, "Error al marcar como entregado"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void confirmDelivery(int deliveryId, int userId, ApiCallback<Void> callback) {
        apiService.confirmDelivery(deliveryId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(getErrorMessage(response, "Error al confirmar entrega"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}