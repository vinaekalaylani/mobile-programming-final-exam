# mobile-programming-final-exam
Mobile Programming - Final Exam

## Implementasi Notifikasi Premium

Fitur notifikasi di aplikasi ini telah dirancang untuk memberikan pengalaman pengguna yang modern, informatif, dan interaktif. Implementasi ini mencakup permintaan izin sesuai standar terbaru, saluran notifikasi berprioritas tinggi, dan fungsionalitas premium seperti tombol aksi, bunyi, dan getar.

### Alur Kerja Notifikasi

1.  **Permintaan Izin**: Saat aplikasi pertama kali dijalankan, `SplashActivity` akan mendeteksi versi Android pengguna. Jika perangkat menjalankan Android 13 (Tiramisu) atau lebih baru, aplikasi akan menampilkan dialog sistem untuk meminta izin `POST_NOTIFICATIONS`.
2.  **Pembuatan Saluran Notifikasi**: Saat `MainActivity` pertama kali dibuat, aplikasi akan membuat "Saluran Notifikasi" (Notification Channel) dengan tingkat kepentingan `HIGH`. Ini adalah langkah wajib untuk memastikan notifikasi dapat muncul sebagai *pop-up* di atas layar, lengkap dengan bunyi dan getar.
3.  **Pemicu Notifikasi**: Notifikasi akan dikirim saat pengguna berhasil mengonfirmasi *booking* di halaman `BookingStep2Activity`.

---

### Penjelasan Kode & Implementasi

#### 1. Meminta Izin Notifikasi (`SplashActivity.java`)

Untuk mematuhi aturan privasi Android 13+, kita harus meminta izin secara eksplisit. Ini dilakukan di `SplashActivity` untuk memastikan izin sudah didapatkan di awal.

```java
// Di dalam SplashActivity.java

private final ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            // Lanjutkan ke login setelah user merespons
            proceedToLogin();
        });

private void askNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Jika izin belum diberikan, tampilkan dialog permintaan
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            proceedToLogin(); // Izin sudah ada, lanjutkan
        }
    } else {
        proceedToLogin(); // Versi Android lama, tidak perlu izin
    }
}
```

**Penjelasan:**
*   `ActivityResultLauncher` digunakan untuk menangani hasil dari dialog permintaan izin.
*   Logika ini hanya berjalan di Android 13 ke atas, memastikan kompatibilitas dengan versi lama.

#### 2. Membuat Saluran Notifikasi (`MainActivity.java`)

Sebelum notifikasi bisa dikirim, salurannya harus dibuat. Ini dilakukan sekali saja saat `MainActivity` dibuka.

```java
// Di dalam onCreate() di MainActivity.java

NotificationHelper.createNotificationChannel(this);
```

Kode di atas memanggil metode dari kelas utilitas kita, `NotificationHelper`, untuk mendaftarkan saluran ke sistem.

#### 3. Logika Notifikasi Premium (`NotificationHelper.java`)

Ini adalah inti dari fungsionalitas notifikasi kita. Kelas ini menangani semua detail untuk membuat notifikasi yang canggih.

```java
// Di dalam NotificationHelper.java

public static void createNotificationChannel(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // 1. Buat Channel dengan Prioritas TINGGI
        NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID, 
            CHANNEL_NAME, 
            NotificationManager.IMPORTANCE_HIGH // Penting untuk pop-up
        );
        
        // 2. Aktifkan Bunyi & Getar
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        channel.setSound(defaultSoundUri, null);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
}

public static void showSuccessNotification(Context context, String queueNumber) {
    // 3. Aksi saat Notifikasi Diklik (Membuka Aplikasi)
    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent openAppPendingIntent = PendingIntent.getActivity(context, 0, intent, ...);

    // 4. Aksi untuk Tombol "Bagikan"
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_TEXT, "Nomor antrian saya..." + queueNumber);
    PendingIntent sharePendingIntent = PendingIntent.getActivity(context, 1, ...);

    // 5. Tampilan Notifikasi yang Bisa Diperluas
    NotificationCompat.Style bigTextStyle = new NotificationCompat.BigTextStyle()
            .bigText("RS. Harapan Sehat\n" + "Poli: Orthopaedi\n" + ...)
            .setSummaryText("Detail Janji Temu");

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar)
            .setLargeIcon(BitmapFactory.decodeResource(..., R.mipmap.ic_launcher))
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

    NotificationManagerCompat.from(context).notify(1, builder.build());
}
```

**Penjelasan Fitur Premium:**
1.  **Prioritas Tinggi (`IMPORTANCE_HIGH`)**: Kunci agar notifikasi muncul sebagai *pop-up* di atas layar.
2.  **Bunyi & Getar**: Memberikan umpan balik multisensori kepada pengguna.
3.  **Aksi Klik (`PendingIntent`)**: Membuat notifikasi bisa diklik untuk membuka aplikasi.
4.  **Tombol Bagikan**: Memungkinkan pengguna untuk berinteraksi lebih jauh dengan membagikan informasi.
5.  **Tampilan Diperluas (`BigTextStyle`)**: Menyajikan informasi detail tanpa harus membuka aplikasi, memberikan pengalaman yang efisien.
6.  **Tombol Aksi (`.addAction`)**: Menyediakan tombol interaktif langsung di dalam notifikasi untuk tindakan cepat.
