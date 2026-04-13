package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Register;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.User;

public class RegisterDAO {
    private DatabaseHelper dbHelper;

    public RegisterDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Register> getAllRegisters(User userId){
        ArrayList<Register> registers = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT re.id, re.cantidad, re.fecha, " +
                        "c.id, c.nombre, " +
                        "u.id, u.nombre_completo, u.username, u.email, u.password, u.genero, u.direccion, u.fecha_nacimiento, " +
                        "r.id, r.nombre " +
                        "FROM Registros re " +
                        "INNER JOIN Combustibles c ON re.id_combustible = c.id " +
                        "INNER JOIN Users u ON re.estacion_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE u.id = ?",
                new String[]{String.valueOf(userId.getId())}
        );

        while(cursor.moveToNext()){
            Register register = new Register();
            register.setId(cursor.getInt(0));
            register.setCantidad(cursor.getDouble(1));
            register.setFechaFormateada(cursor.getString(2));

            Fuel fuel = new Fuel();
            fuel.setId(cursor.getInt(3));
            fuel.setNombre(cursor.getString(4));

            User user = new User();
            user.setId(cursor.getInt(5));
            user.setNombreCompleto(cursor.getString(6));
            user.setUsername(cursor.getString(7));
            user.setEmail(cursor.getString(8));
            user.setPassword(cursor.getString(9));
            user.setGenero(cursor.getString(10));
            user.setDireccion(cursor.getString(11));
            user.setFechaNacimiento(cursor.getString(12));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(13));
            rol.setNombre(cursor.getString(14));

            user.setRol(rol);
            register.setEstacion(user);
            register.setCombustible(fuel);
            registers.add(register);
        }

        cursor.close();
        return registers;
    }

    public void insertarRegistro(Register register){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {

            Cursor cursor = db.rawQuery(
                    "SELECT cantidad_combustible, capacidad_maxima " +
                            "FROM Inventarios " +
                            "WHERE id_estacion = ? AND id_combustible = ?",
                    new String[]{
                            String.valueOf(register.getEstacion().getId()),
                            String.valueOf(register.getCombustible().getId())
                    }
            );

            if (!cursor.moveToFirst()) {
                cursor.close();
                throw new RuntimeException("INVENTARIO_NO_ENCONTRADO");
            }

            double cantidadActual = cursor.getDouble(0);
            double capacidadMaxima = cursor.getDouble(1);
            cursor.close();

            double nuevaCantidad = cantidadActual + register.getCantidad();

            if (nuevaCantidad > capacidadMaxima) {
                throw new RuntimeException("EXCEDE_CAPACIDAD");
            }

            db.execSQL(
                    "INSERT INTO Registros (id_combustible, cantidad, fecha, estacion_id) VALUES (?,?,?,?)",
                    new Object[]{
                            register.getCombustible().getId(),
                            register.getCantidad(),
                            register.getFechaFormateada(),
                            register.getEstacion().getId()
                    }
            );

            db.execSQL(
                    "UPDATE Inventarios SET cantidad_combustible = ? " +
                            "WHERE id_estacion = ? AND id_combustible = ?",
                    new Object[]{
                            nuevaCantidad,
                            register.getEstacion().getId(),
                            register.getCombustible().getId()
                    }
            );

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }
}
