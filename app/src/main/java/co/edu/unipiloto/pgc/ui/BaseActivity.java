package co.edu.unipiloto.pgc.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.model.User;

public class BaseActivity extends AppCompatActivity {

    protected User currentUser;

    protected void setupBottomNavigation(User currentUser) {
        this.currentUser = currentUser;
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        if (currentUser != null && currentUser.getRol() != null) {
            int rolId = currentUser.getRol().getId();
            if (rolId == 5) {
                bottomNav.getMenu().clear();
                bottomNav.inflateMenu(R.menu.bottom_nav_menu_distributor);
            } else {
                bottomNav.getMenu().clear();
                bottomNav.inflateMenu(R.menu.bottom_nav_menu);
            }
        }

        bottomNav.setSelectedItemId(R.id.nav_inventario);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent nextIntent = null;
            if (id == R.id.nav_inventario){
                nextIntent = new Intent(this, InventoryManagementActivity.class);
            }else if (id == R.id.nav_solicitud) {
                nextIntent = new Intent(this, RequestDeliveryActivity.class);
            } else if (id == R.id.nav_calcular) {
                nextIntent = new Intent(this, PriceCalculatorActivity.class);
            } else if (id == R.id.nav_confirmar) {
                nextIntent = new Intent(this, ConfirmDeliveryActivity.class);
            } else if (id == R.id.nav_historial) {
                nextIntent = new Intent(this, FuelHistoryActivity.class);
            } else if (id == R.id.nav_entregas){
                nextIntent = new Intent(this, FuelDeliveryActivity.class);
            } else if (id == R.id.nav_precios){
                nextIntent = new Intent(this, PriceManagmentActivity.class);
            } else if (id == R.id.nav_odometer) {
                nextIntent = new Intent(this, OdometerActivity.class);
            }

            if (nextIntent != null) {
                nextIntent.putExtra("user", currentUser);
                startActivity(nextIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });
    }

    protected void setupToolbar(Toolbar toolbar, User user) {
        this.currentUser = user;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_consultar) {
            if (!(this instanceof PriceConsultationActivity)) {
                intent = new Intent(this, PriceConsultationActivity.class);
            }
        } else if (id == R.id.nav_compras) {
            if (!(this instanceof GasolinePurchasesActivity)) {
                intent = new Intent(this, GasolinePurchasesActivity.class);
            }
        } else if (id == R.id.nav_informacion) {
            if (!(this instanceof UserInformationActivity)) {
                intent = new Intent(this, UserInformationActivity.class);
            }
        } else if (id == R.id.nav_logout) {
            onLogOut(null);
            return true;
        }

        if (intent != null) {
            intent.putExtra("user", currentUser);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLogOut(View view){
        Intent intent = new Intent(this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
