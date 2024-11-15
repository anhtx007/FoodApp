package android.mobile.foodappclient.activity;

import android.content.Intent;
import android.mobile.foodappclient.databinding.ActivityIntroBinding;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.mobile.foodappclient.R;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
    }
    private void setVariable() {
        binding.textViewLogin.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        });

        binding.textViewSignUp.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, SignupActivity.class));

        });
    }
}