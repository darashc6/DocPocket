package es.losinutiles.docpocket;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Esta clase modela el nombre de clase, los dias y la imagen.
 *
 */
public class DatosEscaner {
    private String nombreClase; // Nombre de la clase
    private String dias; // Fecha de la foto escaneada
    private String lenguaje; // Lenguaje de la clase
    private boolean favorito; // Si la clase pertenece a la tabla favoritos

    /**
     *
     * @param nombreClase coge el nombre de la clase
     * @param lenguaje coge el lenguaje de la clase
     */

    public DatosEscaner(String nombreClase, String lenguaje) {
        this.nombreClase = nombreClase;
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        this.dias = df.format(c);;
        this.lenguaje = lenguaje;
        this.favorito=false;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
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

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    @NonNull
    @Override
    public String toString() {
        return "[NombreClase="+nombreClase+", dias="+dias+", lenguaje="+lenguaje+"]";
    }
}
