package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.dao.TransactionDAO;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class PriceCalculatorActivity extends AppCompatActivity {

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
        transactions = transactionDAO.getAllTransactions();
        ruleDAO = new RuleDAO(this);
        rules = ruleDAO.getAllRules();
        TextView textoTransacciones = findViewById(R.id.textoTransacciones);
        String textoCompleto = "";
        for (int i = 0; i < transactions.size(); i++) {
            textoCompleto += "Transaccion " + (i + 1) + ": Fecha: " + transactions.get(i).getFechaFormateada()
                    + " Tipo: " + transactions.get(i).getTipoVehiculo()
                    + " Volumen: " + transactions.get(i).getCantidad() + " Total: "
                    + transactions.get(i).getTotal() + "$\n";
        }
        textoTransacciones.setText(textoCompleto);
    }

    public void onSendCalculate(View view) {
        Spinner tipos = findViewById(R.id.tipos);
        String tipo = tipos.getSelectedItem().toString();
        EditText combustible = findViewById(R.id.combustible);
        if (combustible.getText().toString().isEmpty()) {
            return;
        }
        TextView total = findViewById(R.id.total);
        int volumen = Integer.parseInt(combustible.getText().toString());
        int totalReal = 0;
        if (rules.size() == 0) {
            total.setText("No hay ninguna regla establecida");
            return;
        }
        for (Rule rule : rules) {
            if (!rule.getTipoVehiculo().equals(tipo)) {
                total.setText("No hay ninguna regla establecida para " + tipo);
                return;
            }

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
                    + transactions.get(i).getTotal() + "$\n";
        }
        textoTransacciones.setText(textoCompleto);
        combustible.setText("");
    }

    public void onChangeActivity(View view) {
        Spinner actividades = findViewById(R.id.actividades);
        Intent intent;
        switch (actividades.getSelectedItem().toString()) {
            case "Calcular Precio":
                break;
            case "Registrar Entrada":
                intent = new Intent(this, FuelOutletActivity.class);
                startActivity(intent);
                break;
        }
    }
}