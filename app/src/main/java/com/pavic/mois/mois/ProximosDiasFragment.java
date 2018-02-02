package com.pavic.mois.mois;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import Entidades.Usuario;
import Operaciones.Operaciones;
import Utilidades.AdapterDiaActual;
import Utilidades.AdapterProximosDias;
import Entidades.Dia;
import Entidades.Hora;
import Utilidades.LecturaDiaActual;
import Utilidades.LecturaProximosDias;
import Entidades.Semana;
import Utilidades.LecturaProximosDiasEN;

/**
 * Vamos a ver la funcionalidad del fragmento, posiblemente esto fue lo más difícil con lo
 * que me encontré, ya que no había manera de poder leer el fichero y se cargará la vista,
 * en el ListView. Me di de porrazos hasta que me di cuenta necesitaba alguna manera de poder
 * leer el archivo en segundo plano para poder luego después de leer escribir en el ListView,
 * y eso me di cuneta cuando vimos las ‘AsyncTask’, me permitían ejecutar un código en segundo
 * plano y luego otro al terminar, por lo que cuando termina de leer, se encarga de escribir
 * en el ListView custom adecuado, ya que aquí podemos escribir para el día actual
 * o para los próximos dias
 */
public class ProximosDiasFragment extends Fragment {
    private static final String P_DIAS = "";

    // TODO: Rename and change types of parameters
    private String comprobar_Pdias;

    private ArrayList<Dia> arrayList;
    private AdapterProximosDias adapter;
    private ArrayList<Hora> arrayListDiaActual;
    private AdapterDiaActual adapterDiaActual;
    private ListView list;
    private Operaciones operaciones;

    private OnFragmentInteractionListener mListener;

    public ProximosDiasFragment() {
    }

