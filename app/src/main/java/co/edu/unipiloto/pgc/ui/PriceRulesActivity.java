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
import co.edu.unipiloto.pgc.model.User;

public class PriceRulesActivity extends BaseActivity {

    private ArrayList<Rule> rules;
    private User user;
    private RuleDAO ruleDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_rules);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        ruleDAO = new RuleDAO(this);
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
            rule.setAdmin(user);
            rules.add(rule);
            ruleDAO.insertarRegla(rule);
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

}