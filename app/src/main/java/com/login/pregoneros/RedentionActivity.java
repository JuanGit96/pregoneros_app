package com.login.pregoneros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RedentionActivity extends AppCompatActivity {

    EditText etCodigo;

    Button btnEnviar;

    Button btnMenu;

    ProgressDialog dialog;

    String api_token;

    int user_id;

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

            Intent intent = new Intent(RedentionActivity.this, MenuInicial.class);
            RedentionActivity.this.startActivity(intent);
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
        setContentView(R.layout.activity_redention);

        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        api_token = sp.getString("api_token", null);
        user_id = sp.getInt("id", 0);

        btnEnviar = (Button) findViewById(R.id.btn_send);
        //btnMenu = (Button) findViewById(R.id.btn_goMenu);

        etCodigo = (EditText) findViewById(R.id.codigo);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(RedentionActivity.this);
                dialog.setTitle("Enviando codigo");
                dialog.setMessage("Cargando...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(false);

                final String codigo = etCodigo.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            String code = jsonResponse.getString("code");

                            if (code.equals("201"))
                            {
                                //Redireccionando a ventana de exito
                                Intent pregonesActivosActivity = new Intent(RedentionActivity.this, RedentionSuccessActivity.class);
                                RedentionActivity.this.startActivity(pregonesActivosActivity);
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

                RedentionRequest redentionrequest = new RedentionRequest(api_token,codigo,Integer.toString(user_id) , responseListener);

                RequestQueue queue = Volley.newRequestQueue(RedentionActivity.this);

                queue.add(redentionrequest);


            }
        });


//        btnMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent pregonesActivosActivity = new Intent(RedentionActivity.this, MenuInicial.class);
//                RedentionActivity.this.startActivity(pregonesActivosActivity);
//                finish();
//
//            }
//
//        });

    }
}
