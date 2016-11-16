package br.embrapa.cpao.pragas.resource;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import br.embrapa.cpao.pragas.models.Autor;
import br.embrapa.cpao.pragas.utils.K;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;


/**
 * Classe responsável por fazer comunicação com webservice trocando informações sobre Autor {@link Autor}
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 * @deprecated
 */
public class AutorResource {
    private static final String ENDERECO = K.ENDERECO + "autor/";


    /**
     * Faz download de todos os autores
     *
     * @param context
     * @return
     */
    public static ArrayList<Autor> downloadAutores(Context context) {
        //String endereco = ENDERECO+"download/";
        /*String endereco = ENDERECO + "download.php";

        ArrayList<Autor> autores = new ArrayList<Autor>();
        String json = new Sincronismo().GET(context, endereco);

        Type tipo = new TypeToken<ArrayList<Autor>>() {
        }.getType();
        if (json.equals(K.FALHA_CONEXAO) || json.equals(K.FALHA)) {
            return null;
        }
        try {
            autores = new Gson().fromJson(json, tipo);
        } catch (JsonParseException e) {
            return null;
        }
        return autores;*/
        return null;
    }

}
