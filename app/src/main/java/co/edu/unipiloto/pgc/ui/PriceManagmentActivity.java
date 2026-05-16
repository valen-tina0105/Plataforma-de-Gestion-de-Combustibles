package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.PriceDAO;
import co.edu.unipiloto.pgc.dto.UpdatePriceRequestDTO;
import co.edu.unipiloto.pgc.model.Price;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.PriceManagmentAdapter;

public class PriceManagmentActivity extends BaseActivity {

    private PriceDAO priceDAO;
    private ArrayList<Price> prices;
    private RecyclerView listaPrecios;
    private PriceManagmentAdapter adapterPrecios;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_managment);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        listaPrecios = findViewById(R.id.listaPrecios);
        listaPrecios.setLayoutManager(new LinearLayoutManager(this));



        prices = new ArrayList<>();
        priceDAO = new PriceDAO(this);

        priceDAO.getAllPrices(user, new PriceDAO.PricesCallback() {
            @Override
            public void onSuccess(ArrayList<Price> pricesList) {
                prices = pricesList;
                adapterPrecios = new PriceManagmentAdapter(prices, priceDAO);
                listaPrecios.setAdapter(adapterPrecios);
            }

            @Override
            public void onError(String message) {
                Log.e("PriceManagmentActivity", "Error al obtener precios: " + message);
            }
        });

        setupBottomNavigation(user);

        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);

    }
}