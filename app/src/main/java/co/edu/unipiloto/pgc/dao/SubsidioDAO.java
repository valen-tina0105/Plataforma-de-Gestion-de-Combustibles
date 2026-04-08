package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Subsidio;
import co.edu.unipiloto.pgc.model.User;

public class SubsidioDAO {

    private DatabaseHelper dbHelper;

    public SubsidioDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public Subsidio getSubsidioByUser(User user) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, cupo_total, saldo_disponible FROM Subsidios WHERE user_id = ?",
                new String[]{String.valueOf(user.getId())}
        );

        Subsidio subsidio = null;

        if (cursor.moveToFirst()) {
            subsidio = new Subsidio();
            subsidio.setId(cursor.getInt(0));
            subsidio.setCupoTotal(cursor.getInt(1));
            subsidio.setSaldoDisponible(cursor.getInt(2));
            subsidio.setUser(user);
        }

        cursor.close();

        return subsidio;
    }

    public void actualizarSaldo(Subsidio subsidio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "UPDATE Subsidios SET saldo_disponible = ? WHERE user_id = ?",
                new Object[]{
                        subsidio.getSaldoDisponible(),
                        subsidio.getUser().getId()
                }
        );
    }
}