package co.edu.unipiloto.pgc.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.OdometerLogDAO;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.adapters.OdometerReportAdapter;
import co.edu.unipiloto.pgc.ui.models.OdometerReportItem;

public class OdometerReportActivity extends BaseActivity {

    private OdometerLogDAO odometerLogDAO;
    private DeliveryDAO deliveryDAO;
    private RecyclerView recyclerView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odometer_report);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_gradient_end));

        user = (User) getIntent().getSerializableExtra("user");
        odometerLogDAO = new OdometerLogDAO(this);
        deliveryDAO = new DeliveryDAO(this);

        recyclerView = findViewById(R.id.recyclerReport);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupToolbar(findViewById(R.id.toolbar), user);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.odometer_report_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadReport();
    }

    private void loadReport() {
        deliveryDAO.getDeliveriesByState(user, "PENDIENTE", new DeliveryDAO.ApiCallback<ArrayList<Delivery>>() {
            @Override
            public void onSuccess(ArrayList<Delivery> result) {
                List<Integer> deliveryIds = new ArrayList<>();
                if (result != null) {
                    for (Delivery delivery : result) {
                        deliveryIds.add(delivery.getId());
                    }
                }
                runOnUiThread(() -> loadReportForDeliveries(deliveryIds));
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> loadReportForDeliveries(new ArrayList<>()));
            }
        });
    }

    private void loadReportForDeliveries(List<Integer> deliveryIds) {
        Cursor cursor = odometerLogDAO.getConsolidatedReportByDeliveryIds(deliveryIds);
        ArrayList<OdometerReportItem> items = new ArrayList<>();
        double total = 0.0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int deliveryId = cursor.getInt(0);
                double distance = cursor.getDouble(1);
                total += distance;
                items.add(new OdometerReportItem(deliveryId, distance));
            }
            cursor.close();
        }
        recyclerView.setAdapter(new OdometerReportAdapter(items));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
