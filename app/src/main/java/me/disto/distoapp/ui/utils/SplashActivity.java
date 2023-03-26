package me.disto.distoapp.ui.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import me.disto.distoapp.MainActivity;
import me.disto.distoapp.R;
import me.disto.distoapp.ui.modulo_informacion.InformacionActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, InformacionActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }

}
