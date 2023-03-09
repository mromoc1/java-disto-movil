package me.disto.distoapp.ui.login;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginTask extends AsyncTask<Void, Void, Void> {


    @Override
    protected Void doInBackground(Void... voids) {
        LoginActivity loginActivity = new LoginActivity();

        String user = loginActivity.text_username.getText().toString();
        String password = loginActivity.text_password.getText().toString();
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

    public void execute(String username, String password) {


    }
}
