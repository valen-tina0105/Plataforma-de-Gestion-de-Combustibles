package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RolDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class CreateUsersActivity extends AppCompatActivity {

    private ArrayList<User> users;
    private ArrayList<Rol> roles;
    private UserDAO userDAO = new UserDAO(this);
    private RolDAO rolDAO = new RolDAO(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_users);
        Intent intent = getIntent();
    }

    public void onCreateUser(View view) {
        users = userDAO.getAllUsers();
        roles = rolDAO.getAllRoles();
        EditText textoUsuario = findViewById(R.id.textoUsuario),
                textoContrasenia = findViewById(R.id.textoContrasenia);
        Spinner roles = findViewById(R.id.roles);
        if(textoUsuario.getText().toString().isEmpty()
                || textoContrasenia.getText().toString().isEmpty()){
            Toast.makeText(this,"Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setUsername(textoUsuario.getText().toString());
        user.setPassword(textoContrasenia.getText().toString());
        for(int i=0; i<this.roles.size(); i++){
            if(this.roles.get(i).getNombre().equals(roles.getSelectedItem().toString())){
                user.setRol(this.roles.get(i));
            }
        }

        userDAO.insertarUsuario(user);
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