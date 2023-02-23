package me.disto.test_stt_java;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase no se usa actualmente en la aplicacion pero no la borré porque podria servirnos
 * despues
 *
 * */
public class HiloLectura extends AsyncTask<String, Integer, SpannableStringBuilder> {
    // Aquí puedes definir variables y métodos necesarios para la operación asíncrona
    SpannableStringBuilder spannable;
    String texto;
    String unapalabra;
    String[] palabras;
    PalabrasEnString indices_de_palabras = new PalabrasEnString();
    List<int[]> indice = indices_de_palabras.obtenerIndicesPalabras(texto);
    int inicio;
    int fin;
    int[] indices_de_una_palabra;
    int contador = 0;
    @Override
    protected SpannableStringBuilder doInBackground(String... strings) {
        /***
         * Aquí colocas la lógica que deseas ejecutar en el hilo por separado.
        aqui se ejecutan tareas en segundo plano.
        ¿que es lo que yo quiero ejecutar en segundo plano?
        respuesta el marcado de una palabra x en un texto dado.
         ***/

        // Se obtiene el texto en el que se marcara una palabra
        texto = strings[0];
        // Se obtiene el indice posicional de inicio y termino, de la palabra que sera marcada.
        indices_de_una_palabra = indice.get(contador++);
        // Se marca la palabra en el texto
        spannable = new SpannableStringBuilder(texto);
        spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), indices_de_una_palabra[0], indices_de_una_palabra[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //publishProgress();
        return spannable;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // Aquí puedes actualizar la interfaz de usuario con el progreso de la tarea
        // por ejemplo, actualizar un ProgressBar
        // progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(SpannableStringBuilder result) {
        // Aquí puedes actualizar la interfaz de usuario con el resultado de la tarea

    }

    private static class  PalabrasEnString {

        public List<int[]> obtenerIndicesPalabras(String cadena) {
            List<int[]> indicesPalabras = new ArrayList<>();
            Pattern patronPalabras = Pattern.compile("\\b\\w+\\b");
            Matcher matcher = patronPalabras.matcher(cadena);
            while (matcher.find()) {
                int inicioPalabra = matcher.start();
                int finPalabra = matcher.end() - 1;
                int[] indices = {inicioPalabra, finPalabra};
                indicesPalabras.add(indices);
            }
            return indicesPalabras;
        }
    }
}

