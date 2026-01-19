package com.vinaekal.sisehat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vinaekal.sisehat.util.Session;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfilActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPhone, etBirthDate, etAddress, etJob;
    private Button btnSimpan;
    private Session session;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        session = new Session(this);

        etUsername = findViewById(R.id.edit_username);
        etEmail = findViewById(R.id.edit_email);
        etPhone = findViewById(R.id.edit_telepon);
        etBirthDate = findViewById(R.id.edit_tanggal_lahir);
        etAddress = findViewById(R.id.edit_alamat);
        etJob = findViewById(R.id.edit_pekerjaan);
        btnSimpan = findViewById(R.id.btn_simpan);

        // Load current data from Session
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

        btnSimpan.setOnClickListener(v -> saveProfileToCloud());
    }

    private void saveProfileToCloud() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String job = etJob.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nama dan Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            // Create data map
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("username", username);
            profileData.put("email", email);
            profileData.put("phone", phone);
            profileData.put("birthDate", birthDate);
            profileData.put("address", address);
            profileData.put("job", job);

            // Save to Firestore
            db.collection("users").document(uid)
                    .set(profileData)
                    .addOnSuccessListener(aVoid -> {
                        // Update local session
                        session.saveUsername(username);
                        session.saveProfile(email, phone, birthDate, address, job);

                        Toast.makeText(EditProfilActivity.this, "Profil berhasil disinkronkan ke Cloud", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditProfilActivity.this, "Gagal sinkronisasi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
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