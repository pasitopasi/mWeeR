package Operaciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.pavic.mois.mois.R;

import java.util.ArrayList;

import Entidades.Estadistica;
import Operaciones.ConexionBDD.*;
import Entidades.Usuario;

/**
 * Created by Alejandro on 13/11/2017.
 */

/**
 * En el paquete ‘Operaciones’ tengo las clases relacionadas con la base de datos,
 * por decirlo de alguna manera.
 * Para poder acceder a la base de datos me he diseñado un patrón como explica Google.
 * Explica que hay que usar clases “Contract”, una clase Contract es solamente un contenedor para
 * constantes que definen nombres de URI, tablas y columnas. La clase Contract te permite utilizar
 * las mismas constantes en todas las clases del mismo paquete. Esto te permite cambiar el nombre
 * de una columna en un lugar y que ese cambio se propague en todo el código. En mi caso también
 * he añadido aquí la creación de la base de datos, se podría haber optado hacerla en otra clase,
 * pero daría el mismo resultado. Además, he creado una clase ‘Operaciones’ dentro del mismo
 * paquete en la cual realizo todo lo relacionado con la base de datos, insert, delete, updates…
 */
public class Operaciones {

    public Operaciones(){ }

    /**
     * Funcion que inserta un Usuario, y nos devuelve el ID que obtiene esa inserción.
     * @param context
     * @param usuario
     * @return int
     */
    public int insertarUsuario(Context context, Usuario usuario){

        Conexion conn = new Conexion(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        // Creamos un mapa de valores, para poder pasarselo a la consulta
        ContentValues parametros = new ContentValues();
        parametros.put(TablaUsuarios.NOMBRE_COLUMNA_NOMBRE, usuario.getCorreo());
        parametros.put(TablaUsuarios.NOMBRE_COLUMNA_CIUDAD, usuario.getCiudad());

        // Al insertar la fila, devuelve la clave primaria de la nueva fila, para obtenerla en int
        // hacemos un casting y ya esta
        long usuarioIDLong = db.insert(TablaUsuarios.NOMBRE_TABLA, null, parametros);
        return (int) usuarioIDLong;
    }

    /**
     *
     * @param context
     * @param id
     * @return Usuario
     */
    public Usuario obtenerUsuario(Context context, int id){

        Conexion conn = new Conexion(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        Usuario usuario = new Usuario();

        String[] parametro = {id+""};
        String[] campo = {TablaUsuarios.NOMBRE_COLUMNA_NOMBRE, TablaUsuarios.NOMBRE_COLUMNA_CIUDAD};
        try{

            Cursor cursor = db.query(TablaUsuarios.NOMBRE_TABLA, campo, TablaUsuarios._ID+"=?",
                    parametro, null, null,null);
            cursor.moveToFirst();
            usuario.setCorreo(cursor.getString(0));
            usuario.setCiudad(cursor.getString(1));
            cursor.close();

        }catch(Exception e){
            usuario.setCorreo(null);
            Toast.makeText(context,  context.getString(R.string.error_user), Toast.LENGTH_SHORT).show();
        }

        return usuario;
    }

    /**
     *
     * @param context
     */
    public void insertarEstadisitca(Context context, ArrayList<Estadistica> estadisticas){
        Conexion conn = new Conexion(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        for(Estadistica estadistica : estadisticas){
            try{
                ContentValues parametros = new ContentValues();
                parametros.put(TablaEstadisticas.NOMBRE_COLUMNA_FECHA, estadistica.getFecha());
                parametros.put(TablaEstadisticas.NOMBRE_COLUMNA_TEMP_MIN, estadistica.getTemperatura_minima());
                parametros.put(TablaEstadisticas.NOMBRE_COLUMNA_TEMP_MAX, estadistica.getTemperatura_maxima());
                db.insert(TablaEstadisticas.NOMBRE_TABLA, null, parametros);
            }catch(Exception e){
                Toast.makeText(context,  context.getString(R.string.error_user), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<DataPoint> obtenerEstadisticasMax(Context context){
        Conexion conn = new Conexion(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        ArrayList<DataPoint> datapoints = new ArrayList<DataPoint>();
        String[] campo = {TablaEstadisticas.NOMBRE_COLUMNA_TEMP_MAX};
        try{
            Cursor cursor = db.query(TablaEstadisticas.NOMBRE_TABLA, campo, null,
                    null, null, null,null);
            int posicion = 0;
            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    int temp = cursor.getInt(0);
                    datapoints.add( new DataPoint(posicion, temp) );
                    posicion++;
                } while(cursor.moveToNext());
            }
            cursor.close();
        }catch(Exception e){
            Toast.makeText(context,  context.getString(R.string.error_user), Toast.LENGTH_SHORT).show();
        }
        return datapoints;
    }

    public ArrayList<DataPoint> obtenerEstadisticasMin(Context context){
        Conexion conn = new Conexion(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        ArrayList<DataPoint> datapoints = new ArrayList<DataPoint>();
        String[] campo = {TablaEstadisticas.NOMBRE_COLUMNA_TEMP_MIN};
        try{
            Cursor cursor = db.query(TablaEstadisticas.NOMBRE_TABLA, campo, null,
                    null, null, null,null);
            int posicion = 0;
            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    int temp = cursor.getInt(0);
                    datapoints.add( new DataPoint(posicion, temp) );
                    posicion++;
                } while(cursor.moveToNext());
            }
            cursor.close();

        }catch(Exception e){
            Toast.makeText(context, context.getString(R.string.error_user), Toast.LENGTH_SHORT).show();
        }
        return datapoints;
    }

    /**
     * Función que formateara la base de datos de estadistica, para que siempre haya datos los mas
     * reales posibles, ya que el tiempo varía.
     * @param context
     */
    public void formatearEstadisticas(Context context){
        Conexion conn = new Conexion(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        conn.formatearBDDEstadisticas(db);
    }
}
