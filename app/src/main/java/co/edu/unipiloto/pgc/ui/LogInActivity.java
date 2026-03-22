package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RolDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class LogInActivity extends BaseActivity {
    private ArrayList<User> users;
    private ArrayList<Rol> roles;
    private UserDAO userDAO;
    private RolDAO rolDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_blue));
        userDAO = new UserDAO(this);
        users = userDAO.getAllUsers();
        rolDAO = new RolDAO(this);
        roles = rolDAO.getAllRoles();
        TextView signUp = findViewById(R.id.signUp);

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this::onLogIn);
    }

    public void onLogIn(View view) {
        EditText textoUsuario = findViewById(R.id.textoUsuario),
                textoContrasenia = findViewById(R.id.textoContrasenia);
        User user = userDAO.logIn(
                textoUsuario.getText().toString().trim(),
                textoContrasenia.getText().toString().trim()
        );
        if (user == null) {
            Toast.makeText(this, "Usuario o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent;
        switch (user.getRol().getNombre()) {
            case "Estacion de servicio":
                intent = new Intent(this, FuelOutletActivity.class);
                intent.putExtra("user", user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case "Distribuidor mayorista":
                intent = new Intent(this, FuelDeliveryActivity.class);
                intent.putExtra("user", user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case "Autoridad reguladora":
                intent = new Intent(this, PriceValidationActivity.class);
                intent.putExtra("user", user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case "Administrador de usuarios":
                intent = new Intent(this, CreateUsersActivity.class);
                intent.putExtra("user", user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case "Administrador de reglas":
                intent = new Intent(this, PriceRulesActivity.class);
                intent.putExtra("user", user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case "Usuario vehiculo particular":
                break;
        }
    }

}