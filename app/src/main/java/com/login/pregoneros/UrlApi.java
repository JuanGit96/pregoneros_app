package com.login.pregoneros;


public class UrlApi {

    private static final String urlServer = "http://pregoneros.co/";

    private static final String urlApi = urlServer+"api/";


    public static String getUrlApi() {
        return urlApi;
    }

    public static  String getUrlServer()
    {
        return urlServer;
    }
}
