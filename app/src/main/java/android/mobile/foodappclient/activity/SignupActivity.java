package android.mobile.foodappclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.mobile.foodappclient.databinding.ActivitySignupBinding;
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

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

        binding.btnSignup.setOnClickListener(v -> {
            getSignUp();
        });
    }
    private void getSignUp() {
        String name = binding.edtNameSignup.getText().toString().trim();
        String email = binding.edtEmailSignup.getText().toString().trim();
        String phoneText = binding.edtPhoneSignup.getText().toString().trim();
        String pass = binding.edtPassSignup.getText().toString().trim();

        // Kiểm tra xem các trường có trống không
        if (name.isEmpty() || email.isEmpty() || phoneText.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem chuỗi số điện thoại có chứa ký tự không phải số không
        if (!phoneText.matches("\\d+")) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải lớn hơn 6 kí tự ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi số điện thoại từ chuỗi sang kiểu int
        int phone = Integer.parseInt(phoneText);

        // Tiếp tục xử lý đăng ký nếu không có trường nào trống
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(pass);

        // Gọi API đăng ký

    }

}