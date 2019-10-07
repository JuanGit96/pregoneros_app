package com.login.pregoneros;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PregonesActivosActivity extends AppCompatActivity {

    //a List of type hero for holding list items
    List<Pregon> pregonList;

    //the listview
    ListView listViewPregon;

    String api_token;

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

            Intent intent = new Intent(PregonesActivosActivity.this, MenuInicial.class);
            PregonesActivosActivity.this.startActivity(intent);
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
        setContentView(R.layout.activity_pregones_activos);


        //initializing objects
        pregonList = new ArrayList<>();
        listViewPregon = (ListView) findViewById(R.id.listViewPregon);

        //Cargar datos de session
        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        api_token = sp.getString("api_token", null);
        int idUser = sp.getInt("id", -1);

        //consumiento servicio
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject jsonResponse = new JSONObject(response);

                    String code = jsonResponse.getString("code");

                    if (code.equals("200"))
                    {
                        JSONArray jsonArray = jsonResponse.getJSONArray("data");

                        if (jsonArray.length() == 0)
                        {
                            // Si no hay pregones activos en la base de datos muestra activity respectivo
                            Intent intentPregon = new Intent(PregonesActivosActivity.this, PregonerosNoActivos.class);
                            PregonesActivosActivity.this.startActivity(intentPregon);
                        }

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            try {
                                JSONObject jsonObjectPregon = jsonArray.getJSONObject(i);

                                String companyName = jsonObjectPregon.getString("clientName");
                                String codigoPregon = jsonObjectPregon.getString("identificador_pregon");
                                String fechaFin = jsonObjectPregon.getString("fecha_limite");
                                String objeto = jsonObjectPregon.getString("objetivo");
                                String idPregon = jsonObjectPregon.getString("id");
                                String idCampaign = jsonObjectPregon.getString("idCampaign");
                                String idClient = jsonObjectPregon.getString("idClient");

                                //adding some values to our list
                                pregonList.add(new Pregon(companyName, codigoPregon, fechaFin, "Objetivo: "+objeto,idPregon,idCampaign,idClient));

                            } catch (JSONException e) {

                            }
                        }

                        //creating the adapter
                        ListPregonAdapter adapter = new ListPregonAdapter(PregonesActivosActivity.this, R.layout.custom_list_pregones_activos, pregonList);

                        //attaching adapter to the listview
                        listViewPregon.setAdapter(adapter);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PregonesActivosActivity.this);

                        builder.setMessage("Error al cargar pregones activos")
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

        //en caso de error
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Toast.makeText(getApplicationContext(), "Error en el servidor al cargar pregones activos", Toast.LENGTH_LONG).show();
            }
        };

        PregonesRequest pregonesreq = new PregonesRequest(api_token, responseListener, errorListener);

        RequestQueue queue = Volley.newRequestQueue(PregonesActivosActivity.this);

        queue.add(pregonesreq);



    }

    @Override
    public void onBackPressed() {


        goToMenu();
    }

    private void goToMenu() {
        //redirigiendo a perfil de usuario
        Intent intentProfile = new Intent(PregonesActivosActivity.this, MenuInicial.class);
        PregonesActivosActivity.this.startActivity(intentProfile);
    }

    public void goToMenu (View view)
    {
        //redirigiendo a perfil de usuario
        Intent intentProfile = new Intent(PregonesActivosActivity.this, MenuInicial.class);
        PregonesActivosActivity.this.startActivity(intentProfile);
    }



}
