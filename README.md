# Si Sehat - Solusi Reservasi Layanan Kesehatan Modern

Aplikasi **Si Sehat** adalah solusi mobile inovatif yang dirancang untuk memudahkan pasien dalam melakukan reservasi (booking) janji temu dokter secara online. Dengan fokus pada keamanan data dan kemudahan akses, aplikasi ini mengintegrasikan fitur-fitur modern berstandar industri.

---

## 🚀 Fitur Utama & Implementasi Kode

### 1. Autentikasi Hybrid (Firebase & Biometric)
Sistem login ganda yang menggabungkan keamanan Cloud Google dengan kenyamanan teknologi hardware perangkat.
*   **Firebase Auth:** Mengamankan kredensial pengguna di server Google.
*   **Jetpack Biometric:** Mendukung **Sidik Jari (Fingerprint)**, **Pengenalan Wajah (Face Unlock)**, serta **PIN/Pola Perangkat**.

#### Detail Fitur Biometrik:
- **Capability Check:** Aplikasi mendeteksi ketersediaan hardware biometrik secara otomatis.
- **Strong Authenticator:** Menggunakan tingkat keamanan `BIOMETRIC_STRONG` sesuai standar Android terbaru.
- **Device Credential Fallback:** Jika biometrik tidak tersedia atau gagal, sistem secara otomatis memberikan opsi login menggunakan PIN/Pola HP.

```java
// Implementasi Biometric di LoginActivity.java
private void setupBiometric() {
    BiometricManager biometricManager = BiometricManager.from(this);
    // Mendukung Fingerprint, Face, dan Device PIN
    int authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL;

    switch (biometricManager.canAuthenticate(authenticators)) {
        case BiometricManager.BIOMETRIC_SUCCESS:
            buttonBiometric.setVisibility(View.VISIBLE);
            break;
        default:
            buttonBiometric.setVisibility(View.GONE);
            break;
    }

    BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            // Verifikasi Session Token sebelum masuk ke Dashboard
            if (session.getToken() != null) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }
    });

    BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login Si Sehat")
            .setSubtitle("Gunakan sidik jari, wajah, atau PIN Anda")
            .setAllowedAuthenticators(authenticators)
            .build();
}
```

### 2. Keamanan Sesi Cerdas (Anti-Leak System)
Melindungi privasi data pasien dengan memastikan sesi berakhir saat aplikasi benar-benar ditutup.
*   **Auto-Logout on Task Kill:** Menghapus data login saat aplikasi di-swipe dari recent apps menggunakan Service.
*   **Kill Task (Double Back to Exit):** Mencegah penutupan aplikasi yang tidak disengaja.

```java
// Implementasi Kill Task di MainActivity.java
getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
    @Override
    public void handleOnBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity(); // Tutup semua activity
            System.exit(0);   // Hentikan proses total
        } else {
            Toast.makeText(MainActivity.this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
});
```

### 3. Notifikasi Premium & Cloud Messaging (FCM)
Pemberitahuan real-time yang interaktif langsung dari cloud.
*   **Interactive Actions:** Tombol "Bagikan" (Share) dan "Lihat Janji" langsung di notifikasi.
*   **Heads-up Notification:** Menggunakan High Priority Channel agar muncul di atas layar sebagai pop-up.

```java
// Logic Notifikasi Premium di NotificationHelper.java
public static void showSuccessNotification(Context context, String queueNumber) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle("Booking Berhasil! No Antrian: " + queueNumber)
            .setStyle(new NotificationCompat.BigTextStyle().bigText("Pendaftaran Anda telah berhasil dikonfirmasi..."))
            .addAction(R.drawable.ic_share, "Bagikan", sharePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);
}
```

### 4. Persistence & Last Page Save
Aplikasi mengingat posisi terakhir pengguna sebelum aplikasi ditutup (selama tidak di-kill).
*   **Logic:** Menyimpan nama class activity terakhir ke SharedPreferences dan memuatnya kembali saat SplashActivity.

```java
// SplashActivity Logic untuk Persistence
String lastPage = session.getLastPage();
if (lastPage != null) {
    try {
        Class<?> activityClass = Class.forName(lastPage);
        startActivity(new Intent(this, activityClass));
    } catch (ClassNotFoundException e) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
```

### 5. Profil & Interaksi Tactile
Manajemen data diri dengan pengalaman pengguna yang responsif.
*   **Click Animation:** Memberikan efek visual (Scale Animation 0.97f) saat tombol ditekan untuk feedback tactile.
*   **Real-time Profile Update:** Alamat, No. HP, dan Pekerjaan tersimpan secara permanen di memori lokal.

---

## 🛠️ Stack Teknologi
*   **Backend & Auth:** Google Firebase (Auth & Cloud Messaging)
*   **API Client:** Retrofit 2 & OkHttp 3 (Logging Interceptor)
*   **Local Storage:** SharedPreferences (Session Manager)
*   **Security:** AndroidX Biometric Library (Fingerprint & Face)
*   **UI Components:** Material Design 3 (CardView, CoordinatorLayout, Custom Vectors)

---

## 📄 Struktur Proyek
*   `com.vinaekal.sisehat.network`: Menangani komunikasi Firebase & API Service.
*   `com.vinaekal.sisehat.util`: Berisi `NotificationHelper`, `Session Manager`, dan `SessionService` (Task Killer).
*   `com.vinaekal.sisehat.model`: Data class untuk request dan response booking/auth.

---