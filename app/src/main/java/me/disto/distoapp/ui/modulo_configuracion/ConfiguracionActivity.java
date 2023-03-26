package me.disto.distoapp.ui.modulo_configuracion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.disto.distoapp.MainActivity;
import me.disto.distoapp.R;
import me.disto.distoapp.ui.login.LoginActivity;
import me.disto.distoapp.ui.utils.BaseActivity;
import me.disto.distoapp.ui.utils.UserConfig;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfiguracionActivity extends BaseActivity {

    public static SeekBar seekBarVelReproduccion;
    public static SeekBar seekBarFrecAnticipacion;
    public static TextView velReproduccion;
    public static TextView frecAnticipacion;
    public static Spinner spinnerCantPalabras;
    public static Spinner spinnerPalabrasProblematicas;
    public static String user;
    String cantPalabras;
    public static Spinner spinnerModelo;
    String modelo;
    public static Switch predActiva;
    public static Switch predReactiva;
    public static String predActivaSelected;
    public static String predReactivaSelected;
    Button buttonGuardar;
    Button gestionarPalabras;

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
//        gestionarPalabras = findViewById(R.id.buttonGestionarPalabras);
        //manejador de eventos para el boton de gestionar palabras
        gestionarPalabras.setOnClickListener(v -> {
            Intent intent = new Intent(ConfiguracionActivity.this, RegistroPalabrasProblematicas.class);
            startActivity(intent);
        });
        spinnerModelo = findViewById(R.id.spinnerModelo);
        predActiva = findViewById(R.id.switchActiva);
        predReactiva = findViewById(R.id.switchReactiva);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        initConfiguracion();
        cargarConfiguracionUsuario();
        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_configuracion).setChecked(true);
    }

    private void initConfiguracion() {
        // Establecer el progreso mínimo y máximo
        seekBarVelReproduccion.setMax(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBarVelReproduccion.setMin(0);
        }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBarFrecAnticipacion.setMin(50);
        }

        // Actualizar el valor de la SeekBar cada vez que se arrastra
        seekBarFrecAnticipacion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Actualizar el valor de la SeekBar en incrementos de 10
                progress = (progress / 5) * 5;
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

        predActivaSelected = "False";
        predActiva.setChecked(false);
        predActiva.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                predActivaSelected = "True";
            } else {
                predActivaSelected = "False";

            }
        });
        predReactiva.setChecked(false);
        predReactivaSelected = "False";
        predReactiva.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                predReactivaSelected = "True";
            } else{
                predReactivaSelected = "False";
            }
        });
        buttonGuardar.setOnClickListener(v -> {
            SetConfiguration setConfiguration = new SetConfiguration();
            setConfiguration.execute();
            guardarConfiguracionUsuario();

            System.out.println("UserConfig.user: " + UserConfig.user);
            System.out.println("UserConfig.velReproduccion: " + UserConfig.velReproduccion);
            System.out.println("UserConfig.frecAnticipacion: " + UserConfig.frecAnticipacion);
            System.out.println("UserConfig.cantPalabras: " + UserConfig.cantPalabras);
            System.out.println("UserConfig.modelo: " + UserConfig.modelo);
            System.out.println("UserConfig.predActivaSelected: " + UserConfig.predActivaSelected);
            System.out.println("UserConfig.predReactivaSelected: " + UserConfig.predReactivaSelected);


        });
    }

    private void guardarConfiguracionUsuario() {
        UserConfig.velReproduccion = velReproduccion.getText().toString();
        UserConfig.frecAnticipacion = frecAnticipacion.getText().toString();
        UserConfig.cantPalabras = cantPalabras;
        UserConfig.modelo = modelo;
        UserConfig.predActivaSelected = predActivaSelected;
        UserConfig.predReactivaSelected = predReactivaSelected;
    }

    private void cargarConfiguracionUsuario() {
        seekBarVelReproduccion.setProgress(Integer.parseInt(UserConfig.velReproduccion));
        velReproduccion.setText(UserConfig.velReproduccion);
        seekBarFrecAnticipacion.setProgress(Integer.parseInt(UserConfig.frecAnticipacion));
        frecAnticipacion.setText(UserConfig.frecAnticipacion);
        // recorre los items del spinner y selecciona el que corresponde
        for (int i = 0; i < spinnerCantPalabras.getCount(); i++) {
            if (spinnerCantPalabras.getItemAtPosition(i).toString().equals(UserConfig.cantPalabras)) {
                spinnerCantPalabras.setSelection(i);
            }
        }
        for (int i = 0; i < spinnerModelo.getCount(); i++) {
            if (spinnerModelo.getItemAtPosition(i).toString().equals(UserConfig.modelo)) {
                spinnerModelo.setSelection(i);
            }
        }
        if (UserConfig.predActivaSelected.equals("True")) {
            predActiva.setChecked(true);
        } else {
            predActiva.setChecked(false);
        }
        if (UserConfig.predReactivaSelected.equals("True")) {
            predReactiva.setChecked(true);
        } else {
            predReactiva.setChecked(false);
        }
    }

    class SetConfiguration extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String url = "http://34.82.255.249/setConfigurationFile";
            OkHttpClient client = new OkHttpClient();
            System.out.println("user: " + UserConfig.user);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user", UserConfig.user)
                    .addFormDataPart("playback_speed", velReproduccion.getText().toString())
                    .addFormDataPart("anticipation_frequency", frecAnticipacion.getText().toString())
                    .addFormDataPart("prediction_words_count", cantPalabras.toString())
                    .addFormDataPart("model", modelo)
                    .addFormDataPart("prediction_active", predActivaSelected)
                    .addFormDataPart("reactive_prediction", predReactivaSelected)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.optString("status");
                    if (status.equals("ok")) {
                        Toast.makeText(ConfiguracionActivity.this, "Configuración guardada con éxito", Toast.LENGTH_SHORT).show();
                        // La solicitud fue exitosa, realiza acciones necesarias
                    } else {
                        // La solicitud falló, manejar el error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Error de red, manejarlo
            }
        }
    }
}