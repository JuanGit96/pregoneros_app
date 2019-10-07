package com.login.pregoneros;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class PushRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"pushNotification/";

    PushRequest (String api_token, int idUser, Response.Listener<String> listener)
    {
        super(Request.Method.GET, USER_REQUEST_URL+idUser+"?api_token="+api_token, listener, null);
    }

}
