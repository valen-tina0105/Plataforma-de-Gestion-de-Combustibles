package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Rol;

public class RolDAO {
    private DatabaseHelper dbHelper;

    public RolDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Rol> getAllRoles(){
        ArrayList<Rol> roles = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, nombre " +
                        "FROM Roles",
                null
        );

        while(cursor.moveToNext()){
            Rol rol = new Rol();
            rol.setId(cursor.getInt(0));
            rol.setNombre(cursor.getString(1));

            roles.add(rol);
        }

        cursor.close();
        return roles;
    }

}
