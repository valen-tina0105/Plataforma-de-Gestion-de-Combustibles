package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        transactionDAO = new TransactionDAO(this);
        transactions = transactionDAO.getAllTransactionsByUser(user);

        RecyclerView listaCompras = findViewById(R.id.listaCompras);
        listaCompras.setLayoutManager(new LinearLayoutManager(this));

        adapterCompras = new PurchaseAdapter(transactions);
        listaCompras.setAdapter(adapterCompras);

        Button btnFiltrar = findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this::onFilter);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent sendIntent;
            if (id == R.id.nav_consultar) {
                sendIntent = new Intent(this, PriceConsultationActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.nav_informacion) {
                sendIntent = new Intent(this, UserInformationActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else return id == R.id.nav_compras;
        });

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onFilter(View view) {
        Spinner filter = findViewById(R.id.filtrar);
        int posicion = filter.getSelectedItemPosition();

        if (posicion == 0) {
            transactions = transactionDAO.getAllTransactionsByUser(user);
        } else if (posicion == 1) {
            transactions = transactionDAO.getTransactionsByUserOrderedByStation(user);
        } else if (posicion == 2) {
            transactions = transactionDAO.getTransactionsByUserOrderedByDate(user);
        }

        adapterCompras.updateList(transactions);
    }
}