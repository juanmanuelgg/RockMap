package mundo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Pablo Mesa on 11/03/2015.
 */
public class DBManager {

    //Info rutas
    public final static String TABLA_RUTAS = "rutas";
    public final static String CN_RUTA_NOMBRE = "nombre";
    public final static String CN_RUTA_IMAGEN = "imagen";
    public final static String CN_RUTA_DIFICULTAD = "dificultad";
    public final static String CN_RUTA_ALTURA = "altura";
    public final static String CN_PUNTO_1 = "punto1";
    public final static String CN_PUNTO_2 = "punto2";
    public final static String CN_PUNTO_3 = "punto3";
    public final static String CN_PUNTO_4 = "punto4";


    public final static String CN_RUTA_TIPO = "TipoEscalada";


    public final static String TABLA_ZONAS = "zonas";
    public final static String CN_ZONA_NOMBRE = "nombre";
    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";


    //Info parques
    public final static String TABLA_PARQUES = "parques";

    public final static String CN_PARQUE_NOMBRE = "nombre";

    public final static String CN_PARQUE_POPULARIDAD = "popularidad";

    public final static String CN_ID = "_id";


    public final static String CREATE_TABLE_RUTAS = "create table rutas ( _id integer primary key autoincrement, nombre text not null, imagen text, dificultad text, tipo text, altura integer, zona_id integer, punto1 real, punto2 real, punto3 real, punto4 real, FOREIGN KEY (zona_id) REFERENCES zonas(_id)) ";
            /*"CREATE TABLE " + TABLA_RUTAS + " ( "
            + CN_ID + " integer primary key autoincrement, "
            + CN_RUTA_NOMBRE + " text not null, "
            + CN_RUTA_IMAGEN + " text, "
            + CN_RUTA_DIFICULTAD + " text, "
            + CN_RUTA_TIPO + " text, "
            + CN_RUTA_ALTURA + " integer, "
            + "zona_id integer, "
            + " FOREIGN KEY (zona_id) REFERENCES "+ TABLA_ZONAS + "("+CN_ID+"))";*/

    public final static String CREATE_TABLE_PARQUES = "CREATE TABLE " + TABLA_PARQUES + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_PARQUE_NOMBRE + " text not null,"
            + CN_PARQUE_POPULARIDAD + " integer,"
            + LATITUDE +" real,"
            + LONGITUDE + " real)";

    public final static String CREATE_TABLE_ZONAS = "CREATE TABLE " + TABLA_ZONAS + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_ZONA_NOMBRE + " text not null,"
            + "parque_id integer,"
            + " FOREIGN KEY(parque_id) REFERENCES "+ TABLA_PARQUES + "("+CN_ID+"))";

    public final static String CREATE_TABLE_MISRUTASPORHACER = "CREATE TABLE rutasporhacer ("
            + CN_ID + " integer primary key autoincrement,"
            + "ruta_id integer,"
            + " FOREIGN KEY(ruta_id) REFERENCES "+ TABLA_RUTAS + "("+CN_ID+"))";

    public final static String CREATE_TABLE_RUTASREALIZADAS = "CREATE TABLE rutasrealizadas ("
            + CN_ID + " integer primary key autoincrement,"
            + "ruta_id integer,"
            + " FOREIGN KEY(ruta_id) REFERENCES "+ TABLA_RUTAS + "("+CN_ID+"))";

    public final static String CREATE_TABLE_DIFICULTADES = " CREATE TABLE dificultades ( " +
            "_id integer primary key autoincrement,"
            + "USA text, Reino_unido_Tech text,Reino_unido_Adj,Frances text,UIAA text,SAXON text,Oceania text,Sur_Africa text, Finlandia text,Suecia_Noruega text,Brasil text)";

    public final static String CREATE_TABLE_TIPOS_DIFICULTADES = "CREATE TABLE tipos_dificultades ( "
            + "_id integer primary key autoincrement,"
            + "nombre text not null)";


    public final static String CREATE_TABLE_MODE = " CREATE TABLE modo ( _id integer primary key autoincrement" +
            ", nombre text not null)";


