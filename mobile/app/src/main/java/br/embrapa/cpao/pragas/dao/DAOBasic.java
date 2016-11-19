package br.embrapa.cpao.pragas.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

/**
 * Classe abstrata responsável por CRUD com o db do dispositivo,
 * Todos os DAOs de entidades devem herdar desta classe, para reaproveitar código.
 * @author Juarez Arce Franco Junior
 * @version 1.0
 * @param <T>
 */
public abstract class DAOBasic <T extends EntidadePersistivel>{

	protected SQLiteDatabase dataBase = null;
	public abstract String getNomeTabela();
	public abstract String getNomeColunaPrimaryKey();
	public abstract ContentValues entidadeParaContentValues(T entidade);
	public abstract T contentValuesParaEntidade(ContentValues valores);
	
	private Context context;
	
	//CONSTRUTOR
	protected DAOBasic(Context context) {
		this.context = context;
		BDHelper bdHelper = BDHelper.getInstance(context);
		dataBase = bdHelper.getWritableDatabase();
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	
	
	public Context getContext(){
		return context;
	}
	
	public void fecharConexao(){
		if(dataBase!=null && dataBase.isOpen())
			dataBase.close();
	}
	public void salvar(List<T> entidades){
		for(T t : entidades){
			dataBase.replace(getNomeTabela(), null, entidadeParaContentValues(t));
		}
	}
	public void salvar(T entidade){
		dataBase.replace(getNomeTabela(), null, entidadeParaContentValues(entidade));
	}
	
	public void deletar(T entidade){

		String[] arrayArgumentos = {String.valueOf(entidade.getId())};
		dataBase.delete(getNomeTabela(), getNomeColunaPrimaryKey()+" = ?", arrayArgumentos);

	}
	
	public void deletarTodos() {
        dataBase.execSQL("DELETE FROM " + getNomeTabela());
    }
	
	public void editar(T entidade){
		ContentValues valores = entidadeParaContentValues(entidade);
		String[] arrayArgumentos = {String.valueOf(entidade.getId())};
		dataBase.update(getNomeTabela(), valores, getNomeColunaPrimaryKey()+" = ?", arrayArgumentos);
	}
	
	public List<T> recuperarTodos(){
		
		String sql = "SELECT * FROM "+getNomeTabela();
		List<T> list = new ArrayList<T>(recuperarPorQuerySQL(sql));

		return list;
	}
	
	public T recuperarPorID(Integer id){
		String sql = "SELECT * FROM "+getNomeTabela()+" WHERE "+getNomeColunaPrimaryKey()+" = "+id;
		List<T> result = recuperarPorQuerySQL(sql);
		if(result==null || result.isEmpty())
			return null;
		else
			return result.get(0);
	}

	public T recuperarUm(){
		String sql = "SELECT * FROM "+getNomeTabela();
		List<T> result = recuperarPorQuerySQL(sql);
		if(result==null || result.isEmpty())
			return null;
		else
			return result.get(0);
	}
	
	public List<T> recuperarPorQuerySQL(String sql){
		Cursor cursor = dataBase.rawQuery(sql, null);
		List<T> results = new ArrayList<T>();
		if(cursor.moveToFirst()){
			do{
				ContentValues valores = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(cursor, valores);
				T entidade = contentValuesParaEntidade(valores);
				results.add(entidade);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return results;
	}
	
}
