package com.login.pregoneros;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.FOREGROUND_SERVICE;

public class MenuInicial extends AppCompatActivity {

    Button logoutBtn;

    TextView userName;

    String api_token;

    ProgressDialog dialog;

    //para notificaciones push
    private Button btNotificacion;
    private PendingIntent pendingIntent;
    private PendingIntent siPendingIntent;
    private PendingIntent noPendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    //public final static int NOTIFICACION_ID = 0;
    //final variables notificaciones push

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
//        if (id == R.id.action_home) {
//
//            Intent intent = new Intent(MenuInicial.this, MenuInicial.class);
//            MenuInicial.this.startActivity(intent);
//            finish();
//            return true;
//        }
//        if (id == R.id.action_send) {
//
//            // Do something
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);
//Toast.makeText(this, getApplicationContext().toString(), Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, 1000);
        } else {
//
        }


        logoutBtn = (Button) findViewById(R.id.btn_logout);
        userName = (TextView) findViewById(R.id.user_name);


        //datos de session
        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        String nameSessio = sp.getString("username",null);
        String emailSession = sp.getString("email", null);
        api_token = sp.getString("api_token", null);
        int idUser = sp.getInt("id", -1);

        userName.setText(nameSessio);





//        /*
//        * Enviando notificaciones push al iniciar session       *
//        *
//        * */
//
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
//
//                        JSONArray jsonArray = jsonResponse.getJSONArray("data");
//
//                        if (jsonArray.length() > 0)
//                        {
//
//                            for (int i = 0; i < jsonArray.length(); i++)
//                            {
//                                try {
//                                    JSONObject jsonObjectPregon = jsonArray.getJSONObject(i);
//
//                                    int id_pregon = Integer.parseInt(jsonObjectPregon.getString("id_pregon"));
//                                    int id_campania = Integer.parseInt(jsonObjectPregon.getString("id_campaign"));
//                                    int id_cliente = Integer.parseInt(jsonObjectPregon.getString("id_client"));
//                                    String razon_social = jsonObjectPregon.getString("razon_social");
//                                    String identificador_pregon = jsonObjectPregon.getString("identificador_pregon");
//                                    String pago = jsonObjectPregon.getString("pago");
//                                    String message = "¡Gana $"+pago+" al minuto con "+razon_social+"!";
//
//
//                                    //enviando notificaciones push
//                                    setPendingIntent(id_pregon, id_campania, id_cliente, razon_social, identificador_pregon);
//                                    setSiPendingIntent(id_pregon, id_campania, id_cliente, razon_social, identificador_pregon);
//                                    setNoPendingIntent();
//                                    createNotification(message, id_pregon);
//                                    createNotificationChannel(id_pregon);
//
//
//                                } catch (JSONException e) {
//
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuInicial.this);
//
//                                    builder.setMessage("Error al Crear notificaciones")
//                                            .setNegativeButton("Retry", null)
//                                            .create().show();
//
//                                }
//                            }
//
//
//                        }
//
//                    }
//                    else
//                    {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuInicial.this);
//
//                        builder.setMessage("Error al Enviar notificaciones")
//                                .setNegativeButton("Retry", null)
//                                .create().show();
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
//        PushRequest pushrequest = new PushRequest(api_token, idUser, responseListener);
//
//        RequestQueue queue = Volley.newRequestQueue(MenuInicial.this);
//
//        queue.add(pushrequest);
//
//
//
//        /*
//        * Fin de envio de notificaciones push
//        * */











        /*
        * Cerrando Session
        * */
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(MenuInicial.this);
                dialog.setTitle("Cerrando sesión");
                dialog.setMessage("Cargando...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(false);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            String msg = jsonResponse.getString("data");

                            if (msg.equals("User logged out."))
                            {
                                //Borrando datos en session

                                SharedPreferences sp = getSharedPreferences("your_prefs", MainActivity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.remove("username");
                                editor.remove("email");
                                editor.remove("api_token");
                                editor.remove("id");
                                editor.commit();

                                dialog.dismiss();

                                stopService(new Intent(getApplicationContext(),ServiceNotificationsPregones.class));
                                stopService(new Intent(getApplicationContext(),ServiceDailyNotifications.class));

                                // Redirigiendo a pagina inicial de login
                                Intent intentReturns = new Intent(MenuInicial.this, MainActivity.class);
                                MenuInicial.this.startActivity(intentReturns);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MenuInicial.this);

                                builder.setMessage("Error al hacer logout del pregonero")
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

                LogoutRequest logoutrequest = new LogoutRequest(api_token, responseListener);

                RequestQueue queue = Volley.newRequestQueue(MenuInicial.this);

                queue.add(logoutrequest);



            }
        });

    }


    public void goToPregones (View view)
    {
        // Redirigiendo a registerActivity
        Intent intentRegister = new Intent(MenuInicial.this, PregonesActivosActivity.class);
        MenuInicial.this.startActivity(intentRegister);
    }

    public void goToAsk (View view)
    {
        // Redirigiendo a registerActivity
        Intent intentAsk = new Intent(MenuInicial.this, Faq.class);
        MenuInicial.this.startActivity(intentAsk);
    }

    public void goToPagos (View view)
    {
        // Redirigiendo a registerActivity
        //Intent intentRegister = new Intent(MenuInicial.this, HistoryPayments.class);
        Intent intentRegister = new Intent(MenuInicial.this, EnCnstruccionActivity.class);
        intentRegister.putExtra("mesgType", 2);
        MenuInicial.this.startActivity(intentRegister);
    }

    public void goToRedime (View view)
    {
        // Redirigiendo a registerActivity
        Intent intentRegister = new Intent(MenuInicial.this, RedentionActivity.class);
        intentRegister.putExtra("mesgType", 1);
        MenuInicial.this.startActivity(intentRegister);
    }


    //Funciones para notificaciones push

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setSiPendingIntent(int id_pregon, int id_campaign, int id_client, String razon_social, String identificador_pregon){
        Intent intent = new Intent(this, PregonActivity.class);
        intent.putExtra("id_pregon", id_pregon);
        intent.putExtra("id_campaign", id_campaign);
        intent.putExtra("id_cliente", id_client);
        intent.putExtra("nombre_empresa", razon_social);
        intent.putExtra("codigo_pregon", identificador_pregon);
        intent.setAction("dummy_action_" + id_pregon); // para crear un intent unico y que no se sobreescriba por la etiqueta FLAG_UPDATE_CURRENT (probar con PendingIntent.FLAG_ONE_SHOT)
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
    public void setPendingIntent(int id_pregon, int id_campaign, int id_client, String razon_social, String identificador_pregon){
        Intent intent = new Intent(this, PregonActivity.class);
        intent.putExtra("id_pregon", id_pregon);
        intent.putExtra("id_campaign", id_campaign);
        intent.putExtra("id_cliente", id_client);
        intent.putExtra("nombre_empresa", razon_social);
        intent.putExtra("codigo_pregon", identificador_pregon);
        intent.setAction("dummy_action_" + id_pregon); // para crear un intent unico y que no se sobreescriba por la etiqueta FLAG_UPDATE_CURRENT (probar con PendingIntent.FLAG_ONE_SHOT)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PregonActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void createNotificationChannel(int id_pregon){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion_"+id_pregon;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(String message, int notification_id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_work_black_24dp);
        builder.setContentTitle("¡Nuevo pregon!");
        builder.setContentText(message);
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.ic_sms_black_24dp, "Si", siPendingIntent);
        builder.addAction(R.drawable.ic_sms_black_24dp, "No", noPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(notification_id, builder.build());
    }
    //fin finciones para notificaciones push
}



