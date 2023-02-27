package aprendizaje;

public class Word {
    private final String text;
    private final int startIndex;
    private final int endIndex;
    private final double tiempo;

    public Word(String text, int startIndex, int endIndex, double tiempo) {
        this.text = text;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tiempo = tiempo;
    }

    public String getText() {
        return text;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public double getTiempo() {
        return tiempo;
    }
}
