package co.edu.unipiloto.pgc.dao;

import android.content.Context;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RuleDAO {

    private ApiService apiService;


    public interface RulesCallbacK {
        void onSuccess(ArrayList<Rule> rules);

        void onError(String message);
    }

    public interface RuleCallback {
        void onSuccess(Rule rule);

        void onError(String message);
    }

    public RuleDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllRules(RulesCallbacK callback) {
        Call<ArrayList<Rule>> call = apiService.getAllRulees();

        call.enqueue(new Callback<ArrayList<Rule>>() {

            @Override
            public void onResponse(Call<ArrayList<Rule>> call, Response<ArrayList<Rule>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener Rules");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Rule>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }

    public void guardarRegla(Rule rule, RuleCallback callback) {
        Call<Rule> call = apiService.saveRule(rule);

        call.enqueue(new Callback<Rule>() {
            @Override
            public void onResponse(Call<Rule> call, Response<Rule> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al crear Rule");
                }
            }

            @Override
            public void onFailure(Call<Rule> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });

    }
}