package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
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
import co.edu.unipiloto.pgc.dao.InventoryDAO;
import co.edu.unipiloto.pgc.model.Inventory;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.InventoryAdapter;

public class InventoryManagementActivity extends BaseActivity {

    private User user;
    private ArrayList<Inventory> inventories;
    private InventoryDAO inventoryDAO;
    private InventoryAdapter adapterInventario;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_management);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        inventoryDAO = new InventoryDAO(this);
        inventories = inventoryDAO.getAllInventories(user);
        RecyclerView listaInventario = findViewById(R.id.listaInventario);
        listaInventario.setLayoutManager(new LinearLayoutManager(this));

        adapterInventario = new InventoryAdapter(inventories);
        listaInventario.setAdapter(adapterInventario);

        setupBottomNavigation();

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_inventario);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inventario) return true;

            Intent nextIntent = null;
            if (id == R.id.nav_solicitud) {
                nextIntent = new Intent(this, RequestDeliveryActivity.class);
            } else if (id == R.id.nav_calcular) {
                nextIntent = new Intent(this, PriceCalculatorActivity.class);
            } else if (id == R.id.nav_confirmar) {
                nextIntent = new Intent(this, ConfirmDeliveryActivity.class);
            } else if (id == R.id.nav_historial) {
                nextIntent = new Intent(this, FuelHistoryActivity.class);
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
}
