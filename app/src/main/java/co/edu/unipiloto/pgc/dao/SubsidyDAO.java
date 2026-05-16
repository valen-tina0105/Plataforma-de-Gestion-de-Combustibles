package co.edu.unipiloto.pgc.dao;

import android.content.Context;

import co.edu.unipiloto.pgc.model.Subsidy;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubsidyDAO {
    private ApiService apiService;

    public interface SubsidyCallback {
        void onSuccess(Subsidy subsidy);
        void onError(String message);
    }

    public SubsidyDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getSubsidyById(User user, SubsidyCallback callback) {
        Call<Subsidy> call = apiService.getSubsidyById(user.getId());
        call.enqueue(new Callback<Subsidy>() {
            @Override
            public void onResponse(Call<Subsidy> call, Response<Subsidy> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener subsidio");
                }
            }

            @Override
            public void onFailure(Call<Subsidy> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
