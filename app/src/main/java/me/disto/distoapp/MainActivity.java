package me.disto.distoapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import me.disto.distoapp.ui.modulo_aprendizaje_lectura.AprendizajeLecturaActivity;
import me.disto.distoapp.ui.modulo_informacion.InformacionActivity;
import me.disto.distoapp.ui.utils.BaseActivity;
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
                Intent intent = new Intent(MainActivity.this, InformacionActivity.class);
                startActivity(intent);
                finish();
            } else {
                login_status.setText("Usuario o contraseña incorrectos");
//                crear un hilo que muestre el mensaje de error y que despues de 3 segundos lo oculte

            }
        }
    }



}



