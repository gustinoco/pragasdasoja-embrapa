package br.embrapa.cpao.pragas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe responsável por criar e abrir conexões com o banco de dados local.
 * @author user
 * @version 1.0
 */
public class BDHelper extends SQLiteOpenHelper{

	public static final String NOME_BD = "PRAGAS_DO_CAMPO";
	public static final int VERSAO = 2;
	
	private static BDHelper instance;

	public static  BDHelper getInstance(Context context) {
		if(instance==null)
			instance = new BDHelper(context);
		return instance;
	}
	
	public BDHelper(Context context) {
		super(context, NOME_BD,null, VERSAO);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.execSQL(AutorDAO.SCRIPT_CRIAR_TABELA);//removido autor.
		db.execSQL(FotoDAO.SCRIPT_CRIAR_TABELA);
		db.execSQL(CategoriaDAO.SCRIPT_CRIAR_TABELA);
		db.execSQL(PragaDAO.SCRIP_CRIAR_TABELA);
		db.execSQL(CategoriaDAO.SCRIPT_CRIAR_TABELA_RELACAO_PRAGA);
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL(AutorDAO.SCRIPT_DELETAR_TABELA);
		db.execSQL(FotoDAO.SCRIPT_DELETAR_TABELA);
		db.execSQL(CategoriaDAO.SCRIPT_DELETAR_TABELA);
		db.execSQL(PragaDAO.SCRIPT_DELETAR_TABELA);
		db.execSQL(CategoriaDAO.SCRIPT_DELETAR_TABELA_RELACAO);
		onCreate(db);
	}

	


}
