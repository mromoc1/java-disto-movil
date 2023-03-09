package me.disto.distoapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadConfigurationFileTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        String user = "test19";
        String url = "http://34.176.11.115/getConfigurationFile";
        loadConfigurationFile(user, url);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void loadConfigurationFile(String user, String url) {
        OkHttpClient client = new OkHttpClient();

// Construir la solicitud POST con el parámetro "user"
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

// Ejecutar la solicitud y recibir la respuesta JSON
        try (Response response = client.newCall(request).execute()) {
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);

            // Obtener los valores del JSON
            int numberOfWords = jsonObject.optInt("words_count");
            String model_id = jsonObject.optString("model_id");
            String custom_model_id = jsonObject.optString("custom_model_id");
            int max_tokens = jsonObject.optInt("max_tokens");

            // Imprimir los valores obtenidos
            System.out.println("words_count: " + numberOfWords);
            System.out.println("model_id: " + model_id);
            System.out.println("custom_model_id: " + custom_model_id);
            System.out.println("max_tokens: " + max_tokens);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}

