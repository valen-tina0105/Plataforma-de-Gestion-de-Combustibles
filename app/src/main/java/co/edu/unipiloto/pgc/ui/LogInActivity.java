package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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

public class LogInActivity extends AppCompatActivity {
    private ArrayList<User> users;
    private ArrayList<Rol> roles;
    private UserDAO userDAO;
    private RolDAO rolDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Intent intent = getIntent();
        userDAO = new UserDAO(this);
        users = userDAO.getAllUsers();
        rolDAO = new RolDAO(this);
        roles = rolDAO.getAllRoles();
    }

    public void onLogIn(View view){
        EditText textoUsuario = findViewById(R.id.textoUsuario),
                textoContrasenia = findViewById(R.id.textoContrasenia);
        User user = userDAO.logIn(textoUsuario.getText().toString(),textoContrasenia.getText().toString());
        Intent intent;
        switch (user.getRol().getNombre()) {
            case "Estacion de servicio":
                intent = new Intent(this, FuelOutletActivity.class);
                startActivity(intent);
                break;
            case "Distribuidor mayorista":
                break;
            case "Autoridad reguladora":
                break;
            case "Administrador de usuarios":
                intent = new Intent(this, CreateUsersActivity.class);
                startActivity(intent);
                break;
            case "Administrador de reglas":
                intent = new Intent(this, PriceRulesActivity.class);
                startActivity(intent);
                break;
        }
    }

}