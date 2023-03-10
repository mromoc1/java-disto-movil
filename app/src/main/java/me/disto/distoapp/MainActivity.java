package me.disto.distoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import me.disto.distoapp.ui.login.LoginActivity;
import me.disto.distoapp.ui.modulo_aprendizaje_lectura.AprendizajeLecturaActivity;
import me.disto.distoapp.ui.modulo_informacion.InformacionActivity;
import me.disto.distoapp.ui.utils.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
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



