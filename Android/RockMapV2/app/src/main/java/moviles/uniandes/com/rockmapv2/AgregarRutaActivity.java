package moviles.uniandes.com.rockmapv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.ExceptionParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.util.ArrayList;

import mundo.NetworkHelper;
import mundo.Parque;
import mundo.RockMap;
import mundo.WebServiceHelper;


public class AgregarRutaActivity extends Activity {

    private GoogleMap map;

    private int clicks;

    private LatLng[] puntos;

    private EditText txtParque;

    private Button btnTerminar;

    String nombre, pais, rutaImagen, dificultad, parque;

    int altura;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ruta);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapRuta))
                .getMap();

        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);

        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);

        Intent intent = getIntent();

        clicks = 0;



        nombre = intent.getStringExtra("nombre");
        pais = intent.getStringExtra("pais");
        rutaImagen = intent.getStringExtra("imagen");
        dificultad = intent.getStringExtra("dificultad");
        altura = intent.getIntExtra("altura",50);
        parque = intent.getStringExtra("parque");

        RockMap mundo = RockMap.darInstancia(this);

        ArrayList<Parque> parques = mundo.darParquesParaMapa();

        for( Parque p : parques)
        {
            if( p.getNombre().equalsIgnoreCase(parque))
            {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitude(),p.getLongitude()), 9f));
                map.addMarker(new MarkerOptions().position(new LatLng(p.getLatitude(),p.getLongitude())).title(parque));
                break;
            }
        }


        txtParque = (EditText)findViewById(R.id.parqueactual);

        txtParque.setText(parque);
        txtParque.setEnabled(false);

        btnTerminar = (Button)findViewById(R.id.btnTerminar);

        btnTerminar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( clicks < 2 )
                {
                    Toast.makeText(AgregarRutaActivity.this, "Elija dos puntos", Toast.LENGTH_LONG).show();
                }
                else if( clicks == 2)
                {
                    agregarRuta("Agregar ruta al parque " + parque, "¿Desea agregar la ruta?");

                }
            }
        });

        puntos = new LatLng[2];

        map.setOnMapClickListener( new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                clicks++;
                Log.i("AgregarRuta",clicks+"");
                if( clicks < 3 )
                {
                    puntos[clicks-1] = latLng;

                    if( clicks == 2)
                    {
                        map.addMarker(new MarkerOptions().position(puntos[1]).icon(BitmapDescriptorFactory.fromResource(R.drawable.parqueruta)));
                        PolylineOptions poly = new PolylineOptions().add(puntos[0],puntos[1]).color(Color.RED);
                        map.addPolyline(poly);

                    }
                    else if( clicks == 1)
                    {
                        map.addMarker( new MarkerOptions().position(puntos[0]).icon(BitmapDescriptorFactory.fromResource(R.drawable.parqueruta)));
                        Toast.makeText(AgregarRutaActivity.this, "Elija el siguiente punto", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    clicks = 0;
                    puntos = new LatLng[2];
                    map.clear();
                    RockMap mundo = RockMap.darInstancia(getApplication());
                    ArrayList<Parque> parques = mundo.darParquesParaMapa();

                    for( Parque p : parques)
                    {
                        if( p.getNombre().equalsIgnoreCase(parque))
                        {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitude(),p.getLongitude()), 9f));
                            map.addMarker(new MarkerOptions().position(new LatLng(p.getLatitude(),p.getLongitude())).title(parque));
                            break;
                        }
                    }

                }
            }
        });
    }

    private void agregarRuta(String title, String message)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id)
            {
                WebServiceHelper ws = new WebServiceHelper(getApplicationContext());
                NetworkHelper nh = new NetworkHelper(getApplicationContext());

                Log.i("AgregarRuta","subiendo imagen");

                if( puntos == null)
                {
                    Log.i("AgregarRuta","puntos null");

                }
                else
                {
                    if(puntos[0] != null )
                    Log.i("AgregarRuta", puntos[0].latitude+" "+puntos[0].longitude);
                    if(puntos[1] != null)
                    Log.i("AgregarRuta", puntos[1].latitude+" "+puntos[1].longitude);

                }
                if (nh.isNetworkAvailable()) {
                    File imagen = new File(rutaImagen);
                    try {
                        ws.subirImagen(imagen, altura, parque, parque, dificultad, puntos[0].latitude, puntos[0].longitude, puntos[1].latitude, puntos[1].longitude, pais, nombre);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                RockMap mundo = RockMap.darInstancia(getApplicationContext());
                mundo.agregarRuta(rutaImagen, altura, parque, parque, dificultad, puntos[0].latitude, puntos[0].longitude, puntos[1].latitude, puntos[1].longitude, pais, nombre);
                clicks = 0;
                puntos = new LatLng[2];

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog dialog= alertDialog.create();
        dialog.show();
    }

}
