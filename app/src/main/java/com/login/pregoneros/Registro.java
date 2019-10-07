package com.login.pregoneros;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class Registro extends AppCompatActivity {

    private int mYear, mMonth, mDay, sYear, sMonth, sDay;

    Calendar calendar = Calendar.getInstance();

    static final int DATE_ID = 0;

    TextView tv_loginPage;

    EditText etNameUser, etLastnameuser, etBirthUser,  etEmailUser, etPhonelUser, etPasswordUser, etConfirmPasswordUser;

    Button btnRegister, etBirthUserBtn;

    ProgressDialog dialog;

    Context thiscontext = this;

    RadioButton radioButton;

    RadioGroup radioGrup;

    public boolean activeTermsAndConditions;

    String nuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //fecha actual para mostrar en calendario
        sMonth = calendar.get(Calendar.MONTH);
        sYear = calendar.get(Calendar.YEAR);
        sDay = calendar.get(Calendar.DAY_OF_MONTH);

        tv_loginPage = (TextView) findViewById(R.id.tv_goTo_loginPage);

        btnRegister = (Button) findViewById(R.id.user_register);

        etNameUser = (EditText) findViewById(R.id.user_name);
        etLastnameuser = (EditText) findViewById(R.id.user_lastName);
        etBirthUser = (EditText) findViewById(R.id.user_birthdate);
        etBirthUserBtn = (Button) findViewById(R.id.user_birthdate_btn);
        etEmailUser = (EditText) findViewById(R.id.user_email);
        etPhonelUser = (EditText) findViewById(R.id.user_phone);
        etPasswordUser = (EditText) findViewById(R.id.user_password);
        etConfirmPasswordUser = (EditText) findViewById(R.id.user_confirmPassword);

        //deshabilitando texto de fecha
        etBirthUser.setEnabled(false);

        radioButton = (RadioButton) findViewById(R.id.ratio_terms);
        radioGrup = (RadioGroup) findViewById(R.id.radioGrup);
        activeTermsAndConditions = false;
        nuevo = "";


        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!radioButton.isChecked())
                {
                    radioGrup.clearCheck();
                    radioButton.setChecked(true);
                    activeTermsAndConditions = true;
                    return;
                }

                if (activeTermsAndConditions)
                {
                    radioButton.setChecked(false);
                    activeTermsAndConditions = false;
                    return;
                }

                activeTermsAndConditions = true;

            }
        });


        etBirthUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DATE_ID);

            }
        });


        tv_loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Redirigiendo a LoginActivity
                Intent intentLogin = new Intent(Registro.this, MainActivity.class);
                Registro.this.startActivity(intentLogin);

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activeTermsAndConditions)
                {
                    Toast alertMessage = Toast.makeText(getApplicationContext(),"Necesitamos que aceptes terminos y condiciones para seguir con el registro", Toast.LENGTH_LONG);
                    alertMessage.setGravity(Gravity.CENTER, 0, 0);
                    alertMessage.show();
                    return;
                }

                dialog = new ProgressDialog(Registro.this);
                dialog.setTitle("Registrando pregonero");
                dialog.setMessage("Cargando...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(false);

                final String nameUser = etNameUser.getText().toString().trim();
                final String lastnameUser = etLastnameuser.getText().toString().trim();
                final String birthdateUser = etBirthUser.getText().toString().trim();
                final String emailUser = etEmailUser.getText().toString().trim();
                final String phoneUser = etPhonelUser.getText().toString().trim();
                final String passUser = etPasswordUser.getText().toString().trim();
                final String passConfirmUser = etConfirmPasswordUser.getText().toString().trim();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            String code = jsonResponse.getString("code");

                            if (code.equals("201"))
                            {
                                //Guardando datos en session

                                JSONObject resultData = jsonResponse.getJSONObject("data");

                                String username = resultData.optString("name")+" "+resultData.getString("lastName");
                                String email = resultData.getString("email");
                                String api_token = resultData.getString("api_token");
                                int id = resultData.getInt("id");

                                SharedPreferences sp = getSharedPreferences("your_prefs", MainActivity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.putString("api_token", api_token);
                                editor.putInt("id", id);
                                editor.commit();

                                dialog.dismiss();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(new Intent(thiscontext,ServiceNotificationsPregones.class));
                                } else {
                                    startService(new Intent(thiscontext,ServiceNotificationsPregones.class));
                                }

                                // Redirigiendo a Bienvenida a usuario por su registro
                                Intent intentRegister = new Intent(Registro.this, Usuario.class);
                                intentRegister.putExtra("username", username);
                                Registro.this.startActivity(intentRegister);
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
                        }

                    }
                };

                //en caso de error
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error en el servidor al registrar pregonero", Toast.LENGTH_LONG).show();
                    }
                };

                RegisterRequest registerrequest = new RegisterRequest(nameUser, lastnameUser, birthdateUser, emailUser, phoneUser, passUser, passConfirmUser, responseListener, errorListener);

                RequestQueue queue = Volley.newRequestQueue(Registro.this);

                queue.add(registerrequest);

            }
        });




    }


    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (id == DATE_ID)
            return new DatePickerDialog(this, mDateSetListener, sYear, sMonth, sDay);


        return null;
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener =

            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    Toast.makeText(Registro.this, year+"-"+(month+1)+"-"+dayOfMonth+"", Toast.LENGTH_LONG).show();

                    etBirthUser.setText(year+"-"+(month+1)+"-"+dayOfMonth+"");

                }
            };



    public void viewTerms (View view)
    {
        Intent intentterms = new Intent(Registro.this, WebViewTermsDataActivity.class);
        Registro.this.startActivity(intentterms);
    }


}
