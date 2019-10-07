package com.login.pregoneros;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class LoginRequest extends  StringRequest{

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"login";

    private Map<String,String> params;

    LoginRequest (String emailUser, String passUser, Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();

        //params.put("Content-Type", "application/json");
        //params.put("Accept", "application/json");

        params.put("email",emailUser);
        params.put("password",passUser);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }



}
