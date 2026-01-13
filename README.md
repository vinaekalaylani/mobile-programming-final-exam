# mobile-programming-final-exam
Mobile Programming - Final Exam

## Fitur Unggulan Aplikasi Si Sehat

Aplikasi ini mengimplementasikan berbagai fitur modern Android untuk memberikan keamanan dan pengalaman pengguna yang optimal.

---

### 1. Implementasi Notifikasi Premium
Memberikan umpan balik instan saat proses *booking* janji temu.
*   **Teknologi**: Menggunakan `NotificationChannel` dengan `IMPORTANCE_HIGH`.
*   **Interaksi**: Notifikasi mendukung getaran, suara, dan tombol aksi (Lihat Janji & Bagikan).
*   **Izin**: Menangani izin dinamis (Runtime Permission) untuk Android 13+.

### 2. Login Biometrik (Sidik Jari)
Memungkinkan pengguna masuk ke aplikasi secara instan dan aman menggunakan sensor sidik jari perangkat.
*   **Teknologi**: Menggunakan library `androidx.biometric`.
*   **Keamanan**: Menggunakan standar `BiometricPrompt` yang terintegrasi dengan sistem keamanan pusat perangkat (Keystore).
*   **Alur**: Tombol biometrik (ikon gembok) hanya akan muncul jika perangkat pengguna memiliki perangkat keras biometrik yang terdaftar dan aktif.

**Cuplikan Kode (`BiometricHelper.java`):**
```java
public static void showBiometricPrompt(FragmentActivity activity, BiometricCallback callback) {
    BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            callback.onAuthenticationSuccess();
        }
        // ... penanganan error
    });
    // ... konfigurasi prompt info
}
```

### 3. Sistem Keamanan Sesi (RAM-only)
Aplikasi ini menggunakan manajemen sesi yang sangat ketat untuk melindungi privasi pengguna. Sesi bersifat **In-Memory** (disimpan di RAM), bukan di penyimpanan permanen.

*   **Perilaku Kill Process/Swap Up**: Jika pengguna menutup aplikasi dengan cara mengusapnya ke atas (swap up) dari menu Recent Apps, maka proses aplikasi akan mati secara total. Hal ini akan membersihkan variabel `static` di RAM, sehingga token login terhapus secara otomatis. Saat dibuka kembali, pengguna **wajib login ulang**.
*   **Perilaku Background/Minimize**: Jika aplikasi hanya diminimalkan (menekan tombol Home atau berpindah ke aplikasi lain tanpa menghapusnya dari Recent Apps), Android akan menjaga status aplikasi. Saat kembali, pengguna akan langsung berada di halaman terakhir tanpa harus login ulang.

**Cuplikan Kode (`Session.java`):**
```java
public class Session {
    // Variabel static disimpan di RAM. Otomatis terhapus saat sistem Android membunuh proses aplikasi.
    private static String sessionToken = null;

    public void saveToken(String token) { sessionToken = token; }
    public boolean isLoggedIn() { return sessionToken != null; }
}
```

---

## Penjelasan Teknis Notifikasi (Detail)

#### Meminta Izin Notifikasi (`SplashActivity.java`)
Penanganan izin dilakukan di awal aplikasi menggunakan `ActivityResultLauncher` untuk memastikan pengalaman yang mulus.

```java
private void askNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            proceedToLogin();
        }
    }
}
```

#### Logika Notifikasi (`NotificationHelper.java`)
Membangun notifikasi interaktif dengan gaya yang bisa diperluas.

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification_success)
        .setContentTitle("Booking Berhasil!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(new long[]{100, 200, 300})
        .setAutoCancel(true)
        .addAction(R.drawable.ic_open_in_app, "Lihat Janji", openAppPendingIntent);
```
