package br.embrapa.cpao.pragas.dao;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

import br.embrapa.cpao.pragas.models.Apresentacao;

/**
 * Created by Usuário on 18/11/2016.
 */

public class ApresentacaoDAO extends DAOBasic<Apresentacao> {

    public static final String NOME_TABELA 		= "Apresentacao";
    public static final String COLUNA_TEXTO		= "texto";
    public static final String SCRIPT_CRIAR_TABELA =
            "CREATE TABLE "+NOME_TABELA+" ( "+
                    COLUNA_TEXTO		+" TEXT )";


    public static final String SCRIPT_DELETAR_TABELA =
            "DROP TABLE IF EXISTS "+NOME_TABELA;

    private static ApresentacaoDAO instance = null;

    //CONSTRUTOR
    public ApresentacaoDAO(Context context) {
        super(context);
    }

    //RETORNA INSTANCIA ATIVA
    public static ApresentacaoDAO getInstance(Context context){
        if(instance==null)
            instance = new ApresentacaoDAO(context);
        return instance;
    }

    @Override
    public String getNomeTabela() {
        return NOME_TABELA;
    }

    @Override
    public String getNomeColunaPrimaryKey() {
        return COLUNA_TEXTO;
    }

    @Override
    public ContentValues entidadeParaContentValues(Apresentacao apresentacao) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_TEXTO, apresentacao.getTexto());
        return values;
    }


    @Override
    public Apresentacao contentValuesParaEntidade(ContentValues valores) {
        Apresentacao apresentacao  = new Apresentacao();
        apresentacao.setTexto(valores.getAsString(COLUNA_TEXTO));
        return apresentacao;
    }
}
