package Entidades;

/**
 * Created by Alejandro on 27/11/2017.
 */

public class Estadistica {

    private String fecha;
    private int temperatura_minima;
    private int temperatura_maxima;

    public Estadistica(String fecha, int temperatura_minima, int temperatura_maxima) {
        this.setTemperatura_minima(temperatura_minima);
        this.setTemperatura_maxima(temperatura_maxima);
        this.setFecha(fecha);
    }

    public String getFecha() { return fecha; }

    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getTemperatura_minima() { return temperatura_minima; }

    public void setTemperatura_minima(int temperatura_minima) {
        this.temperatura_minima = temperatura_minima;
    }

    public int getTemperatura_maxima() { return temperatura_maxima; }

    public void setTemperatura_maxima(int temperatura_maxima) {
        this.temperatura_maxima = temperatura_maxima;
    }

    @Override
    public String toString() {
        return "Estadistica{" +
                " fecha='" + fecha + '\'' +
                ", temperatura_minima='" + temperatura_minima + '\'' +
                ", temperatura_maxima='" + temperatura_maxima + '\'' +
                " }";
    }
}
