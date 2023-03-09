package me.disto.distoapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import me.disto.distoapp.MainActivity;
import me.disto.distoapp.R;
import me.disto.distoapp.ui.modulo_aprendizaje_lectura.AprendizajeLecturaActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_username = findViewById(R.id.userName);
        text_password = findViewById(R.id.userPassword);
        login_button = findViewById(R.id.loginButton);


//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Login button clicked");
                Intent intent = new Intent(LoginActivity.this, AprendizajeLecturaActivity.class);
                startActivity(intent);
                finish();
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://34.176.11.115/loginUser";
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("user", text_username.getText().toString())
                                .addFormDataPart("password", text_password.getText().toString())
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
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else{

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });
    }


    public void login(View v) {
//        String username = text_username.getText().toString();
//        String password = text_password.getText().toString();
//        LoginTask loginTask = new LoginTask();
//        loginTask.execute();
    }
}



