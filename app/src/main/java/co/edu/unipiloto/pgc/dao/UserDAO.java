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

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT u.id, u.nombre_completo, u.username, u.email, u.password, " +
                        "u.genero, u.direccion, u.fecha_nacimiento, r.id, r.nombre " +
                        "FROM Users u " +
                        "INNER JOIN Roles r ON u.rol_id = r.id",
                null
        );

        while (cursor.moveToNext()) {
            User user = new User();

            user.setId(cursor.getInt(0));
            user.setNombreCompleto(cursor.getString(1));
            user.setUsername(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setGenero(cursor.getString(5));
            user.setDireccion(cursor.getString(6));
            user.setFechaNacimiento(cursor.getString(7));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(8));
            rol.setNombre(cursor.getString(9));

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
        try {
            if (cursor.moveToFirst()) {
                return true;
            }
        } finally {
            cursor.close();
        }
        return false;
    }

    public User logIn(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT u.id, u.nombre_completo, u.username, u.email, u.password, " +
                        "u.genero, u.direccion, u.fecha_nacimiento, r.id, r.nombre " +
                        "FROM Users u " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE u.username = ? AND u.password = ? " +
                        "LIMIT 1",
                new String[]{username, password}
        );
        try {
            if (cursor.moveToFirst()) {

                User user = new User();

                user.setId(cursor.getInt(0));
                user.setNombreCompleto(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setEmail(cursor.getString(3));
                user.setPassword(cursor.getString(4));
                user.setGenero(cursor.getString(5));
                user.setDireccion(cursor.getString(6));
                user.setFechaNacimiento(cursor.getString(7));

                Rol rol = new Rol();
                rol.setId(cursor.getInt(8));
                rol.setNombre(cursor.getString(9));

                user.setRol(rol);

                return user;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public void insertarUsuario(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "INSERT INTO Users (nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id) " +
                        "VALUES (?,?,?,?,?,?,?,?)",
                new Object[]{
                        user.getNombreCompleto(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getGenero(),
                        user.getDireccion(),
                        user.getFechaNacimiento(),
                        user.getRol().getId()
                }
        );

    }

    public ArrayList<User> getAllStations() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT u.id, u.nombre_completo, u.username, u.email, u.password, " +
                        "u.genero, u.direccion, u.fecha_nacimiento, r.id, r.nombre " +
                        "FROM Users u " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE r.id = 1",
                null
        );

        while (cursor.moveToNext()) {
            User user = new User();

            user.setId(cursor.getInt(0));
            user.setNombreCompleto(cursor.getString(1));
            user.setUsername(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setGenero(cursor.getString(5));
            user.setDireccion(cursor.getString(6));
            user.setFechaNacimiento(cursor.getString(7));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(8));
            rol.setNombre(cursor.getString(9));

            user.setRol(rol);

            users.add(user);
        }

        cursor.close();
        return users;
    }

}
