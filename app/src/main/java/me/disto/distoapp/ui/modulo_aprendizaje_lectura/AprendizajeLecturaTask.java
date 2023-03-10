package me.disto.distoapp.ui.modulo_aprendizaje_lectura;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AprendizajeLecturaTask extends AsyncTask<Void, Void, Void>
{
    private final String apiUrl;
    private final byte[] audioBuffer;

    // constructor
    public AprendizajeLecturaTask(String apiUrl, byte[] audioBuffer) {
        this.apiUrl = apiUrl;
        this.audioBuffer = audioBuffer;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            sendAudio(audioBuffer, apiUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void sendAudio(byte[] audioBuffer, String apiUrl) throws IOException {
//        RequestBody requestBody = RequestBody.create(audioBuffer, MediaType.parse("audio/wav"));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("messageFile", "audio.wav", RequestBody.create(audioBuffer, MediaType.parse("audio/wav")))
//                .addFormDataPart("label", "some label")
                .build();
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
