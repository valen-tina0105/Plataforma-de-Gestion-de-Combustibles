package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.TransactionDAO;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.PurchaseAdapter;

public class GasolinePurchasesActivity extends BaseActivity {

    private User user;
    private ArrayList<Transaction> transactions;
    private TransactionDAO transactionDAO;
    private PurchaseAdapter adapterCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasoline_purchases);
        
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        setupToolbar(findViewById(R.id.toolbar), user);

        transactionDAO = new TransactionDAO(this);

        RecyclerView listaCompras = findViewById(R.id.listaCompras);
        listaCompras.setLayoutManager(new LinearLayoutManager(this));

        adapterCompras = new PurchaseAdapter(new ArrayList<>());
        listaCompras.setAdapter(adapterCompras);

        loadTransactions();

        Button btnFiltrar = findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this::onFilter);
    }

    private void loadTransactions() {
        transactionDAO.getAllTransactionsByUser(user, new TransactionDAO.TransactionsCallback() {
            @Override
            public void onSuccess(ArrayList<Transaction> result) {
                runOnUiThread(() -> {
                    transactions = result;
                    adapterCompras.updateList(transactions);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(GasolinePurchasesActivity.this, message, android.widget.Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onFilter(View view) {
        Spinner filter = findViewById(R.id.filtrar);
        int posicion = filter.getSelectedItemPosition();

        if (posicion == 0) {
            loadTransactions();
        } else if (posicion == 1) {
            transactionDAO.getTransactionsByUserOrderedByStation(user, new TransactionDAO.TransactionsCallback() {
                @Override
                public void onSuccess(ArrayList<Transaction> result) {
                    runOnUiThread(() -> {
                        transactions = result;
                        adapterCompras.updateList(transactions);
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> android.widget.Toast.makeText(GasolinePurchasesActivity.this, message, android.widget.Toast.LENGTH_SHORT).show());
                }
            });
        } else if (posicion == 2) {
            transactionDAO.getTransactionsByUserOrderedByDate(user, new TransactionDAO.TransactionsCallback() {
                @Override
                public void onSuccess(ArrayList<Transaction> result) {
                    runOnUiThread(() -> {
                        transactions = result;
                        adapterCompras.updateList(transactions);
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> android.widget.Toast.makeText(GasolinePurchasesActivity.this, message, android.widget.Toast.LENGTH_SHORT).show());
                }
            });
        }
    }
}
