package mundo;

import java.util.ArrayList;

import geometria.Point;

/**
 * Created by Pablo Mesa on 07/03/2015.
 */
public class Zona
{
    private final Parque parque;
    private String nombre;
    private Point p1, p2;
    private ArrayList<Ruta> rutas;

    public Zona(String nombre, Point p1, Point p2, Parque parque)
    {
        this.nombre = nombre;
        this.p1=p1;
        this.p2=p2;
        rutas=new ArrayList<Ruta>();
        this.parque=parque;
    }

    public String getNombre() {
        return nombre;
    }
    public Point getP1() {
        return p1;
    }
    public Point getP2() {
        return p2;
    }
    public ArrayList<Ruta> getRutas() {
        return rutas;
    }
    public Parque getParque() {
        return parque;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setP1(Point p1) {
        this.p1 = p1;
    }
    public void setP2(Point p2) {
        this.p2 = p2;
    }
    public void setRutas(ArrayList<Ruta> rutas) {
        this.rutas = rutas;
    }

    //Metodos
    public void agregarUnaRuta(Ruta nueva)
    {
        rutas.add(nueva);
    }

    public String toString()
    {
        return nombre;
    }

}