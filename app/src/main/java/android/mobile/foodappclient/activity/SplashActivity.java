package android.mobile.foodappclient.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.mobile.foodappclient.R;
import android.mobile.foodappclient.databinding.ActivitySplashBinding;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.lottie.animate().translationX(3000).setDuration(2000).setStartDelay(2900);

        new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, IntroActivity.class));

            }
        }.start();


    }
}