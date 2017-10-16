package br.embrapa.cpao.pragas.http;

import android.content.Context;
import android.net.ConnectivityManager;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by franco on 01/01/16.
 */
public class HttpConnection {

    public static HttpResponse post(Context context, String link, Map<String,String> params) throws IOException {
        if(!existeConexao(context)){
            return connectionFailed();
        }

        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15*1000);
        conn.setConnectTimeout(15*1000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();

        conn.connect();

        HttpResponse response = new HttpResponse();
        response.setCode(conn.getResponseCode());

        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String msg = IOUtils.toString(in, "UTF-8");
            response.setResponse(msg);
        }catch (IOException ex){
            Crashlytics.logException(ex);
            ex.printStackTrace();
        }

        return response;
    }
    private static String getQuery(Map<String,String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;


        for (Map.Entry<String, String> pair : params.entrySet())
        {
            System.out.println(pair.getKey() + "/" + pair.getValue());

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private static HttpResponse connectionFailed(){
        return new HttpResponse(1,"Sem conexão com a internet");
    }


    public static HttpResponse get(Context context,String link) throws IOException {
        if(!existeConexao(context)){
            return connectionFailed();
        }
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15*1000);
        conn.setConnectTimeout(15*1000);
        conn.setRequestMethod("GET");

        //le resposta
        HttpResponse response = new HttpResponse();
        response.setCode(conn.getResponseCode());

        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String msg = IOUtils.toString(in, "UTF-8");
            response.setResponse(msg);
        }catch (IOException ex){
            Crashlytics.logException(ex);
            ex.printStackTrace();
        }

        return response;
    }

    /**
     * Método verifica se android tem acesso a internet
     *
     * @param context
     * @return booleam
     */
    private static boolean existeConexao(Context context) {
        boolean conectado = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        }
        return conectado;
    }
}
