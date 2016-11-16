package br.embrapa.cpao.pragas.resource;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import br.embrapa.cpao.pragas.http.HttpResponse;
import br.embrapa.cpao.pragas.http.HttpSync;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.utils.K;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;


/**
 * Classe responsável por fazer comunicação com webservice trocando informações sobre Praga {@link Praga}
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class PragaResource {
    private static final String ENDERECO = K.ENDERECO + "web_service/praga/";


    public static List<Integer> listaIdsPragasExistentes(Context context){
        String endereco = ENDERECO + "listaIdsPragasExistentes.php";

        List<Integer>  resultado = new HttpSync<List<Integer>>(context,"GET",endereco){

            @Override
            protected List<Integer> erro(HttpResponse httpResponse) {
                return  null;
            }

            @Override
            protected List<Integer> success(HttpResponse httpResponse) {
                Type tipo = new TypeToken<List<Integer>>() {}.getType();
                return new Gson().fromJson(httpResponse.getResponse(),tipo);
            }
        }.execute();

        return resultado;
    }

    public static int countNovas(Context context,
                                 Map<Integer, Integer> pragasExistentes){
        //String endereco = ENDERECO + "download/";
        String endereco = ENDERECO + "getCountPragas.php?map=";

        if(pragasExistentes==null)
            pragasExistentes = new HashMap<>();

        String json = new Gson().toJson(pragasExistentes);
        endereco+=json;

        int resultado = new HttpSync<Integer>(context,"GET", endereco){
            @Override
            protected Integer erro(HttpResponse httpResponse) {
                return -1;
            }

            @Override
            protected Integer success(HttpResponse httpResponse) {
                String json = httpResponse.getResponse();
                return new Gson().fromJson(json,int.class);
            }
        }.execute();

        return resultado;

    }

    /**
     * Método responsável por baixar novas pragas para o aplicativo, É
     * necessário passar como parametro um HashMap composto por Id da praga(key)
     * e a versao de sua publicação(value) para não baixar pragas repetidas,
     * assim otimizando o processo de download.
     *
     * @param context
     * @return
     */
    public static List<Praga> downloadPragas(Context context,
                                                  Map<Integer, Integer> pragasExistentes) throws UnsupportedEncodingException {

        //String endereco = ENDERECO + "download/";
        String endereco = ENDERECO + "download.php?map=";

        // Converte ArrayList de pragas Existentes para Json
        String json = new Gson().toJson(pragasExistentes);// Sicroniza com webService, e troca informações. Json deve receber Map de Praga
        json = URLEncoder.encode(json, "UTF-8");
        endereco += json;

        //executa download
        HttpSync<List<Praga>> httpSync =
                new HttpSync<List<Praga>>(context,"GET",endereco){

            @Override
            protected List<Praga> erro(HttpResponse httpResponse) {
                return null;
            }

            @Override
            protected List<Praga> success(HttpResponse httpResponse) {
                String json = httpResponse.getResponse();
                Type tipo = new TypeToken<List<Praga>>() {}.getType();
                return new Gson().fromJson(json, tipo);
            }
        };

        List<Praga> novasPragas = httpSync.execute();

        // retorna as novas pragas baixadas
        return novasPragas;
    }
}
