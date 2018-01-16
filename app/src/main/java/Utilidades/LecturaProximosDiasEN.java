package Utilidades;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import Entidades.Semana;

/**
 * Created by Alejandro on 17/11/2017.
 */

public class LecturaProximosDiasEN extends DefaultHandler {

    public Semana semana = new Semana(7);

    private boolean name = false;
    private String nam = "";

    public LecturaProximosDiasEN() {
        super();
    }

    public Semana getSemana() {
        return semana;
    }

    public void startElement(String uri, String nombre, String nombreC, Attributes atts) {
        if (nombre.equals("name")) {
            name = true;
        }
        int posicion = 0;
        if (nombre.equals("forecast")) {
            for (int i = 0; i < atts.getLength(); i++) {
                if(nam.equals("Minimum temperature")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value")) {
                        int tempera = Integer.parseInt(atts.getValue(i));
                        semana.getDia(posicion-1).setTemperaturaMinima(tempera);
                    }
                }
                if(nam.equals("Maximum temperature")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value")) {
                        int tempera = Integer.parseInt(atts.getValue(i));
                        semana.getDia(posicion-1).setTemperaturaMaxima(tempera);
                    }
                }
                if(nam.equals("Wind symbol")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value"))
                        semana.getDia(posicion-1).setViento(atts.getValue(i));
                }
                if(nam.equals("Symbol")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value"))
                        semana.getDia(posicion-1).setCielo(atts.getValue(i));
                    if (atts.getQName(i).equals("id"))
                        semana.getDia(posicion-1).setIcono(atts.getValue(i));
                }
                if(nam.equals("Day symbol")) {
                    if (atts.getQName(i).equals("data_sequence"))
                        posicion = Integer.parseInt(atts.getValue(i));
                    if (atts.getQName(i).equals("value"))
                        semana.getDia(posicion-1).setDia(atts.getValue(i));
                }
                if(nam.equals("Atmosphere definition")) {
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