package br.embrapa.cpao.pragas.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import br.embrapa.cpao.pragas.models.Categoria;

/**
 * Classe responsável por por persistir Categoria no banco de dados local.
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class CategoriaDAO extends DAOBasic<Categoria>{

	public static final String NOME_TABELA 	= "Categoria";
	public static final String COLUNA_ID 	= "id";
	public static final String COLUNA_NOME	= "nome";
	
	public static final String NOME_TABELA_RELACAO 	= "Praga_Categoria";
	public static final String COLUNA_ID_CATEGORIA	= "categoria_id";
	public static final String COLUNA_ID_PRAGA	= "praga_id";
	
	public static final String SCRIPT_CRIAR_TABELA =
							"CREATE TABLE "+NOME_TABELA+" ( "+
							COLUNA_ID + " INTEGER PRIMARY KEY, " +
							COLUNA_NOME+" TEXT )";
	
	public static final String SCRIPT_CRIAR_TABELA_RELACAO_PRAGA =
							"CREATE TABLE "+NOME_TABELA_RELACAO+" ( "+
							COLUNA_ID_CATEGORIA+" INTEGER, "+
							COLUNA_ID_PRAGA+" INTEGER, "+
							"PRIMARY KEY("+COLUNA_ID_CATEGORIA+","+COLUNA_ID_PRAGA+"), " +
							"FOREIGN KEY("+COLUNA_ID_CATEGORIA+") REFERENCES "+NOME_TABELA+" ("+COLUNA_ID+"), " +
							"FOREIGN KEY("+COLUNA_ID_PRAGA+") REFERENCES "+PragaDAO.NOME_TABELA+" ("+PragaDAO.COLUNA_ID+") )";
	
	public static final String SCRIPT_DELETAR_TABELA =
			"DROP TABLE IF EXISTS "+NOME_TABELA;
	
	public static final String SCRIPT_DELETAR_TABELA_RELACAO =
			"DROP TABLE IF EXISTS "+NOME_TABELA_RELACAO;
	
	private static CategoriaDAO instance = null;
	
	//CONSTRUTOR
	private CategoriaDAO(Context context) {
		super(context);
	}

	public static CategoriaDAO getInstance(Context context){
		if(instance== null)
			instance = new CategoriaDAO(context);
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
	public ContentValues entidadeParaContentValues(Categoria categoria) {
		ContentValues values = new  ContentValues();
		values.put(COLUNA_ID, categoria.getId());
		values.put(COLUNA_NOME, categoria.getNome());
		return values;
	}

	//Recupera categorias de uma praga
	public ArrayList<Categoria> recuperarCategorias(Integer praga_id){
		String sql = "SELECT * FROM "+NOME_TABELA_RELACAO+" WHERE "+COLUNA_ID_PRAGA+"="+praga_id+";";
		
		Cursor cursor = dataBase.rawQuery(sql, null);
		List<Categoria> results = new ArrayList<Categoria>();
		if(cursor.moveToFirst()){
			do{
				ContentValues valores = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(cursor, valores);
				int idCategoria = valores.getAsInteger(COLUNA_ID_CATEGORIA);
				Categoria categoria = recuperarPorID(idCategoria);;
				
				results.add(categoria);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return new ArrayList<Categoria>(results);
	}
	
	
	public void salvar(List<Categoria> categorias, Integer praga_id){
		if(categorias==null) return;
		for(Categoria c : categorias){
			salvar(c);
			Integer categoria_id = c.getId();
			ContentValues values = new ContentValues();
			values.put(COLUNA_ID_CATEGORIA, categoria_id);
			values.put(COLUNA_ID_PRAGA, praga_id);
			dataBase.replace(NOME_TABELA_RELACAO,null,values);
		}
	}
	
	@Override
	public Categoria contentValuesParaEntidade(ContentValues valores) {
		Categoria categoria = new Categoria();
		categoria.setId(valores.getAsInteger(COLUNA_ID));
		categoria.setNome(valores.getAsString(COLUNA_NOME));
		return categoria;
	}
	
}
