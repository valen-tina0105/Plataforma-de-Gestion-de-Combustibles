package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.dao.UserDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.User;

public class FuelDeliveryActivity extends BaseActivity {

    private ArrayList<Delivery> deliveries;
    private ArrayList<User> stations;
    private DeliveryDAO deliveryDAO;
    private UserDAO userDAO;
    private User user;
    private Spinner estacionDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_delivery);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        deliveryDAO = new DeliveryDAO(this);
        userDAO = new UserDAO(this);
        deliveries = deliveryDAO.getAllDeliveries(user);
        stations = userDAO.getAllStations();
        estacionDestino = findViewById(R.id.estacionDestino);

        ArrayList<String> estaciones = new ArrayList<>();

        for(User user : stations){
            estaciones.add(user.getUsername());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                estaciones
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estacionDestino.setAdapter(adapter);

        TextView textoEntregas = findViewById(R.id.textoEntregas);
        String textoCompleto="";
        for(int i=0; i<deliveries.size(); i++) {
            textoCompleto += "Entrega " + deliveries.get(i).getId() + ": Fecha: " + deliveries.get(i).getFechaFormateada()
                    + " Tipo Combustible: " + deliveries.get(i).getTipoCombustible()
                    + " Cantidad: " +deliveries.get(i).getCantidad()
                    + " Estacion destino: " + deliveries.get(i).getEstacion().getUsername()
                    + " Distribuido por: " + deliveries.get(i).getDistribuidor().getUsername()
                    + "\n";
        }
        textoEntregas.setText(textoCompleto);
    }
    
    public void onRegisterDelivery(View view){
        EditText textoPlaca=findViewById(R.id.textoPlaca), textoCantidad=findViewById(R.id.textoCantidad);
        Spinner tipoCombustible=findViewById(R.id.tipoCombustible);
        if (textoPlaca.getText().toString().isEmpty()||textoCantidad.getText().toString().isEmpty()){
            Toast.makeText(this, "Todos los espacios deben ser rellenados", Toast.LENGTH_SHORT).show();
            return;
        }
        Delivery delivery = new Delivery();
        delivery.setPlaca(textoPlaca.getText().toString());
        delivery.setTipoCombustible(tipoCombustible.getSelectedItem().toString());
        delivery.setCantidad(Integer.parseInt(textoCantidad.getText().toString()));
        delivery.setEstacion(stations.get(estacionDestino.getSelectedItemPosition()));
        delivery.setDistribuidor(user);
        
        deliveries.add(delivery);
        
        deliveryDAO.insertDelivery(delivery);

        TextView textoEntregas = findViewById(R.id.textoEntregas);
        String textoCompleto="";
        for(int i=0; i<deliveries.size(); i++) {
            textoCompleto += "Entrega " + deliveries.get(i).getId() + 1 + ": Fecha: " + deliveries.get(i).getFechaFormateada()
                    + " Tipo Combustible: " + deliveries.get(i).getTipoCombustible()
                    + " Cantidad: " +deliveries.get(i).getCantidad()
                    + " Estacion destino: " + deliveries.get(i).getEstacion().getUsername()
                    + " Distribuido por: " + deliveries.get(i).getDistribuidor().getUsername()
                    + "\n";
        }
        textoEntregas.setText(textoCompleto);

        textoPlaca.setText("");
        textoCantidad.setText("");
    }
}