package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.DeliveriesAdapter;
import co.edu.unipiloto.pgc.ui.adapters.RulesAdapter;

public class PriceRulesActivity extends BaseActivity {

    private ArrayList<Rule> rules;
    private User user;
    private RuleDAO ruleDAO;
    private RulesAdapter adapterReglas;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_rules);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        ruleDAO = new RuleDAO(this);
        rules = ruleDAO.getAllRules();

        RecyclerView listaReglas = findViewById(R.id.listaReglas);
        listaReglas.setLayoutManager(new LinearLayoutManager(this));

        adapterReglas = new RulesAdapter(rules);
        listaReglas.setAdapter(adapterReglas);
        
        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);

        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this::onSendRule);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void onSendRule(View view) {
            Spinner tipoVehiculo = findViewById(R.id.tipoVehiculo);
            String tipo = tipoVehiculo.getSelectedItem().toString();

            EditText precio = findViewById(R.id.precio);

            if(precio.getText().toString().isEmpty()){
                Toast.makeText(this, "El precio no puede estar vacio", Toast.LENGTH_SHORT).show();
                return;
            }

            int textoPrecio = Integer.parseInt(precio.getText().toString());

            Rule rule = new Rule();
            rule.setTipoVehiculo(tipo);
            rule.setPrecio(textoPrecio);
            rule.setAdmin(user);

            ruleDAO.guardarRegla(rule);

            rules = ruleDAO.getAllRules();
            adapterReglas.updateList(rules);

            precio.setText("");
    }

}