package Operaciones;

/**
 * Created by Alejandro on 10/11/2017.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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
/**
 * Una clase Contract es un contenedor para constantes que definen nombres de URI (identificadores
 * uniformes de recursos),tablas y columnas. La clase Contract te permite utilizar las mismas
 * constantes en todas las otras clases del mismo paquete. Esto te permite cambiar el nombre de una
 * columna en un lugar y que ese cambio se propague en todo el código.
 */

public class ConexionBDD {

    /**
     * Para evitar que alguien accidentalmente crea una instancia de la clase contractual
     * hace que el constructor sea privado.
     */
    private ConexionBDD() {
    }

    /* Clase interna que define los contenidos de la tabla */
    // Clase Contract
    public static class TablaUsuarios implements BaseColumns {
        public static final String NOMBRE_TABLA = "usuarios";
        public static final String NOMBRE_COLUMNA_NOMBRE = "nombre";
        public static final String NOMBRE_COLUMNA_CIUDAD = "ciudad";
    }

    /* Clase interna que define los contenidos de la tabla */
    // Clase Contract
    public static class TablaEstadisticas implements BaseColumns {
        public static final String NOMBRE_TABLA = "estadisticas";
        public static final String NOMBRE_COLUMNA_FECHA = "fecha";
        public static final String NOMBRE_COLUMNA_TEMP_MIN = "temp_min";
        public static final String NOMBRE_COLUMNA_TEMP_MAX = "temp_max";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREAR_USUARIOS =
            "CREATE TABLE " + TablaUsuarios.NOMBRE_TABLA + " (" +
                    TablaUsuarios._ID + " INTEGER PRIMARY KEY," +
                    TablaUsuarios.NOMBRE_COLUMNA_NOMBRE + TEXT_TYPE + COMMA_SEP +
                    TablaUsuarios.NOMBRE_COLUMNA_CIUDAD + TEXT_TYPE + " )";

    private static final String SQL_CREAR_ESTADISTICAS =
            "CREATE TABLE " + TablaEstadisticas.NOMBRE_TABLA + " (" +
                    TablaEstadisticas._ID + " INTEGER PRIMARY KEY," +
                    TablaEstadisticas.NOMBRE_COLUMNA_FECHA + TEXT_TYPE +COMMA_SEP +
                    TablaEstadisticas.NOMBRE_COLUMNA_TEMP_MIN + TEXT_TYPE + COMMA_SEP +
                    TablaEstadisticas.NOMBRE_COLUMNA_TEMP_MAX + TEXT_TYPE + " )";


    private static final String SQL_BORRAR_USUARIOS =
            "DROP TABLE IF EXISTS " + TablaUsuarios.NOMBRE_TABLA;

    private static final String SQL_BORRAR_ESTADISTICAS =
            "DROP TABLE IF EXISTS " + TablaEstadisticas.NOMBRE_TABLA;

    public static class Conexion extends SQLiteOpenHelper {

        public static final int VERSION_BASEDEDATOS = 1;
        public static final String NOMBRE_BASEDEDATOS = "USUARIOS.db";

        public Conexion(Context context) {
            super(context, NOMBRE_BASEDEDATOS, null, VERSION_BASEDEDATOS);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREAR_USUARIOS);
            db.execSQL(SQL_CREAR_ESTADISTICAS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_BORRAR_USUARIOS);
            db.execSQL(SQL_BORRAR_ESTADISTICAS);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public void formatearBDDEstadisticas(SQLiteDatabase db) {
            db.execSQL(SQL_BORRAR_ESTADISTICAS);
            db.execSQL(SQL_CREAR_ESTADISTICAS);
        }
    }
}