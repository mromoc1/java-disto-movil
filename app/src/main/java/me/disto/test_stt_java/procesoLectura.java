package me.disto.test_stt_java;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class procesoLectura extends AppCompatActivity {

    private TextView finalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_lectura2);
        // texto que se proporciona para la lectura.
        String texto_para_leer = "Había una vez un perro llamado Max que vivía en la perrera local. Era un perro tranquilo y amable, pero nadie parecía querer adoptarlo debido a su edad avanzada. Un día, un hombre ciego llamado Tom llegó a la perrera en busca de un perro guía. Había perdido la vista en un accidente hace algunos años y estaba buscando un compañero leal que pudiera ayudarlo en su día a día. Cuando Tom conoció a Max, se sintió inmediatamente atraído por su naturaleza tranquila y suave. Aunque Max no había sido entrenado como perro guía, Tom decidió darle una oportunidad y adoptarlo. Al principio, Tom y Max necesitaron tiempo para adaptarse el uno al otro. Pero pronto, se convirtieron en un equipo inseparable. Max aprendió a guiar a Tom con seguridad en las calles de la ciudad, evitando obstáculos y ayudándolo a cruzar las calles. Con el tiempo, Tom y Max desarrollaron un fuerte vínculo emocional. Max no solo se convirtió en los ojos de Tom, sino también en su mejor amigo. Juntos, exploraron la ciudad, tomaron el sol en el parque y disfrutaron de la compañía del otro. A pesar de los desafíos que enfrentaban, Tom y Max siempre estuvieron juntos. Y al final del día, se acurrucaban juntos en el sofá, sabiendo que habían encontrado a su compañero perfecto en el otro.";
        // Se muestra el texto en el componente grafico de la interfaz de usuario.
        finalResult = (TextView) findViewById(R.id.textView);
        finalResult.setText(texto_para_leer);
        // Se agrega un manejador de eventos al boton de la interfaz gráfica.
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Se realiza la tarea que consume mucho tiempo aquí.
                        // Se recorre cada palabra en el texto proporcionado
                        int inicio = 0;
                        int fin;
                        for (String palabra : texto_para_leer.split(" ")) {
                            //se obtiene el indice final de cada palabra contenida en el texto para leer.
                            fin = inicio + palabra.length() - 1;
                            if (inicio != -1) {
                                SpannableStringBuilder spannable2 = new SpannableStringBuilder(texto_para_leer);
                                spannable2.setSpan(new BackgroundColorSpan(Color.YELLOW), inicio, fin, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                finalResult.setText(spannable2);
                                finalResult.invalidate();
                            }
                            //se obtiene el indice de inicio de cada palabra contenida en el texto para leer.
                            inicio = fin + 2;// sumar 2 para la separación del espacio
                        }
                        // Una vez que la tarea ha finalizado, se debe actualizar la interfaz de usuario en el hilo principal.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Aquí se actualiza la interfaz de usuario con los datos obtenidos de la tarea.
                            }
                        });
                    }
                }).start();
            }
        });
    }
}