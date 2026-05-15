package co.edu.unipiloto.pgc.dao;

import android.content.Context;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RuleDAO {

    private ApiService apiService;


    public interface RulesCallbacK {
        void onSuccess(ArrayList<Rule> rules);

        void onError(String message);
    }

    public interface RuleCallback {
        void onSuccess(Rule rule);

        void onError(String message);
    }

    public RuleDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllRules(RulesCallbacK callback) {
        Call<ArrayList<Rule>> call = apiService.getAllRulees();

        call.enqueue(new Callback<ArrayList<Rule>>() {

            @Override
            public void onResponse(Call<ArrayList<Rule>> call, Response<ArrayList<Rule>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener Rules");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Rule>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }

    public void guardarRegla(Rule rule, RuleCallback callback) {
        Call<Rule> call = apiService.saveRule(rule);

        call.enqueue(new Callback<Rule>() {
            @Override
            public void onResponse(Call<Rule> call, Response<Rule> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al crear Rule");
                }
            }

            @Override
            public void onFailure(Call<Rule> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });

    }
}
//    private DatabaseHelper dbHelper;
//
//    public RuleDAO(Context context) {
//        dbHelper = new DatabaseHelper(context);
//    }
//
//    public ArrayList<Rule> getAllRules(){
//        ArrayList<Rule> rules = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery(
//                "SELECT re.id, re.tipo_vehiculo, re.precio, re.fecha, " +
//                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
//                        "r.id, r.nombre " +
//                        "FROM Reglas re " +
//                        "INNER JOIN Users u ON re.admin_id = u.id " +
//                        "INNER JOIN Rulees r ON u.Rule_id = r.id",
//                null
//        );
//
//        while(cursor.moveToNext()){
//            Rule rule = new Rule();
//            rule.setId(cursor.getInt(0));
//            rule.setTipoVehiculo(cursor.getString(1));
//            rule.setPrecio(cursor.getDouble(2));
//            rule.setFechaFormateada(cursor.getString(3));
//
//            User user = new User();
//            user.setId(cursor.getInt(4));
//            user.setNombreCompleto(cursor.getString(5));
//            user.setUsername(cursor.getString(6));
//            user.setEmail(cursor.getString(7));
//            user.setPassword(cursor.getString(8));
//            user.setGenero(cursor.getString(9));
//            user.setDireccion(cursor.getString(10));
//            user.setFechaNacimiento(cursor.getString(11));
//
//            Rule Rule = new Rule();
//            Rule.setId(cursor.getInt(12));
//            Rule.setNombre(cursor.getString(13));
//
//            user.setRule(Rule);
//            rule.setAdmin(user);
//            rules.add(rule);
//        }
//
//        cursor.close();
//        return rules;
//    }
//
//    public void guardarRegla(Rule rule){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery(
//                "SELECT id FROM Reglas WHERE tipo_vehiculo = ?",
//                new String[]{rule.getTipoVehiculo()}
//        );
//
//        if(cursor.moveToFirst()){
//            db.execSQL(
//                    "UPDATE Reglas SET precio = ?, fecha = ?, admin_id = ? WHERE tipo_vehiculo = ?",
//                    new Object[]{
//                            rule.getPrecio(),
//                            rule.getFechaFormateada(),
//                            rule.getAdmin().getId(),
//                            rule.getTipoVehiculo()
//                    }
//            );
//        } else {
//            db.execSQL(
//                    "INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id) VALUES (?,?,?,?)",
//                    new Object[]{
//                            rule.getTipoVehiculo(),
//                            rule.getPrecio(),
//                            rule.getFechaFormateada(),
//                            rule.getAdmin().getId()
//                    }
//            );
//        }
//
//        cursor.close();
//    }