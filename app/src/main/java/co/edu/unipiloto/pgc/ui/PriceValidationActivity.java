package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.TransactionDAO;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.ui.adapters.DeliveriesAdapter;
import co.edu.unipiloto.pgc.ui.adapters.HistoryAdapter;
import co.edu.unipiloto.pgc.ui.adapters.TransactionAdapter;

public class PriceValidationActivity extends BaseActivity {

    private ArrayList<Transaction> transactions;
    private TransactionDAO transactionDAO;
    private HistoryAdapter adapterHistorial;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_validation);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));
        transactionDAO = new TransactionDAO(this);
        transactions = transactionDAO.getValidatedTransactions();
        RecyclerView listaHistorial = findViewById(R.id.listaHistorial);
        listaHistorial.setLayoutManager(new LinearLayoutManager(this));

        adapterHistorial = new HistoryAdapter(transactions);
        listaHistorial.setAdapter(adapterHistorial);

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }
}