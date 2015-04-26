package mundo;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import moviles.uniandes.com.rockmapv2.R;

/**
 * Created by Pablo Mesa on 08/04/2015.
 */
public class WebServiceHelper {
    public final static String IMAGEN = "file";
    public final static String NOMBRE = "nombre";
    public final static String ZONA = "zona";
    public final static String PARQUE = "parque";
    public final static String ALTURA = "altura";
    public final static String PUNTO1 = "punto1";
    public final static String PUNTO2 = "punto2";
    public final static String PUNTO3 = "punto3";
    public final static String PUNTO4 = "punto4";
    public final static String PAIS = "pais";
    public final static String DIFICULTAD = "dificultad";





    private String rutaWS;
    private Context context;
    private ArrayList<Ruta> rutas;

    public WebServiceHelper(Context ctx)
    {
        context = ctx;
        rutaWS = ctx.getString(R.string.imagesService);
    }

    public ArrayList<Ruta> darRutasPorPais( String pais )
    {
        Log.i("WebServiceHelper","Descargando rutas para el pais: " + pais);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rutas = new ArrayList<Ruta>();
        client.get(context.getString(R.string.contentService) + "/" + pais,new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String response)
            {
                Log.i("WebServiceHelper",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String zona = jsonArray.getJSONObject(i).getString("zona");
                        String pais = jsonArray.getJSONObject(i).getString("pais");
                        String parque = jsonArray.getJSONObject(i).getString("parque");
                        String dificultad = jsonArray.getJSONObject(i).getString("dificultad");
                        int altura = Integer.parseInt(jsonArray.getJSONObject(i).getString("altura"));
                        String nombre = jsonArray.getJSONObject(i).getString("nombre");
                        float p1 = Float.parseFloat(jsonArray.getJSONObject(i).getString("punto1"));
                        float p2 = Float.parseFloat(jsonArray.getJSONObject(i).getString("punto2"));
                        float p3 = Float.parseFloat(jsonArray.getJSONObject(i).getString("punto3"));
                        float p4 = Float.parseFloat(jsonArray.getJSONObject(i).getString("punto4"));
                        String imagen = jsonArray.getJSONObject(i).getString("imagen").split("/")[1];
                        RockMap mundo = RockMap.darInstancia(context);
                        mundo.agregarRutaPorWebService(zona,parque,imagen,dificultad,altura,p1,p2,p3,p4,pais,nombre);

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                rutas = new ArrayList<Ruta>();
            }
            @Override
            public void onFailure(Throwable error)
            {
                error.printStackTrace();
            }
        });
        return rutas;
    }

    public void subirImagen(File archivo, int altura, String zona, String parque, String dificultad, double p1, double p2, double p3, double p4, String pais, String nombreRuta) throws Exception
    {
        Log.i("WebServiceHelper","Comenzando a subir el archivo " + archivo.getAbsolutePath( ) );
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rp.put(IMAGEN,archivo);
        rp.add(PAIS,pais);
        rp.put(ALTURA,String.valueOf(altura));
        rp.put(ZONA,zona);
        rp.put(PARQUE,parque);
        rp.put(DIFICULTAD,dificultad);
        rp.add(NOMBRE,nombreRuta);
        rp.put(PUNTO1,String.valueOf(p1));
        rp.put(PUNTO2,String.valueOf(p2));
        rp.put(PUNTO3,String.valueOf(p3));
        rp.put(PUNTO4,String.valueOf(p4));




        client.post(rutaWS,rp,new TextHttpResponseHandler()
        {
                    public void onSuccess(int ii, PreferenceActivity.Header[] headers, String s) {
                        // Hide Progress Dialog
                        try {
                            // JSON Object
                            JSONObject obj = new JSONObject(s);
                            // When the JSON response has status boolean value assigned with true
                            Log.i("WebServiceHelper",s + " - " + obj.toString());
                            if(obj.getBoolean("status")){
                                // Navigate to Home screen
                            }
                            // Else display error message
                            else{
                                Log.i("WebServiceHelper","la imagen se encuentra en el servidor");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                    // When the response returned by REST has Http response code other than '200'
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        // When Http response code is '404'
                        if(statusCode == 404){
                            Log.i("WebServiceHelper","Requested resource not found");
                        }
                        // When Http response code is '500'
                        else if(statusCode == 500){
                            Log.i("WebServiceHelper","Something went wrong at server end");
                        }
                        // When Http response code other than 404, 500
                        else{
                            Log.i("WebServiceHelper","Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                        }
                        Log.i("WebServiceHelper",error.getMessage());
                    }
                }
        );
    }


    public void agregarParque(String nombre, String pais, double latitude, double longitude)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rp.add(PAIS,pais);

        rp.add(NOMBRE,nombre);
        rp.put(PUNTO1,String.valueOf(latitude));
        rp.put(PUNTO2,String.valueOf(longitude));

        client.post(context.getString(R.string.parkService), rp,new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Successfully got a response
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error)
            {
                // Response failed :(
            }
        });
    }


    public void agregarParquesPorPais(String pais)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(context.getString(R.string.downloadParks)+"/"+pais, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String response)
            {
                Log.i("WebServiceHelper",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String pais = jsonArray.getJSONObject(i).getString("pais");
                        String nombre = jsonArray.getJSONObject(i).getString("nombre");
                        float p1 = Float.parseFloat(jsonArray.getJSONObject(i).getString("punto1"));
                        float p2 = Float.parseFloat(jsonArray.getJSONObject(i).getString("punto2"));
                        RockMap mundo = RockMap.darInstancia(context);
                        mundo.agregarParque(nombre,pais,p1,p2);

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                rutas = new ArrayList<Ruta>();
            }
            @Override
            public void onFailure(Throwable error)
            {
                error.printStackTrace();
            }
        });

    }

    public File descargarImagenDeRuta(String ruta)
    {
        return null;
    }

    public void conectarWS()
    {
        Log.i("WebServiceHelper","Conectando con el web service ... ");
    }

}
