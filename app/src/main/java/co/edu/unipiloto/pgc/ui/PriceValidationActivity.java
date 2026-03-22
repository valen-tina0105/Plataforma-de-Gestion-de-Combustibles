package co.edu.unipiloto.pgc.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.TransactionDAO;
import co.edu.unipiloto.pgc.model.Transaction;

public class PriceValidationActivity extends BaseActivity {

    private ArrayList<Transaction> transactions;
    private TransactionDAO transactionDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_validation);
        transactionDAO = new TransactionDAO(this);
        transactions = transactionDAO.getValidatedTransactions();
        TextView historial = findViewById(R.id.historial);
        String textoCompleto = "";
        for (int i = 0; i < transactions.size(); i++) {
            textoCompleto += "id: " + transactions.get(i).getId() + " Tipo: " + transactions.get(i).getTipoVehiculo()
                    + " Cantidad: " + transactions.get(i).getCantidad() + " Total: "
                    + transactions.get(i).getTotal() + " Fecha: " + transactions.get(i).getFecha()
                    + " Estacion: " + transactions.get(i).getEstacion().getId()
                    + " Estado: " + transactions.get(i).getEstado()
                    + "\n";
        }
        historial.setText(textoCompleto);
        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::onLogOut);
    }
}