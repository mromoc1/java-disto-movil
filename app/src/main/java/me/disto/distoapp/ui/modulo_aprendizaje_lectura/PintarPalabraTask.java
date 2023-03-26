package me.disto.distoapp.ui.modulo_aprendizaje_lectura;

import android.graphics.Color;
import android.os.AsyncTask;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * pinta un texto y lo muestra en un textview
 */

public class PintarPalabraTask extends AsyncTask<String, Void, SpannableStringBuilder > {

    private int inicio;
    private int fin;
    SpannableStringBuilder spannable;
    TextView text_view;

    public PintarPalabraTask(TextView text_view) {
        this.text_view = text_view;
    }

    public void getIntervalo(int inicio, int fin) {
        this.inicio = inicio;
        this.fin = fin;
    }
    /**
     *
     * @deprecated
     *
     */
    @Override
    protected SpannableStringBuilder doInBackground(String... texto_a_pintar) {
        spannable = new SpannableStringBuilder(texto_a_pintar[0]);
        spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), inicio, fin, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    @Override
    protected void onPostExecute(SpannableStringBuilder result) {
        super.onPostExecute(result);
        text_view.setText(result);
    }
}
