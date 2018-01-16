package Entidades;

/**
 * Created by Alejandro on 15/11/2017.
 */

import java.util.ArrayList;

public class Semana {
    private ArrayList<Dia> semana;

    public Semana(int dias) {
        this.semana  = new ArrayList<Dia>();
        for(int i = 0; i < dias; i++) {
            Dia dia = new Dia();
            this.addDia(dia);
        }
    }

    public ArrayList<Dia> getSemana() {
        return semana;
    }

    public void setSemana(ArrayList<Dia> semana) {
        this.semana = semana;
    }

    public void addDia(Dia dia) {
        this.semana.add(dia);
    }

    public Dia getDia( int indice ) {
        return this.semana.get(indice);
    }

}
