package co.edu.unipiloto.pgc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class PriceRulesActivity extends AppCompatActivity {

    private ArrayList<Rule> rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_rules);
        rules = new ArrayList<>();
    }

    public void onSendRule(View view) {
        Spinner tipos = findViewById(R.id.tipos);
        String tipo = tipos.getSelectedItem().toString();
        EditText precio = findViewById(R.id.precio);
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
        Intent intent = new Intent(this, PriceCalculatorActivity.class);
        intent.putExtra("array",rules);
        startActivity(intent);
    }

}