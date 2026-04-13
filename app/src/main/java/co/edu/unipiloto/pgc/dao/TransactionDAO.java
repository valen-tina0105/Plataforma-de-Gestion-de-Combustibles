package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class TransactionDAO {
    private DatabaseHelper dbHelper;

    public TransactionDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Transaction> getAllTransactions(User userId){
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT t.id, t.tipo_vehiculo, t.cantidad, t.total, t.fecha, " +
                        "c.id, c.nombre, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "r.id, r.nombre " +
                        "FROM Transacciones t " +
                        "INNER JOIN Combustibles c ON t.id_combustible = c.id " +
                        "INNER JOIN Users u ON t.user_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE t.estacion_id = ?",
                new String[]{String.valueOf(userId.getId())}
        );

        while(cursor.moveToNext()){
            Transaction transaction = new Transaction();
            transaction.setId(cursor.getInt(0));
            transaction.setTipoVehiculo(cursor.getString(1));
            transaction.setCantidad(cursor.getDouble(2));
            transaction.setTotal(cursor.getDouble(3));
            transaction.setFechaFormateada(cursor.getString(4));

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(5));
            fuel.setNombre(cursor.getString(6));

            User user = new User();
            user.setId(cursor.getInt(7));
            user.setNombreCompleto(cursor.getString(8));
            user.setUsername(cursor.getString(9));
            user.setEmail(cursor.getString(10));
            user.setPassword(cursor.getString(11));
            user.setGenero(cursor.getString(12));
            user.setDireccion(cursor.getString(13));
            user.setFechaNacimiento(cursor.getString(14));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(15));
            rol.setNombre(cursor.getString(16));

            user.setRol(rol);
            transaction.setUsuario(user);
            transaction.setEstacion(userId);
            transaction.setCombustible(fuel);

            transactions.add(transaction);
        }

        cursor.close();
        return transactions;
    }

    public ArrayList<Transaction> getValidatedTransactions(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT t.id, t.tipo_vehiculo, t.cantidad, t.total, t.fecha, " +
                        "c.id, c.nombre, " +
                        "p.precio, (p.precio * t.cantidad), " +
                        "e.id, e.nombre_completo, e.username, e.email, e.password, e.genero, e.direccion, e.fecha_nacimiento, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "roe.id, roe.nombre, " +
                        "rou.id, rou.nombre, " +
                        "CASE WHEN t.total = (p.precio * t.cantidad) " +
                        "THEN 'CUMPLE' ELSE 'NO CUMPLE' END " +
                        "FROM Transacciones t " +
                        "INNER JOIN Combustibles c ON t.id_combustible = c.id " +
                        "INNER JOIN Precios p ON p.id_combustible = t.id_combustible " +
                        "AND p.id_estacion = t.estacion_id " +
                        "INNER JOIN Users u ON t.estacion_id = u.id " +
                        "INNER JOIN Roles rou ON u.rol_id = rou.id " +
                        "INNER JOIN Users e ON t.estacion_id = e.id " +
                        "INNER JOIN Roles roe ON e.rol_id = roe.id ",
                null
        );

        while(cursor.moveToNext()){
            Transaction transaction = new Transaction();
            transaction.setId(cursor.getInt(0));
            transaction.setTipoVehiculo(cursor.getString(1));
            transaction.setCantidad(cursor.getDouble(2));
            transaction.setTotal(cursor.getDouble(3));
            transaction.setFechaFormateada(cursor.getString(4));

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(5));
            fuel.setNombre(cursor.getString(6));

            User estacion = new User();
            estacion.setId(cursor.getInt(9));
            estacion.setNombreCompleto(cursor.getString(10));
            estacion.setUsername(cursor.getString(11));
            estacion.setEmail(cursor.getString(12));
            estacion.setPassword(cursor.getString(13));
            estacion.setGenero(cursor.getString(14));
            estacion.setDireccion(cursor.getString(15));
            estacion.setFechaNacimiento(cursor.getString(16));

            User user = new User();
            user.setId(cursor.getInt(17));
            user.setNombreCompleto(cursor.getString(18));
            user.setUsername(cursor.getString(19));
            user.setEmail(cursor.getString(20));
            user.setPassword(cursor.getString(21));
            user.setGenero(cursor.getString(22));
            user.setDireccion(cursor.getString(23));
            user.setFechaNacimiento(cursor.getString(24));

            Rol rolEstacion = new Rol();
            rolEstacion.setId(cursor.getInt(25));
            rolEstacion.setNombre(cursor.getString(26));

            Rol rolUsuario = new Rol();
            rolUsuario.setId(cursor.getInt(27));
            rolUsuario.setNombre(cursor.getString(28));

            transaction.setEstado(cursor.getString(29));

            user.setRol(rolUsuario);
            estacion.setRol(rolEstacion);
            transaction.setEstacion(estacion);
            transaction.setUsuario(user);
            transaction.setCombustible(fuel);

            transactions.add(transaction);
        }

        cursor.close();
        return transactions;
    }

    public void insertarTransaccion(Transaction transaction){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "INSERT INTO Transacciones " +
                        "(estacion_id, user_id, tipo_vehiculo, cantidad, total, fecha, id_combustible) " +
                        "VALUES (?,?,?,?,?,?,?)",
                new Object[]{
                        transaction.getEstacion().getId(),
                        transaction.getUsuario() != null ? transaction.getUsuario().getId() : null,
                        transaction.getTipoVehiculo(),
                        transaction.getCantidad(),
                        transaction.getTotal(),
                        transaction.getFechaFormateada(),
                        transaction.getCombustible().getId()
                }
        );
    }
}