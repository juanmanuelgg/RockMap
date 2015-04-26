package moviles.uniandes.com.rockmapv2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import mundo.DBManager;
import mundo.RockMap;

public class MainActivity extends Activity
{
    private Spinner spinnerformatos;
    private RockMap mundo;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mundo = new RockMap(this);
        try
        {
            //mundo.removeAll();
            //mundo.createAll();
            Log.i("MIRAR", "Cargar configuracion general");
            //mundo.configurarAplicacion(getBaseContext().getAssets().open("config.properties"));
            Log.i("MIRAR","Cargar contenido de la aplicacion");
            //mundo.cargarContenido(getBaseContext().getAssets().open("contenido.txt"));
            Log.i("MIRAR","Cargar regla de cambio de notacion de dificultad");
            mundo.cargarDificultades(getBaseContext().getAssets().open("tablaDificultades.csv"));
        }
        catch(Exception e)
        {
            Log.i("MIRAR","Salio mal");
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);
        setTitle("RockMap");

        spinnerformatos = (Spinner)findViewById(R.id.spinnerBienvenido);
        String[] from = new String[]{"nombre"};
        int[] to = new int[]{android.R.id.text1};
        cursor = mundo.darDificultades();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,cursor,from,to,0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerformatos.setAdapter(adapter);
    }

    public void ingresar(View v)
    {
        Cursor c = (Cursor) spinnerformatos.getSelectedItem();
        String est = c.getString(c.getColumnIndex("nombre"));
        Log.i("MIRAR", est);
        mundo.setModoSeleccionado(est);
        Log.i("MIRAR","seleccionado: "+mundo.getModoSeleccionado());
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("seleccion",est);
        startActivity(intent);
    }

}
