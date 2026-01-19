package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.vinaekal.sisehat.model.request.BookingRequest;
import com.vinaekal.sisehat.util.NotificationHelper;
import com.vinaekal.sisehat.util.Session;

import java.util.HashMap;
import java.util.Map;

public class BookingStep2Activity extends AppCompatActivity {

    private EditText etPeserta, etCatatan;
    private Button btnKonfirmasi;

    private String hospital, poly, doctor, bookingDateTime;
    private int nextQueue;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_step2);

        etPeserta = findViewById(R.id.etPeserta);
        etCatatan = findViewById(R.id.etCatatan);
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);

        // Ambil data dari Step1
        hospital = getIntent().getStringExtra("hospital");
        poly = getIntent().getStringExtra("poly");
        doctor = getIntent().getStringExtra("doctor");
        bookingDateTime = getIntent().getStringExtra("bookingDateTime");
        nextQueue = getIntent().getIntExtra("nextQueue", 0);

        // Ambil session
        session = new Session(this);
        String username = session.getUsername();
        int userId = session.getUserId();

        // Set peserta
        etPeserta.setText(username);
        etPeserta.setFocusable(false); // readonly

        btnKonfirmasi.setOnClickListener(v -> {
            String catatan = etCatatan.getText().toString().trim();

            if (catatan.isEmpty()) {
                Toast.makeText(this, "Isi catatan / keluhan", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build data map for Firestore
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("userId", userId);
            bookingData.put("hospital", hospital);
            bookingData.put("poly", poly);
            bookingData.put("doctor", doctor);
            bookingData.put("queue_number", "A" + nextQueue);
            bookingData.put("booking_date", bookingDateTime);
            bookingData.put("notes", catatan);
            bookingData.put("status", "Aktif");

            sendBooking(bookingData, "A" + nextQueue);
        });
    }

    private void sendBooking(Map<String, Object> bookingData, String queueNum) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("bookings")
                .add(bookingData)
                .addOnSuccessListener(documentReference -> {
                    // Tampilkan Notifikasi Berhasil
                    NotificationHelper.showSuccessNotification(BookingStep2Activity.this, queueNum);

                    // Lanjut ke BookingSuccessActivity
                    Intent intent = new Intent(BookingStep2Activity.this, BookingSuccessActivity.class);
                    intent.putExtra("queue_number", queueNum);
                    intent.putExtra("hospital", hospital);
                    intent.putExtra("doctor", doctor);
                    intent.putExtra("department", poly);
                    intent.putExtra("booking_date", bookingDateTime);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    NotificationHelper.showFailureNotification(BookingStep2Activity.this);
                    Toast.makeText(BookingStep2Activity.this, "Gagal booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}