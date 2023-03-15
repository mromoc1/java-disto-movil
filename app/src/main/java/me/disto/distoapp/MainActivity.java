package me.disto.distoapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.disto.distoapp.ui.modulo_aprendizaje_lectura.AprendizajeLecturaActivity;
import me.disto.distoapp.ui.modulo_configuracion.ConfiguracionActivity;
import me.disto.distoapp.ui.modulo_informacion.InformacionActivity;
import me.disto.distoapp.ui.utils.BaseActivity;
import me.disto.distoapp.ui.utils.UserConfig;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Request.Builder;
import okhttp3.Response;



public class MainActivity extends BaseActivity {

    private Button login_button;
    private TextView text_username;
    private TextView text_password;
    private TextView login_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_informacion);
        setContentView(R.layout.activity_login);

        text_username = findViewById(R.id.userName);
        login_button = findViewById(R.id.loginButton);
        text_password = findViewById(R.id.userPassword);
        login_status = findViewById(R.id.loginStatus);
        login_button.setOnClickListener(v -> {
            LoginTask loginTask = new LoginTask();
            loginTask.execute();
        });
    }
    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = "http://34.176.11.115/loginUser";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user", text_username.getText().toString())
                    .addFormDataPart("password", text_password.getText().toString())
                    .build();
            Request request = new Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            // Ejecutar la solicitud y recibir la respuesta JSON
            try (Response response = client.newCall(request).execute()) {
                String jsonString = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonString);

                String status = jsonObject.optString("status");
                if (status.equals("ok")){
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                GetConfigurationTask getConfigurationTask = new GetConfigurationTask();
                getConfigurationTask.execute();
                Intent intent = new Intent(MainActivity.this, InformacionActivity.class);
                startActivity(intent);
                finish();
            } else {
                login_status.setText("Usuario o contraseÃ±a incorrectos");
//                crear un hilo que muestre el mensaje de error y que despues de 3 segundos lo oculte

            }
        }
    }

    class GetConfigurationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String user = text_username.getText().toString();
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user", user)
                    .build();

            Request request = new Builder()
                    .url("http://34.176.11.115/getConfigurationFile")
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                JSONObject responseJSON = new JSONObject(responseString);
                UserConfig.user = user;
                UserConfig.longitudMaxima = responseJSON.getString("longitud_maxima");
                UserConfig.largoPalabrasClasificadas = responseJSON.getString("largo_palabras_clasificadas");
                UserConfig.usuarioNuevo = responseJSON.getString("usuario_nuevo");
                UserConfig.velReproduccion = responseJSON.getString("velocidad_reproduccion");
                UserConfig.frecAnticipacion = responseJSON.getString("frecuencia_anticipacion");
                UserConfig.modelo = responseJSON.getString("modelo");
                UserConfig.cantPalabras = responseJSON.getString("cantidad_palabras_prediccion");
                UserConfig.predReactivaSelected = responseJSON.getString("prediccion_reactiva");
                UserConfig.predActivaSelected = responseJSON.getString("prediccion_activa");
                UserConfig.customModel = responseJSON.getString("custom_model");

//                imprimir variables de UserConfig
                System.out.println("UserConfig.user: " + UserConfig.user);
                System.out.println("UserConfig.longitudMaxima: " + UserConfig.longitudMaxima);
                System.out.println("UserConfig.largoPalabrasClasificadas: " + UserConfig.largoPalabrasClasificadas);
                System.out.println("UserConfig.usuarioNuevo: " + UserConfig.usuarioNuevo);
                System.out.println("UserConfig.velReproduccion: " + UserConfig.velReproduccion);
                System.out.println("UserConfig.frecAnticipacion: " + UserConfig.frecAnticipacion);
                System.out.println("UserConfig.modelo: " + UserConfig.modelo);
                System.out.println("UserConfig.cantPalabras: " + UserConfig.cantPalabras);
                System.out.println("UserConfig.predReactivaSelected: " + UserConfig.predReactivaSelected);
                System.out.println("UserConfig.predActivaSelected: " + UserConfig.predActivaSelected);
                System.out.println("UserConfig.customModel: " + UserConfig.customModel);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



}



