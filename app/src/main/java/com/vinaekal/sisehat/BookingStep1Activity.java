package com.vinaekal.sisehat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingStep1Activity extends AppCompatActivity {

    // Deklarasi Variabel UI
    private TextView tvDate, tvMonthLabel, tvTime, tvPeriodLabel;
    private LinearLayout btnSelectDate, btnSelectTime;

    // Calendar untuk menyimpan waktu yang dipilih user
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_step1);

        // 1. Inisialisasi View
        tvDate = findViewById(R.id.tvDate);
        tvMonthLabel = findViewById(R.id.tvMonthLabel);
        tvTime = findViewById(R.id.tvTime);
        tvPeriodLabel = findViewById(R.id.tvPeriodLabel);

        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        Button btnLanjutkan = findViewById(R.id.btnLanjutkan);

        // 2. Set Tanggal Default saat aplikasi dibuka (Hari ini)
        updateDateLabel();
        updateTimeLabel();

        // 3. Logika Klik Pilih TANGGAL
        btnSelectDate.setOnClickListener(v -> {
            // Tampilkan Popup Kalender
            new DatePickerDialog(BookingStep1Activity.this, (view, year, month, dayOfMonth) -> {
                // Simpan pilihan user ke variabel calendar
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Update teks di layar
                updateDateLabel();
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 4. Logika Klik Pilih JAM
        btnSelectTime.setOnClickListener(v -> {
            // Tampilkan Popup Jam
            new TimePickerDialog(BookingStep1Activity.this, (view, hourOfDay, minute) -> {
                // Simpan pilihan user ke variabel calendar
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Update teks di layar
                updateTimeLabel();
            },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show(); // true = Format 24 Jam
        });

        // 5. Tombol Lanjutkan ke Halaman 2
        btnLanjutkan.setOnClickListener(v -> {
            Intent intent = new Intent(BookingStep1Activity.this, BookingStep2Activity.class);
            startActivity(intent);
        });
    }

    // Method untuk memperbarui Teks Tanggal
    private void updateDateLabel() {
        // Format Tanggal (Angka saja, misal: "20")
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.getDefault());
        tvDate.setText(dateFormat.format(calendar.getTime()));

        // Format Bulan (Nama bulan, misal: "Januari") - Pakai Locale Indonesia
        Locale idLocale = new Locale("id", "ID");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", idLocale);
        tvMonthLabel.setText(monthFormat.format(calendar.getTime()));
    }

    // Method untuk memperbarui Teks Jam dan Keterangan Waktu
    private void updateTimeLabel() {
        // Format Jam (Misal: "14:30")
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        tvTime.setText(timeFormat.format(calendar.getTime()));

        // Logika Menentukan Pagi/Siang/Sore/Malam
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String period;

        if (hour >= 0 && hour < 11) {
            period = "Pagi";
        } else if (hour >= 11 && hour < 15) {
            period = "Siang";
        } else if (hour >= 15 && hour < 18) {
            period = "Sore";
        } else {
            period = "Malam";
        }

        tvPeriodLabel.setText(period);
    }
}