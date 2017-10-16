package br.embrapa.cpao.pragas.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import br.embrapa.cpao.pragas.models.PragaFoto;

import static br.embrapa.cpao.pragas.utils.Util.saveArrayToInternalStorage;

/**
 * Classe responsável por por persistir Foto no banco de dados local.
 * 
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class FotoDAO extends DAOBasic<PragaFoto> {

	public static final String NOME_TABELA = "Foto";
	public static final String COLUNA_ID = "id";
	public static final String COLUNA_IDPRAGA = "id_praga";
	public static final String COLUNA_FOTO = "foto";
	public static final String COLUNA_FOTOGRAFO = "fotografo";
	public static final String COLUNA_DESCRICAO = "descricao";

	public static final String SCRIPT_CRIAR_TABELA = "CREATE TABLE "
			+ NOME_TABELA + " ( " + COLUNA_ID + " INTEGER PRIMARY KEY, "
			+ COLUNA_IDPRAGA + " INTEGER, " + COLUNA_FOTO + " BLOB, "
			+ COLUNA_FOTOGRAFO + " TEXT, " + COLUNA_DESCRICAO + " TEXT )";

	public static final String SCRIPT_DELETAR_TABELA = "DROP TABLE IF EXISTS "
			+ NOME_TABELA;

	private static FotoDAO instance = null;

	// CONSTRUTOR
	private FotoDAO(Context context) {
		super(context);
	}

	public static FotoDAO getInstance(Context context) {
		if (instance == null)
			instance = new FotoDAO(context);
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

	public ArrayList<PragaFoto> recuperarUmaFoto(int id_praga) {
		String sql =
				" SELECT " + COLUNA_FOTO + " FROM "+ NOME_TABELA
				+" WHERE "
						+ COLUNA_IDPRAGA + "=" + id_praga + " LIMIT 1";

		return new ArrayList<PragaFoto>(recuperarPorQuerySQL(sql));
	}

	public List<Integer> listaIdFotoPorPraga(int idPraga){
		List<Integer> idsFotos = new ArrayList<>();
		String sql = "SELECT "+ COLUNA_ID +" FROM " +NOME_TABELA+" WHERE "+COLUNA_IDPRAGA +" = "+idPraga;
		Cursor cursor = dataBase.rawQuery(sql, null);

		if(cursor.moveToFirst()){
			do{
				ContentValues valores = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(cursor, valores);
				if(valores.containsKey(COLUNA_ID))
					idsFotos.add(valores.getAsInteger(COLUNA_ID));
			}while(cursor.moveToNext());
		}
		cursor.close();
		return  idsFotos;
	}
	@Override
	public ContentValues entidadeParaContentValues(PragaFoto foto) {
		ContentValues values = new ContentValues();
		values.put(COLUNA_ID, foto.getId());
		values.put(COLUNA_IDPRAGA, foto.getIdPraga());
		values.put(COLUNA_FOTO, foto.getFoto());
		values.put(COLUNA_FOTOGRAFO, foto.getFotografo());
		values.put(COLUNA_DESCRICAO, foto.getDescricao());
		return values;
	}

	@Override
	public void salvar(PragaFoto foto){
		String fileName = foto.getFileName();

		/*if(isExternalStorageWritable()){
			saveArrayToSDCard(getContext(),fileName,foto.getFoto());
		}else{
			saveArrayToInternalStorage(getContext(),fileName,foto.getFoto());
		}*/
		//sempre salva na memoria interna, previne de problemas de como se o usuario trocar de cartao de memória.
		try {

			saveArrayToInternalStorage(getContext(),fileName,foto.getFoto());
			super.salvar(foto);
		} catch (IOException e) {
			Crashlytics.logException(e);
			e.printStackTrace();
			Log.w("InternalStorage", "Erro salvar imagem na memoria interna", e);
		}
	}

	@Override
	public void deletar(PragaFoto foto){
		Uri uriImage =  foto.getUri(getContext());

		String path = uriImage.getPath();
		boolean deletado = new File(path).delete();
		if(deletado)
			Log.i("FOTO DELETADA"," foto "+foto.getId()+" deletada com sucesso da memória interna ("+uriImage.getPath()+")");
		else
			Log.i("FOTO NÃO DELETADA"," foto "+foto.getId()+" não foi deletada da memória interna");
		super.deletar(foto);
	}


	@Override
	public PragaFoto contentValuesParaEntidade(ContentValues valores) {
		PragaFoto foto = new PragaFoto();
		if (valores.containsKey(COLUNA_ID))
			foto.setId(valores.getAsInteger(COLUNA_ID));
		if (valores.containsKey(COLUNA_IDPRAGA))
			foto.setIdPraga(valores.getAsInteger(COLUNA_IDPRAGA));
		if (valores.containsKey(COLUNA_FOTO))
			foto.setFoto(valores.getAsByteArray(COLUNA_FOTO));
		if (valores.containsKey(COLUNA_FOTOGRAFO))
			foto.setFotografo(valores.getAsString(COLUNA_FOTOGRAFO));
		if (valores.containsKey(COLUNA_DESCRICAO))
			foto.setDescricao(valores.getAsString(COLUNA_DESCRICAO));
		return foto;
	}

	public List<PragaFoto> recuperarFotosDaPraga(int idPraga) {
		String sql = "SELECT * FROM " + NOME_TABELA + " WHERE "
				+ COLUNA_IDPRAGA + " = " + idPraga + ";";

		return new ArrayList<PragaFoto>(recuperarPorQuerySQL(sql));
	}
}
