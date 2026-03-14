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
    private UserDAO userDAO;
    private RolDAO rolDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_users);
        userDAO = new UserDAO(this);
        users = userDAO.getAllUsers();
        rolDAO = new RolDAO(this);
        roles = rolDAO.getAllRoles();
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
        if (userDAO.verificarUsername(textoUsuario.getText().toString())){
            Toast.makeText(this,"Username ya existente", Toast.LENGTH_SHORT).show();
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

}