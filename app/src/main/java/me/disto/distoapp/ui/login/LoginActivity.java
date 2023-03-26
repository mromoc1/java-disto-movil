package me.disto.distoapp.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.disto.distoapp.R;
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
    private OkHttpClient client;
    private Button login_button;
    public static String user;
    public static String password;
    private LoginTask loginTask;
//    private TextView login_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_login);

        text_username = findViewById(R.id.userName);
        text_password = findViewById(R.id.userPassword);
        login_button = findViewById(R.id.loginButton);


        login_button.setOnClickListener(v -> {

            loginTask = new LoginTask();
            loginTask.execute();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginTask != null) {
            loginTask.cancel(true);
        }
        if (client != null) {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            client = null;
        }
    }

    public class LoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {

            if (isCancelled()) {
                return null;
            }

            String user = text_username.getText().toString();
            String password = text_password.getText().toString();
            String url = "http://34.176.11.115/loginUser";
            return loginUser(user, password, url);

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success != null && success) {
                Intent intent = new Intent(LoginActivity.this, InformacionActivity.class);
                startActivity(intent);
                finish();

            } else {
                // Show error message
            }
        }

        private Boolean loginUser(String user, String password, String url) {
            client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user", user)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String status = jsonObject.optString("status");
                    return status.equals("ok");
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



