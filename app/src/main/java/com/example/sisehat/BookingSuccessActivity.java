package com.example.sisehat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class BookingSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        TextView tvQueueNumber = findViewById(R.id.tvQueueNumber);
        TextView tvConsultationTime = findViewById(R.id.tvConsultationTime);
        Button btnBack = findViewById(R.id.btnBackToDashboard);

        // FITUR: Nomor Urut Random (1 - 100)
        Random random = new Random();
        int queue = random.nextInt(100) + 1;
        tvQueueNumber.setText(String.valueOf(queue));

        // FITUR: Menampilkan Waktu Konsultasi (Contoh Statis sesuai Desain)
        // Anda bisa membuat ini dinamis berdasarkan jam yang dipilih di Page 1
        tvConsultationTime.setText("09:00 - 09:15 WIB");

        // Logika Tombol Selesai
        btnBack.setOnClickListener(v -> {
            finish(); // Menutup halaman sukses dan kembali
        });
    }
}