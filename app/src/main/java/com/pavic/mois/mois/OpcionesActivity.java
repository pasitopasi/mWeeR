package com.pavic.mois.mois;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

/**
 * Created by Alejandro on 14/11/2017.
 */

/**
 * Esta vista se nos lanzará al seleccionar la parte de opciones, aquí lo único que hará
 * será modificar las opciones y en caso de cambiar el idioma se nos lanzará de nuevo la app
 */
public class OpcionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ponemos el boton de atrás en el toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
        String idioma = pref.getString("idioma_opcion", "");
        Configuration configuracion = new Configuration();
        configuracion.locale = idioma.equals("ING") ? new Locale("en") : new Locale("es");
        getResources().updateConfiguration(configuracion, null);

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
        getFragmentManager().beginTransaction().replace(android.R.id.content, new OpcionesFragment()).commit();

    }
    @Override
    public void onBackPressed() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
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
}
