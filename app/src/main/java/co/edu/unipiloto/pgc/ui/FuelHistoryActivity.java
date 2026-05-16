package co.edu.unipiloto.pgc.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.MovementDAO;
import co.edu.unipiloto.pgc.model.Movement;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.MovementAdapter;

public class FuelHistoryActivity extends BaseActivity {

    private MovementDAO movementDAO;
    private User user;
    private ArrayList<Movement> movements;
    private MovementAdapter adapterMovimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_history);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        movementDAO = new MovementDAO(this);
        movements = new ArrayList<>();


        ListView listaMovimientos = findViewById(R.id.listaMovimientos);

        adapterMovimientos = new MovementAdapter(this, movements);
        listaMovimientos.setAdapter(adapterMovimientos);
        movementDAO.getAllMovements(user, new MovementDAO.MovementCallback() {
            @Override
            public void onSuccess(ArrayList<Movement> movements) {
                FuelHistoryActivity.this.movements = movements;
                runOnUiThread(() -> adapterMovimientos.updateList(movements));
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(FuelHistoryActivity.this, "Error al cargar movimientos: " + message, Toast.LENGTH_SHORT).show());
            }
        });
        Button btnFiltrar = findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this::onFilter);

        setupBottomNavigation(user);

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void onFilter(View view) {
        Spinner filter = findViewById(R.id.filtro);
        int posicion = filter.getSelectedItemPosition();

        if (posicion == 0) {
            movementDAO.getAllMovements(user, new MovementDAO.MovementCallback() {
                @Override
                public void onSuccess(ArrayList<Movement> movements) {
                    FuelHistoryActivity.this.movements = movements;
                    runOnUiThread(() -> adapterMovimientos.updateList(movements));
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> Toast.makeText(FuelHistoryActivity.this, "Error al cargar movimientos: " + message, Toast.LENGTH_SHORT).show());
                }
            });
        } else if (posicion == 1) {
            movementDAO.getMovementsOrderByType(user, new MovementDAO.MovementCallback() {
                @Override
                public void onSuccess(ArrayList<Movement> movements) {
                    FuelHistoryActivity.this.movements = movements;
                    runOnUiThread(() -> adapterMovimientos.updateList(movements));
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> Toast.makeText(FuelHistoryActivity.this, "Error al cargar movimientos: " + message, Toast.LENGTH_SHORT).show());
                }
            });
        } else if (posicion == 2) {
            movementDAO.getMovementsByDate(user, new MovementDAO.MovementCallback() {
                @Override
                public void onSuccess(ArrayList<Movement> movements) {
                    FuelHistoryActivity.this.movements = movements;
                    runOnUiThread(() -> adapterMovimientos.updateList(movements));
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> Toast.makeText(FuelHistoryActivity.this, "Error al cargar movimientos: " + message, Toast.LENGTH_SHORT).show());
                }
            });
        }

    }
}
