package moviles.uniandes.com.rockmapv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import mundo.Ruta;
import mundo.WebServiceHelper;


public class FragmentGPS extends Fragment implements LocationListener
{


    private ImageView imageView;

    private Spinner spinnerPaises;

    private Button btnDescarga;

    private Button btnUbicacion;

    private Button btnMapa;

    private EditText txtUbicacion;

    private LocationManager locationManager;

    private boolean isGPSEnabled;

    private boolean isNetworkEnabled;

    private boolean canGetLocation;

    private Location location;

    private double latitude;

    private double longitude;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gps, null);
        imageView = (ImageView) rootView.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.montana);
        imageView.setLayoutParams( new LinearLayout.LayoutParams(300,300));

        txtUbicacion = (EditText) rootView.findViewById(R.id.location);

        String[] paises = {"Argentina", "Chile", "Colombia","Peru", "USA"};
        spinnerPaises = (Spinner) rootView.findViewById(R.id.spinnerCountries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,paises);
        spinnerPaises.setAdapter(adapter);

        btnDescarga = (Button) rootView.findViewById(R.id.download);
        btnDescarga.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceHelper ws = new WebServiceHelper(getActivity());
                String pais = (String)spinnerPaises.getSelectedItem();
                ws.agregarParquesPorPais(pais);
                ws.darRutasPorPais(pais);

                Toast.makeText(getActivity(),"Se descargó la información de " + pais,Toast.LENGTH_LONG).show();
            }
        });


        btnUbicacion = (Button) rootView.findViewById(R.id.check);
        btnUbicacion.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Location locationfinal = darUbicacion();

                if( locationfinal != null )
                {
                    Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        Address add = geo.getFromLocation(locationfinal.getLatitude(), locationfinal.getLongitude(), 5).get(0);
                        txtUbicacion.setText(add.getCountryName() + " - " + add.getFeatureName());

                    }
                    catch(Exception e) {
                        txtUbicacion.setText("No disponible");


                    }
                }
                else
                {
                    txtUbicacion.setText("No disponible");
                }
            }
        });

        btnMapa = (Button)rootView.findViewById(R.id.gps);
        btnMapa.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mapas = new Intent(getActivity(),MapActivity.class);
                startActivity(mapas);
            }
        });



        return rootView;

    }


    public Location darUbicacion()
    {
        try {
            locationManager = (LocationManager) getActivity()
                    .getSystemService(getActivity().LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.i("GPS","No hay servicio GPS");
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000*60,
                            10, this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if
                            (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1000*60,
                                10, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
        try {
            Address add = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 5).get(0);
            txtUbicacion.setText(add.getLocality());

        }
        catch(Exception e) {
            txtUbicacion.setText("No disponible");


        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(getActivity(),"Imposible obtener ubicación",Toast.LENGTH_LONG).show();
    }
}
