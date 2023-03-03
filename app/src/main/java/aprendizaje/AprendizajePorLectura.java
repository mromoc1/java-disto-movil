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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.disto.test_stt_java.R;

public class AprendizajePorLectura extends AppCompatActivity {

    private TextView vista_de_texto;
    private TextView vista_de_palabra;
    private Button button;
    private String texto_para_leer;
    private int inicio;
    private int fin;
    private int indicador_de_palabra_en_texto;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private String[] palabras_en_texto;
    private String[] palabras_en_texto_auxiliar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprendizaje_por_lectura);
        // texto que se proporciona para la lectura.
        texto_para_leer = "Había una vez un perro llamado Pepe que vivía en la perrera local. Era un " +
                "perro tranquilo y amable, pero nadie parecía querer adoptarlo debido a su edad " +
                "avanzada. Un día, un hombre ciego llamado Tom llegó a la perrera en busca de un " +
                "perro guía. Había perdido la vista en un accidente hace algunos años y estaba " +
                "buscando un compañero leal que pudiera ayudarlo en su día a día. Cuando Tom conoció" +
                " a Pepe, se sintió inmediatamente atraído por su naturaleza tranquila y suave. " +
                "Aunque Pepe no había sido entrenado como perro guía, Tom decidió darle una oportunidad" +
                " y adoptarlo. Al principio, Tom y Pepe necesitaron tiempo para adaptarse el uno al otro." +
                " Pero pronto, se convirtieron en un equipo inseparable. pepe aprendió a guiar a Tom con" +
                " seguridad en las calles de la ciudad, evitando obstáculos y ayudándolo a cruzar las " +
                "calles. Con el tiempo, Tom y Pepe desarrollaron un fuerte vínculo emocional. Pepe no " +
                "solo se convirtió en los ojos de Tom, sino también en su mejor amigo. Juntos, exploraron" +
                " la ciudad, tomaron el sol en el parque y disfrutaron de la compañía del otro. A pesar " +
                "de los desafíos que enfrentaban, Tom y Pepe siempre estuvieron juntos. Y al final del día," +
                " se acurrucaban juntos en el sofá, sabiendo que habían encontrado a su compañero perfecto" +
                " en el otro.";
        //se obtiene cada palabra en el texto para leer
        // la expresion regular permite obtener cada palabra que incluya únicamente los caracteres alfanuméricos
        palabras_en_texto = texto_para_leer.split("[^\\p{L}]+");
        palabras_en_texto_auxiliar = texto_para_leer.split(" ");
        vista_de_texto = findViewById(R.id.contenedor_texto);
        vista_de_palabra = findViewById(R.id.contenedor_palabra);
        button = findViewById(R.id.boton_comenzar_lectura);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Se reconoce el idioma español de latinoamerica
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-LA");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        //especifica el tiempo de espera después de que el usuario termina de hablar antes de finalizar
        // la entrada de voz.
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        //especifica el tiempo máximo permitido de silencio antes de finalizar la entrada de voz,
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, Long.MAX_VALUE);
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
                        Toast toast = Toast.makeText(AprendizajePorLectura.this, "Endpointer is ready to speak.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.END, 0, 0);
                        toast.show();
                        //se marca la primera palabra del texto para leer.
                        inicio = 0;
                        fin = palabras_en_texto[0].length();
                        vista_de_texto.setText(marcarPalabraAmarillo());
                        vista_de_texto.invalidate();
                        indicador_de_palabra_en_texto = 0;
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
                        Toast toast = Toast.makeText(AprendizajePorLectura.this, "Fin de la escucha.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.END, 0, 0);
                        toast.show();
                        button.setEnabled(true);
                    }

                    @Override
                    public void onError(int i) {
                        //A network or recognition error occurred.
                        Toast toast = Toast.makeText(AprendizajePorLectura.this, "Error al escuchar.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.END, 0, 0);
                        toast.show();
                        button.setEnabled(true);
                        vista_de_texto.setText(texto_para_leer);
                    }

                    @Override
                    public void onResults(Bundle bundle) {
                        //Called when recognition results are ready.
                        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        String text = matches.get(matches.size() - 1);
                        evaluarCoincidenciaConLectura(text);
                    }

                    @Override
                    public void onPartialResults(Bundle bundle) {
                        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        String text = matches.get(matches.size() - 1);
                        evaluarCoincidenciaConLectura(text);
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

    private SpannableStringBuilder marcarPalabraAmarillo() {
        SpannableStringBuilder spannable2 = new SpannableStringBuilder(texto_para_leer);
        spannable2.setSpan(new BackgroundColorSpan(Color.YELLOW), inicio, fin, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable2;
    }

    private boolean esPalabraConSignoPuntuacion(String texto){
        // Expresión regular que busca uno o más signos de puntuación al final del string
        Pattern patron = Pattern.compile(".*[.,;:!?]$");
        Matcher matcher = patron.matcher(texto);
        if (matcher.find()) {
            return true;
            // El texto termina en un signo de puntuación
        } else {
            // El texto no termina en un signo de puntuación
            return false;
        }
    }

    private void evaluarCoincidenciaConLectura(String text){
        //Called when partial recognition results are available.
        // obntener la ultima palabra
        String[] palabras_en_tiempo_real = text.split(" ");
        String ultima_palabra_pronunciada = palabras_en_tiempo_real[palabras_en_tiempo_real.length - 1].toLowerCase();
        // se compara la ultima palabra con la palabra que se debe leer; con el
        // cuidado de que ambas palabras sean iguales en minusculas
        String palabra_a_leer = palabras_en_texto[indicador_de_palabra_en_texto].toLowerCase();
        //comparar idicador de palabra en texto con el tamaño del arreglo de palabras
        if(indicador_de_palabra_en_texto < palabras_en_texto.length-1){
            vista_de_palabra.setText(ultima_palabra_pronunciada + " con " + palabra_a_leer);
        }
        if(ultima_palabra_pronunciada.equals(palabra_a_leer)){
            fin = inicio + palabras_en_texto[indicador_de_palabra_en_texto].length();
            vista_de_texto.setText(marcarPalabraAmarillo());
            if(esPalabraConSignoPuntuacion(palabras_en_texto_auxiliar[indicador_de_palabra_en_texto])){
                inicio = fin + 2;// sumar 2 para la separación del espacio y el signo de puntuación
            }
            else{
                inicio = fin + 1;// sumar 1 para la separación del espacio
            }
            indicador_de_palabra_en_texto++;
        }
    }

}