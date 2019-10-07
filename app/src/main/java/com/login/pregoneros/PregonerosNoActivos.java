package com.login.pregoneros;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PregonerosNoActivos extends AppCompatActivity {

    TextView user_name;

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

            Intent intent = new Intent(PregonerosNoActivos.this, MenuInicial.class);
            PregonerosNoActivos.this.startActivity(intent);
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
        setContentView(R.layout.activity_pregoneros_no_activos);

        user_name = (TextView) findViewById(R.id.user_name);

        //nombre de session
        SharedPreferences sp = getSharedPreferences("your_prefs", Perfil.MODE_PRIVATE);
        String nameSessio = sp.getString("username",null);

        user_name.setText(nameSessio);


    }

    public void goToMenu(View view)
    {
        //redirigiendo a perfil de usuario
        Intent intentProfile = new Intent(PregonerosNoActivos.this, MenuInicial.class);
        PregonerosNoActivos.this.startActivity(intentProfile);
    }
}
