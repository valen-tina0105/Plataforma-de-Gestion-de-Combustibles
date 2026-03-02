package co.edu.unipiloto.pgc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    private ArrayList<Rule> rules;
    private ArrayList<Transaction> transactions;
    private ArrayList<User> users;
    private ArrayList<Register> registers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Intent intent = getIntent();
        rules=(ArrayList<Rule>) intent.getSerializableExtra("rules");
        transactions=(ArrayList<Transaction>) intent.getSerializableExtra("transactions");
        registers=(ArrayList<Register>) intent.getSerializableExtra("registers");

        if(registers==null)
            registers = new ArrayList<>();

        if (users == null){
            users = new ArrayList<>();
        }

        if (rules == null)
            rules = new ArrayList<>();

        if(transactions==null)
            transactions = new ArrayList<>();
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