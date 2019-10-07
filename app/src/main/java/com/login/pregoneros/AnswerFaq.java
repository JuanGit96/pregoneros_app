package com.login.pregoneros;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AnswerFaq extends AppCompatActivity {

    TextView stepOne,stepTwo,stepThree,stepFour,stepFive;

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

            Intent intent = new Intent(AnswerFaq.this, MenuInicial.class);
            AnswerFaq.this.startActivity(intent);
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
        setContentView(R.layout.activity_answer_faq);

        stepOne =(TextView) findViewById(R.id.stepOne);
        stepTwo =(TextView) findViewById(R.id.stepTwo);
        stepThree =(TextView) findViewById(R.id.stepThree);
        stepFour =(TextView) findViewById(R.id.stepFour);
        stepFive =(TextView) findViewById(R.id.stepFive);

        createLabels();
    }

    private void createLabels ()
    {
        String text1 = "<font color='gray'>En la sección </font> <font color='#D81B60'>PREGONES </font><font color='gray'>podrás escoger entre los distintos pregones que puedes realizar.\n" +
                "En cada pregón encontrarás la información necesaria para hablar sobre la respectiva marca.</font>";
        String text2 = "<font color='gray'>Para que puedas hablar de la marca, te enviaremos una experiencia sobre esta, esta puede ser desde leer un texto, hasta comprar y consumir el producto.\n"+
                "</font><font color='gray'>En el botón </font><font color='#D81B60'>CUÉNTANOS TU EXPERIENCIA </font><font color='gray'>podrás enviar tu experiencia, después de esto, ya podrás referir la marca a tus amigos y familia.</font>";
        String text3 = "<font color='gray'>Deberás contarle a la mayor cantidad posible de personas sobre la marca, según el objetivo del pregón.</font>";
        String text4 = "<font color='gray'>Cada vez que le cuentes a alguien el pregón, deberás llenar un formulario con información de esta persona y una evidencia de que efectivamente hablaste sobre la marca. \nEn el botón </font><font color='#D81B60'>ENVIAR PERSONA </font><font color='gray'>podrás llenar el formulario.</font>";
        String text5 = "<font color='gray'>Al final de la semana, te pagaremos el acumulado realizado de todos los pregones que hayas realizado.\n" +
                "Recuerda que pagamos vía Nequi, asi que asegurate de tener tu cuenta lista.</font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stepOne.setText(Html.fromHtml(text1,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            stepTwo.setText(Html.fromHtml(text2,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            stepThree.setText(Html.fromHtml(text3,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            stepFour.setText(Html.fromHtml(text4,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            stepFive.setText(Html.fromHtml(text5,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);

        } else {
            stepOne.setText(Html.fromHtml(text1), TextView.BufferType.SPANNABLE);
            stepTwo.setText(Html.fromHtml(text2), TextView.BufferType.SPANNABLE);
            stepThree.setText(Html.fromHtml(text3), TextView.BufferType.SPANNABLE);
            stepFour.setText(Html.fromHtml(text4), TextView.BufferType.SPANNABLE);
            stepFive.setText(Html.fromHtml(text5), TextView.BufferType.SPANNABLE);
        }


    }
}
