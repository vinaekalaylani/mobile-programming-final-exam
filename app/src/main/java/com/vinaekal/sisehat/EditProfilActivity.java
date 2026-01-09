package com.vinaekal.sisehat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.vinaekal.sisehat.util.Session;

import java.util.Calendar;
import java.util.Locale;

public class EditProfilActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPhone, etBirthDate, etAddress, etJob;
    private Button btnSimpan;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        session = new Session(this);

        etUsername = findViewById(R.id.edit_username);
        etEmail = findViewById(R.id.edit_email);
        etPhone = findViewById(R.id.edit_telepon);
        etBirthDate = findViewById(R.id.edit_tanggal_lahir);
        etAddress = findViewById(R.id.edit_alamat);
        etJob = findViewById(R.id.edit_pekerjaan);
        btnSimpan = findViewById(R.id.btn_simpan);

        // Load current data
        etUsername.setText(session.getUsername());
        etEmail.setText(session.getEmail());
        etPhone.setText(session.getPhone());
        etBirthDate.setText(session.getBirthDate());
        etAddress.setText(session.getAddress());
        etJob.setText(session.getJob());

        // Setup DatePicker for etBirthDate
        etBirthDate.setFocusable(false);
        etBirthDate.setClickable(true);
        etBirthDate.setOnClickListener(v -> showDatePicker());

        btnSimpan.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String birthDate = etBirthDate.getText().toString();
            String address = etAddress.getText().toString();
            String job = etJob.getText().toString();

            // Simpan ke session
            session.saveUsername(username);
            session.saveProfile(email, phone, birthDate, address, job);

            Toast.makeText(this, "Profil diperbarui", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke ProfilActivity
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year1);
                    etBirthDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
