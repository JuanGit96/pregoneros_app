package com.login.pregoneros;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class PregonesRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"pregones/";

    PregonesRequest (String api_token, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Request.Method.GET, USER_REQUEST_URL+"?api_token="+api_token, listener, errorListener);
    }
}
