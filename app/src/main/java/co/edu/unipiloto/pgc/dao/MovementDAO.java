package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
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
                "SELECT m.id, m.tipo, m.cantidad, m.total, m.fecha, m.tipo_movimiento, " +
                        "e.id, e.nombre_completo, e.username, e.email, e.password, e.genero, e.direccion, e.fecha_nacimiento, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "re.id, re.nombre, " +
                        "ru.id, ru.nombre, " +
                        "c.id, c.nombre " +
                        "FROM Movimientos m " +
                        "INNER JOIN Users e ON m.estacion_id = e.id " +
                        "LEFT JOIN Users u ON m.user_id = u.id " +
                        "INNER JOIN Roles re ON e.rol_id = re.id " +
                        "LEFT JOIN Roles ru ON u.rol_id = ru.id " +
                        "INNER JOIN Combustibles c ON m.id_combustible = c.id " +
                        "WHERE m.estacion_id = ? " +
                        "ORDER BY m.fecha ASC",
                new String[]{String.valueOf(userId.getId())}
        );

        while (cursor.moveToNext()) {
            Movement movement = new Movement();
            movement.setId(cursor.getInt(0));
            movement.setTipoVehiculo(cursor.isNull(1) ? null : cursor.getString(1));

            movement.setCantidad(cursor.getDouble(2));
            movement.setTotal(cursor.isNull(3) ? null : cursor.getDouble(3));
            movement.setFecha(cursor.getString(4));
            movement.setTipoMovimiento(cursor.getString(5));

            User estacion = new User();
            estacion.setId(cursor.getInt(6));
            estacion.setNombreCompleto(cursor.getString(7));
            estacion.setUsername(cursor.getString(8));
            estacion.setEmail(cursor.getString(9));
            estacion.setPassword(cursor.getString(10));
            estacion.setGenero(cursor.getString(11));
            estacion.setDireccion(cursor.getString(12));
            estacion.setFechaNacimiento(cursor.getString(13));

            User usuario = null;
            if (!cursor.isNull(14)) {
                usuario = new User();
                usuario.setId(cursor.getInt(14));
                usuario.setNombreCompleto(cursor.getString(15));
                usuario.setUsername(cursor.getString(16));
                usuario.setEmail(cursor.getString(17));
                usuario.setPassword(cursor.getString(18));
                usuario.setGenero(cursor.getString(19));
                usuario.setDireccion(cursor.getString(20));
                usuario.setFechaNacimiento(cursor.getString(21));
                movement.setUsuario(usuario);

            }

            Rol rolEstacion = new Rol();
            rolEstacion.setId(cursor.getInt(22));
            rolEstacion.setNombre(cursor.getString(23));
            estacion.setRol(rolEstacion);

            Rol rolUsuario = null;
            if (!cursor.isNull(24)) {
                rolUsuario = new Rol();
                rolUsuario.setId(cursor.getInt(24));
                rolUsuario.setNombre(cursor.getString(25));
                usuario.setRol(rolUsuario);
            }

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(26));
            fuel.setNombre(cursor.getString(27));

            movement.setCombustible(fuel);
            movement.setEstacion(estacion);
            movements.add(movement);
        }

        cursor.close();
        return movements;
    }

    public ArrayList<Movement> getMovementsOrderByType(User userId) {

        ArrayList<Movement> movements = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "m.id, m.tipo, m.cantidad, m.total, m.fecha, m.tipo_movimiento, " +
                        "e.id, e.nombre_completo, e.username, e.email, e.password, e.genero, e.direccion, e.fecha_nacimiento, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "re.id, re.nombre, " +
                        "ru.id, ru.nombre, " +
                        "c.id, c.nombre " +
                        "FROM Movimientos m " +
                        "INNER JOIN Users e ON m.estacion_id = e.id " +
                        "LEFT JOIN Users u ON m.user_id = u.id " +
                        "INNER JOIN Roles re ON e.rol_id = re.id " +
                        "LEFT JOIN Roles ru ON u.rol_id = ru.id " +
                        "INNER JOIN Combustibles c ON m.id_combustible = c.id " +
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
            movement.setTipoVehiculo(cursor.isNull(1) ? null : cursor.getString(1));
            movement.setCantidad(cursor.getDouble(2));
            movement.setTotal(cursor.isNull(3) ? null : cursor.getDouble(3));
            movement.setFecha(cursor.getString(4));
            movement.setTipoMovimiento(cursor.getString(5));

            User estacion = new User();
            estacion.setId(cursor.getInt(6));
            estacion.setNombreCompleto(cursor.getString(7));
            estacion.setUsername(cursor.getString(8));
            estacion.setEmail(cursor.getString(9));
            estacion.setPassword(cursor.getString(10));
            estacion.setGenero(cursor.getString(11));
            estacion.setDireccion(cursor.getString(12));
            estacion.setFechaNacimiento(cursor.getString(13));

            User usuario = null;
            if (!cursor.isNull(14)) {
                usuario = new User();
                usuario.setId(cursor.getInt(14));
                usuario.setNombreCompleto(cursor.getString(15));
                usuario.setUsername(cursor.getString(16));
                usuario.setEmail(cursor.getString(17));
                usuario.setPassword(cursor.getString(18));
                usuario.setGenero(cursor.getString(19));
                usuario.setDireccion(cursor.getString(20));
                usuario.setFechaNacimiento(cursor.getString(21));

                movement.setUsuario(usuario);
            }

            Rol rolEstacion = new Rol();
            rolEstacion.setId(cursor.getInt(22));
            rolEstacion.setNombre(cursor.getString(23));
            estacion.setRol(rolEstacion);

            Rol rolUsuario = null;
            if (!cursor.isNull(24)) {
                rolUsuario = new Rol();
                rolUsuario.setId(cursor.getInt(24));
                rolUsuario.setNombre(cursor.getString(25));
                usuario.setRol(rolUsuario);
            }

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(26));
            fuel.setNombre(cursor.getString(27));

            movement.setCombustible(fuel);
            movement.setEstacion(estacion);
            movements.add(movement);
        }

        cursor.close();
        return movements;
    }

    public ArrayList<Movement> getMovementsByDate(User userId) {

        ArrayList<Movement> movements = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "m.id, m.tipo, m.cantidad, m.total, m.fecha, m.tipo_movimiento, " +
                        "e.id, e.nombre_completo, e.username, e.email, e.password, e.genero, e.direccion, e.fecha_nacimiento, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "re.id, re.nombre, " +
                        "ru.id, ru.nombre, " +
                        "c.id, c.nombre " +
                        "FROM Movimientos m " +
                        "INNER JOIN Users e ON m.estacion_id = e.id " +
                        "LEFT JOIN Users u ON m.user_id = u.id " +
                        "INNER JOIN Roles re ON e.rol_id = re.id " +
                        "LEFT JOIN Roles ru ON u.rol_id = ru.id " +
                        "INNER JOIN Combustibles c ON m.id_combustible = c.id " +
                        "WHERE m.estacion_id = ? " +
                        "ORDER BY m.fecha DESC",
                new String[]{String.valueOf(userId.getId())}
        );

        while (cursor.moveToNext()) {

            Movement movement = new Movement();
            movement.setId(cursor.getInt(0));
            movement.setTipoVehiculo(cursor.isNull(1) ? null : cursor.getString(1));
            movement.setCantidad(cursor.getDouble(2));
            movement.setTotal(cursor.isNull(3) ? null : cursor.getDouble(3));
            movement.setFecha(cursor.getString(4));
            movement.setTipoMovimiento(cursor.getString(5));

            User estacion = new User();
            estacion.setId(cursor.getInt(6));
            estacion.setNombreCompleto(cursor.getString(7));
            estacion.setUsername(cursor.getString(8));
            estacion.setEmail(cursor.getString(9));
            estacion.setPassword(cursor.getString(10));
            estacion.setGenero(cursor.getString(11));
            estacion.setDireccion(cursor.getString(12));
            estacion.setFechaNacimiento(cursor.getString(13));

            User usuario = null;
            if (!cursor.isNull(14)) {
                usuario = new User();
                usuario.setId(cursor.getInt(14));
                usuario.setNombreCompleto(cursor.getString(15));
                usuario.setUsername(cursor.getString(16));
                usuario.setEmail(cursor.getString(17));
                usuario.setPassword(cursor.getString(18));
                usuario.setGenero(cursor.getString(19));
                usuario.setDireccion(cursor.getString(20));
                usuario.setFechaNacimiento(cursor.getString(21));

                movement.setUsuario(usuario);
            }

            Rol rolEstacion = new Rol();
            rolEstacion.setId(cursor.getInt(22));
            rolEstacion.setNombre(cursor.getString(23));
            estacion.setRol(rolEstacion);

            Rol rolUsuario = null;
            if (!cursor.isNull(24)) {
                rolUsuario = new Rol();
                rolUsuario.setId(cursor.getInt(24));
                rolUsuario.setNombre(cursor.getString(25));
                usuario.setRol(rolUsuario);
            }

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(26));
            fuel.setNombre(cursor.getString(27));

            movement.setCombustible(fuel);
            movement.setEstacion(estacion);
            movements.add(movement);
        }

        cursor.close();
        return movements;
    }

}
