package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vinaekal.sisehat.model.content.RegisterContent;
import com.vinaekal.sisehat.model.request.RegisterRequest;
import com.vinaekal.sisehat.model.response.ApiResponse;
import com.vinaekal.sisehat.network.ApiClient;
import com.vinaekal.sisehat.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText editFullname, editEmail, editPassword, editConfirmPassword;
    Button buttonRegister;
    TextView textLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editFullname = findViewById(R.id.editFullname);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textLogin = findViewById(R.id.textLogin);

        buttonRegister.setOnClickListener(v -> register());

        textLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void register() {
        String fullname = editFullname.getText().toString();
        String email = editEmail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();
        String confirm = editConfirmPassword.getText().toString().trim();

        if (fullname.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        RegisterRequest request = new RegisterRequest(fullname, email, pass);

        apiService.register(request).enqueue(new Callback<ApiResponse<RegisterContent>>() {
            @Override
            public void onResponse(Call<ApiResponse<RegisterContent>> call, Response<ApiResponse<RegisterContent>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if ("0".equals(response.body().getCode())) {
                        Toast.makeText(RegisterActivity.this, "Register berhasil, silakan login", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RegisterContent>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
