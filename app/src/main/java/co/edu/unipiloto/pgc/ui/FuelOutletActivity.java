package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.dao.FuelDAO;
import co.edu.unipiloto.pgc.dao.RegisterDAO;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.RegisterAdapter;

public class FuelOutletActivity extends BaseActivity {

    private ArrayList<Register> registers;
    private RegisterDAO registerDAO;
    private User user;
    private RegisterAdapter adapterRegistros;
    private Spinner tipoCombustible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_outlet);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        registerDAO = new RegisterDAO(this);
        registers = registerDAO.getAllRegisters(user);
        tipoCombustible = findViewById(R.id.tipoCombustible);

        FuelDAO fuelDAO = new FuelDAO(this);
        ArrayList<Fuel> listaCombustibles = fuelDAO.getAllFuels();

        ArrayAdapter<Fuel> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaCombustibles
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoCombustible.setAdapter(adapter);

        RecyclerView listaRegistros = findViewById(R.id.listaRegistros);
        listaRegistros.setLayoutManager(new LinearLayoutManager(this));

        adapterRegistros = new RegisterAdapter(registers);
        listaRegistros.setAdapter(adapterRegistros);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this::onEntryRegister);

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
            } else if (id == R.id.nav_inventario) {
                sendIntent = new Intent(this, InventoryManagementActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                return true;
            } else return id == R.id.nav_registrar;
        });

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    public void onEntryRegister(View view){
        EditText textoCantidad = findViewById(R.id.textoCantidad);
        if (textoCantidad.getText().toString().isEmpty()){
            return;
        }

        Register register = new Register();
        register.setCombustible((Fuel) tipoCombustible.getSelectedItem());
        register.setCantidad(Double.parseDouble(textoCantidad.getText().toString()));
        register.setEstacion(user);

        try {
            registerDAO.insertarRegistro(register);
            registers = registerDAO.getAllRegisters(user);
            adapterRegistros.updateList(registers);

        } catch (RuntimeException e) {

            if (e.getMessage().equals("EXCEDE_CAPACIDAD")) {
                Toast.makeText(this,
                        "No se puede registrar. Excede la capacidad del inventario",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        textoCantidad.setText("");
        tipoCombustible.setSelection(0);
    }

}