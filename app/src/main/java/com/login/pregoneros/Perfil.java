package com.login.pregoneros;

import android.content.ClipData;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Menu;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class Perfil extends AppCompatActivity {

    TextView user_name, user_lastName, user_birthday, user_email;

    MenuItem logoutBtn;

    String api_token;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        logoutBtn = menu.findItem(R.id.logout_btn);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        user_name = findViewById(R.id.user_name);
        user_lastName = findViewById(R.id.user_lastName);
        user_birthday = findViewById(R.id.user_birthdate);
        user_email = findViewById(R.id.user_email);


        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        String nameSessio = sp.getString("name",null);
        String emailSession = sp.getString("email", null);
        api_token = sp.getString("api_token", null);
        int idUser = sp.getInt("id", -1);


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

                        String name = data.getString("name");
                        String lastName = data.getString("lastName");
                        String birthDay = data.getString("dateBirth");
                        String email = data.getString("email");


                        //cargando datos del usuario

                        user_name.setText(name);
                        user_lastName.setText(lastName);
                        user_birthday.setText(birthDay);
                        user_email.setText(email);

                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Perfil.this);

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

        DataUserRequest profilerequest = new DataUserRequest(api_token, idUser, responseListener);

        RequestQueue queue = Volley.newRequestQueue(Perfil.this);

        queue.add(profilerequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_btn:


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

                                stopService(new Intent(getApplicationContext(),ServiceNotificationsPregones.class));


                                // Redirigiendo a pagina inicial de login
                                Intent intentReturns = new Intent(Perfil.this, MainActivity.class);
                                Perfil.this.startActivity(intentReturns);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Perfil.this);

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

                RequestQueue queue = Volley.newRequestQueue(Perfil.this);

                queue.add(logoutrequest);





                return true;
//            case R.id.item2:
//            ... code ...
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }    }
}
