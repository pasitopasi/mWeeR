package com.pavic.mois.mois;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import Entidades.Estadistica;
import Entidades.Semana;
import Entidades.Usuario;
import Operaciones.Operaciones;
import Utilidades.LecturaProximosDias;

/**
 * Otro gran inconveniente que encontré fue la utilización de tablas para la vista de datos.
 * En este apartado también uso tareas en segundo plano debido a que tenia que volver a leer las
 * temperaturas del fichero, debido a que van variando y por lo tanto no puedo almacenar siempre
 * las mismas, en cuanto las leo las introduzco en la base de datos, podría usarse para otro caso.
 * Luego encontré una librería llamada GraphView, la cual es muy fácil de importar y usar debido a
 * la wiki que tienen, es muy intuitiva de usar. Pongo su página web:
 *                              http://www.android-graphview.org/
 *
 */
public class RegistroActivity extends AppCompatActivity {

    private  Operaciones operaciones;
    private ArrayList<Estadistica> estadisticas;
    private ArrayList<DataPoint> temperaturas_minima;
    private ArrayList<DataPoint> temperaturas_maxima;
    private TextView temp_minima;
    private TextView temp_maxima;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Ponemos el boton de atrás en el toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Ponemos la barra de notificaciones de un color mas oscura
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.barra_notifi));
        }

        pref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
        SharedPreferences.Editor editor = pref.edit();
        int last_id = pref.getInt("ultimo_id", 0);
        editor.commit();
        System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
        operaciones = new Operaciones();
        // Formateamos la bdd
        operaciones.formatearEstadisticas(getApplicationContext());

        Usuario usuario = operaciones.obtenerUsuario(getApplicationContext(), last_id);
        String url_pd = "";
        if(usuario.getCiudad().equals("Albacete")){
            url_pd = "http://api.tiempo.com/index.php?api_lang=es&localidad="+209+"&affiliate_id=1o73toi4cjxw";
        }
        if(usuario.getCiudad().equals("Almansa")){
            url_pd = "http://api.tiempo.com/index.php?api_lang=es&localidad="+521+"&affiliate_id=1o73toi4cjxw";
        }
        new RecuperarEstadisticasE().execute(url_pd);
        temp_minima = (TextView)findViewById(R.id.temp_media);
        temp_maxima = (TextView)findViewById(R.id.temp_media_e);
        temp_minima.setTextColor(Color.BLUE);
        temp_maxima.setTextColor(Color.RED);

    }

    public void pintarGrafica(){
        temperaturas_minima = operaciones.obtenerEstadisticasMin(getApplicationContext());
        temperaturas_maxima = operaciones.obtenerEstadisticasMax(getApplicationContext());
        GraphView grafica = (GraphView) findViewById(R.id.grafica);
        // Creamos las lineas del gráfico y las añadimos a la gráfica
        LineGraphSeries<DataPoint> temperaturas_maximas = new LineGraphSeries<>(
                temperaturas_maxima.toArray(new DataPoint[temperaturas_maxima.size()]));
        grafica.addSeries(temperaturas_maximas);
        LineGraphSeries<DataPoint> temperaturas_minimas = new LineGraphSeries<>(
                temperaturas_minima.toArray(new DataPoint[temperaturas_minima.size()]));
        grafica.getSecondScale().addSeries(temperaturas_minimas);
        // Obtenemos los valores para ponerlos en la barra de abcisas
        double maxima_temperatura = grafica.getViewport().getMaxY(true);
        double minima_temperatura = grafica.getSecondScale().getMinY(true);
        // Ponemos el color a las barras
        temperaturas_minimas.setColor(Color.BLUE);
        temperaturas_maximas.setColor(Color.RED);
        // Ponemos el valor manualmente en la barra Y de la parte de temperatura minima
        grafica.getSecondScale().setMaxY(maxima_temperatura);
        grafica.getSecondScale().setMinY(minima_temperatura);
        // Ponemos el valor manualmente en la barra Y de la parte de temperatura maxima
        grafica.getViewport().setYAxisBoundsManual(true);
        grafica.getViewport().setMaxY(maxima_temperatura);
        grafica.getViewport().setMinY(minima_temperatura);
        // Obtenemos las fechas para poner los valores en el eje Y
        Calendar calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DATE);
        int mes = calendario.get(Calendar.MONTH);
        String dia_0 = dia+"-"+(mes + 1);
        calendario.set(calendario.get(Calendar.YEAR), mes, (dia+1) );
        dia = calendario.get(Calendar.DATE);
        mes = calendario.get(Calendar.MONTH);
        String dia_1 = dia+"-"+(mes + 1);
        calendario.set(calendario.get(Calendar.YEAR), mes, (dia+1) );
        dia = calendario.get(Calendar.DATE);
        mes = calendario.get(Calendar.MONTH);
        String dia_2 = dia+"-"+(mes + 1);
        calendario.set(calendario.get(Calendar.YEAR), mes, (dia+1) );
        dia = calendario.get(Calendar.DATE);
        mes = calendario.get(Calendar.MONTH);
        String dia_3 = dia+"-"+(mes + 1);
        calendario.set(calendario.get(Calendar.YEAR), mes, (dia+1) );
        dia = calendario.get(Calendar.DATE);
        mes = calendario.get(Calendar.MONTH);
        String dia_4 = dia+"-"+(mes + 1);
        calendario.set(calendario.get(Calendar.YEAR), mes, (dia+1) );
        dia = calendario.get(Calendar.DATE);
        mes = calendario.get(Calendar.MONTH);
        String dia_5 = dia+"-"+(mes + 1);
        calendario.set(calendario.get(Calendar.YEAR), mes, (dia+1) );
        dia = calendario.get(Calendar.DATE);
        mes = calendario.get(Calendar.MONTH);
        String dia_6 = dia+"-"+(mes + 1);
        // Ponemos el valor en el eje X
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(grafica);
        staticLabelsFormatter.setHorizontalLabels(new String[] {dia_0, dia_1, dia_2, dia_3, dia_4, dia_5, dia_6});
        grafica.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        grafica.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.BLACK);
    }

    @Override
    public void onBackPressed() {
        int last_id = pref.getInt("ultimo_id", 0);
        Intent intent =new Intent(getApplicationContext(), MenuActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("ultimo_usuario", last_id);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    // Capturamos la accion de darle a atrás de la barra
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Aqui leemos de nuevo del fichero para poder tener los datos actualizados.
     */
    class RecuperarEstadisticasE extends AsyncTask<String, Void, Semana> {
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
                // Aqui tenemos la estadistica de 7 días
                estadisticas = gestor_pdias.getEstadisticas();
                // la insertamos en la bdd
                operaciones.insertarEstadisitca(getApplicationContext(), estadisticas);
                pintarGrafica();
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }


}