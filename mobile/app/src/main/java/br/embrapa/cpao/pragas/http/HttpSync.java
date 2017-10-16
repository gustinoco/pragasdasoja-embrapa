package br.embrapa.cpao.pragas.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.Map;

/**
 * Created by franco on 01/01/16.
 */
public abstract class HttpSync<T> {
    private Context context;
    private String url;
    private String method;
    Map<String,String> params;

    public HttpSync(Context context, String method, String url) {
        this.context = context;
        this.url = url;
        this.method = method;
    }

    public HttpSync(Context context, String method, String url, Map<String, String> params) {
        this.context = context;
        this.url = url;
        this.method = method;
        this.params = params;
    }

    public final T execute() {
        HttpResponse httpResponse = null;
        try {
            switch (method) {
                case "GET":
                    httpResponse = HttpConnection.get(context, url);
                    break;
                case "POST":
                    httpResponse = HttpConnection.post(context,url,params);
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
            httpResponse = new HttpResponse(-1, "erro ao acessar conteúdo");
        }
        return onPostExecute(httpResponse);
    }



    private T onPostExecute(HttpResponse httpResponse) {
        switch (httpResponse.getCode()) {
            case 200:
                return success(httpResponse);
            case 1://sem internet
                return erro1(httpResponse);
            case 404:
                httpResponse.setResponse("Erro 404: Conteúdo não encontrado");
                return erro404(httpResponse);
            case 401:
                httpResponse.setResponse("Erro 401: Sem autorização para acessar o conteúdo no servidor");
                return erro401(httpResponse);
            case 400:
                httpResponse.setResponse("Erro 400: Requisição inválida");
                return erro400(httpResponse);
            case 429:
                httpResponse.setResponse("Erro 429: Taxa limite excedido");
                return erro429(httpResponse);
            case 500:
                httpResponse.setResponse("Erro 500: Ocorreu uma falha interna do servidor");
                return erro500(httpResponse);
            case 503:
                httpResponse.setResponse("Erro 503: Serviço indisponível no momento");
                return erro503(httpResponse);
            default:
                Log.e("ERRO HTTP REQUEST", httpResponse.toString());
                httpResponse.setResponse("Erro de conexão");
                return erro(httpResponse);
        }


    }


    protected abstract T erro(HttpResponse httpResponse);

    //sem conexão com a internet
    protected T erro1(HttpResponse httpResponse) {
        return erro(httpResponse);
    }

    //outros erros
    protected T erro404(HttpResponse httpResponse) {
        return erro(httpResponse);
    }

    protected T erro400(HttpResponse httpResponse) {
        return erro(httpResponse);
    }

    protected T erro401(HttpResponse httpResponse) {return erro(httpResponse);}

    protected T erro429(HttpResponse httpResponse) { return erro(httpResponse);}

    protected T erro500(HttpResponse httpResponse) {
        return erro(httpResponse);
    }

    protected T erro503(HttpResponse httpResponse) {
        return erro(httpResponse);
    }

    //sucesso
    protected abstract T success(HttpResponse httpResponse);
}
