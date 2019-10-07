package com.login.pregoneros;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class EnCnstruccionActivity extends AppCompatActivity {

    TextView TvMessage;

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

            Intent intent = new Intent(EnCnstruccionActivity.this, MenuInicial.class);
            EnCnstruccionActivity.this.startActivity(intent);
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
        setContentView(R.layout.activity_en_cnstruccion);

        //datos desde actividad anterior
        Intent intent = getIntent();
        int msqType = intent.getIntExtra("mesgType",0);

        TvMessage = (TextView) findViewById(R.id.textConstruction);

        createMessage(msqType);

    }

    public void goToMenu (View view)
    {
        //redirigiendo a perfil de usuario
        Intent intentProfile = new Intent(EnCnstruccionActivity.this, MenuInicial.class);
        EnCnstruccionActivity.this.startActivity(intentProfile);
    }

    private void createMessage (int message)
    {

        String text = "";
        String text2 = "";
        String numberCel = "";

        switch (message)
        {
            case 1:
                text = "Si quieres redimir algún código" +
                        " de nuestros pregoneros, escribe" +
                        " al número";

                numberCel = "304 155 8835";

                text2 = "con el código" +
                        " para hacer efectiva la promoción.";
                break;
            case 2:
                text = "Si quieres consultar el dinero que has" +
                        " ganado, o solicitar el pago, comunícate" +
                        " al número";

                numberCel = "304 155 8835";

                break;

            default:

                text = "Si quieres consultar Algo con nuestro equipo de atencion al cliente" +
                        " comunícate" +
                        " al número";

                numberCel = "304 155 8835";
        }

        String MessajeConstruct = "<font color='black'>"+text+"</font> "+"<font color='gray'>"+numberCel+"</font> "+" <font color='black'>"+text2+"</font> ";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TvMessage.setText(Html.fromHtml(MessajeConstruct,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            TvMessage.setText(Html.fromHtml(MessajeConstruct), TextView.BufferType.SPANNABLE);
        }


    }

}
