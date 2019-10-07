package com.login.pregoneros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class RedentionSuccessActivity extends AppCompatActivity {


    TextView tv_goTo_menu;

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

            Intent intent = new Intent(RedentionSuccessActivity.this, MenuInicial.class);
            RedentionSuccessActivity.this.startActivity(intent);
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
        setContentView(R.layout.activity_redention_success);

//        tv_goTo_menu = (TextView) findViewById(R.id.tv_goTo_menu);
//
//        tv_goTo_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent pregonesActivosActivity = new Intent(RedentionSuccessActivity.this, MenuInicial.class);
//                RedentionSuccessActivity.this.startActivity(pregonesActivosActivity);
//            }
//        });

    }
}
