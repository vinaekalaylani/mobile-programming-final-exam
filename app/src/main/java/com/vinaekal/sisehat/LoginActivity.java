package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vinaekal.sisehat.util.Session;
import com.vinaekal.sisehat.util.SessionService;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button buttonLogin, buttonBiometric;
    TextView textSubDescription;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // 🔹 Auto-login jika session masih ada
        Session session = new Session(this);
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonBiometric = findViewById(R.id.buttonBiometric);
        textSubDescription = findViewById(R.id.textSubDescription);

        buttonLogin.setOnClickListener(v -> login());

        textSubDescription.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        setupBiometric();
    }

    private void setupBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL;

        switch (biometricManager.canAuthenticate(authenticators)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                buttonBiometric.setVisibility(Button.VISIBLE);
                break;
            default:
                buttonBiometric.setVisibility(Button.GONE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Session session = new Session(LoginActivity.this);
                if (session.getToken() != null) {
                    Toast.makeText(LoginActivity.this, "Login Berhasil (Biometrik)", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Silakan login manual terlebih dahulu untuk pertama kali", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Otentikasi gagal", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login Si Sehat")
                .setSubtitle("Gunakan sidik jari, wajah, atau PIN Anda")
                .setAllowedAuthenticators(authenticators)
                .build();

        buttonBiometric.setOnClickListener(v -> biometricPrompt.authenticate(promptInfo));
    }

    private void login() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Session session = new Session(LoginActivity.this);
                            session.saveToken(user.getUid()); // Gunakan UID sebagai token
                            session.saveUsername(user.getDisplayName() != null ? user.getDisplayName() : "User");
                            session.saveProfile(user.getEmail(), "", "", "", "");

                            Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                            // Jalankan Service untuk deteksi kill task
                            startService(new Intent(LoginActivity.this, SessionService.class));

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session session = new Session(this);
        if (session.isLoggedIn()) {
            session.saveLastPage(getClass().getName());
        }
    }
}