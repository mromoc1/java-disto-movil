package me.disto.distoapp.ui.modulo_aprendizaje_lectura;

public class Palabra {

    private String palabra;
    private  double tiempo_en_que_se_dijo_la_palabra;
    private boolean es_por_salto;

    public void setPalabra(String palabra){
        this.palabra = palabra;
    }

    public String getPalabra(){
        return palabra;
    }

    public void setTiempo_en_que_se_dijo_la_palabra(double tiempo_en_que_se_dijo_la_palabra) {
        this.tiempo_en_que_se_dijo_la_palabra = tiempo_en_que_se_dijo_la_palabra;
    }

    public double getTiempo_en_que_se_dijo_la_palabra() {
        return tiempo_en_que_se_dijo_la_palabra;
    }

    public void setEs_por_salto(boolean es_por_salto) {
        this.es_por_salto = es_por_salto;
    }

    public boolean getEs_por_salto() {
        return es_por_salto;
    }
}
