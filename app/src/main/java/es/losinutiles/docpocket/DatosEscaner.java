package es.losinutiles.docpocket;

/**
 * Esta clase modela el nombre de clase, los dias y la imagen.
 *
 */
public class DatosEscaner {
    private String nombreClase;
    private String dias;
    private int idImagen;

    public DatosEscaner(String nombreClase, String dias, int idImagen) {
        this.nombreClase = nombreClase;
        this.dias = dias;
        this.idImagen = idImagen;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }
}
