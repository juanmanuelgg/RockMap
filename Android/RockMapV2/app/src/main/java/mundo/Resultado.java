package mundo;

import java.util.Date;

/**
 * Created by Pablo Mesa on 07/03/2015.
 */
public class Resultado {

    private double tiempo;
    private Date fecha;
    private boolean encadenada;

    public Resultado(double tiempo, Date fecha, boolean encadenada)
    {
        this.tiempo = tiempo;
        this.fecha = fecha;
        this.encadenada=encadenada;
    }

    public double getTiempo() {
        return tiempo;
    }
    public Date getFecha() {
        return fecha;
    }
    public boolean isEncadenada(){
        return encadenada;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public void setEncadenada(boolean encadenada) {
        this.encadenada = encadenada;
    }
}
