package me.disto.distoapp.ui.modulo_aprendizaje_lectura;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.utils.BaseActivity;

public class AprendizajeLecturaActivity extends BaseActivity implements RecognitionListener{

    private TextView vista_de_texto;
    private TextView vista_de_palabra_dicha;
    private TextView vista_de_palabra_esperada;
    private Button button_iniciar_lectura;
    private Button boton_detener_lectura;
    // las variables inicio y fin controlan el pintado en amarillo de una seccion del texto para leer.
    // en este caso la seccion del texto corresponde a la palabra que se esta leyendo.
    private int inicio;
    private int fin;
    // indica la palabra marcada en amarillo. Es decir, la palabra que se debe decir.
    private int indicador_de_palabra_en_texto;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private String[] palabras_en_texto;
    private String[] palabras_en_texto_auxiliar;
    // lista de objetos de tipo Palabra que contiene el tiempo en el cual se dijo cada palabra.
    // este valor se usa como referencia para clasificar las palabras en problematicas o no.
    private ArrayList<Palabra> palabras_a_clasificar;
    //indica la palabra que se va a clasificar una vez haya terminado el tiempo de escucha.
    private int indicador_de_palabra_a_clasificar;
    // tiempo en el cual se inicia la escucha. este valor es necesario para poder clasificar la primera palabra.
    private double medicion_tiempo_inicio_escucha;
    //contiente la clasificacion de cada palabra.
    private Map<String, String> resultado_clasificacion;

