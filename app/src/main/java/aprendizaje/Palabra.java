package aprendizaje;

public class Palabra {

    private String palabra;
    private  double tiempo_en_que_se_dijo_la_palabra;

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
}
