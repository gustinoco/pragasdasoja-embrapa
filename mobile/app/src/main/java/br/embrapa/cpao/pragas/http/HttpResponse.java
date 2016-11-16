package br.embrapa.cpao.pragas.http;

import android.graphics.Bitmap;

/**
 * Created by franco on 01/01/16.
 */
public class HttpResponse {

    int code;
    String response;

    public HttpResponse() {
    }

    public HttpResponse(int code) {
        this.code = code;
    }

    public HttpResponse(int code, String response) {
        this.code = code;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString(){
        return response;
    }
}
