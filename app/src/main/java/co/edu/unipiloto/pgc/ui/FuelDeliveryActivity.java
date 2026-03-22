package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.ui.adapters.DeliveriesAdapter;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.User;

public class FuelDeliveryActivity extends BaseActivity {

    private ArrayList<Delivery> deliveries;
    private ArrayList<User> stations;
    private DeliveryDAO deliveryDAO;
    private UserDAO userDAO;
    private User user;
    private Spinner estacionDestino;
    private DeliveriesAdapter adapterEntregas;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_delivery);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        deliveryDAO = new DeliveryDAO(this);
        userDAO = new UserDAO(this);
        deliveries = deliveryDAO.getAllDeliveries(user);
        stations = userDAO.getAllStations();
        estacionDestino = findViewById(R.id.estacionDestino);

        ArrayList<String> estaciones = new ArrayList<>();

        for (User station : stations) {
            estaciones.add(station.getUsername());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                estaciones
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacionDestino.setAdapter(adapter);

        RecyclerView listaEntregas = findViewById(R.id.listaEntregas);
        listaEntregas.setLayoutManager(new LinearLayoutManager(this));

        adapterEntregas = new DeliveriesAdapter(deliveries);
        listaEntregas.setAdapter(adapterEntregas);

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this::registerDelivery);

    }

    @SuppressLint("NotifyDataSetChanged")
    public void registerDelivery(View view) {
        EditText textoPlaca = findViewById(R.id.textoPlaca), textoCantidad = findViewById(R.id.textoCantidad);
        Spinner tipoCombustible = findViewById(R.id.tipoCombustible);
        if (textoPlaca.getText().toString().isEmpty() || textoCantidad.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos los espacios deben ser rellenados", Toast.LENGTH_SHORT).show();
            return;
        }
        Delivery delivery = new Delivery();
        delivery.setPlaca(textoPlaca.getText().toString());
        delivery.setTipoCombustible(tipoCombustible.getSelectedItem().toString());
        delivery.setCantidad(Integer.parseInt(textoCantidad.getText().toString()));
        delivery.setEstacion(stations.get(estacionDestino.getSelectedItemPosition()));
        delivery.setDistribuidor(user);

        deliveries.add(delivery);

        deliveryDAO.insertDelivery(delivery);

        adapterEntregas.notifyDataSetChanged();

        textoPlaca.setText("");
        textoCantidad.setText("");
        tipoCombustible.setSelection(0);
        estacionDestino.setSelection(0);
    }

}