    private String texto_para_leer = "Había una vez un perro llamado Pepe que vivía en la perrera local. Era un " +
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprendizaje_lectura);

        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_lectura).setChecked(true);

        //se obtiene cada palabra en el texto para leer
        // la expresion regular permite obtener cada palabra que incluya únicamente los caracteres alfanuméricos
        palabras_en_texto = texto_para_leer.split("[^\\p{L}]+");
        indicador_de_palabra_a_clasificar = 0;
        palabras_en_texto_auxiliar = texto_para_leer.split(" ");
        resultado_clasificacion = new HashMap<>();
        vista_de_texto = findViewById(R.id.contenedor_texto);
        vista_de_palabra_dicha = findViewById(R.id.contenedor_palabra_dicha);
        vista_de_palabra_esperada = findViewById(R.id.contenedor_palabra_esperada);
        button_iniciar_lectura = findViewById(R.id.boton_comenzar_lectura);
        boton_detener_lectura = findViewById(R.id.boton_detener_lectura);
        // Se muestra el texto en el componente grafico de la interfaz de usuario.
        vista_de_texto.setText(texto_para_leer);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        // El parametro del metodo setOnClickListener corresponde a un objeto anonimo que implementa
        // la interfaz OnclickListener
        button_iniciar_lectura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palabras_a_clasificar = crearObjetosPalabras(palabras_en_texto);
                resultado_clasificacion = new HashMap<>();
                medicion_tiempo_inicio_escucha = System.currentTimeMillis();
                startSpeechRecognition(v);
                button_iniciar_lectura.setEnabled(false);
                Toast toast = Toast.makeText(AprendizajeLecturaActivity.this, "hable ahora", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.END, 0, 0);
                toast.show();

            }
        });
        boton_detener_lectura.setOnClickListener(new View.OnClickListener() {
            //si el boton de detener la escucha ha sido presionado, entonces se reinician
            //las variables de control
            @Override
            public void onClick(View v) {
                //se marca la primera palabra del texto para leer.
                inicio = 0;
                fin = palabras_en_texto[0].length();
                vista_de_texto.setText(marcarPalabraAmarillo());
                vista_de_texto.invalidate();
                indicador_de_palabra_en_texto = 0;
                indicador_de_palabra_a_clasificar = 0;
                stopSpeechRecognition(v);
                Toast toast = Toast.makeText(AprendizajeLecturaActivity.this, "proceso detenido.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.END, 0, 0);
                toast.show();
                clasificarPalabras(palabras_a_clasificar);
                button_iniciar_lectura.setEnabled(true);
                vista_de_palabra_dicha.setText("");
                vista_de_palabra_esperada.setText("");
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
        return matcher.find();
    }

    /**
     *
     * @param text: corresponde al texto que contiene la palabra que se debe leer.
     * evalua si la palabra que ha dicho el usuario corresponde con la palabra que se debe leer.
     * nota: La palabra que se debe leer esta marcada en el texto con el color amarillo.
     * Este metodo controla las variables que definen la seccion del texto que se debe pintar en
     * funcion de la coincidencia entre la palabra que se espera leer y la que el usuario ha dicho.
     */
    private void evaluarCoincidenciaConLectura(String text){
        //Called when partial recognition results are available.
        if(palabras_a_clasificar.get(indicador_de_palabra_a_clasificar).getTiempo_en_que_se_dijo_la_palabra()!=-1){
            indicador_de_palabra_a_clasificar++;
        }
        // obntener la ultima palabra
        String[] palabras_en_tiempo_real = text.split(" ");
        String ultima_palabra_pronunciada = palabras_en_tiempo_real[palabras_en_tiempo_real.length - 1].toLowerCase();
        // se compara la ultima palabra con la palabra que se debe leer; con el
        // cuidado de que ambas palabras sean iguales en minusculas
        String palabra_a_leer = palabras_en_texto[indicador_de_palabra_en_texto].toLowerCase();
        vista_de_palabra_dicha.setText(ultima_palabra_pronunciada);
        if(ultima_palabra_pronunciada.equals(palabra_a_leer)){
            fin = inicio + palabras_en_texto[indicador_de_palabra_en_texto].length();
            vista_de_texto.setText(marcarPalabraAmarillo());
            //se toma el tiempo en el cual se dijo la palabra
            tomarTiempo(palabras_a_clasificar.get(indicador_de_palabra_a_clasificar));
            Log.d("DISTO", "palabra pronunciada: " + palabras_a_clasificar.get(indicador_de_palabra_a_clasificar).getPalabra());
            Log.d("DISTO", "tiempo en que se dijo: " + palabras_a_clasificar.get(indicador_de_palabra_a_clasificar).getTiempo_en_que_se_dijo_la_palabra());
            if(esPalabraConSignoPuntuacion(palabras_en_texto_auxiliar[indicador_de_palabra_en_texto])){
                inicio = fin + 2;// sumar 2 para la separación del espacio y el signo de puntuación
            }
            else{
                inicio = fin + 1;// sumar 1 para la separación del espacio
            }
            indicador_de_palabra_en_texto++;
        }
        //vista_de_palabra_esperada.setText(palabra_a_leer);
    }

    // se toma el tiempo en el cual se dijo la palabra
    private void tomarTiempo(Palabra intervalo){
        intervalo.setTiempo_en_que_se_dijo_la_palabra(System.currentTimeMillis());
    }

    /**
     * @param palabras_a_clasificar
     * @return
     * Contiene la logica de control para el proceso de clasificacion de palabras.
     * Este método se ejecuta al finalizar el proceso de escucha.
     * nota: Para la clasificacion de la primera palabra contenida en el arreglo de palabras_a_clasificar
     * se calcula ladiferencia de esta con el valor contenido en el atributo medicion_tiempo_inicio_escucha
     * para la clasificacion a partir de la segunda palabra contenida
     * en el arreglo de palabras_a_clasificar se procede de la siguiente manera:
     * 1. Se recorre el arreglo de palabras a clasificar
     * 2. Se obtiene el tiempo en el cual se dijo la palabra en el instante i
     * 3. Se obtiene el tiempo en el cual se dijo la palabra en el instante i+1
     * 4. Se calcula la diferencia entre los tiempos de los instantes i e i+1
     * 5. Si la diferencia es menor a un valor x en milisegundos, se clasifica la palabra como "no problematica"
     * 6. Si la diferencia es mayor a un valor x en milisegundos, se clasifica la palabra como "problematica"
     */
    private Map<String, String> clasificarPalabras(ArrayList<Palabra> palabras_a_clasificar){
        double valor_i;
        double valor_i_mas_1;

        Log.d("DISTO CLASIFICADOR","Ejecutando el clasificador de palabras...");
        // para la clasificacion de la primera palabra
        valor_i_mas_1 = palabras_a_clasificar.get(0).getTiempo_en_que_se_dijo_la_palabra();
        double diferencia;
        double rango = 4000;
        if(medicion_tiempo_inicio_escucha != -1 && valor_i_mas_1 != -1){
            diferencia =  valor_i_mas_1 - medicion_tiempo_inicio_escucha;
            Log.d("DISTO","diferencia para " +palabras_a_clasificar.get(0).getPalabra()+ " : " + diferencia);
            if(diferencia < rango){
                resultado_clasificacion.put(palabras_a_clasificar.get(0).getPalabra(),"no problematica");
            }
            else{
                resultado_clasificacion.put(palabras_a_clasificar.get(0).getPalabra(),"problematica");
            }
        }
        for(int i = 0; i < palabras_a_clasificar.size()-1; i++){
            valor_i= palabras_a_clasificar.get(i).getTiempo_en_que_se_dijo_la_palabra();
            valor_i_mas_1= palabras_a_clasificar.get(i+1).getTiempo_en_que_se_dijo_la_palabra();
            if(valor_i != -1 && valor_i_mas_1 != -1){
                diferencia =  valor_i_mas_1 - valor_i;
                Log.d("DISTO","diferencia para " +palabras_a_clasificar.get(i+1).getPalabra()+ " : " + diferencia);
                //controla que las palabras se clasifiquen solo una vez
                if(!resultado_clasificacion.containsKey(palabras_a_clasificar.get(i+1).getPalabra())){
                    if(diferencia < rango){
                        resultado_clasificacion.put(palabras_a_clasificar.get(i+1).getPalabra(),"no problematica");
                    }
                    else{
                        resultado_clasificacion.put(palabras_a_clasificar.get(i+1).getPalabra(),"problematica");
                    }
                }
            }
        }
        for (Map.Entry<String, String> entry : resultado_clasificacion.entrySet()) {
            String palabra = entry.getKey();
            String  clasificacion = entry.getValue();
            Log.d("prueba map","palabra: " + palabra + " clasificacion: " + clasificacion);
        }
        return resultado_clasificacion;
    }

    private void cargarResultadosDeClasificacion(){

    }

    /**
     * Crea un arraylist de objetos palabra a partir de las palabras contenidas en el arreglo de texto_a_leer.
     * @param texto_a_leer : Arreglo de palabras que contiene el texto a leer
     * @return ArrayList<Palabra> : Arreglo de objetos de tipo Palabra
     */
    private ArrayList<Palabra> crearObjetosPalabras(@NonNull String[] texto_a_leer){
        palabras_a_clasificar = new ArrayList<Palabra>();
        for(int i = 0; i < texto_a_leer.length; i++){
            Palabra una_palabra = new Palabra();
            una_palabra.setPalabra(texto_a_leer[i]);
            una_palabra.setTiempo_en_que_se_dijo_la_palabra(-1);
            palabras_a_clasificar.add(una_palabra);
        }
        return palabras_a_clasificar;
    }

    private void startSpeechRecognition(View v) {
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, Long.MAX_VALUE);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, Long.MAX_VALUE);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        // Se reconoce el idioma español de latinoamerica
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-LA");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizer.startListening(intent);
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

    private void stopSpeechRecognition(View v) {
        speechRecognizer.stopListening();
        speechRecognizer.cancel();
        speechRecognizer.destroy();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        //Called when the endpointer is ready for the user to start speaking.
    }

    @Override
    public void onBeginningOfSpeech() {}

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        //Called after the user stops speaking.
    }

    @Override
    public void onError(int i) {
        //A network or recognition error occurred.
        Toast toast = Toast.makeText(AprendizajeLecturaActivity.this, "Error de lectura.", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.END, 0, 0);
        toast.show();
        button_iniciar_lectura.setEnabled(true);
        vista_de_texto.setText(texto_para_leer);
        resetSpeechRecognizer();
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onResults(Bundle bundle) {
        //Called when recognition results are ready.
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String palabra_en_tiempo_real = matches.get(matches.size() - 1);
        evaluarCoincidenciaConLectura(palabra_en_tiempo_real);
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String palabra_en_tiempo_real = matches.get(matches.size() - 1);
        evaluarCoincidenciaConLectura(palabra_en_tiempo_real);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}