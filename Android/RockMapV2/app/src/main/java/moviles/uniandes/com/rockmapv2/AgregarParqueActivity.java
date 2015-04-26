package moviles.uniandes.com.rockmapv2;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import mundo.NetworkHelper;
import mundo.RockMap;
import mundo.WebServiceHelper;


public class AgregarParqueActivity extends Activity
{

    private EditText txtPaisPorGeo;

    private EditText txtNombre;

    private Button btnDone;

    private Spinner spinnerPaises;

    private EditText txtLatitude;

    private EditText txtLongitude;

    private RockMap rockMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_parque);

        rockMap = RockMap.darInstancia(this);

        Intent intent = getIntent();
        final double latitude = intent.getDoubleExtra("latitude",0);
        final double longitude = intent.getDoubleExtra("longitude",0);

        Geocoder geo = new Geocoder(this);
        Address direccion = null;
        try {
            List<Address> address = geo.getFromLocation(latitude,longitude,1);
            direccion = address.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtNombre = (EditText)findViewById(R.id.editTextName);

        txtPaisPorGeo = (EditText)findViewById(R.id.paisGPS);
        if(direccion != null) {
            txtPaisPorGeo.setText(direccion.getCountryName());
        }

        txtLatitude = (EditText)findViewById(R.id.latitude);
        txtLongitude = (EditText)findViewById(R.id.longitude);


        txtLatitude.setText(String.valueOf(latitude));
        txtLongitude.setText(String.valueOf(longitude));

        spinnerPaises = (Spinner)findViewById(R.id.spinnerCountries2);
        String[] paises = {"Argentina", "Chile", "Colombia","Peru", "USA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,paises);
        spinnerPaises.setAdapter(adapter);

        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String nombre = txtNombre.getText().toString();
                String pais = txtPaisPorGeo.getText().toString();
                if( nombre != null && !nombre.equals(""))
                {
                    if(pais == null || !pais.equals(""))
                        pais = (String)spinnerPaises.getSelectedItem();
                    else
                        Toast.makeText(getApplicationContext(),"Se tomara el pais ingresado manualmente",Toast.LENGTH_LONG).show();
                    rockMap.agregarParque(nombre,pais,latitude,longitude);

                    WebServiceHelper ws = new WebServiceHelper(getApplicationContext());
                    NetworkHelper nh = new NetworkHelper(getApplication());
                    if( nh.isNetworkAvailable( ) )
                    {
                        ws.agregarParque(nombre,pais,latitude,longitude);
                        Toast.makeText(getApplicationContext(),"El parque se ha guardado en el servidor",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Red no disponible, no se puede subir el parque al servidor",Toast.LENGTH_LONG).show();
                    }
                    finish();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Ingrese un nombre para el parque",Toast.LENGTH_LONG).show();
                }
            }
        });

        txtLatitude.setKeyListener(null);
        txtLongitude.setKeyListener(null);
        txtPaisPorGeo.setKeyListener(null);



    }



}
