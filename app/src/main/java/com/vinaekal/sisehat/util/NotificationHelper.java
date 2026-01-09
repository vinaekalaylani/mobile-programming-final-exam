package com.vinaekal.sisehat.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.vinaekal.sisehat.MainActivity;
import com.vinaekal.sisehat.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "booking_channel";
    private static final String CHANNEL_NAME = "Booking Notifications";
    private static final String CHANNEL_DESC = "Notifications for booking status";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 1. Buat Channel dengan Prioritas TINGGI
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH // Penting untuk pop-up
            );
            channel.setDescription(CHANNEL_DESC);

            // 2. Aktifkan Bunyi & Getar
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            channel.setSound(defaultSoundUri, null);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void showSuccessNotification(Context context, String queueNumber) {
        // 3. Aksi saat Notifikasi Diklik (Membuka Aplikasi)
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent openAppPendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // 4. Aksi untuk Tombol "Bagikan"
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Yeay! Saya sudah booking di Si Sehat. Nomor antrian saya adalah " + queueNumber + ".");
        PendingIntent sharePendingIntent = PendingIntent.getActivity(context, 1,
                Intent.createChooser(shareIntent, "Bagikan via"),
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // 5. Tampilan Notifikasi yang Bisa Diperluas
        NotificationCompat.Style bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText("Pendaftaran Anda telah berhasil dikonfirmasi.\n\n" +
                        "Nomor Antrian: " + queueNumber + "\n" +
                        "Silakan datang tepat waktu sesuai jadwal yang dipilih.")
                .setSummaryText("Detail Janji Temu");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendar)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Booking Berhasil! No Antrian: " + queueNumber)
                .setContentText("Ketuk untuk detail atau lihat aksi di bawah.")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioritas tinggi
                .setSound(defaultSoundUri) // Bunyi notifikasi
                .setVibrate(new long[]{100, 200, 300}) // Pola getar
                .setContentIntent(openAppPendingIntent) // Aksi klik
                .setStyle(bigTextStyle) // Tampilan diperluas
                .setAutoCancel(true)
                // 6. Tombol Aksi Interaktif
                .addAction(R.drawable.ic_open_in_app, "Lihat Janji", openAppPendingIntent)
                .addAction(R.drawable.ic_share, "Bagikan", sharePendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(1, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void showFailureNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendar)
                .setContentTitle("Booking Gagal")
                .setContentText("Terjadi kesalahan saat melakukan booking. Silakan coba lagi.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(2, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
