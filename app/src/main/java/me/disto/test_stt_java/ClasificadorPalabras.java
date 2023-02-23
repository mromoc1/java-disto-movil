package me.disto.test_stt_java;

import android.widget.TextView;

public class ClasificadorPalabras {
    private final String  text;
    public ClasificadorPalabras(TextView tv , String texto){
        text = texto;
        tv.setText(texto);
    }

    public void iniciarEscaneadoLectura(){



        //se marca en negrita la palabra esperada por el "modulo de aprendizaje en base a lectura"
        //se obtiene la palabra pronunciada por el usuario
        // se hace busca un match entre la palabra esperada y la pronuciada
    }

    private void obtenerAudio(){

    }
    private void obtenerTextoLectura(){

    }

    private void marcarPalabraEnTexto(){

    }

}
