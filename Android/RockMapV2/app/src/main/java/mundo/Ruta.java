package mundo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Pablo Mesa on 07/03/2015.
 */
public class Ruta
{
    private Zona zona;
    private String nombre, imagen, dificultad, tipoEscalada;
    private double numEstrellas, altura;
    private ArrayList<Resultado> resultados;
    private double punto1, punto2, punto3, punto4;



    public Ruta(String nombre, String imagen, String dificultad, double numEstrellas, String tipoEscalada, double altura, Zona zona)
    {
        this.nombre=nombre;
        this.imagen=imagen;
        this.dificultad=dificultad;
        this.numEstrellas=numEstrellas;
        this.tipoEscalada = tipoEscalada;
        this.altura=altura;
        resultados=new ArrayList<Resultado>();
        this.zona=zona;
    }

    public Ruta()
    {
        resultados=new ArrayList<Resultado>();
    }

    //Getters & Setters
    public String getNombre() {
        return nombre;
    }
    public String getImagen() {
        return imagen;
    }
    public String getDificultad() {
        return dificultad;
    }
    public double getNumEstrellas() {
        return numEstrellas;
    }
    public String getTipoEscalada() {
        return tipoEscalada;
    }
    public double getAltura() {
        return altura;
    }
    public ArrayList<Resultado> getResultados() {
        return resultados;
    }
    public Zona getZona() {
        return zona;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
    public void setNumEstrellas(double numEstrellas) {
        this.numEstrellas = numEstrellas;
    }
    public void setTipoEscalada(String tipoEscalada) {
        this.tipoEscalada = tipoEscalada;
    }
    public void setAltura(double altura) {
        this.altura = altura;
    }
    public void setResultados(ArrayList<Resultado> resultados) {
        this.resultados = resultados;
    }

    //Metodos
    public void agregarResultado(Resultado rta)
    {
        resultados.add(rta);
    }
    public String toString()
    {
        return nombre;
    }

    public double getPunto1() {
        return punto1;
    }

    public void setPunto1(double punto1) {
        this.punto1 = punto1;
    }

    public double getPunto2() {
        return punto2;
    }

    public void setPunto2(double punto2) {
        this.punto2 = punto2;
    }

    public double getPunto3() {
        return punto3;
    }

    public void setPunto3(double punto3) {
        this.punto3 = punto3;
    }

    public double getPunto4() {
        return punto4;
    }

    public void setPunto4(double punto4) {
        this.punto4 = punto4;
    }
}
