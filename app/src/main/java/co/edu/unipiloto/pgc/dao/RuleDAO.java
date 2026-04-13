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
                "SELECT re.id, re.tipo_vehiculo, re.precio, re.fecha, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "r.id, r.nombre " +
                        "FROM Reglas re " +
                        "INNER JOIN Users u ON re.admin_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id",
                null
        );

        while(cursor.moveToNext()){
            Rule rule = new Rule();
            rule.setId(cursor.getInt(0));
            rule.setTipoVehiculo(cursor.getString(1));
            rule.setPrecio(cursor.getDouble(2));
            rule.setFechaFormateada(cursor.getString(3));

            User user = new User();
            user.setId(cursor.getInt(4));
            user.setNombreCompleto(cursor.getString(5));
            user.setUsername(cursor.getString(6));
            user.setEmail(cursor.getString(7));
            user.setPassword(cursor.getString(8));
            user.setGenero(cursor.getString(9));
            user.setDireccion(cursor.getString(10));
            user.setFechaNacimiento(cursor.getString(11));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(12));
            rol.setNombre(cursor.getString(13));

            user.setRol(rol);
            rule.setAdmin(user);
            rules.add(rule);
        }

        cursor.close();
        return rules;
    }

    public void guardarRegla(Rule rule){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM Reglas WHERE tipo_vehiculo = ?",
                new String[]{rule.getTipoVehiculo()}
        );

        if(cursor.moveToFirst()){
            db.execSQL(
                    "UPDATE Reglas SET precio = ?, fecha = ?, admin_id = ? WHERE tipo_vehiculo = ?",
                    new Object[]{
                            rule.getPrecio(),
                            rule.getFechaFormateada(),
                            rule.getAdmin().getId(),
                            rule.getTipoVehiculo()
                    }
            );
        } else {
            db.execSQL(
                    "INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id) VALUES (?,?,?,?)",
                    new Object[]{
                            rule.getTipoVehiculo(),
                            rule.getPrecio(),
                            rule.getFechaFormateada(),
                            rule.getAdmin().getId()
                    }
            );
        }

        cursor.close();
    }

}
