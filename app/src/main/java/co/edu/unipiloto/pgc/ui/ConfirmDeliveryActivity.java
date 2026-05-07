package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
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
            } else if (id == R.id.nav_historial) {
                sendIntent = new Intent(this, FuelHistoryActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.nav_inventario) {
                sendIntent = new Intent(this, InventoryManagementActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else return id == R.id.nav_registrar;
        });

        loadDeliveries();

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    private void loadDeliveries() {
        deliveries = deliveryDAO.getDeliveriesByStation(user.getId());

        adapter = new ConfirmDeliveryAdapter(deliveries, user, deliveryDAO, this::loadDeliveries);
        recyclerView.setAdapter(adapter);
    }
}