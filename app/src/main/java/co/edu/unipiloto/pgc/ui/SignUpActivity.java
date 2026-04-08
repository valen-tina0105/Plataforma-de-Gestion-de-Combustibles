package co.edu.unipiloto.pgc.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private Spinner spinnerRoles;


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
        spinnerRoles = findViewById(R.id.roles);

        ArrayList<String> rolesTexto = new ArrayList<>();

        for (Rol rol : roles) {
            if (!rol.getNombre().contains("Administrador"))
                rolesTexto.add(rol.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                rolesTexto
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);

        TextView txtDireccion = findViewById(R.id.direccion);
        Button btnUbicacion = findViewById(R.id.btnUbicacion);

        btnUbicacion.setOnClickListener(v -> {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                obtenerDireccion(lat, lng, txtDireccion);
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        });

        EditText fecha = findViewById(R.id.fechaNacimiento);

        fecha.setOnClickListener(v -> {

            Calendar calendario = Calendar.getInstance();

            int year = calendario.get(Calendar.YEAR);
            int month = calendario.get(Calendar.MONTH);
            int day = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        @SuppressLint("DefaultLocale") String fechaBD = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);

                        @SuppressLint("DefaultLocale") String fechaUI = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear);

                        fecha.setText(fechaUI);
                        fecha.setTag(fechaBD);

                    }, year, month, day);
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, -18);

            datePicker.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePicker.show();
        });

        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(this::onSignUp);
    }

    private void obtenerDireccion(double lat, double lng, TextView txtDireccion) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> direcciones = geocoder.getFromLocation(lat, lng, 1);

            if (direcciones != null && !direcciones.isEmpty()) {
                Address direccion = direcciones.get(0);

                String direccionTexto = direccion.getAddressLine(0);

                txtDireccion.setText(direccionTexto);
            }

        } catch (IOException e) {
            e.printStackTrace();
            txtDireccion.setText("Error obteniendo dirección");
        }
    }

    public void onSignUp(View view) {

        EditText nombre = findViewById(R.id.textoNombreCompleto);
        EditText usuario = findViewById(R.id.textoUsuario);
        EditText email = findViewById(R.id.textoEmail);
        EditText password = findViewById(R.id.textoContrasenia);
        EditText confirmPassword = findViewById(R.id.textoConfirmarContrasenia);

        TextView direccion = findViewById(R.id.direccion);
        Spinner spinnerRoles = findViewById(R.id.roles);
        RadioGroup radioGenero = findViewById(R.id.radioGenero);
        EditText fecha = findViewById(R.id.fechaNacimiento);

        if (nombre.getText().toString().isEmpty() ||
                usuario.getText().toString().isEmpty() ||
                email.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                confirmPassword.getText().toString().isEmpty() ||
                direccion.getText().toString().isEmpty() ||
                fecha.getText().toString().isEmpty()) {

            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userDAO.verificarUsername(usuario.getText().toString())) {
            Toast.makeText(this, "Username ya existente", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGenero.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Seleccione género", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadio = findViewById(selectedId);
        String genero = selectedRadio.getText().toString();
        Rol rolSeleccionado = roles.get(spinnerRoles.getSelectedItemPosition());
        User user = new User();
        user.setNombreCompleto(nombre.getText().toString());
        user.setUsername(usuario.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setGenero(genero);
        user.setDireccion(direccion.getText().toString());
        String fechaBD = (String) fecha.getTag();
        user.setFechaNacimiento(fechaBD);
        user.setRol(rolSeleccionado);

        userDAO.insertarUsuario(user);

        Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
}