package br.embrapa.cpao.pragas.resource;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.embrapa.cpao.pragas.http.HttpResponse;
import br.embrapa.cpao.pragas.http.HttpSync;
import br.embrapa.cpao.pragas.models.Apresentacao;
import br.embrapa.cpao.pragas.models.Autor;
import br.embrapa.cpao.pragas.utils.K;


/**
 * Classe responsável por fazer comunicação com webservice trocando informações sobre Autor {@link Autor}
 *
 * @author Gustavo TinocoJunior
 * @version 1.0
 * @deprecated
 */
public class ApresentacaoResource {
    private static final String ENDERECO = K.ENDERECO + "web_service/";


    /**
     * Faz download da
     *
     * @param context
     * @return
     */
    public static Apresentacao atualizaApresentacao(Context context){
        String endereco = ENDERECO + "apresentacao/download.php";


        String result = new HttpSync<String>(context,"GET",endereco,null) {
            @Override
            protected String erro(HttpResponse httpResponse) {
                return K.FALHA_CONEXAO;
            }

            @Override
            protected String success(HttpResponse httpResponse) {
                return httpResponse.getResponse();
            }
        }.execute();




        Gson gson = new Gson();
        Apresentacao apresentacao =  gson.fromJson(result, Apresentacao.class);
        return apresentacao;
    }

}
