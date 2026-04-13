package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.Fuel;
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
                "SELECT e.id, e.placa, e.cantidad, e.fecha, " +
                        "c.id, c.nombre, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "d.id, d.nombre_completo, d.username, d.email, d.password, d.genero, d.direccion, d.fecha_nacimiento, " +
                        "ur.id, ur.nombre, " +
                        "dr.id, dr.nombre " +
                        "FROM Entregas e " +
                        "INNER JOIN Combustibles c ON e.id_combustible = c.id " +
                        "INNER JOIN Users u ON e.estacion_destino_id = u.id " +
                        "INNER JOIN Roles ur ON u.rol_id = ur.id " +
                        "INNER JOIN Users d ON e.distribuidor_id = d.id " +
                        "INNER JOIN Roles dr ON d.rol_id = dr.id " +
                        "WHERE d.id = ?",
                new String[]{String.valueOf(distribuidorId.getId())}
        );

        while(cursor.moveToNext()){
            Delivery delivery = new Delivery();
            delivery.setId(cursor.getInt(0));
            delivery.setPlaca(cursor.getString(1));
            delivery.setCantidad(cursor.getDouble(2));
            delivery.setFechaFormateada(cursor.getString(3));

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(4));
            fuel.setNombre(cursor.getString(5));

            User estacion = new User();
            estacion.setId(cursor.getInt(6));
            estacion.setNombreCompleto(cursor.getString(7));
            estacion.setUsername(cursor.getString(8));
            estacion.setEmail(cursor.getString(9));
            estacion.setPassword(cursor.getString(10));
            estacion.setGenero(cursor.getString(11));
            estacion.setDireccion(cursor.getString(12));
            estacion.setFechaNacimiento(cursor.getString(13));

            User distribuidor = new User();
            distribuidor.setId(cursor.getInt(14));
            distribuidor.setNombreCompleto(cursor.getString(15));
            distribuidor.setUsername(cursor.getString(16));
            distribuidor.setEmail(cursor.getString(17));
            distribuidor.setPassword(cursor.getString(18));
            distribuidor.setGenero(cursor.getString(19));
            distribuidor.setDireccion(cursor.getString(20));
            distribuidor.setFechaNacimiento(cursor.getString(21));

            Rol rolEstacion = new Rol();
            rolEstacion.setId(cursor.getInt(22));
            rolEstacion.setNombre(cursor.getString(23));

            Rol rolDist = new Rol();
            rolDist.setId(cursor.getInt(24));
            rolDist.setNombre(cursor.getString(25));

            estacion.setRol(rolEstacion);
            delivery.setEstacion(estacion);
            distribuidor.setRol(rolDist);
            delivery.setDistribuidor(distribuidor);
            delivery.setCombustible(fuel);
            deliveries.add(delivery);
        }

        cursor.close();
        return deliveries;
    }

    public void insertDelivery(Delivery delivery) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {

            Cursor cursor = db.rawQuery(
                    "SELECT cantidad_combustible, capacidad_maxima " +
                            "FROM Inventarios " +
                            "WHERE id_estacion = ? AND id_combustible = ?",
                    new String[]{
                            String.valueOf(delivery.getEstacion().getId()),
                            String.valueOf(delivery.getCombustible().getId())
                    }
            );

            if (!cursor.moveToFirst()) {
                cursor.close();
                throw new RuntimeException("Inventario no encontrado");
            }

            double cantidadActual = cursor.getDouble(0);
            double capacidadMaxima = cursor.getDouble(1);
            cursor.close();

            double nuevaCantidad = cantidadActual + delivery.getCantidad();

            if (nuevaCantidad > capacidadMaxima) {
                throw new RuntimeException("EXCEDE_CAPACIDAD");
            }

            db.execSQL(
                    "INSERT INTO Entregas (placa, id_combustible, cantidad, fecha, estacion_destino_id, distribuidor_id) " +
                            "VALUES (?,?,?,?,?,?)",
                    new Object[]{
                            delivery.getPlaca(),
                            delivery.getCombustible().getId(),
                            delivery.getCantidad(),
                            delivery.getFechaFormateada(),
                            delivery.getEstacion().getId(),
                            delivery.getDistribuidor().getId()
                    }
            );

            db.execSQL(
                    "UPDATE Inventarios SET cantidad_combustible = ? " +
                            "WHERE id_estacion = ? AND id_combustible = ?",
                    new Object[]{
                            nuevaCantidad,
                            delivery.getEstacion().getId(),
                            delivery.getCombustible().getId()
                    }
            );

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }
}
