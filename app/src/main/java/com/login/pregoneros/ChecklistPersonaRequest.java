package com.login.pregoneros;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class ChecklistPersonaRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"usuarioPregon";

    private Map<String,String> params;

    ChecklistPersonaRequest (String api_token, String pregonId, String campaignId, String clientId, String userId, String userName, String celular, String email,String edad,
                     String sexo, String dondeConoces, String interests, String why, String comments, String ubicacion, String latitud, String longitud,
                             String photo, String video, String audio1, String audio2, String codigo_redime, String pregonType, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, REGISTER_REQUEST_URL+"?api_token="+api_token, listener, errorListener);

        params = new HashMap<>();

        params.put("pregon_id", pregonId);
        params.put("campaign_id", campaignId);
        params.put("client_id", clientId);
        params.put("user_id",userId);
        params.put("nombre",userName);
        params.put("celular",celular);
        params.put("email",email);
        params.put("edad",edad);
        params.put("sexo", sexo);
        params.put("donde_lo_conoces",dondeConoces);
        params.put("interesado",interests);
        params.put("why",why);
        params.put("comentarios",comments);
        params.put("pregonType",pregonType);

        if (!ubicacion.equals("null"))
        {
            params.put("ubicacion",ubicacion);
            params.put("latitud",latitud);
            params.put("longitud",longitud);
        }

        params.put("codigo_redime",codigo_redime);

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
