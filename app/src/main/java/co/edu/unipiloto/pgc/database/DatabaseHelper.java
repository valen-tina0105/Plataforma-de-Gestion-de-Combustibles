package co.edu.unipiloto.pgc.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "PGC";
    private Context context;
    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,1);
        context = this.context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ejecutarSQLDesdeArchivo(db, "PGC.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void ejecutarSQLDesdeArchivo(SQLiteDatabase db, String archivo) {
        try {
            InputStream is = context.getAssets().open(archivo);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sql = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                sql.append(linea);
            }

            String[] sentencias = sql.toString().split(";");

            for (String sentencia : sentencias) {
                if (!sentencia.trim().isEmpty()) {
                    db.execSQL(sentencia);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
