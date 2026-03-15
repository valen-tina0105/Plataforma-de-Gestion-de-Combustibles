package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RegisterDAO;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class FuelOutletActivity extends BaseActivity {

    private ArrayList<Register> registers;
    private RegisterDAO registerDAO;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_outlet);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        registerDAO = new RegisterDAO(this);
        registers = registerDAO.getAllRegisters();
        TextView textoRegistros = findViewById(R.id.textoRegistros);
        String textoCompleto="";
        for(int i=0; i<registers.size(); i++) {
            textoCompleto += "Entrada " + (i + 1) + ": Fecha: " + registers.get(i).getFechaFormateada()
                    + " Tipo: " + registers.get(i).getTipoCombustible()
                    + " Cantidad: " +registers.get(i).getCantidad()
                    + " Registrado por: " + registers.get(i).getEstacion().getUsername()
                + "$\n";
        }
        textoRegistros.setText(textoCompleto);
    }

    public void onEntryRegister(View view){
        EditText textoCantidad = findViewById(R.id.textoCantidad);
        Spinner tipoCombustible = findViewById(R.id.tipoCombustible);
        if (textoCantidad.getText().toString().isEmpty()){
            return;
        }

        TextView textoRegistros = findViewById(R.id.textoRegistros);
        Register register = new Register();
        register.setTipoCombustible(tipoCombustible.getSelectedItem().toString());
        register.setCantidad(Integer.parseInt(textoCantidad.getText().toString()));
        register.setEstacion(user);
        registers.add(register);
        registerDAO.insertarRegistro(register);
        String textoCompleto="";
        for(int i=0; i < registers.size(); i++) {
            textoCompleto += "Entrada " + (i + 1) + ": Fecha: " + registers.get(i).getFechaFormateada()
                    + " Tipo: " + registers.get(i).getTipoCombustible()
                    + " Cantidad: " +registers.get(i).getCantidad()
                    + " Registrado por: " + registers.get(i).getEstacion().getUsername()
                    + "$\n";
        }
        textoRegistros.setText(textoCompleto);
        textoCantidad.setText("");
    }

    public void onChangeActivity(View view){
        Spinner actividades = findViewById(R.id.actividades);
        Intent intent;
        switch (actividades.getSelectedItem().toString()){
            case "Calcular Precio":
                intent = new Intent(this, PriceCalculatorActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            case "Registar Entrada":
                break;
        }
    }
}