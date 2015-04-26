package mundo;

import java.util.ArrayList;

import geometria.Poligono;

/**
 * Created by USER on 08/03/2015.
 */
public class Parque
{
    private String nombre;
    private double popularidad;
    private Poligono poligono;
    private ArrayList<Zona> zonas;

    private double latitude;

    private double longitude;
    //private Ubicacion ubicacion;

    public Parque(String nombre, double popularidad, Poligono poligono)
    {
        this.nombre=nombre;
        this.popularidad=popularidad;
        this.poligono=poligono;
        zonas=new ArrayList<Zona>();
    }

    public Parque()
    {
        zonas = new ArrayList<Zona>();
    }

    //Getters y Setters
    public String getNombre() {
        return nombre;
    }
    public double getPopularidad() {
        return popularidad;
    }
    public Poligono getPoligono() {
        return poligono;
    }
    public ArrayList<Zona> getZonas() {
        return zonas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPopularidad(double popularidad) {
        this.popularidad = popularidad;
    }
    public void setPoligono(Poligono poligono) {
        this.poligono = poligono;
    }
    public void setZonas(ArrayList<Zona> zonas) {
        this.zonas = zonas;
    }

    //Metodos
    public void agregarZona(Zona nueva)
    {
        zonas.add(nueva);
    }
    public String toString(){return nombre;}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
