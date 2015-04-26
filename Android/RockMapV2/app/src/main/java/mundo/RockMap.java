package mundo;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;

import geometria.Point;
import geometria.Poligono;

public class RockMap
{
    public static String ESC_DEPORTIVA;
    public static String ESC_CLASICA;
    public static int CANTIDAD_GRADOS_DIFICULTAD;

    private ArrayList<Ruta> rutasPorHacer;
    private TreeMap<String, ArrayList<Ruta>> rutasSistema;
    private ArrayList<Ruta> rutasRealizadas;
    private ArrayList<Ruta> rutasConsultadas;
    private Ruta rutaAVer;

    private ArrayList<Parque> parques;

    private TreeMap<String, ArrayList<String>> tablaDificultades;
    private String modoSeleccionado;

    private String[] encabezadosDificultad;
    private String numeroVSdificultad[];

    private InputStream archivoContenido;
    private static RockMap instancia;

    private DBManager db;

    private Context ctx;

    private String[] niveles;


    public static RockMap darInstancia(Context ctx)
    {
        if(instancia==null) instancia=new RockMap(ctx);
        return instancia;
    }

    public RockMap(Context ctx)
    {
        parques=new ArrayList<Parque>();
        rutasPorHacer = new ArrayList<Ruta>();
        rutasRealizadas = new ArrayList<Ruta>();
        rutasSistema=new TreeMap<String, ArrayList<Ruta>>();
        tablaDificultades=new TreeMap<String,ArrayList<String>>();
        this.ctx = ctx;
        db = new DBManager(ctx);
        //numeroVSdificultad=new String[...]; mejor inicilaizo en cargarDificultades
    }

    //Getters & Setters
    public ArrayList<Ruta> getRutasPorHacer() {
        return rutasPorHacer;
    }
    public ArrayList<Ruta> getRutasRealizadas()
   {
       return rutasRealizadas;
   }
    public ArrayList<Parque> getParques() {
        return parques;
    }
    public String getModoSeleccionado() {
        return modoSeleccionado;
    }
    public String[] getEncabezadosDificultad() {
        return encabezadosDificultad;
    }
    public ArrayList<Ruta> getRutasConsultadas() {
        return rutasConsultadas;
    }
    public Ruta getRutaAVer() {
        return rutaAVer;
    }

    public void setRutasPorHacer(ArrayList<Ruta> rutasPorHacer) {
        this.rutasPorHacer = rutasPorHacer;
    }
    public void setRutasRealizadas(ArrayList<Ruta> rutasRealizadas) {
        this.rutasRealizadas = rutasRealizadas;
    }
    public void setModoSeleccionado(String modoSeleccionado) {
        this.modoSeleccionado = modoSeleccionado;
        convertirArreglo();
        db.cambiarModo(modoSeleccionado);
    }
    public void setRutaAVer(Ruta rutaAVer) {
        this.rutaAVer = rutaAVer;
    }

    //Metodos Extra
    public void configurarAplicacion(InputStream arch) throws IOException
    {
        Log.i("MIRAR","Inicia craga de propiedades");
        Properties p = new Properties();

        if( arch != null ) p.load(arch);
        else throw new IOException("Ocurrió un error cargando puntos");

        //TODO: Aqui decimos cosas como donde persistir o donde van las imagenes
        ESC_DEPORTIVA=p.getProperty("Constante.rutaDeportiva");
        ESC_CLASICA=p.getProperty("Constante.rutaClasica");

        CANTIDAD_GRADOS_DIFICULTAD=Integer.parseInt(p.getProperty("cantidad.gradosDificultad"));

        Log.i("MIRAR",ESC_DEPORTIVA+" "+ESC_CLASICA);
        modoSeleccionado=p.getProperty("Escala.Dificultad");
    }

