package com.login.pregoneros;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PregonActivity extends AppCompatActivity {

    String api_token;
    String usernameSession;
    String codigo_redime;
    int notification_id;

    Button upAction;

    TextView companyName;
    TextView pregonCode;
    TextView objetivo;
    TextView pago;
    TextView fechaLimite;
    TextView pregon;
    TextView experiencia;
    TextView codigoredime;
    TextView beneficioredime;
    TextView evidencia;

    int id_pregon;
    int id_campaign;
    int id_cliente;
    String nombre_empresa;
    String codigo_pregon;
    String material_extra;

    boolean approvedExperience;

    DownloadManager downloadManager;

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

            Intent intent = new Intent(PregonActivity.this, MenuInicial.class);
            PregonActivity.this.startActivity(intent);
            return true;
        }
//        if (id == R.id.action_send) {
//
//            // Do something
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregon);

        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        api_token = sp.getString("api_token", null);
        usernameSession = sp.getString("username", null);

        codigo_redime = usernameSession+" me invita";

        companyName = (TextView) findViewById(R.id.fcompania);
        pregonCode = (TextView) findViewById(R.id.fcodigopregon);
        objetivo = (TextView) findViewById(R.id.fobjetivo);
        pago = (TextView) findViewById(R.id.fpago);
        fechaLimite = (TextView) findViewById(R.id.ffecha);
        pregon = (TextView) findViewById(R.id.fpregon);
        experiencia = (TextView) findViewById(R.id.fexperiencia);
        codigoredime = (TextView) findViewById(R.id.fcodigoredime);
        beneficioredime = (TextView) findViewById(R.id.fbeneficio);
        evidencia = (TextView) findViewById(R.id.fevidencia);


        upAction = (Button) findViewById(R.id.upAction);

        Intent intent = getIntent();
        id_pregon = intent.getIntExtra("id_pregon",0);
        id_campaign = intent.getIntExtra("id_campaign",0);
        id_cliente = intent.getIntExtra("id_cliente",0);
        nombre_empresa = intent.getStringExtra("nombre_empresa");
        codigo_pregon = intent.getStringExtra("codigo_pregon");
        notification_id = intent.getIntExtra("notification_id",0);

        //cerrar notificacion push
        //Toast.makeText(getApplicationContext(),nombre_empresa+">>"+notification_id,Toast.LENGTH_LONG).show();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(notification_id);


        valiarExperienciaAprobada();


        upAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (approvedExperience)
                {
                    // Redirigiendo a checklist
                    Intent intentPregon = new Intent(PregonActivity.this, ChecklistPersona.class);
                    intentPregon.putExtra("id_pregon", id_pregon);
                    intentPregon.putExtra("id_campaign", id_campaign);
                    intentPregon.putExtra("id_cliente", id_cliente);
                    intentPregon.putExtra("nombre_empresa", nombre_empresa);
                    intentPregon.putExtra("codigo_pregon", codigo_pregon);
                    intentPregon.putExtra("codigo_redime", codigo_redime);
                    PregonActivity.this.startActivity(intentPregon);
                }
                else
                {
                    //mensajeNoAprobado();
                    // Redirigiendo a experiencia
                    Intent intentExperiencia = new Intent(PregonActivity.this, ExperienciaActivity.class);
                    intentExperiencia.putExtra("id_pregon", id_pregon);
                    intentExperiencia.putExtra("id_campaign", id_campaign);
                    intentExperiencia.putExtra("id_cliente", id_cliente);
                    intentExperiencia.putExtra("nombre_empresa", nombre_empresa);
                    intentExperiencia.putExtra("codigo_pregon", codigo_pregon);
                    PregonActivity.this.startActivity(intentExperiencia);
                }


            }
        });

//        upExperiencia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                valiarExperienciaAprobada();
//
//
//                if (approvedExperience)
//                {
//                    mensajeAprobado();
//                }
//                else
//                {
//                    // Redirigiendo a experiencia
//                    Intent intentExperiencia = new Intent(PregonActivity.this, ExperienciaActivity.class);
//                    intentExperiencia.putExtra("id_pregon", id_pregon);
//                    intentExperiencia.putExtra("id_campaign", id_campaign);
//                    intentExperiencia.putExtra("id_cliente", id_cliente);
//                    intentExperiencia.putExtra("nombre_empresa", nombre_empresa);
//                    intentExperiencia.putExtra("codigo_pregon", codigo_pregon);
//                    PregonActivity.this.startActivity(intentExperiencia);
//                }
//
//
//
//            }
//        });



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject jsonResponse = new JSONObject(response);

                    String code = jsonResponse.getString("code");

                    if (code.equals("200"))
                    {

                        JSONObject data = jsonResponse.getJSONObject("data");

                        String companiname = data.getString("clientName");
                        String pregoncode = data.getString("identificador_pregon");
                        String objetivor = data.getString("objetivo");
                        String pagor = data.getString("pago");
                        String fechalimiter = data.getString("fecha_limite");
                        String pregonr = data.getString("pregon");
                        String experienciar = data.getString("experiencia");

                        String codigoredimer = "sin codigo";
                        String beneficioredimer = "";

                        if (!data.getString("beneficio_redime").equals("null"))
                        {
                            codigoredimer = codigo_redime;
                            beneficioredimer = data.getString("beneficio_redime");
                        }

                        String evidenciar = data.getString("evidencia");
                        material_extra = data.getString("support_material");


                        //cargando datos del usuario

                        companyName.setText(companiname);
                        //pregonCode.setText(pregoncode);
                        objetivo.setText(objetivor);
                        pago.setText(pagor);
                        fechaLimite.setText(fechalimiter);
                        pregon.setText(pregonr);
                        experiencia.setText(experienciar);
                        codigoredime.setText(codigoredimer);
                        beneficioredime.setText(beneficioredimer);
                        evidencia.setText(evidenciar);

                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PregonActivity.this);

                        builder.setMessage("Error al registrar pregonero")
                                .setNegativeButton("Retry", null)
                                .create().show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };

        PregonRequest pregonrequest = new PregonRequest(api_token, id_pregon, responseListener);

        RequestQueue queue = Volley.newRequestQueue(PregonActivity.this);

        queue.add(pregonrequest);



        //swipeRefresh
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshChecklist);
        //refreshLayout.bringToFront();
