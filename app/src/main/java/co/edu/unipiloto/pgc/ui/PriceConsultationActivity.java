package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.User;

public class PriceConsultationActivity extends BaseActivity {

    private TextView txtTipoVehiculo, txtPrecio, txtFecha;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_consultation);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent sendIntent;
            if (id == R.id.nav_compras) {
                sendIntent = new Intent(this, GasolinePurchasesActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                return true;
            } else if (id == R.id.nav_informacion) {
                sendIntent = new Intent(this, UserInformationActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);                    return true;
            } else return id == R.id.nav_consultar;
        });

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);

        txtTipoVehiculo = findViewById(R.id.txtTipoVehiculo);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtFecha = findViewById(R.id.txtFecha);

        cargarPrecio();
    }

    @SuppressLint("SetTextI18n")
    private void cargarPrecio() {
        RuleDAO ruleDAO = new RuleDAO(this);
        ArrayList<Rule> rules = ruleDAO.getAllRules();

        for (Rule rule : rules) {
            if (rule.getId() == 1) {
                txtTipoVehiculo.setText("Tipo: " + rule.getTipoVehiculo());
                txtPrecio.setText("Precio: $" + rule.getPrecio());
                txtFecha.setText("Actualizado: " + rule.getFechaFormateada());
                break;
            }
        }
    }
}