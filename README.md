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
// Logic Login Biometrik di LoginActivity.java
private void setupBiometric() {
    int authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL;
    BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            // Login Berhasil jika Token Session tersedia
            if (session.isLoggedIn()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }
    });
}
```

### 2. Cloud Data Synchronization (Firestore)
Aplikasi ini sudah sepenuhnya menggunakan **Firebase Firestore** untuk penyimpanan data yang stabil dan real-time.
*   **Real-time Booking:** Data reservasi disimpan langsung ke koleksi `bookings` di Cloud Firestore.
*   **Profile Sync:** Data diri pasien (alamat, telepon, pekerjaan) disinkronkan ke koleksi `users` berdasarkan `UID` unik.

```java
// Simpan Profil ke Firestore di EditProfilActivity.java
private void saveProfileToCloud() {
    FirebaseUser user = mAuth.getCurrentUser();
    if (user != null) {
        db.collection("users").document(user.getUid())
                .set(profileData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profil Sinkron ke Cloud", Toast.LENGTH_SHORT).show();
                });
    }
}
```

### 3. Keamanan Sesi Cerdas (Anti-Leak System)
Melindungi privasi data pasien dengan memastikan sesi berakhir saat aplikasi benar-benar ditutup.
*   **Auto-Logout on Task Kill:** Menghapus data login saat aplikasi di-swipe dari recent apps menggunakan `SessionService`.
*   **Auto-Biometric Prompt:** Jika aplikasi hanya diminimize, saat dibuka kembali user akan diminta sidik jari secara otomatis sebelum masuk ke Dashboard.

### 4. Notifikasi Premium & Cloud Messaging (FCM)
Pemberitahuan real-time yang interaktif langsung dari cloud Google.
*   **Interactive Actions:** Tombol "Bagikan" (Share) dan "Lihat Janji" langsung di notifikasi.
*   **FCM Service:** Menangani pesan masuk di latar belakang melalui `MyFirebaseMessagingService`.

### 5. Persistence & Last Page Save
Aplikasi mengingat posisi terakhir pengguna sebelum aplikasi ditutup (selama tidak di-kill).
*   **Logic:** Menyimpan nama class activity terakhir ke SharedPreferences dan memuatnya kembali saat SplashActivity dijalankan.

---

## 🛠️ Stack Teknologi
*   **Database & Auth:** Google Firebase (Authentication, Cloud Firestore, Cloud Messaging)
*   **Networking:** Retrofit 2 & OkHttp 3 (Digunakan untuk legacy API integration)
*   **Local Storage:** SharedPreferences (Session Manager)
*   **Security:** AndroidX Biometric Library (Fingerprint & Face)
*   **UI Components:** Material Design 3 (CardView, CoordinatorLayout, Custom Vectors)

---

## ⚙️ Konfigurasi Firebase (Penting)
Untuk menghindari kegagalan sinkronisasi data ("Server Error" atau "Gagal Sinkron"), pastikan pengaturan **Firestore Rules** di Firebase Console sudah diset sebagai berikut:

```javascript
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

## 📄 Struktur Proyek
*   `com.vinaekal.sisehat.network`: Implementasi `MyFirebaseMessagingService` dan `ApiClient`.
*   `com.vinaekal.sisehat.util`: Berisi `NotificationHelper`, `Session`, dan `SessionService` (Task Killer).
*   `com.vinaekal.sisehat.model`: Data class untuk request dan response.
