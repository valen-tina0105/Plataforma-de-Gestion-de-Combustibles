package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.FuelDAO;
import co.edu.unipiloto.pgc.dao.InventoryDAO;
import co.edu.unipiloto.pgc.dao.PriceDAO;
import co.edu.unipiloto.pgc.dao.RuleDAO;
import co.edu.unipiloto.pgc.dao.SubsidyDAO;
import co.edu.unipiloto.pgc.dao.TransactionDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Inventory;
import co.edu.unipiloto.pgc.model.Price;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Subsidy;
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
    private ArrayList<Inventory> inventories;
    private InventoryDAO inventoryDAO;
    private ArrayList<Price> prices;
    private PriceDAO priceDAO;
    private Spinner tipoCombustible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_calculator);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        transactionDAO = new TransactionDAO(this);
        transactions = transactionDAO.getAllTransactions(user);
        ruleDAO = new RuleDAO(this);
        ruleDAO.getAllRules(new RuleDAO.RulesCallbacK() {
            @Override
            public void onSuccess(ArrayList<Rule> rules) {
                PriceCalculatorActivity.this.rules = rules;
            }

            @Override
            public void onError(String message) {
                Log.e("PriceCalculatorActivity", "Error al obtener reglas: " + message);
            }
        });
        priceDAO = new PriceDAO(this);
        prices = priceDAO.getAllPrices(user);
        inventoryDAO = new InventoryDAO(this);
        inventories = inventoryDAO.getAllInventories(user);
        tipoCombustible = findViewById(R.id.tipoCombustible);

        FuelDAO fuelDAO = new FuelDAO(this);
        ArrayList<Fuel> listaCombustibles = fuelDAO.getAllFuels();

        ArrayAdapter<Fuel> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaCombustibles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoCombustible.setAdapter(adapter);

        alerta();

        RecyclerView listaTransacciones = findViewById(R.id.listaTransacciones);
        listaTransacciones.setLayoutManager(new LinearLayoutManager(this));

        adapterTransacciones = new TransactionAdapter(transactions);
        listaTransacciones.setAdapter(adapterTransacciones);

        Button btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(this::onSendCalculate);

        setupBottomNavigation();

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_calcular);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_calcular) return true;

            Intent nextIntent = null;
            if (id == R.id.nav_solicitud) {
                nextIntent = new Intent(this, RequestDeliveryActivity.class);
            } else if (id == R.id.nav_confirmar) {
                nextIntent = new Intent(this, ConfirmDeliveryActivity.class);
            } else if (id == R.id.nav_historial) {
                nextIntent = new Intent(this, FuelHistoryActivity.class);
            } else if (id == R.id.nav_inventario) {
                nextIntent = new Intent(this, InventoryManagementActivity.class);
            }

            if (nextIntent != null) {
                nextIntent.putExtra("user", user);
                startActivity(nextIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });
    }

    public void onSendCalculate(View view) {

        Spinner tipoVehiculo = findViewById(R.id.tipoVehiculo);
        EditText textoCantidad = findViewById(R.id.textoCantidad);
        EditText textoUsername = findViewById(R.id.textoUsername);
        TextView total = findViewById(R.id.total);
        String tipoDeVehiculo = tipoVehiculo.getSelectedItem().toString();

        if (textoCantidad.getText().toString().isEmpty()) {
            Toast.makeText(this, "La cantidad no puede estar vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        if (textoUsername.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese username del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(textoCantidad.getText().toString());
        String username = textoUsername.getText().toString();

        UserDAO userDAO = new UserDAO(this);

        userDAO.getUserByUsername(username, new UserDAO.LoginCallback() {

            @Override
            public void onSuccess(User usuarioCliente) {

                runOnUiThread(() -> {

                    SubsidyDAO subsidyDAO = new SubsidyDAO(PriceCalculatorActivity.this);
                    Subsidy subsidy = subsidyDAO.getSubsidyById(usuarioCliente);

                    if (rules.isEmpty()) {
                        Toast.makeText(PriceCalculatorActivity.this,
                                "No hay ninguna regla establecida",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double totalReal = 0;
                    double totalConDescuento = 0;

                    Fuel fuel = (Fuel) tipoCombustible.getSelectedItem();

                    for (Price price : prices) {

                        if (price.getCombustible().getId() == fuel.getId()) {

                            totalReal = cantidad * price.getPrecio();

                            if (subsidy != null && subsidy.getSubsidio() == 1) {

                                double porcentaje = subsidy.getPorcentaje();
                                double descuento = totalReal * (porcentaje / 100.0);
                                totalConDescuento = totalReal - descuento;

                            } else {
                                totalConDescuento = totalReal;
                            }

                            for (Inventory inventory : inventories) {

                                if (inventory.getCombustible().getId() == fuel.getId()) {

                                    double nuevaCantidad =
                                            inventory.getCantidadCombustible() - cantidad;

                                    if (nuevaCantidad < 0) {

                                        Toast.makeText(
                                                PriceCalculatorActivity.this,
                                                "No hay suficiente combustible",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        return;
                                    }

                                    inventoryDAO.actualizarCantidad(
                                            inventory.getId(),
                                            nuevaCantidad
                                    );

                                    inventory.setCantidadCombustible(nuevaCantidad);
                                }
                            }

                            break;
                        }
                    }

                    Transaction transaction = new Transaction();

                    transaction.setTipoVehiculo(tipoDeVehiculo);
                    transaction.setCombustible(fuel);
                    transaction.setCantidad(cantidad);
                    transaction.setTotal(totalConDescuento);
                    transaction.setEstacion(user);
                    transaction.setUsuario(usuarioCliente);

                    transactionDAO.insertarTransaccion(transaction);

                    transactions = transactionDAO.getAllTransactions(user);
                    adapterTransacciones.updateList(transactions);

                    textoCantidad.setText("");
                    textoUsername.setText("");
                    tipoVehiculo.setSelection(0);
                    tipoCombustible.setSelection(0);

                    alerta();
                });
            }

            @Override
            public void onError(String message) {

                runOnUiThread(() ->
                        Toast.makeText(
                                PriceCalculatorActivity.this,
                                "Usuario no existe",
                                Toast.LENGTH_SHORT
                        ).show()
                );
            }
        });
    }

    public void alerta(){

        for(Inventory inventory:inventories){
            switch(inventory.getCombustible().getId()){
                case 1:
                    if(inventory.getCantidadCombustible()<=inventory.getNivelMinimo()){
                        Snackbar.make(findViewById(android.R.id.content),
                                        "Cantidad de Gasolina Corriente muy bajo",
                                        Snackbar.LENGTH_LONG)
                                .setAction("Ver", v -> {
                                    Intent intent = new Intent(this, InventoryManagementActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                })
                                .show();
                        return;
                    }
                    break;
                case 2:
                    if(inventory.getCantidadCombustible()<=inventory.getNivelMinimo()){
                        Snackbar.make(findViewById(android.R.id.content),
                                        "Cantidad de Gasolina Extra muy bajo",
                                        Snackbar.LENGTH_LONG)
                                .setAction("Ver", v -> {
                                    Intent intent = new Intent(this, InventoryManagementActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                })
                                .show();
                        return;
                    }
                    break;
                case 3:
                    if(inventory.getCantidadCombustible()<=inventory.getNivelMinimo()){
                        Snackbar.make(findViewById(android.R.id.content),
                                        "Cantidad de ACPM(Diésel) muy bajo",
                                        Snackbar.LENGTH_LONG)
                                .setAction("Ver", v -> {
                                    Intent intent = new Intent(this, InventoryManagementActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                })
                                .show();
                        return;
                    }
                    break;
                case 4:
                    if(inventory.getCantidadCombustible()<=inventory.getNivelMinimo()){
                        Snackbar.make(findViewById(android.R.id.content),
                                        "Cantidad de Gas Natural Vehicular Corriente muy bajo",
                                        Snackbar.LENGTH_LONG)
                                .setAction("Ver", v -> {
                                    Intent intent = new Intent(this, InventoryManagementActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                })
                                .show();
                        return;
                    }
                    break;
            }
        }
    }

}
