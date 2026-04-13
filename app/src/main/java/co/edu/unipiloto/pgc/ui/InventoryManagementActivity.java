package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.InventoryDAO;
import co.edu.unipiloto.pgc.model.Inventory;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.HistoryAdapter;
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

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent sendIntent;
            if (id == R.id.nav_calcular) {
                sendIntent = new Intent(this, PriceCalculatorActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                return true;
            } else if (id == R.id.nav_historial) {
                sendIntent = new Intent(this, FuelHistoryActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                return true;
            } else if (id == R.id.nav_registrar) {
                sendIntent = new Intent(this, FuelOutletActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                return true;
            } else return id == R.id.nav_inventario;
        });

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }
}