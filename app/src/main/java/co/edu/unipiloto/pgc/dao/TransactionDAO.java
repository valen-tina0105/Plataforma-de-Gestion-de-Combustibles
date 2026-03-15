package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Transaction;
import co.edu.unipiloto.pgc.model.User;

public class TransactionDAO {
    private DatabaseHelper dbHelper;

    public TransactionDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Transaction> getAllTransactions(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT t.id, t.tipo_vehiculo, t.cantidad, t.total, t.fecha, u.id, u.username, u.password, r.id, r.nombre " +
                        "FROM Transacciones t " +
                        "INNER JOIN Users u ON t.estacion_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id",
                null
        );

        while(cursor.moveToNext()){
            Transaction transaction = new Transaction();
            transaction.setId(cursor.getInt(0));
            transaction.setTipoVehiculo(cursor.getString(1));
            transaction.setCantidad(cursor.getInt(2));
            transaction.setTotal(cursor.getInt(3));
            transaction.setFechaFormateada(cursor.getString(4));

            User user = new User();
            user.setId(cursor.getInt(5));
            user.setUsername(cursor.getString(6));
            user.setPassword(cursor.getString(7));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(8));
            rol.setNombre(cursor.getString(9));

            user.setRol(rol);
            transaction.setEstacion(user);
            transactions.add(transaction);
        }

        cursor.close();
        return transactions;
    }

    public void insertarTransaccion(Transaction transaction){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "INSERT INTO Transacciones (estacion_id, tipo_vehiculo, cantidad, total, fecha) VALUES (?,?,?,?,?)",
                new Object[]{transaction.getEstacion().getId(), transaction.getTipoVehiculo(),
                        transaction.getCantidad(), transaction.getTotal(),
                        transaction.getFechaFormateada()}
        );

    }
}
