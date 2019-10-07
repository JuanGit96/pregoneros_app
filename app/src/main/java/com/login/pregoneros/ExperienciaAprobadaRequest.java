package com.login.pregoneros;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class ExperienciaAprobadaRequest extends StringRequest {


    private static final String USER_REQUEST_URL =  getUrlApi()+"experiencia/aprobado/";

    ExperienciaAprobadaRequest(String api_token, int id_pregon, int id_usuario, Response.Listener<String> listener)
    {
        super(Request.Method.GET, USER_REQUEST_URL+id_pregon+'/'+id_usuario+"?api_token="+api_token, listener, null);
    }

}
