package Entidades;

/**
 * Created by Alejandro on 17/11/2017.
 */

public class Hora {

    private String hora;
    private String temperatura;
    private String icono;
    private String viento;
    private String lluvias;
    private String humedad;
    private String cota_nieve;
    private String sensacion_termica;

    public Hora() {
    }

    public String getSensacion_termica() {
        return sensacion_termica;
    }

    public void setSensacion_termica(String sensacion_termica) {
        this.sensacion_termica = sensacion_termica;
    }

    public String getCota_nieve() {
        return cota_nieve;
    }

    public void setCota_nieve(String cota_nieve) {
        this.cota_nieve = cota_nieve;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getViento() {
        return viento;
    }

    public void setViento(String viento) {
        this.viento = viento;
    }

    public String getLluvias() {
        return lluvias;
    }

    public void setLluvias(String lluvias) {
        this.lluvias = lluvias;
    }

    public String toStringEN() {
        return  "\tTemperature: " + this.temperatura
                + "\n\tWind: " + this.viento
                + "\n\tRains: " + this.lluvias
                + "\n\tHumidity: " + this.humedad
                + "\n\tSnow bound: " + this.cota_nieve
                + "\n\tThermal sensation: " + this.sensacion_termica;
    }

    @Override
    public String toString() {
        return "\tTemperatura: " + this.temperatura
                + "\n\tViento: " + this.viento
                + "\n\tLluvias: " + this.lluvias
                + "\n\tHumedad: " + this.humedad
                + "\n\tCota de nieve: " + this.cota_nieve
                + "\n\tSensacion termica: " + this.sensacion_termica;

    }

}