//        final TextView textView = (TextView) findViewById(R.id.Random);

        refreshLayout.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary, R.color.colorGrayText);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        refreshLayout.setRefreshing(false);

//                        int min = 65;
//                        int max = 95;
//
//                        Random random = new Random();
//
//                        int i = random.nextInt(max-min+1)+min;
//
//                        textView.setText(String.valueOf(i));

                        valiarExperienciaAprobada();

                        Toast.makeText(getApplicationContext(),"Pagina recargada",Toast.LENGTH_LONG).show();

                    }
                },3000);
            }
        });


    }

    @Override
    public void onBackPressed() {


        goToPregonesActivos();
    }

    private void goToPregonesActivos() {
        // Redirigiendo a registerActivity
        Intent intentRegister = new Intent(PregonActivity.this, PregonesActivosActivity.class);
        PregonActivity.this.startActivity(intentRegister);
    }

    public void goToMenu(View view)
    {
        //redirigiendo a perfil de usuario
        Intent intentProfile = new Intent(PregonActivity.this, MenuInicial.class);
        PregonActivity.this.startActivity(intentProfile);
    }

    public void valiarExperienciaAprobada ()
    {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject jsonResponse = new JSONObject(response);

                    String code = jsonResponse.getString("code");

                    if (code.equals("200"))
                    {

                        JSONObject data = jsonResponse.getJSONObject("data");

                        String approved = data.getString("approved");

                        if (approved.equals("1"))
                        {
                            approvedExperience = true;
                            upAction.setText("SUBIR PREGÓN");
                        }
                        else
                        {
                            approvedExperience = false;
                            upAction.setText("CUÉNTANOS TU EXPERIENCIA");

                        }

                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PregonActivity.this);

                        builder.setMessage("Error al registrar pregonero")
                                .setNegativeButton("Retry", null)
                                .create().show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };

        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        int id_usuario = sp.getInt("id", -1);

        ExperienciaAprobadaRequest pregonrequest = new ExperienciaAprobadaRequest(api_token, id_pregon, id_usuario,responseListener);

        RequestQueue queue = Volley.newRequestQueue(PregonActivity.this);

        queue.add(pregonrequest);

    }


    public void mensajeAprobado ()
    {
        final CharSequence[] opciones = {"Aceptar"};

        final android.support.v7.app.AlertDialog.Builder alertOpciones = new android.support.v7.app.AlertDialog.Builder(PregonActivity.this);
        alertOpciones.setTitle("Tu experiencia ha sido aprobada");
        alertOpciones.setMessage("Tu experiencia ha sido aprobada, no es necesario enviarla de nuevo");

        //Seteando las opciones
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


//                if (opciones[which].equals("SI"))
//                {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package",getPackageName(),null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                }
//                else
//                {
//                    Toast alertPermisos = Toast.makeText(getApplication(),"Necesitamos dichos permisos para el funcionamiento de la aplicacion",Toast.LENGTH_SHORT);
//                    alertPermisos.setGravity(Gravity.CENTER, 0, 0);
//                    alertPermisos.show();
//                    dialog.dismiss();
//
//                    goToPregonesActivos();
//                }
            }
        });

        alertOpciones.show();
    }

    public void mensajeNoAprobado  ()
    {
        final CharSequence[] opciones = {"Aceptar"};

        final android.support.v7.app.AlertDialog.Builder alertOpciones = new android.support.v7.app.AlertDialog.Builder(PregonActivity.this);
        alertOpciones.setTitle("Tu experiencia aun no ha sido aprobada... ");
        alertOpciones.setMessage("Envia la respectiva experiencia o espera a que nuestros operadores la aprueben");

        //Seteando las opciones
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


//                if (opciones[which].equals("SI"))
//                {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package",getPackageName(),null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                }
//                else
//                {
//                    Toast alertPermisos = Toast.makeText(getApplication(),"Necesitamos dichos permisos para el funcionamiento de la aplicacion",Toast.LENGTH_SHORT);
//                    alertPermisos.setGravity(Gravity.CENTER, 0, 0);
//                    alertPermisos.show();
//                    dialog.dismiss();
//
//                    goToPregonesActivos();
//                }
            }
        });

        alertOpciones.show();
    }

    public void downloadSupportMaterial(View view)
    {
        if (material_extra.equals("null"))
        {
            Toast.makeText(PregonActivity.this, "Este pregón no cuenta con material de apoyo", Toast.LENGTH_LONG).show();
            return;
        }

        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

        String url = UrlApi.getUrlServer()+"storage/"+
                id_cliente+"/"+
                id_campaign+"/"+
                id_pregon+"/support_material/"
                +material_extra;

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        Long reference = downloadManager.enqueue(request);

    }

}
