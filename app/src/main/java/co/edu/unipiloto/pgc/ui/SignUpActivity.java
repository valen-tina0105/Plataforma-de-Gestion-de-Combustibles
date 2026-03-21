package co.edu.unipiloto.pgc.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RolDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;

public class SignUpActivity extends AppCompatActivity {

    private ArrayList<User> users;
    private ArrayList<Rol> roles;
    private UserDAO userDAO;
    private RolDAO rolDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_blue));
        userDAO = new UserDAO(this);
        users = userDAO.getAllUsers();
        rolDAO = new RolDAO(this);
        roles = rolDAO.getAllRoles();

    }

    public void onSignUp(View view) {
        EditText textoUsuario = findViewById(R.id.textoUsuario),
                textoContrasenia = findViewById(R.id.textoContrasenia);
        if (textoUsuario.getText().toString().isEmpty()
                || textoContrasenia.getText().toString().isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userDAO.verificarUsername(textoUsuario.getText().toString())) {
            Toast.makeText(this, "Username ya existente", Toast.LENGTH_SHORT).show();
            return;
        }
        if (textoContrasenia.getText().toString().length() > 20) {
            Toast.makeText(this, "Contraseña muy larga", Toast.LENGTH_SHORT).show();
            return;
        }
        if (textoUsuario.getText().toString().length() > 20) {
            Toast.makeText(this, "Usuario muy largo", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.setUsername(textoUsuario.getText().toString());
        user.setPassword(textoContrasenia.getText().toString());
        user.setRol(this.roles.get(5));

        userDAO.insertarUsuario(user);
        Toast.makeText(this, "Usuario Creado Correctamente", Toast.LENGTH_SHORT).show();
        textoUsuario.setText("");
        textoContrasenia.setText("");
    }
}