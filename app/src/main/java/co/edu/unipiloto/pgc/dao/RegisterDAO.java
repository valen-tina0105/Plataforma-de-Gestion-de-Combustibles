package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
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
                "SELECT re.id, re.tipo_combustible, re.cantidad, re.fecha, u.id, u.username, u.password, r.id, r.nombre " +
                        "FROM Registros re " +
                        "INNER JOIN Users u ON re.estacion_id = u.id " +
                        "INNER JOIN Roles r ON u.rol_id = r.id " +
                        "WHERE u.id = ?",
                        new String[]{String.valueOf(userId.getId())}
        );

        while(cursor.moveToNext()){
            Register register = new Register();
            register.setId(cursor.getInt(0));
            register.setTipoCombustible(cursor.getString(1));
            register.setCantidad(cursor.getInt(2));
            register.setFechaFormateada(cursor.getString(3));

            User user = new User();
            user.setId(cursor.getInt(4));
            user.setUsername(cursor.getString(5));
            user.setPassword(cursor.getString(6));

            Rol rol = new Rol();
            rol.setId(cursor.getInt(7));
            rol.setNombre(cursor.getString(8));

            user.setRol(rol);
            register.setEstacion(user);
            registers.add(register);
        }

        cursor.close();
        return registers;
    }

    public void insertarRegistro(Register register){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "INSERT INTO Registros (tipo_combustible, cantidad, fecha, estacion_id) VALUES (?,?,?,?)",
                new Object[]{
                        register.getTipoCombustible(),
                        register.getCantidad(),
                        register.getFechaFormateada(),
                        register.getEstacion().getId()
                }
        );
    }
}
