package me.disto.distoapp.ui.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.modulo_aprendizaje_discurso.AprendizajeDiscursoActivity;
import me.disto.distoapp.ui.modulo_aprendizaje_lectura.AprendizajeLecturaActivity;
import me.disto.distoapp.ui.modulo_generacion_sugerencia.GeneracionSugerenciaActivity;
import me.disto.distoapp.ui.modulo_configuracion.ConfiguracionActivity;
import me.disto.distoapp.ui.modulo_informacion.InformacionActivity;

public class BaseActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    protected BottomNavigationView bottomNavigationView;

    protected void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(this);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Class activityClass = null;
        switch (item.getItemId()) {
            case R.id.menu_button_home:
                activityClass = InformacionActivity.class;
                break;
            case R.id.menu_button_sugerencia:
                activityClass = GeneracionSugerenciaActivity.class;
                break;
            case R.id.menu_button_discurso:
                activityClass = AprendizajeDiscursoActivity.class;
                break;
            case R.id.menu_button_lectura:
                activityClass = AprendizajeLecturaActivity.class;
                break;
            case R.id.menu_button_configuracion:
                activityClass = ConfiguracionActivity.class;
                break;
        }
        return loadActivity(activityClass);
    }
    private boolean loadActivity(Class activityClass) {
        if (activityClass != null) {
            Intent intent = new Intent(this, activityClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            return true;
        }
        return false;
    }
}