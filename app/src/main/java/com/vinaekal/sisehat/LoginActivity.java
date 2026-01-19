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

import com.vinaekal.sisehat.model.request.LoginRequest;
import com.vinaekal.sisehat.model.response.ApiResponse;
import com.vinaekal.sisehat.model.content.LoginContent;
import com.vinaekal.sisehat.network.ApiClient;
import com.vinaekal.sisehat.network.ApiService;
import com.vinaekal.sisehat.util.Session;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button buttonLogin, buttonBiometric;
    TextView textSubDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 🔹 Auto-login
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
                if (session.getUsername() != null && !session.getUsername().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Silakan login manual terlebih dahulu sekali", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Otentikasi error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Otentikasi gagal", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login Si Sehat")
                .setSubtitle("Gunakan sidik jari, wajah, atau PIN/Pola Anda")
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

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<ApiResponse<LoginContent>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginContent>> call, Response<ApiResponse<LoginContent>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginContent> body = response.body();

                    if ("0".equals(body.getCode())) {
                        Session session = new Session(LoginActivity.this);
                        session.saveToken(body.getContent().getToken());
                        session.saveUsername(body.getContent().getUser().getUsername());
                        session.saveUserId(body.getContent().getUser().getId());

                        Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginContent>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}