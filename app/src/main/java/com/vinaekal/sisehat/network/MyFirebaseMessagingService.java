package com.vinaekal.sisehat.network;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vinaekal.sisehat.util.NotificationHelper;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Jika pesan berisi data payload
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String queueNumber = remoteMessage.getData().get("queue_number");

            if (queueNumber != null) {
                NotificationHelper.showSuccessNotification(this, queueNumber);
            }
        }

        // Jika pesan berisi notifikasi standar dari console
        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            // Anda bisa menyesuaikan ini untuk memanggil NotificationHelper
            NotificationHelper.showSuccessNotification(this, "Update");
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Kirim token ini ke server Anda jika diperlukan untuk kirim notifikasi individu
    }
}