    public final static String eliminar1 = "delete from rutas where 1=1";
    public final static String eliminar2 = "delete from zonas where 1=1";
    public final static String eliminar3 = "delete from parques where 1=1";
    public final static String eliminar4 = "delete from dificultades where 1=1";

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context ctx)
    {
        helper = new DBHelper(ctx);
        db = helper.getWritableDatabase();
    }


    public void agregarParque(String nombre, int popularidad)
    {
        ContentValues cv = new ContentValues();
        cv.put(CN_PARQUE_NOMBRE,nombre);
        cv.put(CN_PARQUE_POPULARIDAD,popularidad);
        db.insert(TABLA_PARQUES,null,cv);
    }

    public void agregarZona(String nombreParque, String nombreZona)
    {
        db.execSQL("INSERT INTO " + TABLA_ZONAS + "(nombre,parque_id) VALUES('"+ nombreZona+"',(SELECT _id FROM parques p WHERE p.nombre='"+nombreZona+"'))");
    }


    public void agregarRuta(String nombreZona, String nombreRuta, String imagen, String dificultad, int altura, String tipo )
    {
        String sql = "INSERT INTO " + TABLA_RUTAS + "( nombre, imagen, altura, zona_id)" +
                " VALUES('"+nombreRuta+"','"+imagen+"',"+altura+", (SELECT _id FROM zonas z WHERE z.nombre = '"+nombreZona+"') )";
        String sql2 = "UPDATE rutas SET dificultad = '"+dificultad+"' WHERE nombre = '"+nombreRuta+"'";
        Log.i("IMPORTANTE",sql);
        Log.i("IMPORTANTE",CREATE_TABLE_RUTAS);

        db.execSQL(sql);
        db.execSQL(sql2);
    }


    public void agregarRutaPorWebService(String zona, String parque, String imagen, String dificultad, int altura, float p1, float p2, float p3, float p4, String pais, String nombre)
    {
        String sqlZonas = "insert into zonas(nombre,parque_id) values('"+parque+"', (select _id from parques where nombre = '"+parque+"' order by _id limit 1))";
        String sqlRutas = "insert into rutas(nombre, imagen, dificultad, altura, punto1, punto2, punto3, punto4, zona_id) values('"+nombre+"','"+imagen+"','"+dificultad+"',"+altura+","+p1+","+p2+","+p3+","+p4+",(select _id from zonas where nombre = '"+parque+"' order by _id limit 1))";
        db.execSQL(sqlZonas);
        db.execSQL(sqlRutas);
    }

    public Cursor buscarRutasPorParam(String minimo, String maximo, String altura, String parque)
    {
        //String sql = "SELECT r._id, r.nombre, r.imagen, r.dificultad FROM rutas r INNER JOIN zonas z ON z._id = r.zona_id INNER JOIN parques p ON p._id = z.parque_id " +
        //  "WHERE r.dificultad >= '"+minimo+"' and r.dificultad <= '"+maximo+"' and r.altura <="+ altura +" and p.nombre = '"+parque+"'";

        String sql = "select _id,nombre, dificultad, altura from rutas where altura <= " + altura ;
        String[] columnas = new String[]{CN_ID,CN_RUTA_NOMBRE,CN_RUTA_DIFICULTAD,CN_RUTA_ALTURA};

        Cursor cursor = db.query(TABLA_RUTAS,columnas,CN_RUTA_ALTURA+"<=?", new String[]{altura},null,null,null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("nombre")));
            cursor.moveToNext();
        }

        String[] arr = new String[names.size()];
        for(int i = 0; i < names.size(); i++ )
        {
            arr[i] = names.get(i);
            Log.i("IMP",arr[i]);
        }

        return cursor;
    }


    public Cursor buscarRutasPorHacer( )
    {
        //String sql = "SELECT r._id, r.nombre, r.imagen, r.dificultad FROM rutas r INNER JOIN zonas z ON z._id = r.zona_id INNER JOIN parques p ON p._id = z.parque_id " +
        //  "WHERE r.dificultad >= '"+minimo+"' and r.dificultad <= '"+maximo+"' and r.altura <="+ altura +" and p.nombre = '"+parque+"'";

        String sql = "select _id,nombre, dificultad, altura from rutasporhacer" ;
        String[] columnas = new String[]{CN_ID,CN_RUTA_NOMBRE,CN_RUTA_DIFICULTAD,CN_RUTA_ALTURA};

        Cursor cursor = db.query(TABLA_RUTAS,columnas,null, null,null,null,null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("nombre")));
            cursor.moveToNext();
        }

        String[] arr = new String[names.size()];
        for(int i = 0; i < names.size(); i++ )
        {
            arr[i] = names.get(i);
            Log.i("IMP",arr[i]);
        }

        return cursor;
    }


    public Cursor darRutaPorNombre(String nombre)
    {
        String[] columnas = new String[]{CN_ID,CN_RUTA_NOMBRE,CN_RUTA_IMAGEN,CN_RUTA_DIFICULTAD};

        return db.query(TABLA_RUTAS,columnas,CN_RUTA_NOMBRE+"=?", new String[]{nombre},null,null,null);
    }


    public void agregarRutaARutasPorHacer(String nombre)
    {
        String sql = "INSERT INTO rutasporhacer(ruta_id) VALUES ( (SELECT  _id FROM rutas r WHERE r.nombre = '"+nombre+"' ORDER BY _id LIMIT 1))";
        db.execSQL(sql);
    }


    public void agregarRutaARutasRealizadas(String nombre)
    {
        String sql = "INSERT INTO rutasrealizadas(ruta_id) VALUES ( (SELECT _id FROM rutas r WHERE r.nombre = '"+nombre+"'))";
        String sql2 = "DELETE FROM rutasporhacer WHERE ruta_id = ((SELECT _id FROM rutas r WHERE r.nombre = '"+nombre+"'))";

        db.execSQL(sql);
        db.execSQL(sql2);
    }


    public void agregarRegistroDificultades( String[] format)
    {
        //+ "USA text, Reino_unido_Tech text,Reino_unido_Adj,Frances text,UIAA text,SAXON text,Oceania text,Sur_Africa text, Finlandia text,Suecia_Noruega text,Brasil text)";
        String[] tip = new String[]{"USA","Reino_unido_Tech","Reino_unido_Adj","Frances","UIAA","SAXON","Oceania","Sur_Africa","Finlandia","Suecia_Noruega","Brasil"};
        ContentValues v = new ContentValues();
        for (int i = 0; i < tip.length; i++ )
        {
            v.put(tip[i],format[i]);
        }

        db.insert("dificultades",null,v);
    }

    public Cursor darDificultades()
    {
        return db.query("tipos_dificultades",new String[]{"_id","nombre"},null,null,null,null,"_id DESC","11");
    }

    public Cursor darParques()
    {
        return db.query(TABLA_PARQUES,new String[]{CN_ID,CN_PARQUE_NOMBRE},null,null,null,null,null);
    }

    public ArrayList<Parque> darParquesParaMapa()
    {
        Cursor c =  db.query(TABLA_PARQUES,new String[]{CN_ID,CN_PARQUE_NOMBRE,LATITUDE,LONGITUDE},null,null,null,null,null);
        ArrayList<Parque> parques = new ArrayList<Parque>();
        if(c.moveToFirst())
        {
            do{
                String nombre = c.getString(c.getColumnIndex(CN_PARQUE_NOMBRE));
                double latitude = c.getDouble(c.getColumnIndex(LATITUDE));
                double longitude = c.getDouble(c.getColumnIndex(LONGITUDE));

                Parque parque = new Parque();
                parque.setNombre(nombre);
                parque.setLatitude(latitude);
                parque.setLongitude(longitude);
                parques.add(parque);

            }
            while(c.moveToNext());
        }

        return parques;


    }


    public void agregarTiposDificultades()
    {
        String[] tip = new String[]{"USA","Reino_unido_Tech","Reino_unido_Adj","Frances","UIAA","SAXON","Oceania","Sur_Africa","Finlandia","Suecia_Noruega","Brasil"};

        for( int i = 0; i < tip.length; i++ )
        {
            ContentValues v = new ContentValues();
            v.put("nombre",tip[i]);
            db.insert("tipos_dificultades",null,v);
            Log.i("Agregue", tip[0]);
        }

    }


    public String[] darDificultadesPorModo(String modo)
    {
        if(modo == null)
            modo = "USA";
        Cursor cursor = db.query("dificultades",new String[]{modo.toString()},null,null,null,null,null,null);

        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex(modo)));
            cursor.moveToNext();
        }
        cursor.close();

        String[] arr = new String[names.size()];
        for(int i = 0; i < names.size(); i++ )
        {
            arr[i] = names.get(i);
        }
        return arr;
    }

    public Cursor darDificultadesPorModoCursor(String modo)
    {
        if(modo == null)
            modo = "USA";
        return db.query("dificultades",new String[]{modo.toString()},null,null,null,null,null,null);

    }


    public void cambiarModo(String modo)
    {
        /*Log.i("DBManager","cambiando el modo de la app a : " + modo);
        String sql = "UPDATE modo SET nombre = '"+modo+"' WHERE nombre is not null";
        try {
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            sql = "INSERT INTO modo (nombre) VALUES ('"+modo+"')";
            db.execSQL(sql);
        }*/

        String sql = "INSERT INTO modo (nombre) VALUES ('"+modo+"')";
        db.execSQL(sql);
    }

    public String[] darDificultadSegunModoActual()
    {
        String modo = null;
        try {
            Cursor c =db.rawQuery("SELECT _id, nombre FROM modo ORDER BY _id DESC LIMIT 1",null);
            if(c.moveToFirst())
                modo = c.getString(c.getColumnIndex(CN_RUTA_NOMBRE));

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return darDificultadesPorModo(modo);
    }

    public ArrayList<Ruta> darRutas( )
    {
        String[] campos = new String[]{CN_ID,CN_RUTA_NOMBRE,CN_RUTA_DIFICULTAD, CN_RUTA_IMAGEN,CN_RUTA_ALTURA, CN_PUNTO_1, CN_PUNTO_2, CN_PUNTO_3, CN_PUNTO_4};
        Cursor c = db.query(TABLA_RUTAS,campos,null,null,null,null,null);
        ArrayList<Ruta> rutas = new ArrayList<Ruta>();
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String codigo= c.getString(c.getColumnIndex(CN_RUTA_NOMBRE));
                String nombre = c.getString(c.getColumnIndex(CN_RUTA_DIFICULTAD));
                String imagen = c.getString(c.getColumnIndex(CN_RUTA_IMAGEN));
                String altura = c.getString(c.getColumnIndex(CN_RUTA_ALTURA));
                String dificultad = c.getString(c.getColumnIndex(CN_RUTA_DIFICULTAD));


                double punto1 = c.getDouble(c.getColumnIndex(CN_PUNTO_1));
                double punto2 = c.getDouble(c.getColumnIndex(CN_PUNTO_2));
                double punto3 = c.getDouble(c.getColumnIndex(CN_PUNTO_3));
                double punto4 = c.getDouble(c.getColumnIndex(CN_PUNTO_4));

                Ruta r = new Ruta();
                r.setNombre(nombre);
                r.setAltura(Integer.parseInt(altura));
                r.setDificultad(dificultad);
                r.setImagen(imagen);
                r.setPunto1(punto1);
                r.setPunto2(punto2);
                r.setPunto3(punto3);
                r.setPunto4(punto4);
                rutas.add(r);


            } while(c.moveToNext());
        }

        return rutas;
    }


    public void subirImagen(String imagen, int altura, String zona, String parque, String dificultad, double p1, double p2, double p3, double p4, String pais, String nombreRuta)
    {
        String sql = "insert into rutas(imagen,altura,dificultad,punto1,punto2,punto3,punto4,nombre,zona_id) " +
                "values('"+imagen+"',"+altura+",'"+dificultad+"',"+p1+","+p2+","+p3+","+p4+",'"+nombreRuta+"'," +
                " (select _id from zonas where nombre='"+zona+"' order by _id limit 1))";

        db.execSQL(sql);

    }

    public void agregarParque(String nombre, String pais, double latitude, double longitude)
    {
        ContentValues cv = new ContentValues();
        cv.put(CN_PARQUE_NOMBRE, nombre);
        cv.put(LATITUDE, latitude);
        cv.put(LONGITUDE,longitude);
        try {
            db.insert(TABLA_PARQUES, null, cv);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public String darImagenDeRutaPorNombre(String nombre)
    {
        Cursor cursor = db.query("rutas",new String[]{CN_ID,CN_RUTA_NOMBRE,CN_RUTA_IMAGEN},CN_RUTA_NOMBRE+"=?",new String[]{nombre},null,null,null,"1");
        if( cursor.moveToFirst( ) )
        {
            return cursor.getString(cursor.getColumnIndex(CN_RUTA_IMAGEN));
        }

        return "";
    }



    public void removeAll()
    {
        helper.removeAll(db);
    }

    public void createAll()
    {
        helper.createAll(db);
    }





}
