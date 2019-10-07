package com.login.pregoneros;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.FOREGROUND_SERVICE;

public class ServiceNotificationsPregones extends Service {

    //para notificaciones push
    private PendingIntent pendingIntent;
    private PendingIntent siPendingIntent;
    private PendingIntent noPendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";

    String api_token;

    String typePushNotification = "";

    private final int IDENTIDICADOR_EXPERIENCIA = 95135786;
    private final int IDENTIDICADOR_PREGONES = 359634353;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate ()
    {
        super.onCreate();

        Log.d("MyApp","servicio creado");

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }

    }

    @Override
    public int onStartCommand (Intent intent, int flag, int idProcess)
    {
        if(existSession())
        {
            Timer timer;
            timer = new Timer();

            TimerTask task = new TimerTask() {

                @Override
                public void run()
                {
                    //datos de session
                    SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
                    String nameSessio = sp.getString("username",null);
                    String emailSession = sp.getString("email", null);
                    api_token = sp.getString("api_token", null);
                    int idUser = sp.getInt("id", -1);


                    /*
                     * Enviando notificaciones push al iniciar session       *
                     *
                     * */

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(String response) {

                            try
                            {
                                JSONObject jsonResponse = new JSONObject(response);

                                String code = jsonResponse.getString("code");

                                if (code.equals("200"))
                                {

                                    JSONArray jsonArray = jsonResponse.getJSONArray("data");

                                    if (jsonArray.length() > 0)
                                    {

                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            try {
                                                JSONObject jsonObjectPregon = jsonArray.getJSONObject(i);

                                                int id_pregon = Integer.parseInt(jsonObjectPregon.getString("id_pregon"));
                                                int id_campania = Integer.parseInt(jsonObjectPregon.getString("id_campaign"));
                                                int id_cliente = Integer.parseInt(jsonObjectPregon.getString("id_client"));
                                                String razon_social = jsonObjectPregon.getString("razon_social");
                                                String identificador_pregon = jsonObjectPregon.getString("identificador_pregon");
                                                String message = "¡Hay un nuevo pregon de "+razon_social+"!";


                                                //enviando notificaciones push
                                                typePushNotification = "";
                                                String action = "dummy_action_";
                                                int notification_id = (IDENTIDICADOR_PREGONES+id_pregon);
                                                //Toast.makeText(getApplicationContext(),razon_social+">>"+notification_id,Toast.LENGTH_LONG).show();

                                                setPendingIntent(id_pregon, id_campania, id_cliente, razon_social, identificador_pregon,action,notification_id);
                                                setSiPendingIntent(id_pregon, id_campania, id_cliente, razon_social, identificador_pregon,action,notification_id);
                                                setNoPendingIntent();
                                                createNotification(message, notification_id);
                                                createNotificationChannel(notification_id);


                                            } catch (JSONException e) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                                                builder.setMessage("Error al Crear notificaciones")
                                                        .setNegativeButton("Retry", null)
                                                        .create().show();

                                            }
                                        }


                                    }

                                }
                                else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                                    builder.setMessage("Error al Enviar notificaciones")
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

                    PushRequest pushrequest = new PushRequest(api_token, idUser, responseListener);

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    queue.add(pushrequest);



                    /*
                     * Fin de envio de notificaciones push
                     * */

                    startExperienceService();



                }
            };
            // Empezamos dentro de 10ms y luego lanzamos la tarea cada 1000ms
            timer.schedule(task, 10000, 600000);

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Problemas con la sesion de pregoneros",Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy ()
    {

    }



    public boolean existSession ()
    {
        //datos de session
        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        String nameSession = sp.getString("username",null);

        if (nameSession != null)
            return true;
        else
            return false;
    }


    //Funciones para notificaciones push

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setSiPendingIntent(int id_pregon, int id_campaign, int id_client, String razon_social, String identificador_pregon, String action, int notification_id){
        Intent intent = new Intent(this, PregonActivity.class);
        intent.putExtra("id_pregon", id_pregon);
        intent.putExtra("id_campaign", id_campaign);
        intent.putExtra("id_cliente", id_client);
        intent.putExtra("nombre_empresa", razon_social);
        intent.putExtra("codigo_pregon", identificador_pregon);
        intent.putExtra("notification_id", notification_id);
        intent.setAction(action + id_pregon); // para crear un intent unico y que no se sobreescriba por la etiqueta FLAG_UPDATE_CURRENT (probar con PendingIntent.FLAG_ONE_SHOT)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PregonActivity.class);
        stackBuilder.addNextIntent(intent);
        siPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setNoPendingIntent(){
        Intent intent = new Intent(this, PregonesActivosActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PregonesActivosActivity.class);
        stackBuilder.addNextIntent(intent);
        noPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setPendingIntent(int id_pregon, int id_campaign, int id_client, String razon_social, String identificador_pregon, String action, int notification_id){
        Intent intent = new Intent(this, PregonActivity.class);
        intent.putExtra("id_pregon", id_pregon);
        intent.putExtra("id_campaign", id_campaign);
        intent.putExtra("id_cliente", id_client);
        intent.putExtra("nombre_empresa", razon_social);
        intent.putExtra("codigo_pregon", identificador_pregon);
        intent.putExtra("notification_id", notification_id);
        intent.setAction(action + id_pregon); // para crear un intent unico y que no se sobreescriba por la etiqueta FLAG_UPDATE_CURRENT (probar con PendingIntent.FLAG_ONE_SHOT)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PregonActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void createNotificationChannel(int notification_id){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion_"+notification_id;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(String message, int notification_id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_stat_name);
        if (!typePushNotification.equals("experienceApprovedPush"))
        {
            builder.setContentTitle("¡Nuevo pregon!");
        }
        else
        {
            builder.setContentTitle("¡Experiencia aprobada!");
        }
        builder.setContentText(message);
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);

        if (!typePushNotification.equals("experienceApprovedPush"))
        {
            builder.addAction(R.drawable.ic_sms_black_24dp, "Si", siPendingIntent);
            builder.addAction(R.drawable.ic_sms_black_24dp, "No", noPendingIntent);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(notification_id, builder.build());
    }
    //fin finciones para notificaciones push

    private void startExperienceService()
    {
        //datos de session
        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        String nameSessio = sp.getString("username",null);
        api_token = sp.getString("api_token", null);
        int idUser = sp.getInt("id", -1);


        /*
         * Enviando notificaciones push al iniciar session       *
         *
         * */

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject jsonResponse = new JSONObject(response);

                    String code = jsonResponse.getString("code");

                    if (code.equals("200"))
                    {

                        JSONArray jsonArray = jsonResponse.getJSONArray("data");

                        if (jsonArray.length() > 0)
                        {

                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                try {
                                    JSONObject jsonObjectPregon = jsonArray.getJSONObject(i);

                                    int id_pregon = Integer.parseInt(jsonObjectPregon.getString("pregon_id"));
                                    int id_campania = Integer.parseInt(jsonObjectPregon.getString("campaign_id"));
                                    int id_cliente = Integer.parseInt(jsonObjectPregon.getString("client_id"));
                                    String razon_social = jsonObjectPregon.getString("razon_social");
                                    String identificador_pregon = jsonObjectPregon.getString("identificador_pregon");
                                    String message = "¡Tu experiencia de "+razon_social+" ha sido aprobada, ya puedes empezar a dar el pregón!";


                                    //enviando notificaciones push
                                    typePushNotification = "experienceApprovedPush";
                                    int notification_id = (IDENTIDICADOR_EXPERIENCIA+id_pregon);

                                    String action = "experience_action_";
                                    setPendingIntent(id_pregon, id_campania, id_cliente, razon_social, identificador_pregon,action,notification_id);
                                    setSiPendingIntent(id_pregon, id_campania, id_cliente, razon_social, identificador_pregon,action,notification_id);
                                    setNoPendingIntent();
                                    createNotification(message, notification_id);
                                    createNotificationChannel(notification_id);


                                } catch (JSONException e) {

//                                                AlertDialog.Builder builder = new AlertDialog.Builder(MenuInicial.this);
//
//                                                builder.setMessage("Error al Crear notificaciones")
//                                                        .setNegativeButton("Retry", null)
//                                                        .create().show();

                                }
                            }


                        }

                    }
                    else
                    {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuInicial.this);
//
//                                    builder.setMessage("Error al Enviar notificaciones")
//                                            .setNegativeButton("Retry", null)
//                                            .create().show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };

        PushExperienceApprovedRequest pushrequest = new PushExperienceApprovedRequest(api_token, idUser, responseListener);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        queue.add(pushrequest);



        /*
         * Fin de envio de notificaciones push
         * */
    }
}
