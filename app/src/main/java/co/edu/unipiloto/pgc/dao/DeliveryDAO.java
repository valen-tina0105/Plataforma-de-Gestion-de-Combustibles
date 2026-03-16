package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;

public class DeliveryDAO {
    private DatabaseHelper dbHelper;

    public DeliveryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Delivery> getAllDeliveries(User distribuidorId){
        ArrayList<Delivery> deliveries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT e.id, e.placa, e.tipo_combustible, e.cantidad, e.fecha, " +
                        "u.id, u.username, u.password, r.id, r.nombre, " +
                        "d.id, d.username, d.password, dr.id, dr.nombre " +
                        "FROM Entregas e " +
                        "INNER JOIN Users u ON e.estacion_destino_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "INNER JOIN Users d ON e.distribuidor_id = d.id " +
                        "INNER JOIN Roles dr ON d.rol_id = dr.id " +
                        "WHERE d.id = ?",
                new String[]{String.valueOf(distribuidorId.getId())}
        );

        while(cursor.moveToNext()){

            Delivery delivery = new Delivery();
            delivery.setId(cursor.getInt(0));
            delivery.setPlaca(cursor.getString(1));
            delivery.setTipoCombustible(cursor.getString(2));
            delivery.setCantidad(cursor.getInt(3));
            delivery.setFechaFormateada(cursor.getString(4));

            User estacion = new User();
            estacion.setId(cursor.getInt(5));
            estacion.setUsername(cursor.getString(6));
            estacion.setPassword(cursor.getString(7));

            Rol rolEstacion = new Rol();
            rolEstacion.setId(cursor.getInt(8));
            rolEstacion.setNombre(cursor.getString(9));

            estacion.setRol(rolEstacion);
            delivery.setEstacion(estacion);

            User distribuidor = new User();
            distribuidor.setId(cursor.getInt(10));
            distribuidor.setUsername(cursor.getString(11));
            distribuidor.setPassword(cursor.getString(12));

            Rol rolDist = new Rol();
            rolDist.setId(cursor.getInt(13));
            rolDist.setNombre(cursor.getString(14));

            distribuidor.setRol(rolDist);
            delivery.setDistribuidor(distribuidor);

            deliveries.add(delivery);
        }

        cursor.close();
        return deliveries;
    }

    public void insertDelivery(Delivery delivery){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "INSERT INTO Entregas (placa, tipo_combustible, cantidad, fecha, estacion_destino_id, distribuidor_id) " +
                        "VALUES (?,?,?,?,?,?)",
                new Object[]{
                        delivery.getPlaca(),
                        delivery.getTipoCombustible(),
                        delivery.getCantidad(),
                        delivery.getFechaFormateada(),
                        delivery.getEstacion().getId(),
                        delivery.getDistribuidor().getId()
                }
        );
    }
}
