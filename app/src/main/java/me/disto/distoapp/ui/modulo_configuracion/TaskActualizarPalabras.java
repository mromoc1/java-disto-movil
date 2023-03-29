package me.disto.distoapp.ui.modulo_configuracion;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import me.disto.distoapp.ui.utils.UserConfig;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskActualizarPalabras extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent;
        try {
            jsonContent = objectMapper.writeValueAsString(UserConfig.wordsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", UserConfig.user)
                .addFormDataPart("jsonContent", jsonContent)
                .build();
        Request request = new Request.Builder()
                .url("http://34.82.255.249/setWords")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
