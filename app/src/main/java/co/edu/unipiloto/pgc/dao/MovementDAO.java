package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Movement;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;

public class MovementDAO {
    private DatabaseHelper dbHelper;

    public MovementDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Movement> getAllMovements(User userId) {

        ArrayList<Movement> movements = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT m.id, m.tipo, m.cantidad, m.total, m.fecha, m.estacion_id, m.tipo_movimiento, " +
                        "u.id, u.username, u.password, r.id, r.nombre " +
                        "FROM Movimientos m " +
                        "INNER JOIN Users u ON m.estacion_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE m.estacion_id = ? " +
                        "ORDER BY m.fecha ASC",
                new String[]{String.valueOf(userId.getId())}
        );

        while (cursor.moveToNext()) {

            Movement movement = new Movement();

            movement.setId(cursor.getInt(0));
            movement.setTipo(cursor.getString(1));
            movement.setCantidad(cursor.getInt(2));
            movement.setTotal(cursor.isNull(3) ? null : cursor.getInt(3));
            movement.setFecha(cursor.getString(4));
            movement.setTipoMovimiento(cursor.getString(6));

            User user = new User();
            user.setId(cursor.getInt(7));
            user.setUsername(cursor.getString(8));
            user.setPassword(cursor.getString(9));

            movement.setEstacion(user);


            Rol rol = new Rol();
            rol.setId(cursor.getInt(10));
            rol.setNombre(cursor.getString(11));

            user.setRol(rol);

            movements.add(movement);
        }

        cursor.close();
        return movements;
    }

    public ArrayList<Movement> getMovementsOrderByType(User userId) {

        ArrayList<Movement> movements = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT m.id, m.tipo, m.cantidad, m.total, m.fecha, m.estacion_id, m.tipo_movimiento, " +
                        "u.id, u.username, u.password, r.id, r.nombre " +
                        "FROM Movimientos m " +
                        "INNER JOIN Users u ON m.estacion_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE m.estacion_id = ? " +
                        "ORDER BY CASE " +
                        "WHEN m.tipo_movimiento = 'ENTRADA' THEN 0 " +
                        "WHEN m.tipo_movimiento = 'SALIDA' THEN 1 " +
                        "END, m.fecha DESC",
                new String[]{String.valueOf(userId.getId())}
        );

        while (cursor.moveToNext()) {

            Movement movement = new Movement();

            movement.setId(cursor.getInt(0));
            movement.setTipo(cursor.getString(1));
            movement.setCantidad(cursor.getInt(2));
            movement.setTotal(cursor.isNull(3) ? null : cursor.getInt(3));
            movement.setFecha(cursor.getString(4));
            movement.setTipoMovimiento(cursor.getString(6));

            User user = new User();
            user.setId(cursor.getInt(7));
            user.setUsername(cursor.getString(8));
            user.setPassword(cursor.getString(9));

            movement.setEstacion(user);

            Rol rol = new Rol();
            rol.setId(cursor.getInt(10));
            rol.setNombre(cursor.getString(11));

            user.setRol(rol);

            movements.add(movement);
        }

        cursor.close();
        return movements;
    }
    public ArrayList<Movement> getMovementsByDate(User userId) {

        ArrayList<Movement> movements = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT m.id, m.tipo, m.cantidad, m.total, m.fecha, m.estacion_id, m.tipo_movimiento, " +
                        "u.id, u.username, u.password, r.id, r.nombre " +
                        "FROM Movimientos m " +
                        "INNER JOIN Users u ON m.estacion_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE m.estacion_id = ? " +
                        "ORDER BY m.fecha DESC",
                new String[]{String.valueOf(userId.getId())}
        );

        while (cursor.moveToNext()) {

            Movement movement = new Movement();

            movement.setId(cursor.getInt(0));
            movement.setTipo(cursor.getString(1));
            movement.setCantidad(cursor.getInt(2));
            movement.setTotal(cursor.isNull(3) ? null : cursor.getInt(3));
            movement.setFecha(cursor.getString(4));
            movement.setTipoMovimiento(cursor.getString(6));

            User user = new User();
            user.setId(cursor.getInt(7));
            user.setUsername(cursor.getString(8));
            user.setPassword(cursor.getString(9));

            movement.setEstacion(user);

            Rol rol = new Rol();
            rol.setId(cursor.getInt(10));
            rol.setNombre(cursor.getString(11));

            user.setRol(rol);

            movements.add(movement);
        }

        cursor.close();
        return movements;
    }


}
