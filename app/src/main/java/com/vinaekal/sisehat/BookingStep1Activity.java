package com.vinaekal.sisehat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class BookingStep1Activity extends AppCompatActivity {

    private EditText etSelectDate;
    private Spinner spinnerTime, spinnerHospital, spinnerPoly, spinnerDoctor;
    private TextView tvMaxKuota, tvNextQueue;
    private Button btnLanjutkan;

    private Calendar calendar = Calendar.getInstance();
    private final int MAX_KUOTA = 25;
    private int nextQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_step1);

        // Inisialisasi UI
        etSelectDate = findViewById(R.id.etSelectDate);
        spinnerTime = findViewById(R.id.spinnerTime);
        spinnerHospital = findViewById(R.id.spinnerHospital);
        spinnerPoly = findViewById(R.id.spinnerPoly);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        tvMaxKuota = findViewById(R.id.tvMaxKuota);
        tvNextQueue = findViewById(R.id.tvNextQueue);
        btnLanjutkan = findViewById(R.id.btnLanjutkan);

        // Setup Max Kuota
        tvMaxKuota.setText("" + MAX_KUOTA);

        // Setup Next Queue (acak 4–25)
        nextQueue = new Random().nextInt(MAX_KUOTA - 4 + 1) + 4;
        tvNextQueue.setText("" + nextQueue);

        // Setup spinnerTime (3 opsi jam)
        String[] jamOptions = {"09:00", "13:00", "16:00"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, jamOptions);
        spinnerTime.setAdapter(timeAdapter);

        // Setup spinner hospital / poly / doctor (hardcode)
        String[] hospitals = {"RS Siloam", "RS Harapan Kita", "RS Mitra Keluarga"};
        String[] polys = {"Cardiology", "Dermatology", "Neurology", "Pediatrics"};
        String[] doctors = {"Dr. Andi", "Dr. Budi", "Dr. Citra", "Dr. Dedi"};

        spinnerHospital.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hospitals));
        spinnerPoly.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, polys));
        spinnerDoctor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doctors));

        // Klik EditText untuk pilih Tanggal
        etSelectDate.setOnClickListener(v -> {
            new DatePickerDialog(BookingStep1Activity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateEditText();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Tombol Lanjut
        btnLanjutkan.setOnClickListener(v -> {
            // Validasi date dulu
            String selectedDate = etSelectDate.getText().toString().trim();
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Pilih tanggal dulu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ambil spinner values
            String selectedTime = spinnerTime.getSelectedItem().toString();
            String hospital = spinnerHospital.getSelectedItem().toString();
            String poly = spinnerPoly.getSelectedItem().toString();
            String doctor = spinnerDoctor.getSelectedItem().toString();

            // Konversi date dari EditText ke format yyyy-MM-dd
            // Asumsikan etSelectDate format "dd MMMM yyyy", misal "09 Januari 2026"
            SimpleDateFormat sdfInput = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String bookingDate = "";
            try {
                bookingDate = sdfOutput.format(sdfInput.parse(selectedDate));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Format tanggal salah", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gabungkan date + time
            String bookingDateTime = bookingDate + "T" + selectedTime + ":00";

            // Intent ke Step2
            Intent intent = new Intent(BookingStep1Activity.this, BookingStep2Activity.class);
            intent.putExtra("hospital", hospital);
            intent.putExtra("poly", poly);
            intent.putExtra("doctor", doctor);
            intent.putExtra("bookingDateTime", bookingDateTime);
            intent.putExtra("nextQueue", nextQueue);
            startActivity(intent);
        });

    }

    private void updateDateEditText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        etSelectDate.setText(dateFormat.format(calendar.getTime()));
    }
}
