package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.dao.TransactionDAO;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class PriceCalculatorActivity extends BaseActivity {

    private ArrayList<Transaction> transactions;

    private TransactionDAO transactionDAO;
    private ArrayList<Rule> rules;
    private RuleDAO ruleDAO;
    private User user;

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
        TextView textoTransacciones = findViewById(R.id.textoTransacciones);
        String textoCompleto = "";
        for (int i = 0; i < transactions.size(); i++) {
            textoCompleto += "Transaccion " + transactions.get(i).getId() + ": Fecha: " + transactions.get(i).getFechaFormateada()
                    + " Tipo: " + transactions.get(i).getTipoVehiculo()
                    + " Volumen: " + transactions.get(i).getCantidad() + " Total: "
                    + transactions.get(i).getTotal()
                    + "$ Cobrado por: " + transactions.get(i).getEstacion().getUsername() + "\n";
        }
        textoTransacciones.setText(textoCompleto);
        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    public void onSendCalculate(View view) {
        Spinner tipoVehiculo = findViewById(R.id.tipoVehiculo);
        String tipo = tipoVehiculo.getSelectedItem().toString();
        EditText textoCantidad = findViewById(R.id.textoCantidad);
        if (textoCantidad.getText().toString().isEmpty()) {
            Toast.makeText(this, "La cantidad no puede estar vacia", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView total = findViewById(R.id.total);
        int volumen = Integer.parseInt(textoCantidad.getText().toString());
        int totalReal = 0;
        if (rules.isEmpty()) {
            Toast.makeText(this, "No hay ninguna regla establecida", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean encontrada = false;
        for (Rule rule : rules) {
            if (rule.getTipoVehiculo().equals(tipo)) {
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            Toast.makeText(this, "No hay ninguna regla establecida para " + tipo, Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getTipoVehiculo().equalsIgnoreCase(tipo)) {
                totalReal = volumen * rules.get(i).getPrecio();
                total.setText("Total: " + totalReal + "$");
                break;
            } else {
                total.setText("Regla no establecida");
            }
        }
        TextView textoTransacciones = findViewById(R.id.textoTransacciones);
        Transaction transaction = new Transaction();
        transaction.setTipoVehiculo(tipo);
        transaction.setCantidad(volumen);
        transaction.setTotal(totalReal);
        transaction.setEstacion(user);
        transactions.add(transaction);
        transactionDAO.insertarTransaccion(transaction);
        String textoCompleto = "";
        for (int i = 0; i < transactions.size(); i++) {
            textoCompleto += "Transaccion " + (i + 1) + ": Fecha: " + transactions.get(i).getFechaFormateada()
                    + " Tipo: " + transactions.get(i).getTipoVehiculo()
                    + " Volumen: " + transactions.get(i).getCantidad() + " Total: "
                    + "$ Cobrado por: " + transactions.get(i).getEstacion().getUsername() + transactions.get(i).getTotal() + "\n";
        }
        textoTransacciones.setText(textoCompleto);
        textoCantidad.setText("");
    }

    public void onChangeActivity(View view) {
        Spinner actividades = findViewById(R.id.actividades);
        Intent intent;
        switch (actividades.getSelectedItem().toString()) {
            case "Calcular Precio":
                break;
            case "Registrar Entrada":
                intent = new Intent(this, FuelOutletActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            case "Consultar Historial":
                intent = new Intent(this, FuelHistoryActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
        }
    }
}