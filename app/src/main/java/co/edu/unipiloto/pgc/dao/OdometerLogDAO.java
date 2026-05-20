package co.edu.unipiloto.pgc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import co.edu.unipiloto.pgc.database.DatabaseHelper;

public class OdometerLogDAO {

    private static final String TABLE_NAME = "OdometerLogs";
    private static final String COL_ID = "id";
    private static final String COL_DELIVERY_ID = "delivery_id";
    private static final String COL_TIMESTAMP = "timestamp";
    private static final String COL_LAT = "lat";
    private static final String COL_LON = "lon";
    private static final String COL_DISTANCE_TOTAL = "distance_total_m";

    private final DatabaseHelper dbHelper;

    public OdometerLogDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertLog(int deliveryId, long timestamp, double lat, double lon, double distanceTotalMeters) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DELIVERY_ID, deliveryId);
        values.put(COL_TIMESTAMP, timestamp);
        values.put(COL_LAT, lat);
        values.put(COL_LON, lon);
        values.put(COL_DISTANCE_TOTAL, distanceTotalMeters);
        db.insert(TABLE_NAME, null, values);
    }

    public double getTotalDistanceByDelivery(int deliveryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT MAX(" + COL_DISTANCE_TOTAL + ") FROM " + TABLE_NAME + " WHERE " + COL_DELIVERY_ID + "=?";
        String[] args = {String.valueOf(deliveryId)};
        Cursor cursor = db.rawQuery(query, args);
        double total = 0.0;
        if (cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0.0 : cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public void clearLogsForDelivery(int deliveryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, COL_DELIVERY_ID + "=?", new String[]{String.valueOf(deliveryId)});
    }

    public Cursor getConsolidatedReport() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + COL_DELIVERY_ID + ", MAX(" + COL_DISTANCE_TOTAL + ") AS total_distance "
                + "FROM " + TABLE_NAME + " GROUP BY " + COL_DELIVERY_ID + " ORDER BY " + COL_DELIVERY_ID + " DESC";
        return db.rawQuery(query, null);
    }

    public Cursor getConsolidatedReportByDeliveryIds(List<Integer> deliveryIds) {
        if (deliveryIds == null || deliveryIds.isEmpty()) {
            return null;
        }
        StringBuilder placeholders = new StringBuilder();
        String[] args = new String[deliveryIds.size()];
        for (int i = 0; i < deliveryIds.size(); i++) {
            if (i > 0) {
                placeholders.append(",");
            }
            placeholders.append("?");
            args[i] = String.valueOf(deliveryIds.get(i));
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + COL_DELIVERY_ID + ", MAX(" + COL_DISTANCE_TOTAL + ") AS total_distance "
                + "FROM " + TABLE_NAME + " WHERE " + COL_DELIVERY_ID + " IN (" + placeholders + ")"
                + " GROUP BY " + COL_DELIVERY_ID + " ORDER BY " + COL_DELIVERY_ID + " DESC";
        return db.rawQuery(query, args);
    }
}