    public void cargarContenido(InputStream arch) throws Exception
    {
        Log.i("MIRAR","Inicializar carga de contenido");
        archivoContenido=arch;
        BufferedReader br=new BufferedReader(new InputStreamReader(archivoContenido));
        Log.i("MIRAR","Encontro el archivo");

        //Estructuras de datos para procesar la entrada
        int numeroDeParques=0, numeroDeZonas=0, numeroDeRutas=0, numeroDePuntos=0, i=0, j=0;
        double xi=0, yi=0, x1=0, y1=0, x2=0, y2=0, popularidadParque=0, popularidadRuta=0, altura=0;
        String imagen="", nombreParque="", nombreZona="", nombreRuta="", dificultad="", tipoEscalada="", aux[], aux2[];
        Point[] puntos;
        Poligono poligono;
        Parque esteParque;
        Zona estaZona;
        Ruta estaRuta;

        //-----------------INIT leer archivo---------------
        numeroDeParques=Integer.parseInt(br.readLine());
        Log.i("MIRAR","Numero de Parques: "+numeroDeParques);
        while(numeroDeParques--!=0)// Crea un nuevo parque
        {
            // Lee la descricion del parque
            aux=br.readLine().split(":");
            nombreParque=aux[0];
            popularidadParque=Double.parseDouble(aux[1]);
            numeroDePuntos=Integer.parseInt(aux[2]);
            puntos=new Point[numeroDePuntos];

            for(i=3; i<aux.length; i++)
            {
                aux2=aux[i].split(",");
                xi=Double.parseDouble(aux2[0]);
                yi=Double.parseDouble(aux2[1]);
                puntos[i-3]=new Point(xi, yi);
            }
            poligono=new Poligono(puntos);
            esteParque=new Parque(nombreParque, popularidadParque,poligono);
            db.agregarParque(nombreParque,(int)popularidadParque);

            Log.i("MIRAR","Parque creado: "+esteParque);

            numeroDeZonas=Integer.parseInt(br.readLine());
            Log.i("MIRAR","Numero de zonas: "+numeroDeZonas);

            while(numeroDeZonas--!=0)
            {
                aux=br.readLine().split(":");
                nombreZona=aux[0];
                x1=Double.parseDouble(aux[1]);
                y1=Double.parseDouble(aux[2]);
                x2=Double.parseDouble(aux[3]);
                y2=Double.parseDouble(aux[4]);
                estaZona=new Zona(nombreZona,new Point(x1,y1),new Point(x2,y2),esteParque);
                db.agregarZona(nombreParque,nombreZona);
                Log.i("MIRAR","Zona creada: "+estaZona);

                numeroDeRutas=Integer.parseInt(br.readLine());
                while(numeroDeRutas--!=0)
                {
                    aux=br.readLine().split(":");
                    nombreRuta=aux[0];
                    imagen=aux[1];
                    popularidadRuta=Double.parseDouble(aux[2]);
                    dificultad=aux[3];
                    tipoEscalada=aux[4];
                    altura=Double.parseDouble(aux[5]);
                    estaRuta=new Ruta(nombreRuta,imagen,dificultad,popularidadRuta,tipoEscalada,altura,estaZona);
                    db.agregarRuta(nombreZona,nombreRuta,imagen,dificultad,(int)altura,"roca");
                    estaZona.agregarUnaRuta(estaRuta);

                    ArrayList<Ruta> rutasConEsaDific= rutasSistema.get(dificultad);
                    if(rutasConEsaDific==null) rutasConEsaDific=new ArrayList<Ruta>();
                    rutasConEsaDific.add(estaRuta);
                    rutasSistema.put(dificultad,rutasConEsaDific);
                }
                esteParque.agregarZona(estaZona);
            }
            parques.add(esteParque);
        }
        br.close();
        Log.i("MIRAR","finalizo carga "+getParques().get(0));
        Log.i("MIRAR","las rutas en el sistema son"+rutasSistema);
    }

    public void cargarDificultades(InputStream arch) throws Exception
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(arch));
        db.agregarTiposDificultades();
        String linea = br.readLine();
        linea = br.readLine();
       while( linea != null )
       {
            String[] dif = linea.split(";");
           db.agregarRegistroDificultades(dif);
           linea = br.readLine();

       }

        br.close();
    }

    public ArrayList<Ruta> determinarRutasSegunCriterio(int dificultadMinima, int dificultadMaxima)
    {
        return null;
    }

    public String verDificultadSeleccionada(int nivel)
    {
        convertirArreglo();
        return niveles[nivel];
    }

    public void hacerBusqueda(int min, int max, int altura, String parqueNombre)
    {
        ArrayList<Ruta> rutasTemp, rta=new ArrayList<Ruta>();
        for (int i=min; i<=max; i++)
        {
            rutasTemp=rutasSistema.get(numeroVSdificultad[i]);
            if(rutasTemp!=null)
            {
                for(Ruta esta : rutasTemp)
                {
                    if(esta.getAltura() <= altura && esta.getZona().getParque().getNombre().equals(parqueNombre))
                        rta.add(esta);
                }
            }
        }
        rutasConsultadas=rta;
        Log.i("MIRAR",rutasConsultadas.toString());
    }

    public Cursor darDificultades()
    {
        return db.darDificultades();
    }

    public Cursor darParques()
    {
        return db.darParques();
    }

    public void convertirArreglo()
    {
        niveles = db.darDificultadesPorModo(modoSeleccionado);
    }

    public Cursor busqueda(int min, int max, int altura, String parqueNombre)
    {
        modoSeleccionado = "USA";
        convertirArreglo();
        return db.buscarRutasPorParam(niveles[min],niveles[max],String.valueOf(altura),parqueNombre);
    }


    public void agregarRutaPorHacer( String nombre )
    {
        db.agregarRutaARutasPorHacer(nombre);
    }

    public Cursor darRutasPorHacer()
    {
        return db.buscarRutasPorHacer();
    }

    public void agregarRuta(String imagen, int altura, String zona, String parque, String dificultad, double p1, double p2, double p3, double p4, String pais, String nombreRuta)
    {
        db.subirImagen(imagen,altura,zona,parque,dificultad,p1,p2,p3,p4,pais,nombreRuta);
    }

    public String[] darDificultadesDelModoActual()
    {
        return db.darDificultadSegunModoActual();
    }


    public ArrayList<Ruta> darRutas()
    {
        return db.darRutas();
    }


    public void agregarParque(String nombre, String pais, double latitude, double longitude)
    {
        db.agregarParque(nombre,pais,latitude,longitude);
    }

    public ArrayList<Parque> darParquesParaMapa()
    {
        return db.darParquesParaMapa();
    }

    public void agregarRutaPorWebService(String zona, String parque, String imagen, String dificultad, int altura, float p1, float p2, float p3, float p4, String pais, String nombre)
    {
        db.agregarRutaPorWebService(zona,parque,imagen,dificultad,altura,p1,p2,p3,p4,pais,nombre);
    }


    public String darImagenRutaPorNombre(String nombre)
    {
        return db.darImagenDeRutaPorNombre(nombre);
    }


    public void removeAll()
    {
        db.removeAll();
    }

    public void createAll()
    {
        db.createAll();
    }
}
