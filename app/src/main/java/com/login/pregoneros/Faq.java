package com.login.pregoneros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Faq extends AppCompatActivity {

    Button btnTutorial, btnVideoTutorial;

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

            Intent intent = new Intent(Faq.this, MenuInicial.class);
            Faq.this.startActivity(intent);
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
        setContentView(R.layout.activity_faq);

        btnTutorial = (Button) findViewById(R.id.tutorialClick);
        btnVideoTutorial = (Button) findViewById(R.id.videoTutorialClick);

        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //redirigiendo a tutorial
                Intent intentAnswer = new Intent(Faq.this, AnswerFaq.class);
                Faq.this.startActivity(intentAnswer);

            }
        });

        btnVideoTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //redirigiendo a tutorial
                Intent intentVideo = new Intent(Faq.this, WebViewTutorialActivity.class);
                Faq.this.startActivity(intentVideo);

            }
        });


    }

    public void goToMenu (View view)
    {
        //redirigiendo a perfil de usuario
        Intent intentProfile = new Intent(Faq.this, MenuInicial.class);
        Faq.this.startActivity(intentProfile);
    }
}
