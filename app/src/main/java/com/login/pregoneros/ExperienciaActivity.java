package com.login.pregoneros;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ExperienciaActivity extends AppCompatActivity {

    Spinner spinnerYouBuy;

    ImageView imageDefault;

    VideoView videoDefault;

    Button botonImagen, botonVideo, botonAudio, boton2Audio, btn_enviarExperiencia, audioDefault, audio2Default;

    EditText comments, why;

    TextView nombre_codigo;

    //TextView TViewNombreApellido,TViewTelCelular,TViewEdad,TViewSexo,TViewDeDone,TViewInteresado,TViewPorque,TViewComentarios;

    LinearLayout linearVideo, linearAudio, linearImagen;

    String api_token;

    int numberFIleAudio;

    int youBuy = 0;

    //Ruta para guardar imagen desde celular
    private final String CARPETA_RAIZ = "misImagenesPregoneros/";

    private final String RUTA_IMAGEN = CARPETA_RAIZ+"misFotos";

    private final String RUTA_VIDEO = CARPETA_RAIZ+"misVideos";

    private final String RUTA_AUDIO = CARPETA_RAIZ+"misAudios";

    MediaRecorder grabacion;
    MediaRecorder grabacion2;

    //Guardar ruta de imagen (almacenamiento)
    String path;

    //Guardar video
    String pathVideo;

    //guardar audio
    String pathAudio;
    String pathAudio2;

    Uri audioUriGaleria;
    Uri audioUriGaleria2;

    //para reproduccion u manipulacion de audio
    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayer2;

    String imagenString = "null";
    String fileString = "null";
    String videoString = "null";
    String audioString = "null";
    String audio2String = "null";


    //codigos para diferenciar si se carga una imagen desde galeria o si es tomada desde camara
    private final int COD_GALERIA = 10;
    private final int COD_CAMARA = 20;
    private final int COD_FILE = 30;
    private final int COD_VIDEO = 40;
    private final int COD_GALERIA_VIDEO = 50;
    private final int COD_GALERIA_AUDIO = 60;
    private final int COD_AUDIO = 70;

    int estadoGrabacion1 = 0;
    int estadoGrabacion2 = 0;

    private final int ESTADO_SIN_INICIAR = 0;
    private final int ESTADO_PREPARADO_PARA_GRABAR = 1;
    private final int ESTADO_GRABANDO = 2;
    private final int ESTADO_LISTO_PARA_REPRODUCIR = 3;
    private final int ESTADO_REPRODUCIENDO = 4;
    private final int ESTADO_PAUSADO = 5;

    ProgressDialog dialog;

    //datos de pregon, cliente, campaña
    int pregonId;
    int campaingId;
    int clientId;
    int idUser;


    //para validar que campos multimedia mostrar
    String mostrarAudioCamp;
    String mostrarFotoCamp;
    String mostrarVideoCamp;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {

            Intent intent = new Intent(ExperienciaActivity.this, MenuInicial.class);
            ExperienciaActivity.this.startActivity(intent);
            return true;
        }
//        if (id == R.id.action_send) {
//
//            // Do something
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia);


        //datos desde actividad anterior
        Intent intent = getIntent();
        pregonId = intent.getIntExtra("id_pregon",0);
        campaingId = intent.getIntExtra("id_campaign",0);
        clientId = intent.getIntExtra("id_cliente",0);
        final String nombre_empresa = intent.getStringExtra("nombre_empresa");
        final String codigo_pregon = intent.getStringExtra("codigo_pregon");


        nombre_codigo = (TextView) findViewById(R.id.name_code);
        nombre_codigo.setText(nombre_empresa/*+" - "+codigo_pregon*/);

        imageDefault = (ImageView) findViewById(R.id.imageDefault);
        botonImagen = (Button) findViewById(R.id.addImage);

        videoDefault = (VideoView) findViewById(R.id.videoDefault);
        botonVideo = (Button) findViewById(R.id.addVideo);

        audioDefault = (Button) findViewById(R.id.audioDefault);
        //audio2Default = (Button) findViewById(R.id.audio2Default);

        botonAudio = (Button) findViewById(R.id.addAudio);
        //boton2Audio = (Button) findViewById(R.id.addAudio2);

        btn_enviarExperiencia = (Button) findViewById(R.id.btn_enviarExperiencia);

        comments = (EditText) findViewById(R.id.comments);
        why = (EditText) findViewById(R.id.why);

        linearAudio = (LinearLayout) findViewById(R.id.multimediaAudio);
        linearImagen = (LinearLayout) findViewById(R.id.multimediaImagen);
        linearVideo = (LinearLayout) findViewById(R.id.multimediaVideo);

        spinnerYouBuy = (Spinner) findViewById(R.id.spinnerYouBuy);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.intereses_spinner, android.R.layout.simple_spinner_item);
        spinnerYouBuy.setAdapter(adapter);


        //tomando datos de session

        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        final String nameSessio = sp.getString("username",null);
        String emailSession = sp.getString("email", null);
        api_token = sp.getString("api_token", null);
        idUser = sp.getInt("id", -1);


        if (validaPermisos())
        {
            botonImagen.setEnabled(true);
//            botonVideo.setEnabled(true);
//            botonAudio.setEnabled(true);
//            boton2Audio.setEnabled(true);
            btn_enviarExperiencia.setEnabled(true);
        }
        else
        {
            Toast errorPermisionMessage = Toast.makeText(getApplicationContext(),"Para que la aplicacion funcione necesitamos que aceptes todos los permisos", Toast.LENGTH_LONG);
            errorPermisionMessage.show();

            botonImagen.setEnabled(false);
//            botonVideo.setEnabled(false);
//            botonAudio.setEnabled(false);
//            boton2Audio.setEnabled(false);
            btn_enviarExperiencia.setEnabled(false);
        }


        //mostrandoCamposAdjuntoPorPregon();

        spinnerYouBuy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(getApplicationContext(), "Selecciono "+parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();

                if (parent.getItemAtPosition(position).toString().equals("Si"))
                    youBuy = 1;

                if (parent.getItemAtPosition(position).toString().equals("No"))
                    youBuy = 0;

                if (!parent.getItemAtPosition(position).toString().equals("Si") && !parent.getItemAtPosition(position).toString().equals("Np"))
                    youBuy = 5;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //Al darclick en enviar
        btn_enviarExperiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(ExperienciaActivity.this);
                dialog.setTitle("Enviando tu experiencia");
                dialog.setMessage("Cargando...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(false);

                //Capturando data para consumir web Service

                final String pregonIdR = pregonId+"";
                final String campaingIdR = campaingId+"";
                final String clientIdR = clientId+"";
                final String userIdR = Integer.toString(idUser);
                final String whyR = why.getText().toString();
                final String commentsR = comments.getText().toString();
                final String youBuyR = Integer.toString(youBuy);
                final String imageR = imagenString;
                final String videoR = videoString;
                final String audio1R = audioString;
                final String audio2R = audio2String;


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            String code = jsonResponse.getString("code");

                            if (code.equals("201"))
                            {
                                //Mostrando mensaje de exito
                                dialog.dismiss();
                                Toast alertMessage = Toast.makeText(getApplicationContext(),"Experiencia enviada con exito Pronto la revisaremos...", Toast.LENGTH_LONG);
                                alertMessage.setGravity(Gravity.CENTER, 0, 0);
                                alertMessage.show();

                                // Redirigiendo a pagina de pregon
                                Intent intentReturns = new Intent(ExperienciaActivity.this, PregonActivity.class);
                                intentReturns.putExtra("api_token", api_token);
                                intentReturns.putExtra("id_pregon", pregonId);
                                intentReturns.putExtra("id_campaign", campaingId);
                                intentReturns.putExtra("id_cliente", clientId);
                                intentReturns.putExtra("nombre_empresa", nombre_empresa);
                                intentReturns.putExtra("codigo_pregon", codigo_pregon);
                                ExperienciaActivity.this.startActivity(intentReturns);

                            }

                            if (code.equals("400"))
                            {
                                //Mostrando errores en pantalla
                                dialog.dismiss();
                                String error = jsonResponse.getString("error");

                                Toast alertMessage = Toast.makeText(getApplicationContext(),error, Toast.LENGTH_LONG);
                                alertMessage.setGravity(Gravity.CENTER, 0, 0);
                                alertMessage.show();
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                };

                //en caso de error
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error en el servidor al enviar experiencia", Toast.LENGTH_LONG).show();
                    }
                };

                ExperienciaRequest registerrequest = new ExperienciaRequest(api_token, pregonIdR, campaingIdR, clientIdR, userIdR, nameSessio,whyR, commentsR, youBuyR,
                        imageR, videoR,audio1R,audio2R, responseListener, errorListener);

                RequestQueue queue = Volley.newRequestQueue(ExperienciaActivity.this);

                queue.add(registerrequest);


            }
        });


    }



    public boolean validaPermisos ()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)  && (checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED) && (
                checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED   ))
            return true;


        if ((shouldShowRequestPermissionRationale(CAMERA))  ||  (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(RECORD_AUDIO))
                || ((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) && (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) ))
            cargarDIalogoRecomendacion();
        else
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,RECORD_AUDIO,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);


        return false;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100)
        {
            if (grantResults.length == 5 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[2]==PackageManager.PERMISSION_GRANTED && (grantResults[3]==PackageManager.PERMISSION_GRANTED || grantResults[4]==PackageManager.PERMISSION_GRANTED))
            {
                botonImagen.setEnabled(true);
//                botonImagen.setEnabled(true);
//                botonVideo.setEnabled(true);
//                botonAudio.setEnabled(true);
//                boton2Audio.setEnabled(true);
                btn_enviarExperiencia.setEnabled(true);


            }
            else
            {
                solicitarPermisosManual();
            }
        }

