package mundo;

/**
 * Created by Pablo Mesa on 07/03/2015.
 */
public class Ubicacion {

    public String depto;

    public String pais;

    public Ubicacion(String depto, String pais) {
        this.depto = depto;
        this.pais = pais;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
