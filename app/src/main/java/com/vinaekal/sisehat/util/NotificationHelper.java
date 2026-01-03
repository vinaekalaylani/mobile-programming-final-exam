package com.vinaekal.sisehat.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.vinaekal.sisehat.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "booking_channel";
    private static final String CHANNEL_NAME = "Booking Notifications";
    private static final String CHANNEL_DESC = "Notifications for booking status";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void showSuccessNotification(Context context, String queueNumber) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendar) // Ganti dengan ikon notifikasi Anda
                .setContentTitle("Booking Berhasil!")
                .setContentText("Anda mendapatkan nomor antrian " + queueNumber)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // ID notifikasi (bisa dibuat unik jika perlu menampilkan banyak notifikasi)
        notificationManager.notify(1, builder.build());
    }

    public static void showFailureNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendar) // Ganti dengan ikon notifikasi Anda
                .setContentTitle("Booking Gagal")
                .setContentText("Terjadi kesalahan saat melakukan booking. Silakan coba lagi.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2, builder.build());
    }
}
