package es.losinutiles.docpocket;

import androidx.annotation.NonNull;

/**
 * Esta clase modela el nombre de clase, los dias y la imagen.
 *
 */
public class DatosEscaner {
    private String nombreClase;
    private String dias;
    private int idImagen;
    private boolean favorito;

    /**
     *
     * @param nombreClase coge el nombre de la clase
     * @param dias coge los dias que han pasado desde que se hizo la consulta
     * @param idImagen da una imagen u otra
     */

    public DatosEscaner(String nombreClase, String dias, int idImagen) {
        this.nombreClase = nombreClase;
        this.dias = dias;
        this.idImagen = idImagen;
        this.favorito=false;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public DatosEscaner(String nombreClase, String dias) {
        this.nombreClase = nombreClase;
        this.dias = dias;
    }

    public DatosEscaner() {
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

    @NonNull
    @Override
    public String toString() {
        return "[NombreClase="+nombreClase+", dias="+dias+", idImagen="+idImagen+"]";
    }
}
