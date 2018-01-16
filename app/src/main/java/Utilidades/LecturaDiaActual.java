package Utilidades;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import Entidades.Dia;

/**
 * Created by Alejandro on 17/11/2017.
 */

public class LecturaDiaActual extends DefaultHandler {

    private int dias = 0;
    private boolean hour;
    private int hora_contador = 0;

    private Dia dia = new Dia();

    public LecturaDiaActual() {
        super();
    }

    public Dia getDia() {
        dia.limitarHoraActual();
        return dia;
    }

    public void startElement(String uri, String nombre, String nombreC, Attributes atts) {
        if (nombre.equals("day"))
            dias++;
        if (dias == 1 && nombre.equals("day"))
            dia.setDia(atts.getValue(1));
        if (nombre.equals("hour"))
            hour = true;
        if (dias == 1 && nombre.equals("sun")) {
            dia.setSolIn( atts.getValue(0) );
            dia.setSolOut( atts.getValue(2) );
        }
        if (dias == 1 && hour) {
            if (nombre.equals("hour"))
                dia.getHora(hora_contador).setHora(atts.getValue(0));
            if (nombre.equals("temp"))
                dia.getHora(hora_contador).setTemperatura(atts.getValue(0) + atts.getValue(1));
            if (nombre.equals("symbol"))
                dia.getHora(hora_contador).setIcono(atts.getValue(0));
            if (nombre.equals("wind"))
                dia.getHora(hora_contador).setViento(atts.getValue(0) +" "+ atts.getValue(1));
            if (nombre.equals("rain"))
                dia.getHora(hora_contador).setLluvias(atts.getValue(0) +" "+ atts.getValue(1));
            if (nombre.equals("humidity"))
                dia.getHora(hora_contador).setHumedad(atts.getValue(0)+"%");
            if (nombre.equals("snowline"))
                dia.getHora(hora_contador).setCota_nieve(atts.getValue(0)+ " m");
            if (nombre.equals("windchill"))
                dia.getHora(hora_contador).setSensacion_termica(atts.getValue(0) + atts.getValue(1));
        }
    }

    public void endElement(String uri, String nombre, String nombreC) {
        if (nombre.equals("hour")) {
            hour = !hour;
            hora_contador++;
        }
    }

}
