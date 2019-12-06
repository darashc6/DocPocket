package es.losinutiles.docpocket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOError;
import java.util.ArrayList;

public class ActividadCamara extends AppCompatActivity {
    private Bundle bundle; // Bundle para devolver los datos de otra actividad
    private String[] permisosCamara; // Permisos necesarios para la cámara
    private String[] palabras; // Almacena las palabras escaneadas por texto. Los divide de 1 en 1 utilizando split
    private ArrayList<String> palabrasEscaneadas; // Palabra que se va a utlizar para buscar documentación sobre ella
    private Uri uri_imagen; // Uri de la imagen
    private LinearLayout datosEscaneados; // Layout donde se almacena todos los datos de lo escaneado (texto, lenguaje elegido)
    private ImageView imagenCamara; // Imagen de la camara
    private TextView textoEscaneado; // Texto escaneado a partir de la imagen de la cámara
    private TextView lenguajeElegido; // Texto con el lenguaje que ha elegido el usuario
    private static int camaraIDPermision=300; // Código para los permisos de la cámara
    private static int cogerImagenCamaraID=300; // Código para los permisos de recortar el imagen
    private DatabaseReference referencia; // Base de datos de referencia

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_actividad_camara);

        permisosCamara=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        uri_imagen=null;
        palabras=null;
        palabrasEscaneadas=new ArrayList<String>();
        datosEscaneados=findViewById(R.id.datosEscaneado);
        imagenCamara=findViewById(R.id.imagenCamara);
        textoEscaneado=findViewById(R.id.textoEscaneado);
        lenguajeElegido=findViewById(R.id.lenguajeElegido);
        datosEscaneados.setVisibility(View.INVISIBLE);

        bundle=getIntent().getExtras();
        lenguajeElegido.setText("Ha elegido el lenguaje "+bundle.getString("lenguajeElegido"));

        activarCamara();
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
     * @param requestCode Código del permiso que solicitamos
     * @param permissions Permisos que solicitamos
     * @param grantResults Resultado del diálogo de los permisos
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == camaraIDPermision) {
            if (grantResults.length>0) {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                    intentCamara();
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
            intentCamara();
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
     * @param requestCode Código del permiso que hemos solicitado
     * @param resultCode Código del resultado de la actividad
     * @param data No nos hace falta para este caso
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
                imagenCamara.setImageURI(imagenCropUri);

                // Para el reconocimiento de texto, necesitamos convertir esa imagen en drawable bitmap
                BitmapDrawable bmd = (BitmapDrawable) imagenCamara.getDrawable();
                Bitmap bm = bmd.getBitmap();

                // Creamos el reconocedor de texto
                TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

                if (!textRecognizer.isOperational()) { // Comprueba si el detector está en funcionamiento
                    Toast.makeText(getApplicationContext(), "textRecoginzer no disponible", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frameCropeado = new Frame.Builder().setBitmap(bm).build();
                    SparseArray<TextBlock>  items = textRecognizer.detect(frameCropeado);
                    final StringBuilder texto=new StringBuilder();
                    // Vamos a conseguir texto hasta que el frame no tenga texto

                    for (int i=0; i<items.size(); i++) {
                        TextBlock item=items.valueAt(i);
                        texto.append(item.getValue());
                        texto.append(" \n");
                        //Creo un array de String en el que se guardan las palabras de la cadena de texto
                        //por separadas para poder realizar la consulta en firebase
                    }
                    textoEscaneado.setText(texto);
                    palabras=texto.toString().toLowerCase().split(" ");
                    try{
                        referencia= FirebaseDatabase.getInstance().getReference();
                        referencia.child("DocumentacionJava").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()){
                                    //Implementado las consultas.
                                    //Hay que rellenar la base de datos con las clases y su respectivo valor en minuscula
                                    //Quitar filewriter del child y sustituirlo por el string que surge de la camara.
                                    //Itero sobre array para sacar cada palabra y buscarla en firebase

                                    for(int i=0;i<palabras.length;i++){
                                        Log.d("Datos","Esto es el length de palabras dentro de la funcion \n"+palabras.length+
                                                "ahora voy a buscar la palabra "+palabras[i]);

                                        DataSnapshot probar=dataSnapshot.child(palabras[i]);
                                        Toast.makeText(getApplicationContext(), probar.toString()+"", Toast.LENGTH_LONG).show();
                                        // Log.d("Datos: ",""+probar.toUpperCase());
                                        //Creo variable string palabraElegida que servirá como palabra clave para las busquedas en las paginas
                                        if (probar.getValue()!=null) {
                                            palabrasEscaneadas.add(probar.getValue().toString());
                                        }
                                    }

                                    Toast.makeText(getApplicationContext(), "Palabras encontradas: "+palabrasEscaneadas.size(), Toast.LENGTH_LONG).show();
                                    if (palabrasEscaneadas.size()>0) {
                                        for (String s:palabrasEscaneadas) {
                                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                            textoEscaneado.setText(textoEscaneado.getText()+s);
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "No se ha encontrado una clase correcta", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } catch(IOError error) {
                        error.getMessage();
                    }

                }
                datosEscaneados.setVisibility(View.VISIBLE);
            } else {
                // Esto se ejecuta en el caso de que en la opcion de recortar la imagen, el usuario quiere volver a la camara
                finish();
                Bundle bundleParaReiniciarActividad=new Bundle();
                Intent reiniciarActividad=new Intent(this, ActividadCamara.class);
                bundleParaReiniciarActividad.putString("lenguajeElegido", bundle.getString("lenguajeElegido"));
                reiniciarActividad.putExtras(bundleParaReiniciarActividad);
                startActivity(reiniciarActividad);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void buscarStackOverFlow(View view) {
        Intent webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://stackoverflow.com/search?q="+palabrasEscaneadas.get(0)));
        try{
            startActivity(webIntent);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this,"Ha ocurrido un error, intentalo de nuevo",Toast.LENGTH_LONG).show();
        }
    }

    public void buscarYoutube(View view) {
        Intent appIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+palabrasEscaneadas));
        Intent webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+palabrasEscaneadas));
        try{
            startActivity(appIntent);
        }catch (ActivityNotFoundException ex){
            startActivity(webIntent);
        }
    }

    public void buscarGoogle(View view) {
        Intent webIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q="+palabrasEscaneadas.get(0)));
        try{
            startActivity(webIntent);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this,"Ha ocurrido un error, intentalo de nuevo",Toast.LENGTH_LONG).show();
        }
    }
}
