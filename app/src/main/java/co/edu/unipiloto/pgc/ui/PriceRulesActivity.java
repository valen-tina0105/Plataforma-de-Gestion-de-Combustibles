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
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;

public class PriceRulesActivity extends AppCompatActivity {

    private ArrayList<Rule> rules;
    private ArrayList<Transaction> transactions;
    private ArrayList<Register> registers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_rules);
        Intent intent = getIntent();
        RuleDAO ruleDAO = new RuleDAO(this);
        rules = ruleDAO.getAllRules();
        TextView textoReglas = findViewById(R.id.textoReglas);
        String textoCompleto="";
        for(int i=0; i<rules.size(); i++) {
            textoCompleto += "Regla " + rules.get(i).getId() + ": Tipo: " + rules.get(i).getTipoVehiculo()
                    + " Precio: " + rules.get(i).getPrecio() + "Creado por: " + rules.get(i).getAdmin().getUsername()
                    + "Fecha: " + rules.get(i).getFechaFormateada() + "$\n";
        }
        textoReglas.setText(textoCompleto);
    }

    public void onSendRule(View view) {
        Spinner tipos = findViewById(R.id.tipos);
        String tipo = tipos.getSelectedItem().toString();
        EditText precio = findViewById(R.id.precio);
        if(precio.getText().toString().isEmpty()){
            return;
        }
        TextView textoReglas = findViewById(R.id.textoReglas);
        int textoPrecio = Integer.parseInt(precio.getText().toString());
        boolean tipoExiste = false;
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getTipoVehiculo().equals(tipo)) {
                tipoExiste = true;
                rules.get(i).setPrecio(textoPrecio);
            }
        }
        if (!tipoExiste) {
            Rule rule = new Rule();
            rule.setTipoVehiculo(tipo);
            rule.setPrecio(textoPrecio);
            rules.add(rule);
        }
        String textoCompleto="";
        for(int i=0; i<rules.size(); i++) {
            textoCompleto += "Regla " + rules.get(i).getId() + ": Tipo: " + rules.get(i).getTipoVehiculo()
                    + " Precio: " + rules.get(i).getPrecio() + "Creado por: " + rules.get(i).getAdmin().getUsername()
                    + "Fecha: " + rules.get(i).getFechaFormateada() + "$\n";
        }
        textoReglas.setText(textoCompleto);
        precio.setText("");
    }

    public void onChangeActivity(View view){
        Spinner actividades = findViewById(R.id.actividades);
        Intent intent;
        switch (actividades.getSelectedItem().toString()){
            case "Configurar Precio":
                break;
            case "Calcular Precio":
                intent = new Intent(this, PriceCalculatorActivity.class);
                intent.putExtra("rules",rules);
                intent.putExtra("transactions", transactions);
                intent.putExtra("registers", registers);
                startActivity(intent);
                break;
            case "Crear Usuario":
                intent = new Intent(this, CreateUsersActivity.class);
                intent.putExtra("rules",rules);
                intent.putExtra("transactions", transactions);
                intent.putExtra("registers", registers);
                startActivity(intent);
                break;
            case "Iniciar Sesión":
                intent = new Intent(this, LogInActivity.class);
                intent.putExtra("rules",rules);
                intent.putExtra("transactions", transactions);
                intent.putExtra("registers", registers);
                startActivity(intent);
                break;
            case "Registrar Entrada":
                intent = new Intent(this, FuelOutletActivity.class);
                intent.putExtra("rules",rules);
                intent.putExtra("transactions", transactions);
                intent.putExtra("registers", registers);
                startActivity(intent);
                break;
        }
    }

}