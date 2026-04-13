package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Inventory;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;

public class InventoryDAO {
    private DatabaseHelper dbHelper;
    public InventoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Inventory> getAllInventories(User estacionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Inventory> inventories = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "i.id, i.cantidad_combustible, i.capacidad_maxima, i.nivel_minimo," +
                        "c.id, c.nombre, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, " +
                        "u.genero, u.direccion, u.fecha_nacimiento, " +
                        "r.id, r.nombre " +
                        "FROM Inventarios i " +
                        "INNER JOIN Combustibles c ON i.id_combustible = c.id " +
                        "INNER JOIN Users u ON i.id_estacion = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE u.id = ?",
                new String[]{String.valueOf(estacionId.getId())}
        );

        while (cursor.moveToNext()) {
            Inventory inventory = new Inventory();
            inventory.setId(cursor.getInt(0));
            inventory.setCantidadCombustible(cursor.getDouble(1));
            inventory.setCapacidadMaxima(cursor.getDouble(2));
            inventory.setNivelMinimo(cursor.getDouble(3));

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(4));
            fuel.setNombre(cursor.getString(5));

            User user = new User();
            user.setId(cursor.getInt(6));
            user.setNombreCompleto(cursor.getString(7));
            user.setUsername(cursor.getString(8));
            user.setEmail(cursor.getString(9));
            user.setPassword(cursor.getString(10));
            user.setGenero(cursor.getString(11));
            user.setDireccion(cursor.getString(12));
            user.setFechaNacimiento(cursor.getString(13));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(14));
            rol.setNombre(cursor.getString(15));

            user.setRol(rol);
            inventory.setEstacion(user);
            inventory.setCombustible(fuel);
            inventories.add(inventory);

        }
        cursor.close();
        return inventories;
    }

    public void actualizarCantidad(int inventoryId, double nuevaCantidad) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "UPDATE Inventarios SET cantidad_combustible = ? WHERE id = ?",
                new Object[]{nuevaCantidad, inventoryId}
        );
    }

}
