package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.ConfirmDeliveryAdapter;

public class ConfirmDeliveryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ConfirmDeliveryAdapter adapter;
    private ArrayList<Delivery> deliveries;
    private DeliveryDAO deliveryDAO;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delivery);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        user = (User) getIntent().getSerializableExtra("user");
        deliveryDAO = new DeliveryDAO(this);

        recyclerView = findViewById(R.id.listaEntregasConfirmar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNavigation();
        loadDeliveries();

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_confirmar);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_confirmar) return true;

            Intent nextIntent = null;
            if (id == R.id.nav_solicitud) {
                nextIntent = new Intent(this, RequestDeliveryActivity.class);
            } else if (id == R.id.nav_calcular) {
                nextIntent = new Intent(this, PriceCalculatorActivity.class);
            } else if (id == R.id.nav_historial) {
                nextIntent = new Intent(this, FuelHistoryActivity.class);
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

    private void loadDeliveries() {
        // En el nuevo flujo, ConfirmDeliveryActivity muestra las entregas con estado ENTREGADO
        deliveries = deliveryDAO.getDeliveriesByState(user, "ENTREGADO");
        adapter = new ConfirmDeliveryAdapter(deliveries, user, deliveryDAO, this::loadDeliveries);
        recyclerView.setAdapter(adapter);
    }
}
