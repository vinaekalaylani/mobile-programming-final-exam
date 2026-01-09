package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vinaekal.sisehat.model.content.BookingContent;
import com.vinaekal.sisehat.model.request.BookingRequest;
import com.vinaekal.sisehat.model.response.ApiResponse;
import com.vinaekal.sisehat.network.ApiClient;
import com.vinaekal.sisehat.network.ApiService;
import com.vinaekal.sisehat.util.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        int userId = session.getUserId(); // Pastikan session simpan userId

        // Set peserta
        etPeserta.setText(username);
        etPeserta.setFocusable(false); // readonly

        btnKonfirmasi.setOnClickListener(v -> {
            String catatan = etCatatan.getText().toString().trim();

            if (catatan.isEmpty()) {
                Toast.makeText(this, "Isi catatan / keluhan", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build request
            BookingRequest request = new BookingRequest(
                    userId,
                    hospital,
                    poly,
                    doctor,
                    "A" + nextQueue,
                    bookingDateTime,
                    catatan,
                    "Aktif"
            );

            sendBooking(request);
        });
    }

    private void sendBooking(BookingRequest request) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.createBooking(request).enqueue(new Callback<ApiResponse<BookingContent>>() {
            @Override
            public void onResponse(Call<ApiResponse<BookingContent>> call, Response<ApiResponse<BookingContent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<BookingContent> body = response.body();

                    if ("200".equals(body.getCode())) {
                        BookingContent booking = body.getContent();

                        // Lanjut ke BookingSuccessActivity + kirim data booking
                        Intent intent = new Intent(BookingStep2Activity.this, BookingSuccessActivity.class);
                        intent.putExtra("queue_number", booking.getQueue_number());
                        intent.putExtra("hospital", booking.getHospital());
                        intent.putExtra("doctor", booking.getDoctor());
                        intent.putExtra("department", booking.getDepartment());
                        intent.putExtra("booking_date", booking.getBooking_date());
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(BookingStep2Activity.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BookingStep2Activity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<BookingContent>> call, Throwable t) {
                Toast.makeText(BookingStep2Activity.this, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
