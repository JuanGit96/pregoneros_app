package com.login.pregoneros;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class RedentionRequest  extends StringRequest {

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"user/redention";

    private Map<String,String> params;

    RedentionRequest (String apiToken, String code, String usuario_redime, Response.Listener<String> listener)
    {
        super(Request.Method.POST, REGISTER_REQUEST_URL+"?api_token="+apiToken, listener, null);

        params = new HashMap<>();

        params.put("code",code);
        params.put("usuario_redime",usuario_redime);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
