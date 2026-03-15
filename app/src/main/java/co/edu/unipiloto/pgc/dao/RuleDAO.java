package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class RuleDAO {
    private DatabaseHelper dbHelper;

    public RuleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Rule> getAllRules(){
        ArrayList<Rule> rules = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT re.id, re.tipo_vehiculo, re.precio, re.fecha, u.id, u.username, u.password, r.id, r.nombre " +
                        "FROM Reglas re " +
                        "INNER JOIN Users u ON re.admin_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id",
                null
        );

        while(cursor.moveToNext()){
            Rule rule = new Rule();
            rule.setId(cursor.getInt(0));
            rule.setTipoVehiculo(cursor.getString(1));
            rule.setPrecio(cursor.getInt(2));
            rule.setFechaFormateada(cursor.getString(3));

            User user = new User();
            user.setId(cursor.getInt(4));
            user.setUsername(cursor.getString(5));
            user.setPassword(cursor.getString(6));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(7));
            rol.setNombre(cursor.getString(8));

            user.setRol(rol);
            rule.setAdmin(user);
            rules.add(rule);
        }

        cursor.close();
        return rules;
    }

    public void insertarRegla(Rule rule){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.execSQL(
                "INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id) VALUES (?,?,?,?)",
                new Object[]{rule.getTipoVehiculo(), rule.getPrecio(), rule.getFechaFormateada(),
                rule.getAdmin().getId()}
        );

    }
}
