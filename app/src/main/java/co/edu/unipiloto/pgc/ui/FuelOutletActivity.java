package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RegisterDAO;
import co.edu.unipiloto.pgc.dao.RuleDAO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_outlet);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        registerDAO = new RegisterDAO(this);
        registers = registerDAO.getAllRegisters(user);

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
                return true;
            } else if (id == R.id.nav_historial) {
                sendIntent = new Intent(this, FuelHistoryActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                return true;
            } else return id == R.id.nav_registrar;
        });

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    public void onEntryRegister(View view){
        EditText textoCantidad = findViewById(R.id.textoCantidad);
        Spinner tipoCombustible = findViewById(R.id.tipoCombustible);
        if (textoCantidad.getText().toString().isEmpty()){
            return;
        }

        Register register = new Register();
        register.setTipoCombustible(tipoCombustible.getSelectedItem().toString());
        register.setCantidad(Integer.parseInt(textoCantidad.getText().toString()));
        register.setEstacion(user);
        registerDAO.insertarRegistro(register);
        registers = registerDAO.getAllRegisters(user);
        adapterRegistros.updateList(registers);
        textoCantidad.setText("");
        tipoCombustible.setSelection(0);
    }

}