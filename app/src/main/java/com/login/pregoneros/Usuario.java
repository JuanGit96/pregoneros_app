package com.login.pregoneros;

import android.view.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Usuario extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView tv_profilePage;

        TextView tvuser;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        //tv_profilePage = (TextView) findViewById(R.id.tv_goTo_userProfile);

        tvuser = findViewById(R.id.user_name);

        Intent intent = getIntent();

        String userWelcome = intent.getStringExtra("username");

        tvuser.setText(userWelcome);


    }


    public void goToMenu(View view)
    {
        //redirigiendo a perfil de usuario
        Intent intentProfile = new Intent(Usuario.this, MenuInicial.class);
        Usuario.this.startActivity(intentProfile);
    }
}
