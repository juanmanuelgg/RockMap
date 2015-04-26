package mundo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import moviles.uniandes.com.rockmapv2.AgregarParqueActivity;
import moviles.uniandes.com.rockmapv2.MapActivity;

/**
 * Created by pablomesa on 14/04/15.
 */
public class GPSHelper {

    private LocationManager locationManager;

    private Location location;

    private boolean isGPSEnabled;

    private boolean isNetworkEnabled;

    private boolean canGetLocation;

    private double latitude;

    private double longitude;

    private Context context;

    private MapActivity mapActivity;

    private static GPSHelper instancia;

    public static GPSHelper darInstancia(Context ctx,MapActivity map)
    {
        if( instancia == null )
            instancia = new GPSHelper(ctx,map);
        return instancia;
    }

    public GPSHelper(Context ctx, MapActivity map)
    {
        context = ctx;
        mapActivity = map;
        darUbicacion();
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isGPSEnabled() {
        return isGPSEnabled;
    }

    public boolean isNetworkEnabled() {
        return isNetworkEnabled;
    }

    public double getLatitude() {
        if(location != null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude()
    {
        if(location != null )
            longitude = location.getLongitude();
        return longitude;
    }

    public boolean canGetLocation() {
        return canGetLocation;
    }

    public Location darUbicacion()
    {
        try {
            locationManager = (LocationManager) context
                    .getSystemService(context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.i("GPS", "No hay servicio GPS");
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000*60,
                            10, mapActivity);
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
                                10, mapActivity);
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


    public void mostrarAlertaGPS()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS no activado");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("¿Desea activar el GPS?");
        alertDialog.setPositiveButton("Settings",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);

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
