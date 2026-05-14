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

        setupBottomNavigation();

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_historial);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_historial) return true;

            Intent nextIntent = null;
            if (id == R.id.nav_solicitud) {
                nextIntent = new Intent(this, RequestDeliveryActivity.class);
            } else if (id == R.id.nav_calcular) {
                nextIntent = new Intent(this, PriceCalculatorActivity.class);
            } else if (id == R.id.nav_confirmar) {
                nextIntent = new Intent(this, ConfirmDeliveryActivity.class);
            } else if (id == R.id.nav_inventario) {
                nextIntent = new Intent(this, InventoryManagementActivity.class);
            }

            if (nextIntent != null) {
                nextIntent.putExtra("user", user);
                startActivity(nextIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });
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
