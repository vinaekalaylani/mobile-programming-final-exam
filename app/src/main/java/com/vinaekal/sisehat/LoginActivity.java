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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

        // Munculkan prompt hanya jika sudah diberi izin sebelumnya
        if (!session.isLoggedIn() && session.canUseBiometric()) {
            triggerBiometricPrompt();
        }
    }

    private void setupBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG 
                           | BiometricManager.Authenticators.BIOMETRIC_WEAK
                           | BiometricManager.Authenticators.DEVICE_CREDENTIAL;

        if (biometricManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS) {
            buttonBiometric.setVisibility(View.VISIBLE);
        } else {
            buttonBiometric.setVisibility(View.GONE);
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
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Otentikasi Si Sehat")
                .setSubtitle("Gunakan Wajah, Sidik Jari, atau PIN")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG 
                                         | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
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
                            
                            startService(new Intent(LoginActivity.this, SessionService.class));
                            showBiometricActivationDialog();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showBiometricActivationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Aktifkan Biometrik?")
                .setMessage("Apakah Anda ingin mengaktifkan Sidik Jari/Wajah untuk login berikutnya agar lebih cepat?")
                .setPositiveButton("Ya, Aktifkan", (dialog, which) -> {
                    session.setCanUseBiometric(true);
                    proceedToMain();
                })
                .setNegativeButton("Nanti Saja", (dialog, which) -> {
                    session.setCanUseBiometric(false);
                    proceedToMain();
                })
                .setCancelable(false)
                .show();
    }

    private void proceedToMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}