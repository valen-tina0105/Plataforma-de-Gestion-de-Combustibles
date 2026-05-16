package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RolDAO {

    private ApiService apiService;

    public interface RolesCallbacK {
        void onSuccess(ArrayList<Rol> roles);
        void onError(String message);
    }
    public RolDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllRoles(RolesCallbacK callback){
        Call<ArrayList<Rol>> call = apiService.getAllRoles();

        call.enqueue(new Callback<ArrayList<Rol>>(){

            @Override
            public void onResponse(Call<ArrayList<Rol>> call, Response<ArrayList<Rol>> response) {
                if (response.isSuccessful() && response.body() != null){
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener roles");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Rol>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
