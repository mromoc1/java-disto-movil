package me.disto.distoapp.ui.modulo_generacion_sugerencia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import me.disto.distoapp.MainActivity;
import me.disto.distoapp.R;
import me.disto.distoapp.ui.utils.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeneracionSugerenciaActivity extends BaseActivity implements RecognitionListener, TextToSpeech.OnInitListener{

    //  UI
    private TextView text_transcrito;
    private TextView text_prediction;
    private TextView text_status;
    private Button btn_iniciar;
    private Button btn_detener;
    //  UI

    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private TextToSpeech tts;

    private String aux = "";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generacion_sugerencia);

        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_sugerencia).setChecked(true);

        text_transcrito = findViewById(R.id.text_transcrito);
        text_prediction = findViewById(R.id.text_prediction);
        text_status = findViewById(R.id.text_status);
        btn_iniciar = findViewById(R.id.btn_iniciar);
        btn_detener = findViewById(R.id.btn_detener);

        if(!tienePermisos()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
    }

    private boolean tienePermisos() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void startSpeechRecognition(View v) {
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, Long.MAX_VALUE);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, Long.MAX_VALUE);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        text_status.setText("Status: On");
        speechRecognizer.startListening(intent);
    }

    public void stopSpeechRecognition(View v) {
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
        text_status.setText("Status: Off");
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
    }

    private void resetSpeechRecognizer() {
        if(speechRecognizer != null)
            speechRecognizer.destroy();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        if(SpeechRecognizer.isRecognitionAvailable(this))
            speechRecognizer.setRecognitionListener(this);
        else
            finish();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {}
    @Override
    public void onBeginningOfSpeech() {}
    @Override
    public void onRmsChanged(float v) {}
    @Override
    public void onBufferReceived(byte[] bytes) {}
    @Override
    public void onEndOfSpeech() {}
    @Override
    public void onError(int errorCode) {
        resetSpeechRecognizer();
        speechRecognizer.startListening(intent);
    }
    @Override
    public void onResults(Bundle bundle) {speechRecognizer.startListening(intent);}
    @Override
    public void onPartialResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        StringBuilder text = new StringBuilder();
        for (String result : matches){
            text.append(result).append("\n");
        }
        //OBTENER ULTIMAS 3 PALABRAS
        if(!aux.equals(text.toString())){
            aux = text.toString();
            String[] words = text.toString().split(" ");
            String last3 = "";
            if(words.length > 3){
                last3 = words[words.length-3] + " " + words[words.length-2] + " " + words[words.length-1];
            }else{
                last3 = text.toString();
            }
            text_transcrito.setText(last3);
            //OBTENER ULTIMAS 3 PALABRAS

            // PETICION HTTP
            OkHttpClient client = new OkHttpClient();
            String url = "http://35.199.96.85/test";
            MediaType mediaType = MediaType.get("application/json;");
            String text2 = last3.replaceAll("\\n", "");
            RequestBody body = new FormBody.Builder()
                    .add("palabra", text2)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {e.printStackTrace();}
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        String myResponse = response.body().string();
                        JSONObject json = null;
                        String palabra = null;
                        String wordClass = null;
                        try {
                            json = new JSONObject(myResponse);
                            System.out.println(json);
                            palabra = json.getString("word");
                            wordClass = json.getString("class");
                            System.out.println(palabra);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        System.out.println(palabra);
                        String finalPalabra = palabra;
                        String finalWordClass = wordClass;
                        tts = new TextToSpeech(GeneracionSugerenciaActivity.this, new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status == TextToSpeech.SUCCESS) {
                                    // Establecer el lenguaje de la síntesis de voz
                                    int result = tts.setLanguage(Locale.getDefault());
                                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                        Log.e("TTS", "Lenguaje no soportado");
                                    } else if (!finalPalabra.equals("error") && finalWordClass.equals("pp")) {
                                        // Sintetizar el texto a voz
                                        tts.speak(finalPalabra, TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                } else {
                                    Log.e("TTS", "Error de inicialización");
                                }
                            }
                        });
                        GeneracionSugerenciaActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text_prediction.setText("Palabra predicha: \n" + finalPalabra + "\nClase: " + finalWordClass);
                            }
                        });
                    }
                }
            });

        }
    }
    @Override
    public void onEvent(int i, Bundle bundle) {}
    @Override
    public void onInit(int i) {}
}