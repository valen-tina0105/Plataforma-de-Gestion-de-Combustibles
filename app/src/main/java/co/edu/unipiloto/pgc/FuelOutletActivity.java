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

public class FuelOutletActivity extends AppCompatActivity {

    private ArrayList<Rule> rules;
    private ArrayList<Transaction> transactions;
    private ArrayList<User> users;
    private ArrayList<Register> registers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_outlet);
        Intent intent = getIntent();
        rules=(ArrayList<Rule>) intent.getSerializableExtra("rules");
        transactions=(ArrayList<Transaction>) intent.getSerializableExtra("transactions");
        registers=(ArrayList<Register>) intent.getSerializableExtra("registers");
        if (registers == null)
            registers = new ArrayList<>();
        TextView textoRegistros = findViewById(R.id.textoRegistros);
        String textoCompleto="";
        for(int i=0; i<registers.size(); i++) {
            textoCompleto += "Entrada " + (i + 1) + ": Fecha: " + registers.get(i).getFechaFormateada()
                    + " Tipo: " + registers.get(i).getTipo()
                    + " Cantidad: " +registers.get(i).getCantidad() + "$\n";
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
        registers.add(new Register(tipoCombustible.getSelectedItem().toString(), Integer.parseInt(textoCantidad.getText().toString())));
        String textoCompleto="";
        for(int i=0; i<registers.size(); i++) {
            textoCompleto += "Entrada " + (i + 1) + ": Fecha: " + registers.get(i).getFechaFormateada()
                    + " Tipo: " + registers.get(i).getTipo()
                    + " Cantidad: " +registers.get(i).getCantidad() + "gal\n";
        }
        textoRegistros.setText(textoCompleto);
        textoCantidad.setText("");
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
            case "Registar Entrada":
                break;
        }
    }
}