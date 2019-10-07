package com.login.pregoneros;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    TextView tv_registerPage;

    EditText etEmailUser, etPasswordUser;

    Button btnLogin;

    ProgressDialog dialog;

    Context thiscontext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(existSession())
        {
            // Redirigiendo a Menu principal
            Intent intentMenu = new Intent(MainActivity.this, MenuInicial.class);
            MainActivity.this.startActivity(intentMenu);
        }


        tv_registerPage = (TextView) findViewById(R.id.tv_goTo_registerPage);

        btnLogin = (Button) findViewById(R.id.btn_login);

        etEmailUser = (EditText) findViewById(R.id.login_user);
        etPasswordUser = (EditText) findViewById(R.id.login_password);

        tv_registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Redirigiendo a registerActivity
                Intent intentRegister = new Intent(MainActivity.this, Registro.class);
                MainActivity.this.startActivity(intentRegister);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Iniciando sesión");
                dialog.setMessage("Cargando...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(false);

                final String emailUser = etEmailUser.getText().toString();
                final String passUser = etPasswordUser.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            String code = jsonResponse.getString("code");

                            if (code.equals("200"))
                            {
                                //Guardando datos en session

                                JSONObject resultData = jsonResponse.getJSONObject("data");

                                String username = resultData.optString("name")+" "+resultData.getString("lastName");
                                String email = resultData.getString("email");
                                String api_token = resultData.getString("api_token");
                                int id = resultData.getInt("id");

                                SharedPreferences sp = getSharedPreferences("your_prefs", MainActivity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.putString("api_token", api_token);
                                editor.putInt("id", id);
                                editor.commit();

                                dialog.dismiss();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(new Intent(thiscontext,ServiceNotificationsPregones.class));
                                    startForegroundService(new Intent(thiscontext,ServiceDailyNotifications.class));
                                } else {
                                    startService(new Intent(thiscontext,ServiceNotificationsPregones.class));
                                    startService(new Intent(thiscontext,ServiceDailyNotifications.class));
                                }

                                Intent pregonesActivosActivity = new Intent(MainActivity.this, MenuInicial.class);
                                MainActivity.this.startActivity(pregonesActivosActivity);
                                finish();
                            }

                            if (code.equals("400"))
                            {

                                //Mostrando errores en pantalla

                                String error = jsonResponse.getString("error");

                                dialog.dismiss();

                                Toast alertMessage = Toast.makeText(getApplicationContext(),error, Toast.LENGTH_LONG);
                                alertMessage.setGravity(Gravity.CENTER, 0, 0);
                                alertMessage.show();

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();

                            Toast alertMessage = Toast.makeText(getApplicationContext(),"Error en la comunicacion con el servidor", Toast.LENGTH_LONG);
                            alertMessage.setGravity(Gravity.CENTER, 0, 0);
                            alertMessage.show();
                        }

                    }
                };

                LoginRequest loginrequest = new LoginRequest(emailUser, passUser, responseListener);

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                queue.add(loginrequest);


            }
        });

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



}
