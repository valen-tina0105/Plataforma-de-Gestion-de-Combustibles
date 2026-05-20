package co.edu.unipiloto.pgc.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.model.User;
import co.edu.unipiloto.pgc.ui.InventoryManagementActivity;

public class LowStockNotificationService extends IntentService {

    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_USER = "extra_user";
    private static final String CHANNEL_ID = "low_stock_channel";
    private static final String CHANNEL_NAME = "Alertas de inventario";
    private static final String CHANNEL_DESCRIPTION = "Notificaciones por nivel bajo de combustible";
    private static final int NOTIFICATION_ID = 2001;

    public LowStockNotificationService() {
        super("LowStockNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        String message = intent.getStringExtra(EXTRA_MESSAGE);
        User user = (User) intent.getSerializableExtra(EXTRA_USER);
        if (message == null || message.trim().isEmpty() || user == null) {
            return;
        }

        createNotificationChannel();

        Intent notificationIntent = new Intent(this, InventoryManagementActivity.class);
        notificationIntent.putExtra("user", user);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Inventario bajo")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
