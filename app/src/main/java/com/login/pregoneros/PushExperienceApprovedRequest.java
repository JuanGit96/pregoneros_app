package com.login.pregoneros;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class PushExperienceApprovedRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"pushExpereinces/";

    PushExperienceApprovedRequest(String api_token, int id, Response.Listener<String> listener)
    {
        super(Request.Method.GET, USER_REQUEST_URL+id+"?api_token="+api_token, listener, null);
    }
}