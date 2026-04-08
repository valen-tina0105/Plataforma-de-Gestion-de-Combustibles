package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.dao.SubsidioDAO;
import co.edu.unipiloto.pgc.dao.TransactionDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Subsidio;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.TransactionAdapter;

public class PriceCalculatorActivity extends BaseActivity {

    private ArrayList<Transaction> transactions;

    private TransactionDAO transactionDAO;
    private ArrayList<Rule> rules;
    private RuleDAO ruleDAO;
    private User user;
    private TransactionAdapter adapterTransacciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_calculator);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        transactionDAO = new TransactionDAO(this);
        transactions = transactionDAO.getAllTransactions(user);
        ruleDAO = new RuleDAO(this);
        rules = ruleDAO.getAllRules();

        RecyclerView listaTransacciones = findViewById(R.id.listaTransacciones);
        listaTransacciones.setLayoutManager(new LinearLayoutManager(this));

        adapterTransacciones = new TransactionAdapter(transactions);
        listaTransacciones.setAdapter(adapterTransacciones);

        Button btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(this::onSendCalculate);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent sendIntent;
            if (id == R.id.nav_historial) {
                sendIntent = new Intent(this, FuelHistoryActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                return true;
            } else if (id == R.id.nav_registrar) {
                sendIntent = new Intent(this, FuelOutletActivity.class);
                sendIntent.putExtra("user", user);
                startActivity(sendIntent);
                finish();
                return true;
            } else return id == R.id.nav_calcular;
        });

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    public void onSendCalculate(View view) {

        Spinner tipoVehiculo = findViewById(R.id.tipoVehiculo);
        EditText textoCantidad = findViewById(R.id.textoCantidad);
        EditText textoUsername = findViewById(R.id.textoUsername);
        TextView total = findViewById(R.id.total);

        String tipo = tipoVehiculo.getSelectedItem().toString();

        // 🔹 Validar cantidad
        if (textoCantidad.getText().toString().isEmpty()) {
            Toast.makeText(this, "La cantidad no puede estar vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔹 Validar username
        if (textoUsername.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese username del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        int volumen = Integer.parseInt(textoCantidad.getText().toString());
        String username = textoUsername.getText().toString();

        // 🔹 Buscar usuario cliente
        UserDAO userDAO = new UserDAO(this);
        User usuarioCliente = userDAO.getUserByUsername(username);

        if (usuarioCliente == null) {
            Toast.makeText(this, "Usuario no existe", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔹 Validar reglas
        if (rules.isEmpty()) {
            Toast.makeText(this, "No hay ninguna regla establecida", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalReal = 0;
        boolean encontrada = false;

        for (Rule rule : rules) {
            if (rule.getTipoVehiculo().equalsIgnoreCase(tipo)) {
                totalReal = volumen * rule.getPrecio();
                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            Toast.makeText(this, "No hay regla para " + tipo, Toast.LENGTH_SHORT).show();
            total.setText("Regla no establecida");
            return;
        }

        // 🔥 MOSTRAR TOTAL
        total.setText("Total: " + totalReal + "$");

        // 🔹 MANEJO DE SUBSIDIO
        SubsidioDAO subsidioDAO = new SubsidioDAO(this);
        Subsidio subsidio = subsidioDAO.getSubsidioByUser(usuarioCliente);

        if (subsidio != null) {

            int saldoActual = subsidio.getSaldoDisponible();

            if (saldoActual >= totalReal) {

                // ✅ Descuento completo
                subsidio.setSaldoDisponible(saldoActual - totalReal);
                subsidioDAO.actualizarSaldo(subsidio);

                Toast.makeText(this, "Compra con subsidio aplicada", Toast.LENGTH_SHORT).show();

            } else {
                // ❌ No alcanza saldo
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 🔹 GUARDAR TRANSACCIÓN
        Transaction transaction = new Transaction();
        transaction.setTipoVehiculo(tipo);
        transaction.setCantidad(volumen);
        transaction.setTotal(totalReal);

        // estación logueada
        transaction.setEstacion(user);

        // cliente real
        transaction.setUsuario(usuarioCliente);

        transactionDAO.insertarTransaccion(transaction);

        // 🔹 ACTUALIZAR LISTA
        transactions = transactionDAO.getAllTransactions(user);
        adapterTransacciones.updateList(transactions);

        // 🔹 LIMPIAR CAMPOS
        textoCantidad.setText("");
        textoUsername.setText("");
        tipoVehiculo.setSelection(0);
    }

}