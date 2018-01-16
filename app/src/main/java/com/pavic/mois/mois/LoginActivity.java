package com.pavic.mois.mois;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import Entidades.Usuario;
import Operaciones.Operaciones;

/**
 * En este apartado, nos solicitará un nombre y una ciudad para mostrar la información,
 * estos datos serán introducidos en BDD para su posterior lectura, ya que si escogemos
 * la opción de recordar ciudad y nombre no pasaremos por aquí hasta que desconectemos
 * o cambiemos la preferencia en su defecto.
 */
public class LoginActivity extends AppCompatActivity {

    private CheckBox recordar;
    private Spinner ciudad;
    private EditText nombre;
    private Operaciones operaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos los elementos que usamos en la activity para su posterior uso
        recordar = (CheckBox) findViewById(R.id.recordar);
        ciudad = (Spinner) findViewById(R.id.spinner);
        nombre = (EditText)findViewById(R.id.nombre);
        // Inicializamos la clase operaciones para poder trabajar con la BDD
        operaciones = new Operaciones();
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
        // Uso un spinner para mostrar las ciudades que quiero
        String[] ciudades = { getString( R.string.ciudad_1 ), getString( R.string.ciudad_2 )};
        ciudad.setAdapter(new ArrayAdapter<String>(this, R.layout.ciudades_layout, ciudades));
    }

    /**
     * En esta funcion vamos a recoger los datos del nombre y la ciudad, y los insertaremos a la base
     * de datos, con tal de que podremos acceder a ellos con las funciones oportunas de la clase
     * 'Operaciones'.
     * @param view
     */
    public void login(View view) {
        // Estas lineas lo que hacen es ocultar el teclado virtual que se pone al escribir sobre el EditText.
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(nombre.getWindowToken(), 0);
        // Comprobamos que el nombre no esté vacio
        if( nombre.getText().toString().equals("") ){
            Toast.makeText(this, R.string.fallo_vacio_correo, Toast.LENGTH_SHORT).show();
            return;
        }
        // Se genera la base de datos y se almacena el usuario y la ciudad que desea
        Usuario usuario = new Usuario(nombre.getText().toString(), ciudad.getSelectedItem().toString());
        // La funcion nos devuelve el ID generado
        int id = operaciones.insertarUsuario(this, usuario);
        // Iniciamos los valores predeterminados por código por si acaso.
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( this );
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("usuario_opcion", recordar.isChecked());
        // nos aseguramos que el idioma por defecto sea el español
        editor.putString("idioma_opcion", "ESP");
        // Almacenamos el ID generado para su posterior uso
        editor.putInt("ultimo_id", id);
        editor.commit();
        // Reiniciamos los valores en la vista
        recordar.setChecked(false);
        nombre.setText("");
        // Redirigimos a la nueva Activity que será la vista principal
        //Intent intent = new Intent(Activity_Origen.this, Activity_Destino.class);
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("ultimo_usuario", id);
        startActivity(intent);
        finish();
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

}
