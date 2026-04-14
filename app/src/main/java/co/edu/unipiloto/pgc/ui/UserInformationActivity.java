package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.SubsidyDAO;
import co.edu.unipiloto.pgc.model.Subsidy;
import co.edu.unipiloto.pgc.model.User;

public class UserInformationActivity extends BaseActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtUsername = findViewById(R.id.txtUsername);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtDireccion = findViewById(R.id.txtDireccion);
        TextView txtFecha = findViewById(R.id.txtFecha);
        TextView txtGenero = findViewById(R.id.txtGenero);

        txtNombre.setText("👤 Nombre: " + user.getNombreCompleto());
        txtUsername.setText("🆔 Usuario: " + user.getUsername());
        txtEmail.setText("📧 Email: " + user.getEmail());
        txtDireccion.setText("📍 Dirección: " + user.getDireccion());
        txtFecha.setText("🎂 Nacimiento: " + user.getFechaNacimiento());
        txtGenero.setText("⚧️ Género: " + user.getGenero());

        TextView txtTieneSubsidio = findViewById(R.id.txtTieneSubsidio);
        TextView txtPorcentaje = findViewById(R.id.txtPorcentaje);

        SubsidyDAO subsidioDAO = new SubsidyDAO(this);
        Subsidy subsidio = subsidioDAO.getSubsidyById(user);

        if(subsidio != null && subsidio.getSubsidio() == 1){
            txtTieneSubsidio.setText("✔ Tiene subsidio");
            txtTieneSubsidio.setTextColor(getResources().getColor(android.R.color.holo_green_light));

            txtPorcentaje.setText("Descuento: " + subsidio.getPorcentaje() + "%");
        } else {
            txtTieneSubsidio.setText("✘ No tiene subsidio");
            txtTieneSubsidio.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            txtPorcentaje.setVisibility(View.GONE);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent sendIntent;
            if (id == R.id.nav_consultar) {
                sendIntent = new Intent(this, PriceConsultationActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                return true;
            }  else if (id == R.id.nav_compras) {
                sendIntent = new Intent(this, GasolinePurchasesActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                    return true;
            } else return id == R.id.nav_informacion;
        });

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }
}