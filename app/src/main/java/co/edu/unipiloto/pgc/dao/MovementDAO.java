package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Movement;

public class MovementDAO {
    private DatabaseHelper dbHelper;

    public MovementDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Movement> getAllMovements() {
        ArrayList<Movement> movements = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Movimientos ORDER BY fecha DESC",
                null
        );

        while (cursor.moveToNext()) {

            Movement movement = new Movement(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.isNull(3) ? null : cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            );

            movements.add(movement);
        }

        cursor.close();

        return movements;
    }

    public ArrayList<Movement> getMovementsOrderByType() {
        ArrayList<Movement> movements = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Movimientos " +
                        "ORDER BY CASE " +
                        "WHEN tipo_movimiento = 'ENTRADA' THEN 0 " +
                        "WHEN tipo_movimiento = 'SALIDA' THEN 1 " +
                        "END, fecha DESC",
                null
        );

        while (cursor.moveToNext()) {

            Movement movement = new Movement(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.isNull(3) ? null : cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            );

            movements.add(movement);
        }

        cursor.close();

        return movements;
    }


    public ArrayList<Movement> getMovementsByDate() {
        ArrayList<Movement> movements = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Movimientos ORDER BY fecha DESC",
                null
        );

        while (cursor.moveToNext()) {

            Movement movement = new Movement(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.isNull(3) ? null : cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            );

            movements.add(movement);
        }

        cursor.close();

        return movements;
    }


}
