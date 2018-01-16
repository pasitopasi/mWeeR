package Utilidades;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pavic.mois.mois.R;

import java.util.ArrayList;

import Entidades.Hora;

/**
 * Created by Alejandro on 17/11/2017.
 */


public class AdapterDiaActual extends BaseAdapter {

    private Context context;
    private ArrayList<Hora> arrayList;
    private int sol_in;
    private int sol_out;

    public AdapterDiaActual(Context context, ArrayList<Hora> arrayList, String sol_in, String sol_out){
        this.context = context;
        this.arrayList = arrayList;
        this.sol_in = Integer.parseInt( sol_in.substring(0, 2) );
        this.sol_out = Integer.parseInt( sol_out.substring(0, 2) );
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.dia_actual, null);
        }
        TextView hora = (TextView)view.findViewById(R.id.hora);
        TextView descripcion = (TextView)view.findViewById(R.id.descripcionD);
        ImageView icono = (ImageView) view.findViewById(R.id.iconoDia);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( context );
        String idioma = pref.getString("idioma_opcion", "");
        if ( idioma.equals("ING") ) {
            descripcion.setText(arrayList.get(i).toStringEN());
        } else {
            descripcion.setText(arrayList.get(i).toString());
        }
        hora.setText(arrayList.get(i).getHora().equals("24:00")
                ? "00:00" : arrayList.get(i).getHora());

        // Recupero el icono
        String icono_N = arrayList.get(i).getIcono();
        boolean dia = false;
        // Voy a comprobar si es de dia o no para poner un icono u otro
        String hora_S = arrayList.get(i).getHora();
        if(!hora_S.equals("00:00")){
            int hora_I = Integer.parseInt(hora_S.substring(0, 2));
            if (hora_I >= sol_in && hora_I < ( sol_out + 1 ) ) {
                dia = true;
            }
        }

        switch(icono_N) {
            case "1":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_1);
                else
                    icono.setImageResource(R.drawable.imagen_1l);
                break;
            case "2":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_2);
                else
                    icono.setImageResource(R.drawable.imagen_2l);
                break;
            case "3":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_3);
                else
                    icono.setImageResource(R.drawable.imagen_3l);
                break;
            case "4":
                icono.setImageResource(R.drawable.imagen_4);
                break;
            case "5":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_5);
                else
                    icono.setImageResource(R.drawable.imagen_5l);
                break;
            case "6":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_6);
                else
                    icono.setImageResource(R.drawable.imagen_6l);
                break;
            case "7":
                icono.setImageResource(R.drawable.imagen_7);
                break;
            case "8":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_8);
                else
                    icono.setImageResource(R.drawable.imagen_8l);
                break;
            case "9":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_9);
                else
                    icono.setImageResource(R.drawable.imagen_9l);
                break;
            case "10":
                icono.setImageResource(R.drawable.imagen_10);
                break;
            case "11":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_11);
                else
                    icono.setImageResource(R.drawable.imagen_11l);
                break;
            case "12":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_12);
                else
                    icono.setImageResource(R.drawable.imagen_12l);
                break;
            case "13":
                icono.setImageResource(R.drawable.imagen_13);
                break;
            case "14":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_14);
                else
                    icono.setImageResource(R.drawable.imagen_14l);
                break;
            case "15":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_15);
                else
                    icono.setImageResource(R.drawable.imagen_15l);
                break;
            case "16":
                icono.setImageResource(R.drawable.imagen_16);
                break;
            case "17":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_17);
                else
                    icono.setImageResource(R.drawable.imagen_17l);
                break;
            case "18":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_18);
                else
                    icono.setImageResource(R.drawable.imagen_18l);
                break;
            case "19":
                icono.setImageResource(R.drawable.imagen_19);
                break;
            case "20":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_20);
                else
                    icono.setImageResource(R.drawable.imagen_20l);
                break;
            case "21":
                if ( dia )
                    icono.setImageResource(R.drawable.imagen_21);
                else
                    icono.setImageResource(R.drawable.imagen_21l);
                break;
            case "22":
                icono.setImageResource(R.drawable.imagen_22);
                break;
            default:
                icono.setImageResource(R.drawable.imagen_1);
        }

        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
