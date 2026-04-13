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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.MovementDAO;
import co.edu.unipiloto.pgc.model.Movement;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.MovementAdapter;

public class FuelHistoryActivity extends BaseActivity {

    private MovementDAO movementDAO;
    private User user;
    private ArrayList<Movement> movements;
    private MovementAdapter adapterMovimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_history);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        movementDAO = new MovementDAO(this);
        movements = movementDAO.getAllMovements(user);

        ListView listaMovimientos = findViewById(R.id.listaMovimientos);

        adapterMovimientos = new MovementAdapter(this, movements);
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.nav_registrar) {
                sendIntent = new Intent(this, FuelOutletActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                return true;
            } else if (id == R.id.nav_inventario) {
                sendIntent = new Intent(this, InventoryManagementActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                return true;
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