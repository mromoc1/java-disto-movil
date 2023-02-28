package aprendizaje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.disto.test_stt_java.R;
import reconocimiento.habla.ReconocedorHabla;

public class AprendizajePorLectura extends AppCompatActivity {

    private TextView vista_de_texto;
    private Button button;
    private String texto_para_leer;
    private int inicio;
    private int fin;
    private int contador_de_palabras;
    private SpeechRecognizer speechRecognizer;
    private ReconocedorHabla listenerReconocedorHabla;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprendizaje_por_lectura);

        // texto que se proporciona para la lectura.
        texto_para_leer = "Había una vez un perro llamado Max que vivía en la perrera local. Era un " +
                "perro tranquilo y amable, pero nadie parecía querer adoptarlo debido a su edad " +
                "avanzada. Un día, un hombre ciego llamado Tom llegó a la perrera en busca de un " +
                "perro guía. Había perdido la vista en un accidente hace algunos años y estaba " +
                "buscando un compañero leal que pudiera ayudarlo en su día a día. Cuando Tom conoció" +
                " a Max, se sintió inmediatamente atraído por su naturaleza tranquila y suave. " +
                "Aunque Max no había sido entrenado como perro guía, Tom decidió darle una oportunidad" +
                " y adoptarlo. Al principio, Tom y Max necesitaron tiempo para adaptarse el uno al otro." +
                " Pero pronto, se convirtieron en un equipo inseparable. Max aprendió a guiar a Tom con" +
                " seguridad en las calles de la ciudad, evitando obstáculos y ayudándolo a cruzar las " +
                "calles. Con el tiempo, Tom y Max desarrollaron un fuerte vínculo emocional. Max no " +
                "solo se convirtió en los ojos de Tom, sino también en su mejor amigo. Juntos, exploraron" +
                " la ciudad, tomaron el sol en el parque y disfrutaron de la compañía del otro. A pesar " +
                "de los desafíos que enfrentaban, Tom y Max siempre estuvieron juntos. Y al final del día," +
                " se acurrucaban juntos en el sofá, sabiendo que habían encontrado a su compañero perfecto" +
                " en el otro.";
        //se obtiene cada palabra en el texto para leer
        String[] palabras_en_texto = texto_para_leer.split(" ");
        vista_de_texto = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Se reconoce el idioma español de latinoamerica
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-LA");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        // Se muestra el texto en el componente grafico de la interfaz de usuario.
        vista_de_texto.setText(texto_para_leer);
        // El parametro del metodo setOnClickListener corresponde a un objeto anonimo que implementa
        // la interfaz OnclickListener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se inicia una instancia de speech recognizer.
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(AprendizajePorLectura.this);
                speechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle bundle) {
                        //Called when the endpointer is ready for the user to start speaking.
                        Toast.makeText(AprendizajePorLectura.this, "Endpointer is ready to speak.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        //The user has started to speak.
                    }

                    @Override
                    public void onRmsChanged(float v) {
                        //The sound level in the audio stream has changed.
                    }

                    @Override
                    public void onBufferReceived(byte[] bytes) {
                        //More sound has been received.
                    }

                    @Override
                    public void onEndOfSpeech() {
                        //Called after the user stops speaking.
                        Toast.makeText(AprendizajePorLectura.this, "Fin de la escucha.", Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                        vista_de_texto.setText(texto_para_leer);
                        vista_de_texto.invalidate();
                        inicio = 0;
                        fin = palabras_en_texto[0].length();
                        contador_de_palabras = 0;
                    }

                    @Override
                    public void onError(int i) {
                        //A network or recognition error occurred.
                        Toast.makeText(AprendizajePorLectura.this, "Error al escuchar.", Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                    }

                    @Override
                    public void onResults(Bundle bundle) {
                        //Called when recognition results are ready.
                    }

                    @Override
                    public void onPartialResults(Bundle bundle) {
                        //Called when partial recognition results are available.
                        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        String text = matches.get(0);
                        // obntener la ultima palabra
                        String[] palabras_en_tiempo_real = text.split(" ");
                        String ultima_palabra = palabras_en_tiempo_real[palabras_en_tiempo_real.length - 1];
                        fin = inicio + palabras_en_texto[contador_de_palabras].length();
                        vista_de_texto.setText(marcarTexto());
                        inicio = fin + 1;// sumar 1 para la separación del espacio
                        contador_de_palabras++;
                    }

                    @Override
                    public void onEvent(int i, Bundle bundle) {
                        //Reserved for adding future events.
                    }
                });
                button.setEnabled(false);
                speechRecognizer.startListening(intent);
            }
        });
    }

    private SpannableStringBuilder marcarTexto() {
        SpannableStringBuilder spannable2 = new SpannableStringBuilder(texto_para_leer);
        spannable2.setSpan(new BackgroundColorSpan(Color.YELLOW), inicio, fin, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable2;
    }
}