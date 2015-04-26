package moviles.uniandes.com.rockmapv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import mundo.GPSHelper;
import mundo.Parque;
import mundo.RockMap;
import mundo.Ruta;


public class MapActivity extends Activity implements LocationListener {

    private GPSHelper gpsHelper;

    private GoogleMap map;

    private Button btnBusqueda;

    private RockMap rockMap;

    private ArrayList<Parque> markers;

    private EditText et_place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        rockMap = RockMap.darInstancia(this);

        gpsHelper = GPSHelper.darInstancia(getApplicationContext(),this);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
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

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                map.clear();
                LatLng latlng  = new LatLng(gpsHelper.getLatitude(), gpsHelper.getLongitude());

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 8f));
                map.addMarker( new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.human)));
                desplegarParques();
                return false;
            }
        });

        map.setOnMapLongClickListener( new GoogleMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
                agregarParque("Agregar parque", "¿Desea agregar un parque en esta ubicación?", latLng);
            }
        });

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                float pos = cameraPosition.zoom;
                if(pos > 10f)
                {
                    map.clear();
                    desplegarRutas();
                }
                else if(pos > 8f)
                {
                    map.clear();
                    desplegarRutas();
                    desplegarParques();
                }
                else
                {
                    map.clear();
                    desplegarParques();
                }
                LatLng latlng  = new LatLng(gpsHelper.getLatitude(), gpsHelper.getLongitude());
                map.addMarker( new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.human)));
            }
        });


        btnBusqueda = (Button) findViewById(R.id.btn_show);
        //btnBusqueda.setBackgroundColor(getResources().getColor(R.color.button_material_light));
        //btnBusqueda.setTextColor(Color.parseColor(""));

        et_place = (EditText)findViewById(R.id.et_place);

        btnBusqueda.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String busqueda = et_place.getText().toString();
                if( busqueda != null && !busqueda.equals(""))
                {
                    if(markers != null)
                    {
                        for(Parque parque : markers)
                        {
                            if( parque.getNombre().toLowerCase().contains(busqueda.toLowerCase()))
                            {
                                LatLng latLng = new LatLng(parque.getLatitude(),parque.getLongitude());
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9f));
                                break;
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Ingrese un criterio de busqueda",Toast.LENGTH_SHORT);
                }
            }
        });

        Location myLocation = gpsHelper.darUbicacion();
        if(gpsHelper.canGetLocation())
        {
            //LatLng latlng  = new LatLng(5.127533748439441, -73.78337502479553);
            LatLng latlng  = new LatLng(gpsHelper.getLatitude(), gpsHelper.getLongitude());

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 6));
            map.addMarker( new MarkerOptions().position(latlng).title("Tu").visible(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.human)));
        }
        else
        {
            gpsHelper.mostrarAlertaGPS();
        }


        desplegarParques();
      //  desplegarRutas();






    }


    public void desplegarRutas()
    {
        ArrayList<Ruta> rutas = rockMap.darRutas();

        for(Ruta r : rutas)
        {
            if( r.getPunto1() != 0 && r.getPunto2() != 0 )
            {
                LatLng punto1 = new LatLng(r.getPunto1(),r.getPunto2());
                LatLng punto2 = new LatLng(r.getPunto3(),r.getPunto4());

                map.addMarker( new MarkerOptions().position(punto1).icon(BitmapDescriptorFactory.fromResource(R.drawable.parqueruta)));
                map.addMarker( new MarkerOptions().position(punto2).icon(BitmapDescriptorFactory.fromResource(R.drawable.parqueruta)));

                map.addPolyline(new PolylineOptions().add(punto1,punto2).color(Color.RED));



                Log.i("GoogleMaps","Desplegando ruta de nombre : " + r.getNombre());
            }
        }
    }


    public void desplegarParques()
    {
        markers = new ArrayList<Parque>();
        ArrayList<Parque> parques = rockMap.darParquesParaMapa();

        for(Parque parque : parques)
        {
            markers.add(parque);
            map.addMarker(new MarkerOptions().position(new LatLng(parque.getLatitude(),parque.getLongitude())).title(parque.getNombre()).visible(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.parque)));
        }
    }

    private void agregarParque(String title, String message, final LatLng latLng)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id)
            {
                Intent intent = new Intent(getApplicationContext(),AgregarParqueActivity.class );
                intent.putExtra("latitude",latLng.latitude);
                intent.putExtra("longitude",latLng.longitude);
                startActivity(intent);

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

    @Override
    public void onLocationChanged(Location location)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 1));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this, "Servicio GPS habilitado",Toast.LENGTH_LONG);

    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this, "Habilite el servicio GPS",Toast.LENGTH_LONG);
    }
}
