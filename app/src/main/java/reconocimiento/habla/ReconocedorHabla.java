package reconocimiento.habla;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aprendizaje.AprendizajePorLectura;
import aprendizaje.MarcadorTexto;

public class ReconocedorHabla implements RecognitionListener {

    private int fin;
    private MarcadorTexto marcadorTexto;

    public ReconocedorHabla(String texto_para_leer) {
        marcadorTexto = new MarcadorTexto(texto_para_leer);
        int inicio = 0;
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        // Llamado cuando el reconocedor está listo para recibir audio
    }

    @Override
    public void onBeginningOfSpeech() {
        // Llamado cuando el usuario comienza a hablar
    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        // Llamado cuando el usuario deja de hablar

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {
        // Llamado cuando se reciben los resultados del reconocimiento

    }

    @Override
    public void onPartialResults(Bundle bundle) {
        // Llamado cuando se reciben los resultados del reconocimiento
        //ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        // obtener la ultima palabra reconocida
        // String palabra = matches.get(matches.size() - 1);
        // marcadorTexto.marcarTexto(0, 5);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}

