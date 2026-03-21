package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class CreateUsersActivity extends BaseActivity {

    private ArrayList<User> users;
    private ArrayList<Rol> roles;
    private UserDAO userDAO;
    private RolDAO rolDAO;
    
    private Spinner spinnerRoles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_users);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));
        userDAO = new UserDAO(this);
        users = userDAO.getAllUsers();
        rolDAO = new RolDAO(this);
        roles = rolDAO.getAllRoles();
        spinnerRoles = findViewById(R.id.roles);

        ArrayList<String> rolesTexto = new ArrayList<>();

        for(Rol rol : roles){
            rolesTexto.add(rol.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                rolesTexto
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);
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
        if (textoContrasenia.getText().toString().length() > 20){
            Toast.makeText(this,"Contraseña muy larga", Toast.LENGTH_SHORT).show();
            return;
        }
        if (textoUsuario.getText().toString().length() > 20){
            Toast.makeText(this,"Usuario muy largo", Toast.LENGTH_SHORT).show();
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