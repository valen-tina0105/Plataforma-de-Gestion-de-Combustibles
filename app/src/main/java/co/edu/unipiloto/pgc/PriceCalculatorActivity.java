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

public class PriceCalculatorActivity extends AppCompatActivity {

    private ArrayList<Rule> rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_calculator);
        Intent intent = getIntent();
        rules=(ArrayList<Rule>) intent.getSerializableExtra("array");
    }

    public void onSendCalculate(View view){
        Spinner tipos = findViewById(R.id.tipos);
        String tipo = tipos.getSelectedItem().toString();
        EditText combustible = findViewById(R.id.combustible);
        TextView total = findViewById(R.id.total);
        int volumen = Integer.parseInt(combustible.getText().toString());
        int totalReal=0;
        if(rules.size()==0){
            total.setText("No hay ninguna regla establecida");
        }
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getTipo().equalsIgnoreCase(tipo)) {
                totalReal = volumen*rules.get(i).getPrecio();
                total.setText("Total: "+totalReal+"$");
                break;
            } else {
                total.setText("Regla no establecida");
            }
        }
    }

    public void onChangeActivity(View view){
        Spinner actividades = findViewById(R.id.actividades);
        Intent intent = new Intent(this, PriceRulesActivity.class);
        intent.putExtra("array",rules);
        startActivity(intent);
    }
}