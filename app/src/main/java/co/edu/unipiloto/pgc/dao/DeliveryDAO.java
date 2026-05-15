package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.User;

public class DeliveryDAO {

    private DatabaseHelper dbHelper;

    public DeliveryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertDelivery(Delivery delivery) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "INSERT INTO Entregas (placa, id_combustible, cantidad, fecha, estado, estacion_destino_id, distribuidor_id) " +
                        "VALUES (?,?,?,?,?,?,?)",
                new Object[]{
                        delivery.getPlaca(),
                        delivery.getCombustible().getId(),
                        delivery.getCantidad(),
                        delivery.getFechaFormateada(),
                        "PENDIENTE",
                        delivery.getEstacion().getId(),
                        delivery.getDistribuidor().getId()
                }
        );
    }

    public ArrayList<Delivery> getDeliveriesByState(User user, String estado) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Delivery> list = new ArrayList<>();

        String query = "SELECT e.id, e.placa, e.cantidad, e.fecha, e.estado, " +
                "c.id, c.nombre, " +
                "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                "d.id, d.nombre_completo, d.username, d.email, d.password, d.genero, d.direccion, d.fecha_nacimiento, " +
                "ur.id, ur.nombre, dr.id, dr.nombre " +
                "FROM Entregas e " +
                "INNER JOIN Combustibles c ON e.id_combustible = c.id " +
                "INNER JOIN Users u ON e.estacion_destino_id = u.id " +
                "INNER JOIN Roles ur ON u.rol_id = ur.id " +
                "INNER JOIN Users d ON e.distribuidor_id = d.id " +
                "INNER JOIN Roles dr ON d.rol_id = dr.id ";

        String where;
        String[] args;

        if (user.getRol().getId() == 5) { // Distribuidor
            where = "WHERE d.id = ? AND e.estado = ?";
            args = new String[]{String.valueOf(user.getId()), estado};
        } else { // Estacion
            where = "WHERE u.id = ? AND e.estado = ?";
            args = new String[]{String.valueOf(user.getId()), estado};
        }

        Cursor cursor = db.rawQuery(query + where, args);

        while (cursor.moveToNext()) {
            list.add(mapDelivery(cursor));
        }

        cursor.close();
        return list;
    }

    public void markAsDelivered(int deliveryId, String placa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(
                "UPDATE Entregas SET estado = 'ENTREGADO', placa = ? WHERE id = ? AND estado = 'PENDIENTE'",
                new Object[]{placa, deliveryId}
        );
    }

    public void confirmDelivery(int deliveryId, int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery(
                    "SELECT id_combustible, cantidad, estacion_destino_id, estado " +
                            "FROM Entregas WHERE id = ?",
                    new String[]{String.valueOf(deliveryId)}
            );

            if (!cursor.moveToFirst()) {
                cursor.close();
                throw new RuntimeException("Entrega no encontrada");
            }

            int idCombustible = cursor.getInt(0);
            double cantidad = cursor.getDouble(1);
            int estacionId = cursor.getInt(2);
            String estado = cursor.getString(3);
            cursor.close();

            if (!"ENTREGADO".equals(estado)) {
                throw new RuntimeException("Solo se pueden confirmar entregas con estado ENTREGADO");
            }

            Cursor inv = db.rawQuery(
                    "SELECT cantidad_combustible, capacidad_maxima " +
                            "FROM Inventarios WHERE id_estacion = ? AND id_combustible = ?",
                    new String[]{String.valueOf(estacionId), String.valueOf(idCombustible)}
            );

            if (!inv.moveToFirst()) {
                inv.close();
                throw new RuntimeException("Inventario no encontrado");
            }

            double actual = inv.getDouble(0);
            double max = inv.getDouble(1);
            inv.close();

            double nueva = actual + cantidad;

            if (nueva > max) {
                throw new RuntimeException("EXCEDE_CAPACIDAD");
            }

            db.execSQL(
                    "UPDATE Entregas SET estado = 'CONFIRMADO', fecha_confirmacion = CURRENT_TIMESTAMP, confirmado_por = ? WHERE id = ?",
                    new Object[]{userId, deliveryId}
            );

            db.execSQL(
                    "UPDATE Inventarios SET cantidad_combustible = ? WHERE id_estacion = ? AND id_combustible = ?",
                    new Object[]{nueva, estacionId, idCombustible}
            );

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    private Delivery mapDelivery(Cursor cursor) {
        Delivery d = new Delivery();
        d.setId(cursor.getInt(0));
        d.setPlaca(cursor.getString(1));
        d.setCantidad(cursor.getDouble(2));
        d.setFechaFormateada(cursor.getString(3));
        d.setEstado(cursor.getString(4));

        Fuel fuel = new Fuel();
        fuel.setId(cursor.getInt(5));
        fuel.setNombre(cursor.getString(6));

        User estacion = new User();
        estacion.setId(cursor.getInt(7));
        estacion.setNombreCompleto(cursor.getString(8));
        estacion.setUsername(cursor.getString(9));

        User distribuidor = new User();
        distribuidor.setId(cursor.getInt(15));
        distribuidor.setNombreCompleto(cursor.getString(16));
        distribuidor.setUsername(cursor.getString(17));

        d.setCombustible(fuel);
        d.setEstacion(estacion);
        d.setDistribuidor(distribuidor);

        return d;
    }
}
