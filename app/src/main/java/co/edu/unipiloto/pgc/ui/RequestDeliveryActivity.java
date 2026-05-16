package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.dao.FuelDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.User;

public class RequestDeliveryActivity extends BaseActivity {

    private User user;
    private Spinner spinnerDistribuidor, spinnerCombustible;
    private EditText editCantidad;
    private DeliveryDAO deliveryDAO;
    private UserDAO userDAO;
    private FuelDAO fuelDAO;
    private ArrayList<User> distributors;
    private ArrayList<Fuel> fuels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_delivery);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        user = (User) getIntent().getSerializableExtra("user");
        deliveryDAO = new DeliveryDAO(this);
        userDAO = new UserDAO(this);
        fuelDAO = new FuelDAO(this);

        spinnerDistribuidor = findViewById(R.id.spinnerDistribuidor);
        spinnerCombustible = findViewById(R.id.spinnerCombustible);
        editCantidad = findViewById(R.id.editCantidad);
        Button btnSolicitar = findViewById(R.id.btnSolicitar);
        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        loadSpinners();

        btnSolicitar.setOnClickListener(v -> createRequest());
        btnCerrarSesion.setOnClickListener(this::onLogOut);

        setupBottomNavigation(user);
    }

    private void loadSpinners() {
        userDAO.getAllDistributors(new UserDAO.DistributorCallback() {
            @Override
            public void onSuccess(ArrayList<User> distributors) {
                RequestDeliveryActivity.this.distributors = distributors;
                ArrayList<String> distributorUsernames = new ArrayList<>();
                for (User distributor : distributors) {
                    distributorUsernames.add(distributor.getUsername());
                }
                ArrayAdapter<String> distAdapter = new ArrayAdapter<>(RequestDeliveryActivity.this, android.R.layout.simple_spinner_item, distributorUsernames);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDistribuidor.setAdapter(distAdapter);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RequestDeliveryActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        fuelDAO.getAllFuels(new FuelDAO.FuelsCallback() {
            @Override
            public void onSuccess(ArrayList<Fuel> fuelsList) {
                fuels = fuelsList;
                ArrayAdapter<Fuel> fuelAdapter = new ArrayAdapter<>(RequestDeliveryActivity.this, android.R.layout.simple_spinner_item, fuels);
                fuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCombustible.setAdapter(fuelAdapter);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RequestDeliveryActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createRequest() {
        String cantidadStr = editCantidad.getText().toString();
        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese la cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad = Double.parseDouble(cantidadStr);
        int selectedIndex = spinnerDistribuidor.getSelectedItemPosition();
        if (distributors == null || distributors.isEmpty()) return;
        User distributor = distributors.get(selectedIndex);
        Fuel fuel = (Fuel) spinnerCombustible.getSelectedItem();

        Delivery delivery = new Delivery();
        delivery.setEstacionId(user.getId());
        delivery.setDistribuidorId(distributor.getId());
        delivery.setCombustible(fuel);
        delivery.setCantidad(cantidad);
        delivery.setPlaca("PENDIENTE"); // Initial placeholder for placa
        delivery.setEstado("PENDIENTE");

        deliveryDAO.insertDelivery(delivery, new DeliveryDAO.ApiCallback<Delivery>() {
            @Override
            public void onSuccess(Delivery result) {
                runOnUiThread(() -> {
                    Toast.makeText(RequestDeliveryActivity.this, "Solicitud creada con éxito", Toast.LENGTH_SHORT).show();
                    editCantidad.setText("");
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(RequestDeliveryActivity.this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }


}
