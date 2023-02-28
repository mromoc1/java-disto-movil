package aprendizaje;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;

public class MarcadorTexto {

    private final String texto_para_leer;

    public MarcadorTexto(String texto_para_leer) {
        this.texto_para_leer = texto_para_leer;
    }

    public SpannableStringBuilder marcarTexto(int inicio, int fin){
        SpannableStringBuilder spannable2 = new SpannableStringBuilder(texto_para_leer);
        spannable2.setSpan(new BackgroundColorSpan(Color.YELLOW), inicio, fin, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable2;
    }

}
