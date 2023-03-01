package me.disto.distoapp.ui.modulo_aprendizaje_discurso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class hiloSubirArchivo extends Thread{

    private final File audio;
    private final String usuario;

    public hiloSubirArchivo (File audio, String usuario){
        this.audio = audio;
        this.usuario = usuario;
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://34.30.175.109/upload_wav";
        MediaType mediaType = MediaType.parse("audio/wav");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio", "record.wav", RequestBody.create(mediaType, audio))
                .addFormDataPart("usuario", usuario)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
