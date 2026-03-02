package co.edu.unipiloto.pgc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CreateUsersActivity extends AppCompatActivity {

    private ArrayList<Rule> rules;
    private ArrayList<Transaction> transactions;
    private ArrayList<User> users;
    private ArrayList<Register> registers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_users);
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

    public void onCreateUser(View view) {
        EditText textoUsuario = findViewById(R.id.textoUsuario),
                textoContrasenia = findViewById(R.id.textoContrasenia);
        Spinner roles = findViewById(R.id.roles);
        if(textoUsuario.getText().toString().isEmpty()
                || textoContrasenia.getText().toString().isEmpty()){
            Toast.makeText(this,"Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        users.add(new User(textoUsuario.getText().toString(),
                roles.getSelectedItem().toString(),
                textoContrasenia.getText().toString()));
        Toast.makeText(this, "Usuario Creado Correctamente", Toast.LENGTH_SHORT).show();
        textoUsuario.setText("");
        textoContrasenia.setText("");
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