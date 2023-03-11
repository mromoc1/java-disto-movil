package me.disto.distoapp.ui.modulo_configuracion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.utils.BaseActivity;

public class ConfiguracionActivity extends BaseActivity {

    SeekBar seekBarVelReproduccion;
    SeekBar seekBarFrecAnticipacion;
    TextView velReproduccion;
    TextView frecAnticipacion;
    Spinner spinnerCantPalabras;
    String cantPalabras;
    Spinner spinnerModelo;
    String modelo;
    Switch predActiva;
    Switch predReactiva;
    String predActivaSelected;
    String predReactivaSelected;
    Button buttonGuardar;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        seekBarVelReproduccion = findViewById(R.id.seekBarVelRep);
        velReproduccion = findViewById(R.id.velReproduccion);
        seekBarFrecAnticipacion = findViewById(R.id.seekBarFrecAnt);
        frecAnticipacion = findViewById(R.id.frecAnticipacion);
        spinnerCantPalabras = findViewById(R.id.spinnerCantPalabras);
        spinnerModelo = findViewById(R.id.spinnerModelo);
        predActiva = findViewById(R.id.switchActiva);
        predReactiva = findViewById(R.id.switchReactiva);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        // Establecer el progreso mínimo y máximo
        seekBarVelReproduccion.setMax(100);
        seekBarVelReproduccion.setMin(0);

        // Actualizar el valor de la SeekBar cada vez que se arrastra
        seekBarVelReproduccion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Actualizar el valor de la SeekBar en incrementos de 10
                progress = (progress / 10) * 10;
                seekBarVelReproduccion.setProgress(progress);
                velReproduccion.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Este método se llama cuando el usuario toca la SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Este método se llama cuando el usuario suelta la SeekBar
            }
        });

        // Establecer el progreso mínimo y máximo
        seekBarFrecAnticipacion.setMax(100);
        seekBarFrecAnticipacion.setMin(0);

        // Actualizar el valor de la SeekBar cada vez que se arrastra
        seekBarFrecAnticipacion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Actualizar el valor de la SeekBar en incrementos de 10
                progress = (progress / 10) * 10;
                seekBarFrecAnticipacion.setProgress(progress);
                frecAnticipacion.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Este método se llama cuando el usuario toca la SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Este método se llama cuando el usuario suelta la SeekBar
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_cant_palabras, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCantPalabras.setAdapter(adapter);

        spinnerCantPalabras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get item and save in modelo
                cantPalabras = parent.getItemAtPosition(position).toString();
                System.out.println(cantPalabras);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter_modelos = ArrayAdapter.createFromResource(this, R.array.opciones_modelo, android.R.layout.simple_spinner_item);
        adapter_modelos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModelo.setAdapter(adapter_modelos);

        spinnerModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get item and save in modelo
                modelo = parent.getItemAtPosition(position).toString();
                System.out.println(modelo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        predActiva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    predActivaSelected = "True";
                } else {
                    predActivaSelected = "False";

                }
            }
        });

        predReactiva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    predReactivaSelected = "True";
                } else{
                    predReactivaSelected = "False";
                }
            }
        });

        buttonGuardar.setOnClickListener(v -> {
            //Guardar en la base de datos

            ConfiguracionActivity.this.runOnUiThread(() -> {
                Toast.makeText(ConfiguracionActivity.this, "Configuración guardada", Toast.LENGTH_SHORT).show();
            });

        });

        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_configuracion).setChecked(true);
    }
}