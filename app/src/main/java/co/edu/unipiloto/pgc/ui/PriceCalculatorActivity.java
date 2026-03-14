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
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;

public class PriceCalculatorActivity extends AppCompatActivity {

    private ArrayList<Rule> rules;
    private ArrayList<Transaction> transactions;
    private ArrayList<Register> registers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_calculator);
        Intent intent = getIntent();
        rules=(ArrayList<Rule>) intent.getSerializableExtra("rules");
        transactions=(ArrayList<Transaction>) intent.getSerializableExtra("transactions");
        registers=(ArrayList<Register>) intent.getSerializableExtra("registers");
        if(transactions==null)
            transactions = new ArrayList<>();
        TextView textoTransacciones = findViewById(R.id.textoTransacciones);
        String textoCompleto="";
        for(int i=0; i<transactions.size(); i++) {
            textoCompleto += "Transaccion " + (i + 1) + ": Fecha: " +transactions.get(i).getFechaFormateada()
                    + " Tipo: " + transactions.get(i).getTipo()
                    + " Volumen: " +transactions.get(i).getVolumen() + " Total: "
                    + transactions.get(i).getTotal() + "$\n";
        }
        textoTransacciones.setText(textoCompleto);
    }

    public void onSendCalculate(View view){
        Spinner tipos = findViewById(R.id.tipos);
        String tipo = tipos.getSelectedItem().toString();
        EditText combustible = findViewById(R.id.combustible);
        if(combustible.getText().toString().isEmpty()){
            return;
        }
        TextView total = findViewById(R.id.total);
        int volumen = Integer.parseInt(combustible.getText().toString());
        int totalReal=0;
        if(rules.size()==0){
            total.setText("No hay ninguna regla establecida");
            return;
        }
        for(Rule rule: rules){
            if (!rule.getTipo().equals(tipo)){
                total.setText("No hay ninguna regla establecida para " + tipo);
                return;
            }

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
            textoCompleto += "Transaccion " + (i + 1) + ": Fecha: " + transactions.get(i).getFechaFormateada()
                    + " Tipo: " + transactions.get(i).getTipo()
                    + " Volumen: " +transactions.get(i).getVolumen() + " Total: "
                    + transactions.get(i).getTotal() + "$\n";
        }
        textoTransacciones.setText(textoCompleto);
        combustible.setText("");
    }

    public void onChangeActivity(View view){
        Spinner actividades = findViewById(R.id.actividades);
        Intent intent;
        switch (actividades.getSelectedItem().toString()){
            case "Configurar Precio":
                intent = new Intent(this, PriceRulesActivity.class);
                intent.putExtra("rules",rules);
                intent.putExtra("transactions", transactions);
                intent.putExtra("registers", registers);
                startActivity(intent);
                break;
            case "Calcular Precio":
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