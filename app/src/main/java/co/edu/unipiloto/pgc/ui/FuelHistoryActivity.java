package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.MovementDAO;
import co.edu.unipiloto.pgc.dao.RegisterDAO;
import co.edu.unipiloto.pgc.dao.TransactionDAO;
import co.edu.unipiloto.pgc.model.Movement;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class FuelHistoryActivity extends BaseActivity {

    private MovementDAO movementDAO;
    private User user;
    private ArrayList<Movement> movements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_history);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        movementDAO = new MovementDAO(this);
        movements = movementDAO.getAllMovements();
        TextView historial = findViewById(R.id.historial);
        String textoCompleto = "";
        for (int i = 0; i < movements.size(); i++) {
            textoCompleto += "id: " + movements.get(i).getId() + " Tipo: " + movements.get(i).getTipo()
                    + " Cantidad: " + movements.get(i).getCantidad() + " Total: "
                    + movements.get(i).getTotal() + " Fecha: " + movements.get(i).getFecha()
                    + " Estacion: " + movements.get(i).getEstacionId()
                    + " Tipo de movimiento: " + movements.get(i).getTipoMovimiento()
                    + "\n";
        }
        historial.setText(textoCompleto);
    }

    public void onFilter(View view) {
        Spinner filter = findViewById(R.id.filtro);
        String filtro = filter.getSelectedItem().toString();
        TextView historial = findViewById(R.id.historial);
        if (filtro.equals("Tipo de combustible")) {
            movements = movementDAO.getMovementsOrderByType();
            String textoCompleto = "";
            for (int i = 0; i < movements.size(); i++) {
                textoCompleto += "id: " + movements.get(i).getId() + " Tipo: " + movements.get(i).getTipo()
                        + " Cantidad: " + movements.get(i).getCantidad() + " Total: "
                        + movements.get(i).getTotal() + " Fecha: " + movements.get(i).getFecha()
                        + " Estacion: " + movements.get(i).getEstacionId()
                        + " Tipo de movimiento: " + movements.get(i).getTipoMovimiento()
                        + "\n";
            }
            historial.setText(textoCompleto);
        } else if (filtro.equals("Fecha de transacción")) {
            movements = movementDAO.getMovementsByDate();
            String textoCompleto = "";
            for (int i = 0; i < movements.size(); i++) {
                textoCompleto += "id: " + movements.get(i).getId() + " Tipo: " + movements.get(i).getTipo()
                        + " Cantidad: " + movements.get(i).getCantidad() + " Total: "
                        + movements.get(i).getTotal() + " Fecha: " + movements.get(i).getFecha()
                        + " Estacion: " + movements.get(i).getEstacionId()
                        + " Tipo de movimiento: " + movements.get(i).getTipoMovimiento()
                        + "\n";
            }
            historial.setText(textoCompleto);
        }
    }

    public void onChangeActivity(View view){
        Spinner actividades = findViewById(R.id.actividades);
        Intent intent;
        switch (actividades.getSelectedItem().toString()){
            case "Calcular Precio":
                intent = new Intent(this, PriceCalculatorActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            case "Registrar Entrada":
                intent = new Intent(this, FuelOutletActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            case "Consultar Historial":
                break;
        }
    }

}