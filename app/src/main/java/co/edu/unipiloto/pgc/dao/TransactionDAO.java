package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Movement;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDAO {
    private DatabaseHelper dbHelper;
    private ApiService apiService;

    public interface TransactionsCallback {
        void onSuccess(ArrayList<Transaction> transactions);
        void onError(String message);
    }

    public TransactionDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllTransactions(User user, TransactionsCallback callback) {
        apiService.getTransactionsByStation(user.getId()).enqueue(new Callback<ArrayList<Transaction>>() {
            @Override
            public void onResponse(Call<ArrayList<Transaction>> call, Response<ArrayList<Transaction>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener transacciones: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Transaction>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getAllTransactionsByUser(User user, TransactionsCallback callback) {
        apiService.getTransactionsByUser(user.getId()).enqueue(new Callback<ArrayList<Transaction>>() {
            @Override
            public void onResponse(Call<ArrayList<Transaction>> call, Response<ArrayList<Transaction>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener transacciones: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Transaction>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getTransactionsByUserOrderedByStation(User user, TransactionsCallback callback) {
        apiService.getTransactionsByUserOrderedByStation(user.getId()).enqueue(new Callback<ArrayList<Transaction>>() {
            @Override
            public void onResponse(Call<ArrayList<Transaction>> call, Response<ArrayList<Transaction>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener transacciones: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Transaction>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getTransactionsByUserOrderedByDate(User user, TransactionsCallback callback) {
        apiService.getTransactionsByUserOrderedByDate(user.getId()).enqueue(new Callback<ArrayList<Transaction>>() {
            @Override
            public void onResponse(Call<ArrayList<Transaction>> call, Response<ArrayList<Transaction>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener transacciones: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Transaction>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getValidatedTransactions(TransactionsCallback callback) {
        apiService.getValidatedTransactions().enqueue(new Callback<ArrayList<Transaction>>() {
            @Override
            public void onResponse(Call<ArrayList<Transaction>> call, Response<ArrayList<Transaction>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener transacciones validadas: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Transaction>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }



    public void insertarTransaccion(Transaction transaction, TransactionsCallback callback){
        apiService.insertTransaction(transaction).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Error al insertar transacción: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}