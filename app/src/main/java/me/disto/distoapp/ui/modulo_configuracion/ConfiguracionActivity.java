package me.disto.distoapp.ui.modulo_configuracion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.utils.BaseActivity;

public class ConfiguracionActivity extends BaseActivity {

//    velocidad de text to speech
//    modelo de predicion
//    prediccion de frases o palabras
//    prediccion reactiva
//"nombre usuario"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_configuracion).setChecked(true);

    }
}