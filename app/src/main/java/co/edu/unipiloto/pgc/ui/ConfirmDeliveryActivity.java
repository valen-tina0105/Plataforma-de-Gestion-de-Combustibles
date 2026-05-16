package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

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

        setupBottomNavigation(user);
        loadDeliveries();

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }



    private void loadDeliveries() {
        deliveryDAO.getDeliveriesByState(user, "ENTREGADO", new DeliveryDAO.ApiCallback<ArrayList<Delivery>>() {
            @Override
            public void onSuccess(ArrayList<Delivery> result) {
                runOnUiThread(() -> {
                    deliveries = result;
                    adapter = new ConfirmDeliveryAdapter(deliveries, user, deliveryDAO, ConfirmDeliveryActivity.this::loadDeliveries);
                    recyclerView.setAdapter(adapter);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ConfirmDeliveryActivity.this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
