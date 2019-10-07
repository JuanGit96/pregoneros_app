package com.login.pregoneros;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("Registered")
public class ServiceDailyNotifications extends Service {

    //para notificaciones push
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";

    String api_token;
    private final int IDENTIDICADOR_NOTIFICACION = 916473;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
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
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(existSession())
        {

            Timer timer;
            timer = new Timer();

            TimerTask task = new TimerTask() {

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run()
                {
                    //datos de session
                    SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
                    String nameSession = sp.getString("username",null);

                    /*
                     * Enviando notificaciones push al iniciar session       *
                     *
                     * */
                    setPendingIntent();
                    createNotification();
                    createNotificationChannel();
                    /*
                     * Fin de envio de notificaciones push
                     * */




                }
            };
            // Empezamos en 30min y se repite cada dia
            timer.schedule(task, 1800000, 86400000);

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Problemas con la sesion de pregoneros",Toast.LENGTH_LONG).show();
        }

        return START_STICKY;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void setPendingIntent(){
        Intent intent = new Intent(this, PregonesActivosActivity.class);
        intent.putExtra("notification_id", IDENTIDICADOR_NOTIFICACION);
        intent.setAction("daily_" + IDENTIDICADOR_NOTIFICACION); // para crear un intent unico y que no se sobreescriba por la etiqueta FLAG_UPDATE_CURRENT (probar con PendingIntent.FLAG_ONE_SHOT)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PregonesActivosActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion_"+IDENTIDICADOR_NOTIFICACION;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_stat_name);

        builder.setContentTitle("Gana dinero hablando con la gente");

        builder.setContentText("No olvides revisar los pregones");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(IDENTIDICADOR_NOTIFICACION, builder.build());
    }
    //fin finciones para notificaciones push
}
