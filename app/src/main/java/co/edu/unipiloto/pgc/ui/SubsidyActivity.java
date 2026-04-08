package co.edu.unipiloto.pgc.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.SubsidioDAO;
import co.edu.unipiloto.pgc.model.Subsidio;
import co.edu.unipiloto.pgc.model.User;

public class SubsidyActivity extends BaseActivity {

    private User subsidyUser;

    private TextView viewUsername;
    private TextView viewCupoTotal;
    private TextView viewSaldoDisponible;

    private SubsidioDAO subsidioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subsidy);

        subsidyUser = (User) getIntent().getSerializableExtra("user");

        viewUsername = findViewById(R.id.viewUsername);
        viewCupoTotal = findViewById(R.id.viewCupoTotal);
        viewSaldoDisponible = findViewById(R.id.viewSaldoDisponible);

        subsidioDAO = new SubsidioDAO(this);

        cargarSubsidio();

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    private void cargarSubsidio() {

        if (subsidyUser == null) {
            Toast.makeText(this, "Error al cargar usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        Subsidio subsidio = subsidioDAO.getSubsidioByUser(subsidyUser);

        viewUsername.setText("Usuario: " + subsidyUser.getUsername());

        if (subsidio != null) {

            viewCupoTotal.setText("Cupo total: " + subsidio.getCupoTotal());
            viewSaldoDisponible.setText("Saldo disponible: " + subsidio.getSaldoDisponible());

        } else {
            // 👇 importante para usuarios sin subsidio
            viewCupoTotal.setText("Cupo total: No asignado");
            viewSaldoDisponible.setText("Saldo disponible: No asignado");
        }
    }
}