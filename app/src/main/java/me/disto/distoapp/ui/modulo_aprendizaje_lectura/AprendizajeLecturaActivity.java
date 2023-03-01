package me.disto.distoapp.ui.modulo_aprendizaje_lectura;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.utils.BaseActivity;

public class AprendizajeLecturaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprendizaje_lectura);

        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_lectura).setChecked(true);
    }
}