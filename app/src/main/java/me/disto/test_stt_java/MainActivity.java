package me.disto.test_stt_java;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


import java.util.ArrayList;
import android.view.View;
import android.widget.Button;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements RecognitionListener {
    private SpeechRecognizer speechRecognizer;
    private Intent intent;

    private TextView text_transcrito;
    private TextView text_prediction;
    private TextView text_status;
    private Button btn_iniciar;
    private Button btn_detener;

    private String aux = "";
    private final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_transcrito = findViewById(R.id.text_transcrito);
        text_prediction = findViewById(R.id.text_prediction);
        text_status = findViewById(R.id.text_status);
        btn_iniciar = findViewById(R.id.btn_iniciar);
        btn_detener = findViewById(R.id.btn_detener);

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                speechRecognizer.startListening(intent);
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onResults(Bundle results) {
        speechRecognizer.startListening(intent);
//        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        StringBuilder text = new StringBuilder();
//        for (String result : matches)
//            text.append(result).append("\n");
//        Log.i(LOG_TAG, "onResults" + text.toString());
//        returnedText.setText(text.toString());
//        speech.startListening(recognizerIntent);
    }

    @Override
    public void onError(int errorCode) {
        resetSpeechRecognizer();
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onPartialResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        StringBuilder text = new StringBuilder();
        for (String result : matches){
            text.append(result).append("\n");
        }
        if(!aux.equals(text.toString())){
            aux = text.toString();
            text_transcrito.setText(text.toString());
            String url = "https://20.226.8.136:8080/distoAPI/predecir?palabra=" + text.toString();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try (Response response = client.newCall(request).execute()) {
//                        text_prediction.setText(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            try (Response response = client.newCall(request).execute()) {
////                text_prediction.setText(response.body().string());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        String myresponse = response.body().string();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text_prediction.setText(myresponse);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onBeginningOfSpeech() {}
    @Override
    public void onBufferReceived(byte[] buffer) {}
    @Override
    public void onEndOfSpeech() {}
    @Override
    public void onEvent(int arg0, Bundle arg1) {}
    @Override
    public void onReadyForSpeech(Bundle arg0) {}
    @Override
    public void onRmsChanged(float rmsdB) {}

}