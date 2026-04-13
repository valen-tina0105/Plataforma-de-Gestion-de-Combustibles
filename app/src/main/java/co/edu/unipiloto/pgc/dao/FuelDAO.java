package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;

public class FuelDAO {
    private DatabaseHelper dbHelper;

    public FuelDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Fuel> getAllFuels(){
        ArrayList<Fuel> combustibles = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, nombre " +
                        "FROM Combustibles",
                null
        );

        while(cursor.moveToNext()){
            Fuel combustible = new Fuel();
            combustible.setId(cursor.getInt(0));
            combustible.setNombre(cursor.getString(1));

            combustibles.add(combustible);
        }

        cursor.close();
        return combustibles;
    }
}
