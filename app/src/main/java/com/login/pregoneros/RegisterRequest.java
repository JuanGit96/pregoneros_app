package com.login.pregoneros;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

import static com.login.pregoneros.UrlApi.getUrlApi;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"register";

    private Map<String,String> params;

    RegisterRequest (String nameUser, String lastnameUser, String birthdateUser, String emailUser, String phoneUser,
                            String passUser, String passConfirmUser, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);

        params = new HashMap<>();

        params.put("name",nameUser);
        params.put("email",emailUser);
        params.put("lastName",lastnameUser);
        params.put("password",passUser);
        params.put("password_confirmation",passConfirmUser);
        params.put("dateBirth",birthdateUser);
        params.put("phone",phoneUser);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
