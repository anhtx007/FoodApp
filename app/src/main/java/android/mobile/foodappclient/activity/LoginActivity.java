package android.mobile.foodappclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.mobile.foodappclient.databinding.ActivityLoginBinding;
import android.mobile.foodappclient.model.User;
import android.mobile.foodappclient.service.ApiService;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.mobile.foodappclient.R;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
        binding.btnLogin.setOnClickListener(v -> {
            setLogin();
        });
    }
    private void setLogin() {
        String email = binding.edtEmail.getText().toString();
        String pass = binding.edtPassword.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(pass);


}