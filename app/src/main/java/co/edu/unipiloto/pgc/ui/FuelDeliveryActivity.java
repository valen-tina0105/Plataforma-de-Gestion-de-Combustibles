package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.ui.adapters.DeliveriesAdapter;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.User;

public class FuelDeliveryActivity extends BaseActivity {

    private ArrayList<Delivery> deliveries;
    private DeliveryDAO deliveryDAO;
    private User user;
    private DeliveriesAdapter adapterEntregas;
    private RecyclerView listaEntregas;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_delivery);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));
        
        user = (User) getIntent().getSerializableExtra("user");
        deliveryDAO = new DeliveryDAO(this);

        listaEntregas = findViewById(R.id.listaEntregas);
        listaEntregas.setLayoutManager(new LinearLayoutManager(this));

        loadDeliveries();

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    private void loadDeliveries() {
        // Mostrar únicamente entregas con estado PENDIENTE
        deliveries = deliveryDAO.getDeliveriesByState(user, "PENDIENTE");
        adapterEntregas = new DeliveriesAdapter(deliveries, deliveryDAO, this::loadDeliveries);
        listaEntregas.setAdapter(adapterEntregas);
    }
}
