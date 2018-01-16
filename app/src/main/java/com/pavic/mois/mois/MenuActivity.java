package com.pavic.mois.mois;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import Entidades.Usuario;
import Operaciones.Operaciones;

/**
 * Esta activity no tiene mayor complejidad, desde aquí lanzaremos a
 * todo lo demás, ejecutar los fragments o ir a las opciones de la app.
 */
public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProximosDiasFragment.OnFragmentInteractionListener {

    private Operaciones operaciones;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        // Inicializamos la clase operaciones para poder trabajar con la BDD
        operaciones = new Operaciones();
        // Leeremos el ultimo ID almacenado en el parametro
        int ultimo_ID = getIntent().getExtras().getInt("ultimo_usuario");
        // comprobamos que el ultimo_id tenga datos, en caso contrario empezamos la app de nuevo
        if ((ultimo_ID + "").equals("")) {
            startActivity(new Intent(getBaseContext(), InitialActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
        // me saltaba un error a la hora de coger el TextView, debido a que no estaba cargado aun
        // asi que opte con poner un retardo a la hora de coger ese recurso y funcionó
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                int ultimo_ID = getIntent().getExtras().getInt("ultimo_usuario");
                Usuario usuario = operaciones.obtenerUsuario(getApplicationContext(), ultimo_ID);
                if (usuario.getCorreo() == null) {
                    startActivity(new Intent(getApplicationContext(), InitialActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    finish();
                } else {
                    TextView nombre_user = (TextView) findViewById(R.id.nombre_usuario);
                    nombre_user.setText(usuario.getCorreo() + "\n" + getString(R.string.toast_ciudad) + usuario.getCiudad());
                }
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.salir_pregunta);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.salir_pregunta_si,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })
                .setNegativeButton(R.string.salir_pregunta_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), OpcionesActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.menu_registro) {
            Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        ProximosDiasFragment frament = null;
        boolean fragmentoSelecionado = false;

        if (id == R.id.menu_opcion1) {
            frament = new ProximosDiasFragment();
            fragmentoSelecionado = true;
        } else if (id == R.id.menu_proximos) {
            fragmentoSelecionado = true;
            frament = ProximosDiasFragment.newInstance("si");
        } else if (id == R.id.menu_desconectar) {
            // Aqui tenemos que desconectarnos, por tanto debemos de eliminar las preferencias
            // y lanzar a la activity de LOGIN
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("usuario_opcion", false);
            editor.putString("idioma_opcion", "ESP");
            editor.remove("ultimo_id");
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), InitialActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_salida) {
            onBackPressed();
        }
        if (fragmentoSelecionado)
            getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, frament).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
   