package com.login.pregoneros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SuccesSendChecklistPersona extends AppCompatActivity {

    //datos de pregon, cliente, campaña
    int pregonId;
    int campaingId;
    int clientId;
    int idUser;
    String codigo_redime;
    String nombre_empresa;
    String codigo_pregon;
    String pregoneado_name;
    String codigo_rebound;
    String nombre_pregonero;

    TextView reboundcode, discountcode, txt_roboundCode;

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

            Intent intent = new Intent(SuccesSendChecklistPersona.this, MenuInicial.class);
            SuccesSendChecklistPersona.this.startActivity(intent);
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
        setContentView(R.layout.activity_succes_send_checklist_persona);

        //Variables del activity anterior
        Intent intent = getIntent();
        pregonId = intent.getIntExtra("id_pregon",0);
        campaingId = intent.getIntExtra("id_campaign",0);
        clientId = intent.getIntExtra("id_cliente",0);
        codigo_redime = intent.getStringExtra("codigo_redime");
        codigo_rebound = intent.getStringExtra("codigo_rebound");
        nombre_empresa = intent.getStringExtra("nombre_empresa");
        codigo_pregon = intent.getStringExtra("codigo_pregon");
        pregoneado_name = intent.getStringExtra("pregoneado_name");

        reboundcode = (TextView) findViewById(R.id.reboundcode);
        reboundcode.setText(codigo_rebound);

        discountcode = (TextView) findViewById(R.id.discountcode);
        discountcode.setText(codigo_redime);

        txt_roboundCode = (TextView) findViewById(R.id.txt_roboundCode);
        txt_roboundCode.setText("Con este código "+pregoneado_name+" podrá dar pregones como referido tuyo");


    }

    public void goToPregon(View view) {
        //redirireccion a pagina del pregon
        Intent intentPregon = new Intent(SuccesSendChecklistPersona.this, PregonActivity.class);
        intentPregon.putExtra("id_pregon", pregonId);
        intentPregon.putExtra("id_campaign", campaingId);
        intentPregon.putExtra("id_cliente", clientId);
        intentPregon.putExtra("nombre_empresa", nombre_empresa);
        intentPregon.putExtra("codigo_pregon", codigo_pregon);
        SuccesSendChecklistPersona.this.startActivity(intentPregon);
    }

    public void goToMenu(View view) {
        //redirigiendo a menu principal
        Intent intentMenu = new Intent(SuccesSendChecklistPersona.this, MenuInicial.class);
        SuccesSendChecklistPersona.this.startActivity(intentMenu);
    }
}
