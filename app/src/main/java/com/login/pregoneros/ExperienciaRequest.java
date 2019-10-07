package com.login.pregoneros;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class ExperienciaRequest extends StringRequest {


    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"experiences";

    private Map<String,String> params;

    ExperienciaRequest (String api_token, String pregonId, String campaignId, String clientId,String userId, String usernamename,
                             String why, String comments, String youBuyR, String photo, String video, String audio1, String audio2, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, REGISTER_REQUEST_URL+"?api_token="+api_token, listener, errorListener);

        params = new HashMap<>();

        params.put("pregon_id", pregonId);
        params.put("campaign_id", campaignId);
        params.put("client_id", clientId);
        params.put("user_id",userId);
        params.put("name",usernamename);
        params.put("why",why);
        params.put("opinion",comments);
        params.put("lo_comprarias",youBuyR);


        if (!photo.equals("null"))
            params.put("photo",photo);

        if (!video.equals("null"))
            params.put("video",video);

        if (!audio1.equals("null"))
            params.put("audio1",audio1);

        if (!audio2.equals("null"))
            params.put("audio2",audio2);

    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }

}
