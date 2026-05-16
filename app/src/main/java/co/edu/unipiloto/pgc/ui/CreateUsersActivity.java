package co.edu.unipiloto.pgc.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RolDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Rol;
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
        rolDAO = new RolDAO(this);
        rolDAO.getAllRoles(new RolDAO.RolesCallbacK(){

            @Override
            public void onSuccess(ArrayList<Rol> roles) {
                ArrayList<String> rolesTexto = new ArrayList<>();
                CreateUsersActivity.this.roles = roles;

                for(Rol rol : roles){
                    rolesTexto.add(rol.getNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CreateUsersActivity.this,
                        R.layout.spinner_item,
                        rolesTexto
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRoles.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(CreateUsersActivity.this,
                                message,
                                Toast.LENGTH_SHORT).show()
                );
            }
        });

        spinnerRoles = findViewById(R.id.roles);



        Button btnCrear = findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(this::onCreateUser);
        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    public void onCreateUser(View view) {
        EditText textoUsuario = findViewById(R.id.textoUsuario),
                textoContrasenia = findViewById(R.id.textoContrasenia);
        if(textoUsuario.getText().toString().isEmpty()
                || textoContrasenia.getText().toString().isEmpty()){
            Toast.makeText(this,"Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        userDAO.verificarUsername(textoUsuario.getText().toString(), new UserDAO.UsernameCallback() {
            @Override
            public void onSuccess(boolean exists) {

                if (exists) {
                    runOnUiThread(() ->
                            Toast.makeText(CreateUsersActivity.this,
                                    "Username ya existente",
                                    Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                if (textoContrasenia.getText().toString().length() > 20){
                    Toast.makeText(CreateUsersActivity.this,"Contraseña muy larga", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textoUsuario.getText().toString().length() > 20){
                    Toast.makeText(CreateUsersActivity.this,"Usuario muy largo", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User();
                user.setUsername(textoUsuario.getText().toString());
                user.setPassword(textoContrasenia.getText().toString());
                for(int i=0; i<roles.size(); i++){
                    if(roles.get(i).getNombre().equals(spinnerRoles.getSelectedItem().toString())){
                        user.setRol(roles.get(i));
                    }
                }

                userDAO.insertarUsuario(user, new UserDAO.RegisterCallback() {
                    @Override
                    public void onSuccess(User user) {
                        runOnUiThread(() -> {
                            Toast.makeText(CreateUsersActivity.this, "Usuario Creado Correctamente", Toast.LENGTH_SHORT).show();
                            textoUsuario.setText("");
                            textoContrasenia.setText("");
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> Toast.makeText(CreateUsersActivity.this, message, Toast.LENGTH_SHORT).show());
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(CreateUsersActivity.this,
                                message,
                                Toast.LENGTH_SHORT).show()
                );
            }
        });

    }

}