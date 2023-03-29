package me.disto.distoapp.ui.modulo_configuracion;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.disto.distoapp.ui.utils.UserConfig;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskObtenerPalabras extends AsyncTask<Void, Void, PalabrasMapAdapter> {

    private final OnPalabrasMapAdapterListener listener;

    public TaskObtenerPalabras(OnPalabrasMapAdapterListener listener) {

        this.listener = listener;
    }

    @Override
    protected PalabrasMapAdapter doInBackground(Void... voids) {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", UserConfig.user)
                .build();

        Request request = new Request.Builder()
                .url("http://34.82.255.249/getWords")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            JSONObject responseJSON = new JSONObject(responseString);

            JSONArray wordsArray = responseJSON.getJSONArray("words");
            Map<String, Integer> wordsMap = new HashMap<>();

            for (int i = 0; i < wordsArray.length(); i++) {
                JSONObject wordObject = wordsArray.getJSONObject(i);
                String word = wordObject.getString("word");
                int label = wordObject.getInt("label");
                wordsMap.put(word, label);
            }
            UserConfig.wordsMap = wordsMap;
            for (Map.Entry<String, Integer> entry : wordsMap.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new PalabrasMapAdapter(UserConfig.wordsMap);
    }

    @Override
    protected void onPostExecute(PalabrasMapAdapter adapter) {
        // Llama al método onAdapterCreated() de la interfaz y pasa el adaptador como parámetro
        listener.onPalabrasMapAdapterListener(adapter);
    }
}