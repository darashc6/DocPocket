package es.losinutiles.docpocket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ActividadCamara extends AppCompatActivity {
    private String[] permisosCamara;
    private Uri uri_imagen;
    private static int camaraIDPermision;
    private static int cogerImagenCamaraID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permisosCamara=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        uri_imagen=null;
        camaraIDPermision=300;
        cogerImagenCamaraID=1000;
    }

    /**
     * Función de tipo boolean que retorna si tenemos permisos para la camara y el almacenamiento
     * Retorna true si lo tenemos, false si no
     * Para conseguir una imagen de alta calidad, tendiramos que guardar la imagen al almacenamiento externo, para ello el requisito de su permision
     */
    public boolean comprobarPermisosCamara() {
        boolean permisoCamara=ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)  == PackageManager.PERMISSION_GRANTED;
        boolean permisoAlmacentamiento=ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED;

        return permisoCamara&&permisoAlmacentamiento;
    }

    /**
     * Función que pide los permisos necesarios para llevar a cabo la actividad de la camara
     */
    public void pedirPermisosCamara() {
        ActivityCompat.requestPermissions(this, permisosCamara, camaraIDPermision);
    }

    /**
     * Funcion que realiza una función u otra dependiendo de las permisiones dadas por el usuario
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == camaraIDPermision) {
            if (grantResults.length>0) {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    // TODO intentCamara();
                } else {
                    Toast.makeText(this, "Permiso DENEGADO. Por favor, proporcionanos con los permisos necesarios", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Función que dependiendo de los permisos dados, activa el intent de la camara o no
     */
    public void activarCamara() {
        if (!comprobarPermisosCamara()) {
            // Si no tenemos los permisos necesarios para llevar a cabo la actividad, se lo pedimos
            pedirPermisosCamara();
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)||!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // TODO Si pulsa el botón de nunca mostrar más el dialogo, hacer Toast/AlertDialog
            }
        } else {
            // Si tenemos los permisos necesarios para llevar a cabo la camara, hacemos un intent de la camara
            // TODO intentCamara();
        }
    }

    /**
     * Función que hace un intent para coger la imagen de la camara
     * También será guardada en el almacenamiento externo, para una imagen con mayor calidad
     */
    public void intentCamara() {
        ContentValues cv=new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Imagen"); // Título de la imagen
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Imagen a Texto"); // Descripción de la imagen
        uri_imagen=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent camara=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camara.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagen);
        startActivityForResult(camara, cogerImagenCamaraID);
    }

    /**
     * Devuelve el resultado del intent
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Vamos a recibir la imagen de la cámara
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == cogerImagenCamaraID) {
                // Cuando cogemos la imagen de la cámara, le hacemos crop a la imagen
                CropImage.activity(uri_imagen)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        // Conseguimos el imagen cropeado
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult imagenCrop = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri imagenCropUri=imagenCrop.getUri(); // Conseguir la uri de la imagen cropeada
                // Ponemos esa imagen cropeada en imageView
                // imCrop.setImageURI(imagenCropUri); // TODO Poner ImageView

                // Para el reconocimiento de texto, necesitamos convertir esa imagen en drawable bitmap
                // TODO BitmapDrawable bmd = (BitmapDrawable) imCrop.drawable;
                // TODO Bitmap bm = bmd.bitmap;

                // Creamos el reconocedor de texto
                TextRecognizer textRecognizer = new  TextRecognizer.Builder(getApplicationContext()).build();

                if (!textRecognizer.isOperational()) { // Comprueba si el detector está en funcionamiento
                    Toast.makeText(this, "textRecoginzer no disponible", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO Frame frameCropeado = Frame.Builder().setBitmap(bm).build();
                    // TODO SparseArray<TextBlock>  items = textRecognizer.detect(frameCropeado);
                    StringBuilder texto=new StringBuilder();
                    // Vamos a conseguir texto hasta que el frame no tenga texto
                    /* TODO for (int i=0; i<items.length; i++) {
                        TextBlock item=items.valueAt(i);
                        texto.append(item.value);
                        texto.append("\n");
                    }*/
                    // TODO Intent pantallaDatos=new Intent(getApplicationContext(), ActivityTexto::class.java);
                    Bundle bundleDatos=new Bundle();
                    bundleDatos.putString("textoReconocido", texto.toString());
                    // TODO pantallaDatos.putExtras(bundleDatos);
                    // TODO startActivity(pantallaDatos);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