    public static ProximosDiasFragment newInstance(String proximos_dias) {
        ProximosDiasFragment fragment = new ProximosDiasFragment();
        Bundle args = new Bundle();
        args.putString(P_DIAS, proximos_dias);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comprobar_Pdias = "";
        if (getArguments() != null) {
            comprobar_Pdias = getArguments().getString(P_DIAS);
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( getContext() );
        SharedPreferences.Editor editor = pref.edit();
        String idioma = pref.getString("idioma_opcion", "");
        int last_id = pref.getInt("ultimo_id", 0);
        editor.commit();
        System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
        operaciones = new Operaciones();
        Usuario usuario = operaciones.obtenerUsuario(getContext(), last_id);
        String url_pd = "", url_da = "";
        String idioma_APP = idioma.equals("ING") ? "en" : "es";
        if(usuario.getCiudad().equals("Albacete")){
            // si leemos los proximos dias en ingles revienta, por tanto hay que modificar
            // la lectura de datos en el archivo online poniendo que sea en ingles
            // solo falla si es en proximos dias
            url_pd = "http://api.tiempo.com/index.php?api_lang="+idioma_APP+"&localidad="+209+"&affiliate_id=1o73toi4cjxw";
            url_da = "http://api.tiempo.com/index.php?api_lang=es&localidad="+209+"&affiliate_id=1o73toi4cjxw&v=2&h=1";
        }
        if(usuario.getCiudad().equals("Almansa")){
            url_pd = "http://api.tiempo.com/index.php?api_lang="+idioma_APP+"&localidad="+521+"&affiliate_id=1o73toi4cjxw";
            url_da = "http://api.tiempo.com/index.php?api_lang=es&localidad="+521+"&affiliate_id=1o73toi4cjxw&v=2&h=1";
        }
        /**
         * Como vemos aquí dependiendo de que opción ejecutará una tarea u otra, tenemos
         * en este apartado 3 tareas posibles:
         *  •	RecuperarProximosDiasEN, lectura del archivo de próximos días en inglés
         *  •	RecuperarProximosDias, lectura del archivo de próximos días en español
         *  •	RecuperarDiaActual, lectura de las horas actuales, la peculiaridad de que aquí no
         *      hace falta leer otro fichero para tenerlo en inglés, ya que el servicio web
         *      proporciona números en este apartado, solo traducimos el string para mostrar datos.
         * Cada una escogerá su propio adaptador personalizado.
         *
         * Veremos como se ejecutan, para poder ejecutarlas debemos de enviarles la URL para cada
         * caso, se montará la URL de una manera u otra dependiendo de las opciones escogidas por
         * el usuario, dependerá la ciudad como vemos, el idioma y si ha escogido la opción
         * de próximos días o el día actual.
         *
         * Lo iremos leyendo todo el rato de las SharedPreferences, obtendremos el usuario con
         * el ID almacenado y aquí tendremos la ciudad. El idioma será el que haya escogido
         * también en las preferencias. Con estos datos lanzaremos una tarea u otra con una
         * URL específicamente en cada caso.
         */
        if ( comprobar_Pdias.equals("si")){
        //if ( p_dias ) {
            if( idioma.equals("ING") )
                new RecuperarProximosDiasEN().execute(url_pd);
            else
                new RecuperarProximosDias().execute(url_pd);
        } else {
            new RecuperarDiaActual().execute(url_da);
        }
    }


    class RecuperarProximosDias extends AsyncTask<String, Void, Semana> {

        @Override
        protected Semana doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader xmlreader = parser.getXMLReader();
                LecturaProximosDias gestor_pdias = new LecturaProximosDias();
                xmlreader.setContentHandler(gestor_pdias);
                InputSource is = new InputSource(url.openStream());
                xmlreader.parse(is);

                return gestor_pdias.getSemana();
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Semana semana) {

            try{
                list = (ListView)getActivity().findViewById(R.id.lista_dias_proximos);
                arrayList = new ArrayList<>();
                adapter = new AdapterProximosDias(getContext(), arrayList);
                list.setAdapter(adapter);
                for (int i = 0; i < semana.getSemana().size(); i++) {
                    arrayList.add(semana.getDia(i));
                    adapter.notifyDataSetChanged();
                }
            }catch(Exception e){
                Toast.makeText(getContext(), getText(R.string.toast_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    class RecuperarProximosDiasEN extends AsyncTask<String, Void, Semana> {

        @Override
        protected Semana doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader xmlreader = parser.getXMLReader();
                LecturaProximosDiasEN gestor_pdias = new LecturaProximosDiasEN();
                xmlreader.setContentHandler(gestor_pdias);
                InputSource is = new InputSource(url.openStream());
                xmlreader.parse(is);

                return gestor_pdias.getSemana();
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Semana semana) {

            try{
                list = (ListView)getActivity().findViewById(R.id.lista_dias_proximos);
                arrayList = new ArrayList<>();
                adapter = new AdapterProximosDias(getContext(), arrayList);
                list.setAdapter(adapter);
                for (int i = 0; i < semana.getSemana().size(); i++) {
                    arrayList.add(semana.getDia(i));
                    adapter.notifyDataSetChanged();
                }
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Dia dia = (Dia)adapterView.getItemAtPosition(i);
                        String s =  getText( R.string.toast_dia )  + dia.getDia()
                                + "\n\t\t" + getText( R.string.toast_temp_min ) + dia.getTemperaturaMinima() + getText( R.string.toast_grados )
                                + "\n\t\t" + getText( R.string.toast_temp_max ) + dia.getTemperaturaMaxima() + getText( R.string.toast_grados );
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(Exception e){
                Toast.makeText(getContext(), getText(R.string.toast_internet), Toast.LENGTH_LONG).show();
            }
        }
    }


    class RecuperarDiaActual extends AsyncTask<String, Void, Dia> {
        @Override
        protected Dia doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader xmlreader = parser.getXMLReader();
                LecturaDiaActual gestor_pdias = new LecturaDiaActual();
                xmlreader.setContentHandler(gestor_pdias);
                InputSource is = new InputSource(url.openStream());
                xmlreader.parse(is);
                return gestor_pdias.getDia();
            } catch (Exception e) {
                return null;
            }
        }
        protected void onPostExecute(Dia dia) {
            try{
                list = (ListView)getActivity().findViewById(R.id.lista_dias_proximos);
                arrayListDiaActual = new ArrayList<>();
                adapterDiaActual = new AdapterDiaActual(getContext(), arrayListDiaActual,
                        dia.getSolIn(), dia.getSolOut());
                list.setAdapter(adapterDiaActual);
                for (int i = 0; i < dia.getHoras().size(); i++) {
                    arrayListDiaActual.add(dia.getHora(i));
                    adapterDiaActual.notifyDataSetChanged();
                }
            }catch(Exception e){
                Toast.makeText(getContext(), getText(R.string.toast_internet), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pdias, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
