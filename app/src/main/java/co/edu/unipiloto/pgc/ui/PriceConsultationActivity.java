package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.model.Rule;

public class PriceConsultationActivity extends BaseActivity {

    private TextView txtTipoVehiculo, txtPrecio, txtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_consultation);

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