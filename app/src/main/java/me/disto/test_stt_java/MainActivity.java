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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import java.util.ArrayList;
import java.util.Locale;
//importa librearias para generar un boton
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextView returnedText;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private final String LOG_TAG = "VoiceRecognitionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        returnedText = findViewById(R.id.textView1);
        resetSpeechRecognizer();
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);


//        speech.startListening(recognizerIntent);
//        crea un boton para iniciar el reconocimiento de voz
        Button btn = (Button) findViewById(R.id.btnStart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech.startListening(recognizerIntent);
            }
        });

        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopListening();
                speech.stopListening();
//                destroy
                speech.destroy();



            }
        });

    }

    private void resetSpeechRecognizer() {
        if(speech != null)
            speech.destroy();
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        if(SpeechRecognizer.isRecognitionAvailable(this))
            speech.setRecognitionListener(this);
        else
            finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                speech.startListening(recognizerIntent);
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBeginningOfSpeech() {}
    @Override
    public void onBufferReceived(byte[] buffer) {}
    @Override
    public void onEndOfSpeech() {}

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        StringBuilder text = new StringBuilder();
        for (String result : matches)
            text.append(result).append("\n");
        Log.i(LOG_TAG, "onResults" + text.toString());
//        returnedText.setText(text.toString());
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onError(int errorCode) {
        resetSpeechRecognizer();
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {}

    private String aux = "";
    @Override
    public void onPartialResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        StringBuilder text = new StringBuilder();
        for (String result : matches){
            text.append(result).append("\n");
        }
        if(!aux.equals(text.toString())){
            aux = text.toString();
//            Log.i(LOG_TAG, "onPartialResults: " + text.toString());
//          escribe el texto en el textview
            returnedText = findViewById(R.id.capturedText);
            returnedText.setText(text.toString());
//            send a get request to http://20.226.8.136:8080/distoAPI/predecir?palabra=
            String url = "http://20.226.8.136:8080/distoAPI/predecir?palabra=" + text.toString();



        }else{

        }
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {}

    @Override
    public void onRmsChanged(float rmsdB) {}

}