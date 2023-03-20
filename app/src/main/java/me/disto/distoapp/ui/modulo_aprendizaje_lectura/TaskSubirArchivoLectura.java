package me.disto.distoapp.ui.modulo_aprendizaje_lectura;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import me.disto.distoapp.ui.utils.UserConfig;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskSubirArchivoLectura extends AsyncTask<Void, Void, Void>{

    private File archivo;
    private String usuario;

    public TaskSubirArchivoLectura(File archivo, String usuario){
        this.archivo = archivo;
        this.usuario = usuario;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String user = usuario;
        MediaType mediaType = MediaType.parse("palabras/json");
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user)
                .addFormDataPart("audio", "record.wav", RequestBody.Companion.create(archivo,mediaType))
                .build();
        Request request = new Request.Builder()
                .url("http://35.197.58.139/trainClassificationModel")
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