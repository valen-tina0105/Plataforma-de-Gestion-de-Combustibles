package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;

public class UserDAO {
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT u.id, u.username, u.password, r.id, r.nombre " +
                        "FROM Users u " +
                        "INNER JOIN Rol r ON u.id_rol = r.id",
                null
        );

        while(cursor.moveToNext()){
            User user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(3));
            rol.setNombre(cursor.getString(4));

            user.setRol(rol);

            users.add(user);
        }

        cursor.close();
        return users;
    }

    public boolean verificarUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT username " +
                    "FROM Users " +
                    "WHERE username = ? " +
                    "LIMIT 1",
                    new String[]{username});

            if (cursor.moveToFirst()) {
                return true;
            }
            return false;
    }

    public User logIn(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT u.id, u.username, u.password, r.id, r.nombre " +
                        "FROM Users u " +
                        "INNER JOIN Rol r ON u.rol_id = r.id " +
                        "WHERE u.username = ? AND u.password = ? " +
                        "LIMIT 1",
                new String[]{username, password});

        if (cursor.moveToFirst()) {

            User user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(3));
            rol.setNombre(cursor.getString(4));

            user.setRol(rol);

            return user;
        }
        return null;
    }

    public void insertarUsuario(User user){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "INSERT INTO Users (username, password, rol_id) VALUES (?,?,?)",
                new String[]{user.getUsername(), user.getPassword(), String.valueOf(user.getRol().getId())}
        );

        cursor.close();

    }

}
