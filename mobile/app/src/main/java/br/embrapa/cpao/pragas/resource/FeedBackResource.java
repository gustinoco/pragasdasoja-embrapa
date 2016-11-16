package br.embrapa.cpao.pragas.resource;

import android.content.Context;

import br.embrapa.cpao.pragas.http.HttpResponse;
import br.embrapa.cpao.pragas.http.HttpSync;
import br.embrapa.cpao.pragas.models.Feedback;
import br.embrapa.cpao.pragas.models.UsuarioRetorno;
import br.embrapa.cpao.pragas.utils.K;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por fazer comunicação com webservice trocando informações sobre Feedback {@link Feedback} ou enviar praga desconhecida {@link UsuarioRetorno}
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class FeedBackResource {
    private static final String ENDERECO = K.ENDERECO + "web_service/feedback/";

    //responsável por fazer transferencia dos dados com o servidor
    //Sincronismo sincronismo;

    //contexto atual da aplicação
    Context context;

    public FeedBackResource(Context context){
        this.context = context;
    }
    /**
     * Envia Feedback do usuario
     *
     * @param fb
     * @return
     */
    public String enviarFeedBack(Feedback fb) {
        String endereco = ENDERECO + "salvar.php";
        Map<String,String> params = new HashMap<>();
        params.put("nomeUsuario",fb.getNomeUsuario());
        params.put("descricao",fb.getDescricao());
        params.put("email",fb.getEmail());
        params.put("classificacao",fb.getClassificacao()+"");

        String result = new HttpSync<String>(context,"POST",endereco,params) {
            @Override
            protected String erro(HttpResponse httpResponse) {
                return K.FALHA_CONEXAO;
            }

            @Override
            protected String success(HttpResponse httpResponse) {
                return K.SUCESSO;
            }
        }.execute();
        //sincronismo = new Sincronismo();
        //json = sincronismo.POST(context, endereco, json);
        return result;
    }

    /**
     * Envia praga desconhecida
     *
     * @param user
     * @return
     */
    public String enviarPragaDesconhecida(UsuarioRetorno user) {
        String endereco = ENDERECO + "enviar_praga_desconhecida.php";
        Map<String,String> params = new HashMap<>();
        params.put("json",new Gson().toJson(user));

        String result = new HttpSync<String>(context,"POST",endereco,params) {
            @Override
            protected String erro(HttpResponse httpResponse) {
                return K.FALHA_CONEXAO;
            }

            @Override
            protected String success(HttpResponse httpResponse) {
                return K.SUCESSO;
            }
        }.execute();

        return result;
    }

    public void cancelarSincronismo(){
        //sincronismo.cancelRequest();
    }
}
