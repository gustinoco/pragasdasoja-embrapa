package br.embrapa.cpao.pragas.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.util.Base64;

import br.embrapa.cpao.pragas.models.Autor;


/**
 * Classe responsável por por persistir Autor no banco de dados local.
 * @author Juarez Arce Franco Junior
 * @version 1.0
 * @deprecated
 */
public class AutorDAO extends DAOBasic<Autor>{

	public static final String NOME_TABELA 		= "Autor";
	public static final String COLUNA_ID 		= "id";
	public static final String COLUNA_NOME		= "nome";
	public static final String COLUNA_EMAIL		= "email";
	public static final String COLUNA_TELEFONE 	= "telefone";
	public static final String COLUNA_TELEFONE2	= "telefone2";
	public static final String COLUNA_DESCRICAO	= "descricao";
	public static final String COLUNA_FOTO 	   	= "foto";
	
	public static final String SCRIPT_CRIAR_TABELA =
					"CREATE TABLE "+NOME_TABELA+" ( "+
					COLUNA_ID 		+" INTEGER PRIMARY KEY, " +
					COLUNA_NOME		+" TEXT, " +
					COLUNA_TELEFONE	+" TEXT, " +
					COLUNA_EMAIL	+" TEXT, " +
					COLUNA_TELEFONE2+" TEXT, "+
					COLUNA_DESCRICAO+" TEXT, "+
					COLUNA_FOTO		+" BLOB )";
	
	public static final String SCRIPT_DELETAR_TABELA =
					"DROP TABLE IF EXISTS "+NOME_TABELA;

	private static AutorDAO instance = null;
	
	//CONSTRUTOR
	public AutorDAO(Context context) {
		super(context);
	}
	
	//RETORNA INSTANCIA ATIVA
	public static AutorDAO getInstance(Context context){
		if(instance==null)
			instance = new AutorDAO(context);
		return instance;
	}
	
	@Override
	public String getNomeTabela() {
		return NOME_TABELA;
	}

	@Override
	public String getNomeColunaPrimaryKey() {
		return COLUNA_ID;
	}
	
	@Override
	public void salvar(List<Autor> autores){
		if(autores!=null)
			for(Autor a : autores){
				super.salvar(a);
			}
	}

	@Override
	public ContentValues entidadeParaContentValues(Autor autor) {
		ContentValues values = new ContentValues();
		values.put(COLUNA_ID, autor.getId());
		values.put(COLUNA_NOME, autor.getNome());
		values.put(COLUNA_EMAIL, autor.getEmail());
		values.put(COLUNA_TELEFONE, autor.getTelefone());
		values.put(COLUNA_TELEFONE2, autor.getTelefone2());
		values.put(COLUNA_DESCRICAO, autor.getDescricao());
		if(autor.getStrFoto()!=null)
			values.put(COLUNA_FOTO, Base64.decode(autor.getStrFoto(), Base64.DEFAULT));
		
		return values;
	}


	@Override
	public Autor contentValuesParaEntidade(ContentValues valores) {
		Autor autor = new Autor();
		autor.setId(valores.getAsInteger(COLUNA_ID));
		autor.setNome(valores.getAsString(COLUNA_NOME));
		autor.setEmail(valores.getAsString(COLUNA_EMAIL));
		autor.setTelefone(valores.getAsString(COLUNA_TELEFONE));
		autor.setTelefone2(valores.getAsString(COLUNA_TELEFONE2));
		autor.setDescricao(valores.getAsString(COLUNA_DESCRICAO));
		autor.setFoto(valores.getAsByteArray(COLUNA_FOTO)); 	
		return autor;
	}
}