//        if (requestCode == 1000)
//        {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                locationStart();
//                return;
//            }
//            else
//            {
//                try {
//                    Thread.sleep (5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                pidePermisosGeoLocalizacion();
//            }
//        }
    }

    public void solicitarPermisosManual ()
    {
        final CharSequence[] opciones = {"SI","NO"};

        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ExperienciaActivity.this);
        alertOpciones.setTitle("¿Prefiere dar permisos de forma manual?");

        //Seteando las opciones
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("SI"))
                {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                else
                {
                    Toast alertPermisos = Toast.makeText(getApplication(),"Necesitamos dichos permisos para el funcionamiento de la aplicacion",Toast.LENGTH_SHORT);
                    alertPermisos.setGravity(Gravity.CENTER, 0, 0);
                    alertPermisos.show();
                    dialog.dismiss();

                    goToPregonesActivos();
                }
            }
        });

        alertOpciones.show();
    }

    //Se le dice al usuario que tiene que habilitar permisos para poder usar la aplicacion
    public void cargarDIalogoRecomendacion ()
    {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ExperienciaActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la aplicacion");
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,RECORD_AUDIO,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
            }
        });

        dialogo.show();
    }

    public void uploadImageDefault (View view)
    {
        cargarImagen();
    }

    public  void uploadVideoDefault (View view)
    {
        cargarVideo();
    }

    public void uploadAudio1 (View view)
    {
        cargarAudio(1);
    }

    public void uploadAudio2 (View view)
    {
        cargarAudio(2);
    }

    public void actionAudio1(View view)
    {
        if (estadoGrabacion1 == ESTADO_PREPARADO_PARA_GRABAR && grabacion == null)
        {
            audioUriGaleria = null;
            //Abre la imagen
            File fileAudio = new File(Environment.getExternalStorageDirectory(), RUTA_AUDIO);

            String nombre_audio = "";

            //valida si la imagen se crecorrectamente
            boolean isCreada = fileAudio.exists();


            if (!isCreada)
            {
                isCreada = fileAudio.mkdirs();
            }

            if (isCreada)
            {
                nombre_audio = (System.currentTimeMillis()/1000)+".mp3";
            }


            //ruta de almacenamiento
            pathAudio = Environment.getExternalStorageDirectory()+ File.separator+RUTA_AUDIO+File.separator+nombre_audio;

            //Creando archivo
            File audio = new File(pathAudio);

            grabacion = new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            {
                grabacion.setOutputFile(audio);
            }
            else
            {
                grabacion.setOutputFile(pathAudio);
            }

            try
            {
                grabacion.prepare();
                grabacion.start();
            }
            catch (IOException e){}

            //cambio de apariencia y de estado de la grabacion
            audioDefault.setBackgroundResource(R.drawable.pause);
            estadoGrabacion1 = ESTADO_GRABANDO;
            Toast.makeText(getApplicationContext(),"Grabando...",Toast.LENGTH_SHORT).show();

        }
        else if (estadoGrabacion1 == ESTADO_GRABANDO && grabacion != null) //si se encuantra grabando
        {
            grabacion.stop();
            grabacion.release();
            grabacion = null;
            audioDefault.setBackgroundResource(R.drawable.play);
            estadoGrabacion1 = ESTADO_LISTO_PARA_REPRODUCIR;
            audioUriGaleria = Uri.parse(pathAudio);
            audioString =  encodeMp3file(audioUriGaleria);
            Toast.makeText(getApplicationContext(),"Grabacion finalizada.",Toast.LENGTH_SHORT).show();

        }
        else if (estadoGrabacion1 == ESTADO_LISTO_PARA_REPRODUCIR || estadoGrabacion1 == ESTADO_REPRODUCIENDO || estadoGrabacion1 == ESTADO_PAUSADO)
        {
            //if (audioUriGaleria == null)
            //{
            //  audioUriGaleria = Uri.parse(pathAudio);
            //}



            if (estadoGrabacion1 == ESTADO_LISTO_PARA_REPRODUCIR)
            {
                mediaPlayer = MediaPlayer.create(this, audioUriGaleria);
                mediaPlayer.start();
                Toast.makeText(getApplicationContext(),"Reproduciendo audio...",Toast.LENGTH_SHORT).show();
                //estadoGrabacion1 = ESTADO_LISTO_PARA_REPRODUCIR;
                estadoGrabacion1 = ESTADO_REPRODUCIENDO;
                return;
            }


            /*Reproduciendo con path en string
             *
             * MediaPlayer mediaplayer = new MediaPlayer();
             * try{
             *   mediaplayer.setDataSource(pathAudio);
             *   mediaplayer.prepare();
             * }catch(IOException e){}
             * mediaplayer.start();
             * */

            if (estadoGrabacion1 == ESTADO_REPRODUCIENDO)
            {
                mediaPlayer.pause();
                Toast.makeText(getApplicationContext(),"audio pausado...",Toast.LENGTH_SHORT).show();
                estadoGrabacion1 = ESTADO_PAUSADO;
                return;
            }

            if (estadoGrabacion1 == ESTADO_PAUSADO)
            {
                mediaPlayer.start();
                Toast.makeText(getApplicationContext(),"Reproduciendo audio...",Toast.LENGTH_SHORT).show();
                estadoGrabacion1 = ESTADO_REPRODUCIENDO;
            }

            //estadoGrabacion1 = ESTADO_LISTO_PARA_REPRODUCIR;
        }
        else
            Toast.makeText(getApplicationContext(),"Error en el flujo de grabacion, porfavor contactecon los administradores",Toast.LENGTH_SHORT).show();

    }

    public void actionAudio2(View view)
    {
        if (estadoGrabacion2 == ESTADO_PREPARADO_PARA_GRABAR && grabacion2 == null)
        {
            audioUriGaleria2 = null;
            //Abre la imagen
            File fileAudio = new File(Environment.getExternalStorageDirectory(), RUTA_AUDIO);

            String nombre_audio = "";

            //valida si la imagen se crecorrectamente
            boolean isCreada = fileAudio.exists();


            if (!isCreada)
            {
                isCreada = fileAudio.mkdirs();
            }

            if (isCreada)
            {
                nombre_audio = (System.currentTimeMillis()/1000)+".mp3";
            }


            //ruta de almacenamiento
            pathAudio2 = Environment.getExternalStorageDirectory()+ File.separator+RUTA_AUDIO+File.separator+nombre_audio;

            //Creando archivo
            File audio = new File(pathAudio2);

            grabacion2 = new MediaRecorder();
            grabacion2.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion2.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabacion2.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            {
                grabacion2.setOutputFile(audio);
            }
            else
            {
                grabacion2.setOutputFile(pathAudio2);
            }

            try
            {
                grabacion2.prepare();
                grabacion2.start();
            }
            catch (IOException e){}

            //cambio de apariencia y de estado de la grabacion
            audio2Default.setBackgroundResource(R.drawable.pause);
            estadoGrabacion2 = ESTADO_GRABANDO;
            Toast.makeText(getApplicationContext(),"Grabando...",Toast.LENGTH_SHORT).show();

        }
        else if (estadoGrabacion2 == ESTADO_GRABANDO && grabacion2 != null) //si se encuantra grabando
        {
            grabacion2.stop();
            grabacion2.release();
            grabacion2 = null;
            audio2Default.setBackgroundResource(R.drawable.play);
            estadoGrabacion2 = ESTADO_LISTO_PARA_REPRODUCIR;
            audioUriGaleria2 = Uri.parse(pathAudio2);
            audio2String =  encodeMp3file(audioUriGaleria2);
            Toast.makeText(getApplicationContext(),"Grabacion finalizada.",Toast.LENGTH_SHORT).show();

        }
        else if (estadoGrabacion2 == ESTADO_LISTO_PARA_REPRODUCIR || estadoGrabacion2 == ESTADO_REPRODUCIENDO || estadoGrabacion2 == ESTADO_PAUSADO)
        {
            if (audioUriGaleria2 == null)
            {
                audioUriGaleria2 = Uri.parse(pathAudio2);
            }

            if (estadoGrabacion2 == ESTADO_LISTO_PARA_REPRODUCIR)
            {
                mediaPlayer2 = MediaPlayer.create(this, audioUriGaleria2);
                mediaPlayer2.start();
                Toast.makeText(getApplicationContext(),"Reproduciendo audio...",Toast.LENGTH_SHORT).show();
                //estadoGrabacion1 = ESTADO_LISTO_PARA_REPRODUCIR;
                estadoGrabacion2 = ESTADO_REPRODUCIENDO;
                return;
            }


            /*Reproduciendo con path en string
             *
             * MediaPlayer mediaplayer = new MediaPlayer();
             * try{
             *   mediaplayer.setDataSource(pathAudio);
             *   mediaplayer.prepare();
             * }catch(IOException e){}
             * mediaplayer.start();
             * */

            if (estadoGrabacion2 == ESTADO_REPRODUCIENDO)
            {
                mediaPlayer2.pause();
                Toast.makeText(getApplicationContext(),"audio pausado...",Toast.LENGTH_SHORT).show();
                estadoGrabacion2 = ESTADO_PAUSADO;
                return;
            }

            if (estadoGrabacion2 == ESTADO_PAUSADO)
            {
                mediaPlayer2.start();
                Toast.makeText(getApplicationContext(),"Reproduciendo audio...",Toast.LENGTH_SHORT).show();
                estadoGrabacion2 = ESTADO_REPRODUCIENDO;
            }

            //estadoGrabacion1 = ESTADO_LISTO_PARA_REPRODUCIR;
        }
        else
            Toast.makeText(getApplicationContext(),"Error en el flujo de grabacion, porfavor contactecon los administradores",Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("IntentReset")
    public void cargarImagen()
    {

        final CharSequence[] opciones = {"Tomar foto", "Cargar imagen","Cancelar"};

        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ExperienciaActivity.this);
        alertOpciones.setTitle("seleccione una opcion");

        //Seteando las opciones
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Tomar foto"))
                {
                    //Toast.makeText(getApplication(),"Tomar foto",Toast.LENGTH_SHORT).show();

                    tomarFotografia();
                }
                else if (opciones[which].equals("Cargar imagen"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //ACTION_PICK O ACTION_GET_CONTENT

                    intent.setType("image/");

                    startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"),COD_GALERIA); // va al metodo onActivityResult
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        alertOpciones.show();

    }

    @SuppressLint("IntentReset")
    public void cargarVideo()
    {
        final CharSequence[] opciones = {"Grabar", "Cargar video","Cancelar"};

        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ExperienciaActivity.this);
        alertOpciones.setTitle("seleccione una opcion");

        //Seteando las opciones
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Grabar"))
                {
                    grabarVideo();
                }
                else if (opciones[which].equals("Cargar video"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI); //ACTION_PICK O ACTION_GET_CONTENT

                    intent.setType("video/");

                    startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"),COD_GALERIA_VIDEO); // va al metodo onActivityResult
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        alertOpciones.show();
    }

    @SuppressLint("IntentReset")
    public void cargarAudio(final int numberFile)
    {
        numberFIleAudio = numberFile;

        final CharSequence[] opciones = {"Grabar","Cargar audio","cancelar"};

        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ExperienciaActivity.this);
        alertOpciones.setTitle("Seleccioneuna opcion");

        //seteando las opciones
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Grabar"))
                {
                    cambioEstadoGrabacion();
                }
                else if (opciones[which].equals("Cargar audio"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI); //ACTION_PICK O ACTION_GET_CONTENT

                    intent.setType("audio/");

                    startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"),COD_GALERIA_AUDIO); // va al metodo onActivityResult
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        alertOpciones.show();
    }

    public void cambioEstadoGrabacion ()
    {
        if(grabacion == null) //no hay pistas de audio en ejecucion
        {
            if (numberFIleAudio == 1)
            {
                audioDefault.setBackgroundResource(R.drawable.start);
                estadoGrabacion1 = ESTADO_PREPARADO_PARA_GRABAR;
            }

            if (numberFIleAudio == 2)
            {
                audio2Default.setBackgroundResource(R.drawable.start);
                estadoGrabacion2 = ESTADO_PREPARADO_PARA_GRABAR;
            }
        }
        else
            Toast.makeText(getApplication(),"Hay una grabacion en curso, porfavor terminala para poder empezar una nueva",Toast.LENGTH_SHORT).show();

    }

    public void tomarFotografia()
    {
        //Abre la imagen
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);

        String nombre_imagen = "";

        //valida si la imagen se crecorrectamente
        boolean isCreada = fileImagen.exists();


        if (!isCreada)
        {
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada)
        {
            nombre_imagen = (System.currentTimeMillis()/1000)+".jpg";
        }


        //ruta de almacenamiento
        path = Environment.getExternalStorageDirectory()+ File.separator+RUTA_IMAGEN+File.separator+nombre_imagen;

        //Creando archivo
        File imagen = new File(path);

        //HERRAMIENTA DE DIAGNOSTIICO (Modo desarrollo)
        //StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //StrictMode.setVmPolicy(builder.build());

        //Lanzar la aplicacion de camara
        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ////
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_CAMARA);

        ////

        //enviarimagen y almacenarla
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));


        //startActivityForResult(intent,COD_CAMARA); // va al metodo onActivityResult


    }

    public void grabarVideo()
    {
        //Abre la imagen
        File fileVideo = new File(Environment.getExternalStorageDirectory(), RUTA_VIDEO);

        String nombre_video = "";

        //valida si la imagen se crecorrectamente
        boolean isCreada = fileVideo.exists();


        if (!isCreada)
        {
            isCreada = fileVideo.mkdirs();
        }

        if (isCreada)
        {
            nombre_video = (System.currentTimeMillis()/1000)+".mp4";
        }


        //ruta de almacenamiento
        pathVideo = Environment.getExternalStorageDirectory()+ File.separator+RUTA_VIDEO+File.separator+nombre_video;

        //Creando archivo
        File video = new File(pathVideo);


        //Lanzar la aplicacion de camara
        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            //startActivityForResult(intent,COD_VIDEO); // va al metodo onActivityResult


            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=getApplicationContext().getPackageName()+".provider";
                Uri videoUri= FileProvider.getUriForFile(this,authorities,video);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            }
            else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(video));
            }
        }



        startActivityForResult(intent,COD_VIDEO); // va al metodo onActivityResult

    }

    //Mostrando imagen en ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Bitmap bitmap;
            Bitmap bitmapVideo;

            switch (requestCode)
            {
                case COD_GALERIA:
                    Uri miPath = data.getData();
                    imageDefault.setImageURI(miPath);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath);
                        bitmap = redimencionarImagen(bitmap, 600,800);
                        imageDefault.setImageBitmap(bitmap);
                        imagenString = convertFileToString(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case COD_GALERIA_VIDEO:

                    Uri videoUriGaleria = data.getData();

                    pathVideo = getRealPathFromURI(ExperienciaActivity.this, videoUriGaleria);
                    videoUriGaleria = Uri.parse(pathVideo);

                    if (videoUriGaleria != null)
                    {
                        videoDefault.setVideoURI(videoUriGaleria);
                        videoDefault.start();
                        //videoString = convertFileToBase64String(videoUriGaleria);
                        videoString = encodeMp3file(videoUriGaleria);
                    }

                    break;

                case COD_GALERIA_AUDIO:


                    if (numberFIleAudio == 1)
                    {
                        audioUriGaleria = data.getData();
                        pathAudio = getRealPathFromURI(ExperienciaActivity.this,audioUriGaleria);
                        audioUriGaleria = Uri.parse(pathAudio);

                        if (audioUriGaleria != null)
                        {
                            audioDefault.setBackgroundResource(R.drawable.play);
                            estadoGrabacion1 = ESTADO_LISTO_PARA_REPRODUCIR;
                            Toast.makeText(getApplicationContext(),"audio cargado...",Toast.LENGTH_SHORT).show();
                            //MediaPlayer mediaPlayer = MediaPlayer.create(this, audioUriGaleria);
                            //mediaPlayer.start();
                            audioString =  encodeMp3file(audioUriGaleria);

                        }

                    }

                    if (numberFIleAudio == 2)
                    {
                        audioUriGaleria2 = data.getData();
                        pathAudio2 = getRealPathFromURI(ExperienciaActivity.this,audioUriGaleria2);
                        audioUriGaleria2 = Uri.parse(pathAudio2);

                        if (audioUriGaleria2 != null)
                        {
                            audio2Default.setBackgroundResource(R.drawable.play);
                            estadoGrabacion2 = ESTADO_LISTO_PARA_REPRODUCIR;
                            Toast.makeText(getApplicationContext(),"audio cargado...",Toast.LENGTH_SHORT).show();
                            //MediaPlayer mediaPlayer = MediaPlayer.create(this, audioUriGaleria2);
                            //mediaPlayer.start();
                            audio2String =  encodeMp3file(audioUriGaleria2);

                        }
                    }

                    break;

                case  COD_CAMARA:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override //si el proceso termino completamente
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Ruta de almacenamiento","Path: "+path);
                        }
                    });

                    bitmap = BitmapFactory.decodeFile(path);
                    bitmap = redimencionarImagen(bitmap, 600,800);

                    imageDefault.setImageBitmap(bitmap);

                    imagenString = convertFileToString(bitmap);

                    break;

                case  COD_VIDEO:

                    MediaScannerConnection.scanFile(this, new String[]{pathVideo}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override //si el proceso termino completamente
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Ruta de almacenamiento","Path: "+pathVideo);
                        }
                    });

                    //bitmap = BitmapFactory.decodeFile(pathVideo);

                    //videoString = convertFileToString(bitmap);

                    Uri videoUri = data.getData();


                    videoDefault.setVideoURI(videoUri);
                    videoDefault.start();

                    pathVideo = getRealPathFromURI(ExperienciaActivity.this,videoUri);
                    videoUri = Uri.parse(pathVideo);

                    videoString = encodeMp3file(videoUri);


                    break;

                default:
                    Toast.makeText(getApplication(),"Otro contactese con servicio tecnico",Toast.LENGTH_SHORT).show();


            }

        }
        else
        {
            Toast.makeText(getApplication(),"Error en la aplicacion porfavor contacte al servicio tecnico",Toast.LENGTH_SHORT).show();
        }
    }

    public String convertFileToString (Bitmap bitmapFile)
    {
        String fileString = "";

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmapFile.compress(Bitmap.CompressFormat.JPEG,100,array);

        byte[] fileByte = array.toByteArray();
        fileString = Base64.encodeToString(fileByte,Base64.DEFAULT);

        return fileString;
    }

    public String encodeMp3file (Uri uri)
    {
        File file = new File(uri.getPath());
        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(bytes, 0);
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            //Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void goToPregonesActivos ()
    {
        Intent pregonesActivosActivity = new Intent(ExperienciaActivity.this, PregonesActivosActivity.class);
        ExperienciaActivity.this.startActivity(pregonesActivosActivity);
    }


//    public void mostrandoCamposAdjuntoPorPregon ()
//    {
//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try
//                {
//                    JSONObject jsonResponse = new JSONObject(response);
//
//                    String code = jsonResponse.getString("code");
//
//                    if (code.equals("200"))
//                    {
//                        JSONObject data = jsonResponse.getJSONObject("data");
//
//                        mostrarAudioCamp = data.getString("audio");
//                        mostrarFotoCamp = data.getString("foto");
//                        mostrarVideoCamp = data.getString("video");
//
//                        if (mostrarAudioCamp.equals("false"))
//                            linearAudio.setVisibility(View.GONE);
//                        if (mostrarFotoCamp.equals("false"))
//                            linearImagen.setVisibility(View.GONE);
//                        if (mostrarVideoCamp.equals("false"))
//                            linearVideo.setVisibility(View.GONE);
//
//                    }
//
//                    if (code.equals("400"))
//                    {
//                        //Mostrando errores en pantalla
//                        dialog.dismiss();
//                        String error = jsonResponse.getString("error");
//
//                        Toast alertMessage = Toast.makeText(getApplicationContext(),error, Toast.LENGTH_LONG);
//                        alertMessage.setGravity(Gravity.CENTER, 0, 0);
//                        alertMessage.show();
//                    }
//
//                }
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//
//        MultimediaChecklistRequest registerrequest = new MultimediaChecklistRequest(api_token,pregonId,responseListener);
//
//        RequestQueue queue = Volley.newRequestQueue(ExperienciaActivity.this);
//
//        queue.add(registerrequest);
//    }


    public Bitmap redimencionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo)
    {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if (ancho >anchoNuevo || alto>altoNuevo)
        {
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto =altoNuevo/alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);
        }

        return bitmap;

    }


}
