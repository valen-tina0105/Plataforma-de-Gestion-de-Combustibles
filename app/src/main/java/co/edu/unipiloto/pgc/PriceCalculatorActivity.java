package co.edu.unipiloto.pgc;

import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceControl;
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
    private ArrayList<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_calculator);
        Intent intent = getIntent();
        rules=(ArrayList<Rule>) intent.getSerializableExtra("rules");
        transactions=(ArrayList<Transaction>) intent.getSerializableExtra("transactions");
        if(transactions==null)
            transactions = new ArrayList<>();
        TextView textoTransacciones = findViewById(R.id.textoTransacciones);
        String textoCompleto="";
        for(int i=0; i<transactions.size(); i++) {
            textoCompleto += "Transacciones " + (i + 1) + ": Tipo: " + transactions.get(i).getTipo()
                    + " Volumen: " +transactions.get(i).getVolumen() + " Total: "
                    + transactions.get(i).getTotal() + "$\n";
        }
        textoTransacciones.setText(textoCompleto);
    }

    public void onSendCalculate(View view){
        Spinner tipos = findViewById(R.id.tipos);
        String tipo = tipos.getSelectedItem().toString();
        EditText combustible = findViewById(R.id.combustible);
        if(combustible.getText().toString().equals("")){
            return;
        }
        TextView total = findViewById(R.id.total);
        int volumen = Integer.parseInt(combustible.getText().toString());
        int totalReal=0;
        if(rules.size()==0){
            total.setText("No hay ninguna regla establecida");
            return;
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
        TextView textoTransacciones = findViewById(R.id.textoTransacciones);
            transactions.add(new Transaction(tipo, totalReal, volumen));
        String textoCompleto="";
        for(int i=0; i<transactions.size(); i++) {
            textoCompleto += "Transacciones " + (i + 1) + ": Tipo: " + transactions.get(i).getTipo()
                    + " Volumen: " +transactions.get(i).getVolumen() + " Total: "
                    + transactions.get(i).getTotal() + "$\n";
        }
        textoTransacciones.setText(textoCompleto);
        combustible.setText("");
    }

    public void onChangeActivity(View view){
        Spinner actividades = findViewById(R.id.actividades);
        Intent intent = new Intent(this, PriceRulesActivity.class);
        intent.putExtra("rules",rules);
        intent.putExtra("transactions", transactions);
        startActivity(intent);
    }
}