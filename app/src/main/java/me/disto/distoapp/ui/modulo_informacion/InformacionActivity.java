package me.disto.distoapp.ui.modulo_informacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.utils.BaseActivity;

public class InformacionActivity extends BaseActivity {
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        Intent intent = getIntent();
        user = intent.getStringExtra("user");


        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_home).setChecked(true);
    }
}

