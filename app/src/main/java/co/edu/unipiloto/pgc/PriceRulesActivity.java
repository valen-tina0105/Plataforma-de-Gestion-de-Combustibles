package co.edu.unipiloto.pgc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PriceRulesActivity extends AppCompatActivity {

    private ArrayList<Rule> rules;
    private ArrayList<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_rules);
        Intent intent = getIntent();
        rules=(ArrayList<Rule>) intent.getSerializableExtra("rules");
        transactions=(ArrayList<Transaction>) intent.getSerializableExtra("transactions");
        if(rules==null)
            rules = new ArrayList<>();
        TextView textoReglas = findViewById(R.id.textoReglas);
        String textoCompleto="";
        for(int i=0; i<rules.size(); i++) {
            textoCompleto += "Regla " + (i + 1) + ": Tipo: " + rules.get(i).getTipo()
                    + " Precio: " + rules.get(i).getPrecio() + "$\n";
        }
        textoReglas.setText(textoCompleto);
    }

    public void onSendRule(View view) {
        Spinner tipos = findViewById(R.id.tipos);
        String tipo = tipos.getSelectedItem().toString();
        EditText precio = findViewById(R.id.precio);
        if(precio.getText().toString().equals("")){
            return;
        }
        TextView textoReglas = findViewById(R.id.textoReglas);
        int textoPrecio = Integer.parseInt(precio.getText().toString());
        boolean tipoExiste = false;
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getTipo().equals(tipo)) {
                tipoExiste = true;
                rules.get(i).setPrecio(textoPrecio);
            }
        }
        if (!tipoExiste) {
            rules.add(new Rule(tipo, textoPrecio));
        }
        String textoCompleto="";
        for(int i=0; i<rules.size(); i++) {
            textoCompleto += "Regla " + (i + 1) + ": Tipo: " + rules.get(i).getTipo()
                    + " Precio: " + rules.get(i).getPrecio() + "$\n";
        }
        textoReglas.setText(textoCompleto);
        precio.setText("");
    }

    public void onChangeActivity(View view){
        Spinner actividades = findViewById(R.id.actividades);
        switch (actividades.getSelectedItem().toString()){
            case "Configurar Precio":
                break;
            case "Calcular Precio":
                Intent intent = new Intent(this, PriceCalculatorActivity.class);
                intent.putExtra("rules",rules);
                intent.putExtra("transactions", transactions);
                startActivity(intent);
                break;
        }
    }

}