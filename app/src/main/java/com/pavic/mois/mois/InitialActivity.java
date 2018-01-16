package com.pavic.mois.mois;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Locale;

/**
 * Es la primera activity que se ejecuta siempre. Al arrancar por primera vez la app nos solicitara
 * el permiso de poder acceder a la memoria del teléfono, dato importante para poder usarla.
 * En esta activity también pasa algo importante, ya que dependiendo de que preferencia hayamos
 * escogido en el menú de preferencias, cargará un idioma u otro. Por defecto siempre mostrará
 * el español.
 */

public class InitialActivity extends AppCompatActivity {

    public static int MILISEGUNDOS_ESPERA = 1500;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;

    private int permiso_chequeado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        // Aqui vamos a comprobar la preferencia para poder saber que configuración coger, si la
        // española o la inglesa, compruebo si es inglesa porque por defecto esta la española.
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
        String idioma = pref.getString("idioma_opcion", "ESP");
        // El objeto Locale nos permite instanciar una región del mundo, en lo que respecta a
        // su idioma, e indicandosela a la nueva configuración que vamos a crear,
        // nos cogera los valores del archivo 'strings' correspondientes
        Configuration configuracion = new Configuration();
        configuracion.locale =
                idioma.equals("ING") ? new Locale("en") : new Locale("es");
        getResources().updateConfiguration(configuracion, null);

        // comprobaremos que esta checkeado para que a si no se nos inicie la app sin nuestro permiso
        permiso_chequeado = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }else{
            permiso_chequeado = 0;
        }
        /**
         * Una cosa que me gusta personalmente, es cuando escojo una activity fullscream es que
         * se elimine la barra de notificaciones y la navbar que poseen algunos teléfonos
         * se elimina con el siguiente código:
         */
        // Quitamos barra de notificaciones
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Ocultamos el navbar en caso de que hubiera puramente estetico
        // para que se vea mejor el FULLSCREAM
        View decorview  = getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if(permiso_chequeado == 0){
            /**
             * Otra cosa que tiene la activity es que tiene una espera de 1.5 segundos,
             * necesitaba una activity para poder configurar el idioma y una desde lanzar
             * todo, ya que tengo que comprobar si el usuario escogió la opción de recordar
             * o no, y a partir de ello lanzar una activity u otra.
             * El retardo se consigue con un Handler, viene a ser el caso de un hilo
             * con un tiempo determinado de espera para ejecutarse.
             */
            // Transcurridos unos segundos redirigimos a otra Activity
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    SharedPreferences pref =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    // Iniciamos los valores predeterminados por comandos.
                    Intent intent;
                    if (pref.getBoolean("usuario_opcion", false)) {
                        int last_id = pref.getInt("ultimo_id", 0);
                        intent = new Intent(getApplicationContext(), MenuActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("ultimo_usuario", last_id);
                    } else {
                        // Forzamos el lenguaje español porque se ha desconectado
                        Configuration configuracion = new Configuration();
                        configuracion.locale =  new Locale("es");
                        getResources().updateConfiguration(configuracion, null);
                        intent = new Intent(getApplicationContext(), LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }
                    //redirigimos a otra actividad la cual es la del login
                    permiso_chequeado = 0;
                    startActivity(intent);
                    finish();
                }
            }, MILISEGUNDOS_ESPERA);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Transcurridos unos segundos redirigimos a otra Activity
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //redirigimos a otra actividad la cual es la del login
                            permiso_chequeado = 0;
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, MILISEGUNDOS_ESPERA);
                }
                return;
            }
        }
    }

}
