package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Station;

public class StationDAO {
    private DatabaseHelper dbHelper;

    public StationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Station> getAllStations(double latUser, double lonUser) {

        ArrayList<Station> stations = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT u.nombre_completo, " +
                        "u.direccion, " +
                        "u.latitud, " +
                        "u.longitud, " +
                        "MAX(CASE WHEN c.nombre = 'Gasolina Corriente' THEN p.precio END) AS corriente, " +
                        "MAX(CASE WHEN c.nombre = 'Gasolina Extra' THEN p.precio END) AS extra, " +
                        "MAX(CASE WHEN c.nombre = 'ACPM(Diésel)' THEN p.precio END) AS diesel, " +
                        "MAX(CASE WHEN c.nombre = 'Gas Natural Vehicular' THEN p.precio END) AS gnv, " +
                        "((u.latitud - ?) * (u.latitud - ?) + " +
                        "(u.longitud - ?) * (u.longitud - ?)) AS distancia " +
                        "FROM Users u " +
                        "JOIN Precios p ON u.id = p.id_estacion " +
                        "JOIN Combustibles c ON p.id_combustible = c.id " +
                        "WHERE u.rol_id = 1 " +
                        "GROUP BY u.id " +
                        "ORDER BY distancia ASC " +
                        "LIMIT 5;",

                new String[]{
                        String.valueOf(latUser),
                        String.valueOf(latUser),
                        String.valueOf(lonUser),
                        String.valueOf(lonUser)
                }
        );

        while (cursor.moveToNext()) {

            Station station = new Station();

            station.setNombre(cursor.getString(0));
            station.setDireccion(cursor.getString(1));
            station.setLatitud(cursor.getDouble(2));
            station.setLongitud(cursor.getDouble(3));

            station.setPrecioCorriente(cursor.getDouble(4));
            station.setPrecioExtra(cursor.getDouble(5));
            station.setPrecioDiesel(cursor.getDouble(6));
            station.setPrecioGNV(cursor.getDouble(7));

            float[] results = new float[1];
            Location.distanceBetween(
                    latUser, lonUser,
                    station.getLatitud(), station.getLongitud(),
                    results
            );

            double distanciaKm = results[0] / 1000.0;
            station.setDistancia(distanciaKm);

            stations.add(station);
        }

        cursor.close();
        return stations;
    }
}
