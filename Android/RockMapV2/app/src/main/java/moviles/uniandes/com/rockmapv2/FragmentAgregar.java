package moviles.uniandes.com.rockmapv2;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.app.Activity;
import android.widget.Toast;

import java.io.File;
import java.util.Random;

import mundo.GPSHelper;
import mundo.NetworkHelper;
import mundo.RockMap;
import mundo.WebServiceHelper;

/**
 * Created by Pablo Mesa on 07/03/2015.
 */
public class FragmentAgregar extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private File imagen;

    private boolean habilitar;
    private RockMap mundo;
    private Button botonFoto;
    private Button botonSinFoto;
    private Spinner dificultades;
    private Spinner parques;
    private EditText altura;
    private EditText nombre;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_agregar, null);
        mundo = RockMap.darInstancia(getActivity().getApplicationContext());
        inicializarSpinnerDificultades(rootView);
        inicializarSpinnerParques(rootView);
        botonFoto = (Button) rootView.findViewById(R.id.buttonPicture);
        nombre = (EditText) rootView.findViewById(R.id.editTextName);
        altura = (EditText) rootView.findViewById(R.id.editTextHeight);

        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = nombre.getText().toString()+".jpg";
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imagen = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName);
                Uri uri = Uri.fromFile(imagen);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        return rootView;
    }


    public void inicializarSpinnerDificultades(View rootView )
    {
        dificultades = (Spinner)rootView.findViewById(R.id.spinnerDif);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,mundo.darDificultadesDelModoActual());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dificultades.setAdapter(adapter);
    }

    public void inicializarSpinnerParques(View rootView )
    {
        parques = (Spinner)rootView.findViewById(R.id.spinnerPark);
        String[] from = new String[]{"nombre"};
        int[] to = new int[]{android.R.id.text1};
        Cursor cursor = mundo.darParques();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item,cursor,from,to,0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parques.setAdapter(adapter);
    }

    public void limpiarEditTexts()
    {
        nombre.setText("");
        altura.setText("");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( requestCode == REQUEST_IMAGE_CAPTURE )
        {
            if( resultCode == Activity.RESULT_OK)
            {
                Log.i("FragmentAgregar",imagen.getAbsolutePath());
                if( imagen.exists())
                {

                    Cursor p = (Cursor)parques.getSelectedItem();
                    String nombreRuta = nombre.getText().toString();
                    String dificultad = dificultades.getSelectedItem().toString();
                    int alturaRuta = Integer.parseInt(altura.getText().toString());
                    String pais = "Colombia";
                    String parque =  p.getString(p.getColumnIndex("nombre"));

                    Intent agregarPuntos = new Intent(getActivity(), AgregarRutaActivity.class);
                    agregarPuntos.putExtra("nombre", nombreRuta);
                    agregarPuntos.putExtra("dificultad", dificultad);
                    agregarPuntos.putExtra("pais",pais);
                    agregarPuntos.putExtra("parque", parque);
                    agregarPuntos.putExtra("imagen", imagen.getAbsolutePath());
                    agregarPuntos.putExtra("altura", alturaRuta);
                    startActivity(agregarPuntos);


                    /*if( nh.isNetworkAvailable( ) )
                    {
                        ws.conectarWS();
                        try
                        {
                            ws.subirImagen(imagen,alturaRuta,parque,parque,dificultad,p1,p2,p3,p4,pais,nombreRuta);
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(getActivity(), "No se pudo conectar con el servicio", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No hay conexión a internet, la imagén se guardará en el dispositivo", Toast.LENGTH_LONG).show();
                    }*/




                }
                else
                {
                    Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();

                }
            }
            limpiarEditTexts();
        }

    }
}
