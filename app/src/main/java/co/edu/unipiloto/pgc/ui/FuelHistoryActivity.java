package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.MovementDAO;
import co.edu.unipiloto.pgc.model.Movement;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.DeliveriesAdapter;
import co.edu.unipiloto.pgc.ui.adapters.MovementsAdapter;
import co.edu.unipiloto.pgc.ui.adapters.MovementsListAdapter;

public class FuelHistoryActivity extends BaseActivity {

    private MovementDAO movementDAO;
    private User user;
    private ArrayList<Movement> movements;
    private MovementsListAdapter adapterMovimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_history);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        movementDAO = new MovementDAO(this);
        movements = movementDAO.getAllMovements(user);

        ListView listaMovimientos = findViewById(R.id.listaMovimientos);

        adapterMovimientos = new MovementsListAdapter(this, movements);
        listaMovimientos.setAdapter(adapterMovimientos);

        Button btnFiltrar = findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this::onFilter);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent sendIntent;
            if (id == R.id.nav_calcular) {
                sendIntent = new Intent(this, PriceCalculatorActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                return true;
            } else if (id == R.id.nav_registrar) {
                sendIntent = new Intent(this, FuelOutletActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                return true;
            } else return id == R.id.nav_historial;
        });

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void onFilter(View view) {
        Spinner filter = findViewById(R.id.filtro);
        int posicion = filter.getSelectedItemPosition();

        if (posicion == 0) {
            movements = movementDAO.getAllMovements(user);
        } else if (posicion == 1) {
            movements = movementDAO.getMovementsOrderByType(user);
        } else if (posicion == 2) {
            movements = movementDAO.getMovementsByDate(user);
        }

        adapterMovimientos.updateList(movements);
    }

}