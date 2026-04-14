package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Subsidy;
import co.edu.unipiloto.pgc.model.User;

public class SubsidyDAO {
    private DatabaseHelper dbHelper;

    public SubsidyDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public Subsidy getSubsidyById(User userId) {

        Subsidy subsidy = null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT s.id, s.subsidio, s.porcentaje, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "r.id, r.nombre " +
                        "FROM Subsidios s " +
                        "LEFT JOIN Users u ON s.user_id = u.id " +
                        "LEFT JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE s.user_id = ?",
                new String[]{String.valueOf(userId.getId())}
        );

        if (cursor.moveToFirst()) {
            subsidy = new Subsidy();
            subsidy.setId(cursor.getInt(0));
            subsidy.setSubsidio(cursor.getInt(1));
            subsidy.setPorcentaje(cursor.isNull(2) ? null : cursor.getDouble(2));

            User usuario = new User();
            usuario.setId(cursor.getInt(3));
            usuario.setNombreCompleto(cursor.getString(4));
            usuario.setUsername(cursor.getString(5));
            usuario.setEmail(cursor.getString(6));
            usuario.setPassword(cursor.getString(7));
            usuario.setGenero(cursor.getString(8));
            usuario.setDireccion(cursor.getString(9));
            usuario.setFechaNacimiento(cursor.getString(10));

            if (!cursor.isNull(11)) {
                Rol rolUsuario = new Rol();
                rolUsuario.setId(cursor.getInt(11));
                rolUsuario.setNombre(cursor.getString(12));
                usuario.setRol(rolUsuario);
            }

            subsidy.setUsuario(usuario);
        }

        cursor.close();
        return subsidy;
    }

}
