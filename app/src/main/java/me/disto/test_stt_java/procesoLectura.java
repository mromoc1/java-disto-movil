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
import android.widget.Toast;

public class procesoLectura extends AppCompatActivity {

    private TextView vista_de_texto;
    private String texto_para_leer;
    private int inicio;
    private int fin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_lectura2);
        // texto que se proporciona para la lectura.
        texto_para_leer = "Había una vez un perro llamado Max que vivía en la perrera local. Era un perro tranquilo y amable, pero nadie parecía querer adoptarlo debido a su edad avanzada. Un día, un hombre ciego llamado Tom llegó a la perrera en busca de un perro guía. Había perdido la vista en un accidente hace algunos años y estaba buscando un compañero leal que pudiera ayudarlo en su día a día. Cuando Tom conoció a Max, se sintió inmediatamente atraído por su naturaleza tranquila y suave. Aunque Max no había sido entrenado como perro guía, Tom decidió darle una oportunidad y adoptarlo. Al principio, Tom y Max necesitaron tiempo para adaptarse el uno al otro. Pero pronto, se convirtieron en un equipo inseparable. Max aprendió a guiar a Tom con seguridad en las calles de la ciudad, evitando obstáculos y ayudándolo a cruzar las calles. Con el tiempo, Tom y Max desarrollaron un fuerte vínculo emocional. Max no solo se convirtió en los ojos de Tom, sino también en su mejor amigo. Juntos, exploraron la ciudad, tomaron el sol en el parque y disfrutaron de la compañía del otro. A pesar de los desafíos que enfrentaban, Tom y Max siempre estuvieron juntos. Y al final del día, se acurrucaban juntos en el sofá, sabiendo que habían encontrado a su compañero perfecto en el otro.";
        // Se muestra el texto en el componente grafico de la interfaz de usuario.
        vista_de_texto = findViewById(R.id.textView);
        vista_de_texto.setText(texto_para_leer);
        Button button = findViewById(R.id.button);
        // Se agrega un manejador de eventos al boton de la interfaz gráfica.
        // El parametro del metodo setOnClickListener corresponde a un objeto anonimo que implementa la interfaz OnclickListener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se crea un objeto anonimo de la clase Thread
                // el parametro de la instancia corresponde a un objeto anonimo que implementa la
                // interaz Runnable
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(false);
                            }
                        });
                        // Se recorre cada palabra en el texto proporcionado
                        inicio = 0;
                        for (String palabra : texto_para_leer.split(" ")) {
                            //se obtiene el indice final de cada palabra contenida en el texto para leer.
                            fin = inicio + palabra.length();
                            if (inicio != -1) {
                                SpannableStringBuilder spannable2 = new SpannableStringBuilder(texto_para_leer);
                                spannable2.setSpan(new BackgroundColorSpan(Color.YELLOW), inicio, fin, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                vista_de_texto.setText(spannable2);
                                try {
                                    Thread.sleep(50);// debe ir 260 millis
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //se obtiene el indice de inicio de cada palabra contenida en el texto para leer.
                            inicio = fin + 1;// sumar 1 para la separación del espacio
                        }
                        vista_de_texto.setText(texto_para_leer);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(procesoLectura.this, "Lectura finalizada.", Toast.LENGTH_SHORT).show();
                                button.setEnabled(true);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}