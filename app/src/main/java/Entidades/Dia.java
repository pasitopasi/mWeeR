package Entidades;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Alejandro on 15/11/2017.
 */

public class Dia {

    private final int HORAS_DIA = 24;

    private int temperatura_minima;
    private int temperatura_maxima;
    private String viento;
    private String cielo;
    private String dia;
    private String descripcion;
    private String icono;
    private String sol_in;
    private String sol_out;
    private ArrayList<Hora> horas;

    public Dia() {
        horas = new ArrayList<>();
        for(int i = 0; i < HORAS_DIA; i++) {
            horas.add(new Hora());
        }
    }

    public void setTemperaturaMinima(int temperatura) {
        this.temperatura_minima = temperatura;
    }

    public int getTemperaturaMinima() {
        return temperatura_minima;
    }

    public void setTemperaturaMaxima(int temperatura) {
        this.temperatura_maxima = temperatura;
    }

    public int getTemperaturaMaxima() {
        return temperatura_maxima;
    }

    public String getSolIn() {
        return sol_in;
    }

    public void setSolIn(String sol_in) {
        this.sol_in = sol_in;
    }

    public String getSolOut() {
        return sol_out;
    }

    public void setSolOut(String sol_out) { this.sol_out = sol_out; }

    public String getViento() {
        return viento;
    }

    public void setViento(String viento) {
        this.viento = viento;
    }

    public String getCielo() {
        return cielo;
    }

    public void setCielo(String cielo) {
        this.cielo = cielo;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() { return icono; }

    public void setIcono(String icono) { this.icono = icono; }

    public ArrayList<Hora> getHoras() { return horas; }

    public void setHoras(ArrayList<Hora> horas) { this.horas = horas; }

    public void setHora(Hora hora) { this.horas.add(hora); }

    public Hora getHora(int posicion) { return this.horas.get(posicion); }

    public void limitarHoraActual() {
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY) == 0 ?
                23 : calendario.get(Calendar.HOUR_OF_DAY) -1;// - 1;
        ArrayList<Hora> horas_nuevo = new ArrayList<>();
        if(hora == 23) {
            horas_nuevo.add(horas.get(hora));
            hora = 0;
        }
            for(int i = hora; i < HORAS_DIA-1; i++) {
                horas_nuevo.add(horas.get(i));
            }
        setHoras(horas_nuevo);
    }

    public String toStringHoras() {
        String cadena = "Dia: " + this.dia;
        for(int i = 0; i < horas.size(); i++) {
            cadena += "\n" +horas.get(i).toString();
        }
        return cadena;
    }

    @Override
    public String toString() {
        return "Dia: " + this.dia
                + "\n\tTemperatura minima: " 	+ this.temperatura_minima
                + "\n\tTemperatura maxima: "	+ this.temperatura_maxima
                + "\n\tViento: " 	+ this.viento
                + "\n\tCielo: "	+ this.cielo
                + "\n\tIcono: "	+ this.icono
                + "\n\tDescripcion dia: "	+ this.descripcion;
    }

}
