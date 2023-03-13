package me.disto.distoapp.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.disto.distoapp.MainActivity;
import me.disto.distoapp.R;
import me.disto.distoapp.ui.modulo_aprendizaje_lectura.AprendizajeLecturaActivity;
import me.disto.distoapp.ui.modulo_informacion.InformacionActivity;
import me.disto.distoapp.ui.utils.BaseActivity;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends BaseActivity {

    TextView text_username;
    TextView text_password;
    private Button login_button;
    public static String user;
    public static String password;
//    private TextView login_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_username = findViewById(R.id.userName);
        text_password = findViewById(R.id.userPassword);
        login_button = findViewById(R.id.loginButton);


      login_button.setOnClickListener(v -> {
            LoginTask loginTask = new LoginTask();
            loginTask.execute();
            System.out.println("Login button pressed");
      });
    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            String user = text_username.getText().toString();
            String password = text_password.getText().toString();
            String url = "http://34.176.11.115/loginUser";
            loginUser(user, password, url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        private Boolean loginUser(String user, String password, String url) {
            OkHttpClient client = new OkHttpClient();

// Construir la solicitud POST con el parámetro "user"
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user", user)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

// Ejecutar la solicitud y recibir la respuesta JSON
            try (Response response = client.newCall(request).execute()) {
                String jsonString = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonString);

                String status = jsonObject.optString("status");
                if (status.equals("ok")){
                    Intent intent = new Intent(LoginActivity.this, InformacionActivity.class);
                    startActivity(intent);
                    return true;
                }
                else{
                    return false;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
}



