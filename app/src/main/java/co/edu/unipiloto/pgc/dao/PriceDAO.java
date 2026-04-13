package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Price;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;

public class PriceDAO {

    private DatabaseHelper dbHelper;

    public PriceDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Price> getAllPrices(User estacionId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Price> prices = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "p.id, p.precio, " +
                        "c.id, c.nombre, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, " +
                        "u.genero, u.direccion, u.fecha_nacimiento, " +
                        "r.id, r.nombre " +
                        "FROM Precios p " +
                        "INNER JOIN Combustibles c ON p.id_combustible = c.id " +
                        "INNER JOIN Users u ON p.id_estacion = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE u.id = ?",
                new String[]{String.valueOf(estacionId.getId())}
        );

        while (cursor.moveToNext()) {
            Price price = new Price();
            price.setId(cursor.getInt(0));
            price.setPrecio(cursor.getDouble(1));

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(2));
            fuel.setNombre(cursor.getString(3));

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
            price.setCombustible(fuel);
            price.setEstacion(user);
            prices.add(price);


        }
        cursor.close();
        return prices;
    }
}
