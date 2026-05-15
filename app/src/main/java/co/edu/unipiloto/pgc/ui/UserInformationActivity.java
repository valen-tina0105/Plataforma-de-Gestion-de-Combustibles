package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.SubsidyDAO;
import co.edu.unipiloto.pgc.model.Subsidy;
import co.edu.unipiloto.pgc.model.User;

public class UserInformationActivity extends BaseActivity {

    private User user;
    private TextView txtTieneSubsidio, txtPorcentaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        setupToolbar(findViewById(R.id.toolbar), user);

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

        txtTieneSubsidio = findViewById(R.id.txtTieneSubsidio);
        txtPorcentaje = findViewById(R.id.txtPorcentaje);

        loadSubsidyInfo();
    }

    private void loadSubsidyInfo() {
        SubsidyDAO subsidioDAO = new SubsidyDAO(this);
        subsidioDAO.getSubsidyById(user, new SubsidyDAO.SubsidyCallback() {
            @Override
            public void onSuccess(Subsidy subsidio) {
                if(subsidio != null && subsidio.getSubsidio() == 1){
                    txtTieneSubsidio.setText("✔ Tiene subsidio");
                    txtTieneSubsidio.setTextColor(getResources().getColor(android.R.color.holo_green_light));

                    txtPorcentaje.setVisibility(View.VISIBLE);
                    txtPorcentaje.setText("Descuento: " + subsidio.getPorcentaje() + "%");
                } else {
                    txtTieneSubsidio.setText("✘ No tiene subsidio");
                    txtTieneSubsidio.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    txtPorcentaje.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
                txtTieneSubsidio.setText("✘ Error al verificar subsidio");
                txtPorcentaje.setVisibility(View.GONE);
            }
        });
    }
}
