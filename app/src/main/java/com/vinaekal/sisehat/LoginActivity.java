package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    private EditText editEmail, editPassword;
    private Button buttonLogin, buttonBiometric;
    private TextView textSubDescription;
    private FirebaseAuth mAuth;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        session = new Session(this);

        // Auto-login jika token benar-benar masih ada (tidak di-kill)
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

        // 🔹 LOGIC: Jika aplikasi di-kill, token hilang tapi canUseBiometric masih TRUE
        if (!session.isLoggedIn() && session.canUseBiometric()) {
            triggerBiometricPrompt();
        }
    }

    private void setupBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL;

        switch (biometricManager.canAuthenticate(authenticators)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                buttonBiometric.setVisibility(View.VISIBLE);
                break;
            default:
                buttonBiometric.setVisibility(View.GONE);
                break;
        }

        buttonBiometric.setOnClickListener(v -> triggerBiometricPrompt());
    }

    private void triggerBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Biometrik berhasil -> paksa masuk karena user sudah terverifikasi secara hardware
                Toast.makeText(LoginActivity.this, "Otentikasi Berhasil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Otentikasi Si Sehat")
                .setSubtitle("Gunakan Sidik Jari/Wajah/PIN")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

        biometricPrompt.authenticate(promptInfo);
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
                            session.saveToken(user.getUid());
                            session.saveUsername(user.getDisplayName() != null ? user.getDisplayName() : "User");
                            session.saveProfile(user.getEmail(), "", "", "", "");
                            session.setCanUseBiometric(true); // Aktifkan biometrik setelah login manual sukses

                            startService(new Intent(LoginActivity.this, SessionService.class));
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}