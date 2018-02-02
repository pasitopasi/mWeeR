package Utilidades;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pavic.mois.mois.R;

import java.util.ArrayList;

import Entidades.Dia;

/**
 * Created by Alejandro on 15/11/2017.
 */

public class AdapterProximosDias extends BaseAdapter{

    private Context context;
    private ArrayList<Dia> arrayList;

    public AdapterProximosDias(Context context, ArrayList<Dia> arrayList){
        this.context = context;
        this.arrayList = arrayList;
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
            view = layoutInflater.inflate(R.layout.dias_proximos, null);
        }
        TextView dia = (TextView)view.findViewById(R.id.dia);
        TextView cielo = (TextView)view.findViewById(R.id.cielo);
        TextView viento = (TextView)view.findViewById(R.id.viento);
        TextView descripcion = (TextView)view.findViewById(R.id.descripcion);
        ImageView icono = (ImageView) view.findViewById(R.id.icono);

        dia.setText(arrayList.get(i).getDia());
        cielo.setText(arrayList.get(i).getCielo());
        viento.setText(arrayList.get(i).getViento());
        descripcion.setText(arrayList.get(i).getDescripcion());

        // Añadido en el examen
        TextView tem_max = (TextView)view.findViewById(R.id.tem_max);
        TextView temp_min = (TextView)view.findViewById(R.id.temp_min);
        tem_max.setText(arrayList.get(i).getTemperaturaMaxima()+ context.getString(R.string.simbolo_grados));
        temp_min.setText(arrayList.get(i).getTemperaturaMinima()+ context.getString(R.string.simbolo_grados));

        
        String icono_N = arrayList.get(i).getIcono();
        switch(icono_N) {
            case "1":
                icono.setImageResource(R.drawable.imagen_1);
                break;
            case "2":
                icono.setImageResource(R.drawable.imagen_2);
                break;
            case "3":
                icono.setImageResource(R.drawable.imagen_3);
                break;
            case "4":
                icono.setImageResource(R.drawable.imagen_4);
                break;
            case "5":
                icono.setImageResource(R.drawable.imagen_5);
                break;
            case "6":
                icono.setImageResource(R.drawable.imagen_6);
                break;
            case "7":
                icono.setImageResource(R.drawable.imagen_7);
                break;
            case "8":
                icono.setImageResource(R.drawable.imagen_8);
                break;
            case "9":
                icono.setImageResource(R.drawable.imagen_9);
                break;
            case "10":
                icono.setImageResource(R.drawable.imagen_10);
                break;
            case "11":
                icono.setImageResource(R.drawable.imagen_11);
                break;
            case "12":
                icono.setImageResource(R.drawable.imagen_12);
                break;
            case "13":
                icono.setImageResource(R.drawable.imagen_13);
                break;
            case "14":
                icono.setImageResource(R.drawable.imagen_14);
                break;
            case "15":
                icono.setImageResource(R.drawable.imagen_15);
                break;
            case "16":
                icono.setImageResource(R.drawable.imagen_16);
                break;
            case "17":
                icono.setImageResource(R.drawable.imagen_17);
                break;
            case "18":
                icono.setImageResource(R.drawable.imagen_18);
                break;
            case "19":
                icono.setImageResource(R.drawable.imagen_19);
                break;
            case "20":
                icono.setImageResource(R.drawable.imagen_20);
                break;
            case "21":
                icono.setImageResource(R.drawable.imagen_21);
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
