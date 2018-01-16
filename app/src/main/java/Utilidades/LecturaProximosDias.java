package Utilidades;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import Entidades.Estadistica;
import Entidades.Semana;

/**
 * Created by Alejandro on 17/11/2017.
 */

public class LecturaProximosDias extends DefaultHandler {

    public Semana semana = new Semana(7);
    private ArrayList<Estadistica> estadisticas = new ArrayList<>();
    private boolean name = false;
    private String nam = "";

    public LecturaProximosDias() {
        super();
        for (int i = 0; i<7; i++){
            estadisticas.add(new Estadistica("", 0, 0));
        }
    }

    public Semana getSemana() {
        return semana;
    }

    public ArrayList<Estadistica> getEstadisticas(){
        return estadisticas;
    }

    public void startElement(String uri, String nombre, String nombreC, Attributes atts) {
        if (nombre.equals("name")) {
            name = true;
        }
        int posicion = 0;
        if (nombre.equals("forecast")) {
            for (int i = 0; i < atts.getLength(); i++) {
                if(nam.equals("Temperatura mínima")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value")) {
                        int tempera = Integer.parseInt(atts.getValue(i));
                        semana.getDia(posicion-1).setTemperaturaMinima(tempera);
                        estadisticas.get(posicion-1).setTemperatura_minima(tempera);
                    }
                }
                if(nam.equals("Temperatura máxima")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value")) {
                        int tempera = Integer.parseInt(atts.getValue(i));
                        semana.getDia(posicion-1).setTemperaturaMaxima(tempera);
                        estadisticas.get(posicion-1).setTemperatura_maxima(tempera);
                    }
                }
                if(nam.equals("Variable Símbolo del Viento")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value"))
                        semana.getDia(posicion-1).setViento(atts.getValue(i));
                }
                if(nam.equals("Variable Símbolo")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value"))
                        semana.getDia(posicion-1).setCielo(atts.getValue(i));
                    if (atts.getQName(i).equals("id"))
                        semana.getDia(posicion-1).setIcono(atts.getValue(i));
                }
                if(nam.equals("Variable Símbolo día")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value"))
                        semana.getDia(posicion-1).setDia(atts.getValue(i));
                }
                if(nam.equals("Definición de Atmosfera")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value"))
                        semana.getDia(posicion-1).setDescripcion(atts.getValue(i));
                }
            }
        }
    }

    public void characters(char[] ch, int inicio, int longitud) throws SAXException {
        String car = new String(ch, inicio, longitud);
        car = car.replaceAll("[\t\n]", "");
        if (name) {
            nam = car;
            name = !name;
        }
    }
}