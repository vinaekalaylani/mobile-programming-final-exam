package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vinaekal.sisehat.model.ApiRequest;
import com.vinaekal.sisehat.model.ApiResponse;
import com.vinaekal.sisehat.model.LoginContent;
import com.vinaekal.sisehat.network.ApiClient;
import com.vinaekal.sisehat.network.ApiService;
import com.vinaekal.sisehat.util.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button buttonLogin;
    TextView textSubDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 🔹 Auto-login
        Session session = new Session(this);
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, BookingStep1Activity.class));
            finish();
            return;
        }

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textSubDescription = findViewById(R.id.textSubDescription);

        buttonLogin.setOnClickListener(v -> login());

        textSubDescription.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void login() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        ApiRequest request = new ApiRequest(email, password);

        apiService.login(request).enqueue(new Callback<ApiResponse<LoginContent>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginContent>> call, Response<ApiResponse<LoginContent>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginContent> body = response.body();

                    if ("0".equals(body.getCode())) {
                        Session session = new Session(LoginActivity.this);
                        session.saveToken(body.getContent().getToken());

                        Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, BookingStep1Activity.class));
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