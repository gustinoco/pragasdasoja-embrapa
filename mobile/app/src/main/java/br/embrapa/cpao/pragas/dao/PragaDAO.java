package br.embrapa.cpao.pragas.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Base64;
import android.util.Log;

import br.embrapa.cpao.pragas.models.Autor;
import br.embrapa.cpao.pragas.models.Categoria;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.models.PragaFoto;
import br.embrapa.cpao.pragas.utils.ComparatorPraga;

/**
 * Classe responsável por por persistir Praga no banco de dados local.
 * 
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class PragaDAO extends DAOBasic<Praga> {

	// ATRIBUTOS RELACIONADOS AO NOME DA TABELA E SUAS RESPECTIVAS COLUNAS
	public static final String NOME_TABELA = "Praga";
	public static final String COLUNA_ID = "id";
	//public static final String COLUNA_AUTOR = "autor";
	public static final String COLUNA_NOME = "nome";
	public static final String COLUNA_NOME_CIENTIFICO = "nome_cientifico";
	public static final String COLUNA_DATA_CRICACAO = "data_criacao";
	public static final String COLUNA_DATA_ALTERACAO = "data_alteracao";
	public static final String COLUNA_DESCRICAO = "descricao";
	public static final String COLUNA_BIOLOGIA = "biologia";
	public static final String COLUNA_COMPORTAMENTO = "comportamento";
	public static final String COLUNA_DANOS = "danos";
	public static final String COLUNA_LOCALIZACAO = "localizacao";
	public static final String COLUNA_CONTROLE = "controle";
	public static final String COLUNA_BIBLIOGRAFIA = "bibliografia";
	public static final String COLUNA_THUMBNAIL = "thumbnail";
	public static final String COLUNA_VERSAO_PUBLICACAO = "versao_publicacao";

	// SCRIPT PARA CRIAR TABELA NO BANCO DE DADOS LOCAL
	public static final String SCRIP_CRIAR_TABELA = "CREATE TABLE "
			+ NOME_TABELA
			+ " ( "
			+ COLUNA_ID	+ " INTEGER PRIMARY KEY, "
			//+ COLUNA_AUTOR+ " INTEGER, " // FOREIGN KEY DE AUTOR
			+ COLUNA_NOME 	+ " TEXT, "
			+ COLUNA_NOME_CIENTIFICO+ " TEXT, "
			+ COLUNA_DATA_CRICACAO	+ " INTEGER, "// guarda valor decimal da data, para recuperar apenas com new Date(data);
			+ COLUNA_DATA_ALTERACAO + " INTEGER, " + COLUNA_DESCRICAO + " TEXT, "
			+ COLUNA_BIOLOGIA + " TEXT, " + COLUNA_COMPORTAMENTO + " TEXT,  "
			+ COLUNA_DANOS + " TEXT, " + COLUNA_LOCALIZACAO + " TEXT,  "
			+ COLUNA_CONTROLE + " TEXT, " + COLUNA_BIBLIOGRAFIA + " TEXT,  "
			+ COLUNA_VERSAO_PUBLICACAO + " INTEGER,  " 
			+ COLUNA_THUMBNAIL+" BLOB "
			//+ "FOREIGN KEY ("+ COLUNA_AUTOR + ") REFERENCES " + AutorDAO.NOME_TABELA + " ("+ AutorDAO.COLUNA_ID + ") "
			+");";

	// SCRIPT PARA DELETAR TABELA, PODE SER CHAMADO QUANDO A VERSÃO DO BANCO DE DADOS É
	// ATUALIZADA
	public static final String SCRIPT_DELETAR_TABELA = "DROP TABLE IF EXISTS "
			+ NOME_TABELA;

	private static PragaDAO instance;

	public static PragaDAO getInstance(Context context) {
		if (instance == null)
			instance = new PragaDAO(context);
		return instance;
	}

	private PragaDAO(Context context) {
		super(context);

	}

	@Override
	public void salvar(List<Praga> pragas) {
		for (Praga p : pragas) {
			salvar(p);
		}
	}





	/**
	 * Método salvar praga no bd
	 */
	@Override
	public void salvar(Praga praga) {
		// salvar fotos da praga
		FotoDAO daoFoto = FotoDAO.getInstance(getContext());
		List<Integer> idsFoto= daoFoto.listaIdFotoPorPraga(praga.getId());

		/*fotos nao sao mais salvo nesse método, elas são baixadas separadas da praga, para nao ocorrer estouro de memória
		for (PragaFoto f : praga.getFotos()) {
			f.setIdPraga(praga.getId());
			// converte String Base64 para array de bytes
			f.setFoto(Base64.decode(f.getFotoBase64(), Base64.DEFAULT));
			f.setFotoBase64(null);
			daoFoto.salvar(f);

		}*/


		//deleta fotos que n correspondem com a atualização caso o sistema esteja fazendo um update
		for(int idFoto : idsFoto){
			boolean existe = false;
			for (PragaFoto f : praga.getFotos()){
				if(idFoto==f.getId()) {
					existe = true;
					break;
				}
			}
			//se foto nao existe na atualização, entao deve removela
			if(!existe){
				daoFoto.deletar(new PragaFoto(idFoto,praga.getId()));
				Log.e("FOTO DELETADA","ID: "+idFoto);
			}
		}

		/*
		 * //salvar Autor AutorDAO daoAutor =
		 * AutorDAO.getInstance(getContext());
		 * daoAutor.salvar(praga.getAutor());
		 */

		// salvar praga
		super.salvar(praga);

		// salvar Categoria
		CategoriaDAO daoCategoria = CategoriaDAO.getInstance(getContext());
		daoCategoria.salvar(praga.getCategorias(), praga.getId());

	}

	@Override
	public void deletar(Praga praga){
		for(int i=0 ; i< praga.getFotos().size();i++) {
			praga.getFotos().get(i).setIdPraga(praga.getId());
			FotoDAO.getInstance(getContext()).deletar(praga.getFotos().get(i));
		}
		super.deletar(praga);
	}

	public void deletar(List<Integer> ids){
		List<Praga> pragas = recuperarByIds(ids);
		if(pragas!=null && !pragas.isEmpty())
			for(Praga p : pragas)
				deletar(p);
	}
	@Override
	public void deletarTodos(){
		List<Praga> pragas = recuperarTodos();
		for(Praga p : pragas){
			deletar(p);
		}
	}

	public List<Praga> recuperarByIds(List<Integer> ids){
		if(ids==null || ids.isEmpty())
			return null;
		String sql = "SELECT " + COLUNA_ID + " , " + COLUNA_NOME + " , "
				+ COLUNA_NOME_CIENTIFICO +" , "+COLUNA_THUMBNAIL+" FROM "+getNomeTabela()+
				" WHERE ";
		int cont =0;
		for(Integer id : ids){
			if(cont==0){
				cont++;
				sql+=" "+COLUNA_ID+" = "+id+" ";
			}else {
				sql += " OR " + COLUNA_ID + " = " + id + " ";
			}
		}
		List<Praga> list = new ArrayList<Praga>(recuperarPorQuerySQL(sql));

		//recupera fotos
		for(Praga p : list){
			FotoDAO daoFoto = FotoDAO.getInstance(getContext());
			List<PragaFoto> fotos = daoFoto.recuperarFotosDaPraga(p.getId());
			p.setFotos(fotos);
		}
		//ordena lista de pragas por ordem alfabetica
		Collections.sort(list, new ComparatorPraga());
		return list;
	}
	@Override
	public List<Praga> recuperarTodos() {

		String sql = "SELECT " + COLUNA_ID + "," + COLUNA_NOME + ","
				+ COLUNA_NOME_CIENTIFICO +",  "+COLUNA_THUMBNAIL+" FROM " + getNomeTabela()
				+ " ORDER BY " + COLUNA_NOME + " ";

		ArrayList<Praga> list = new ArrayList<Praga>(recuperarPorQuerySQL(sql));
		//recupera fotos
		for(Praga p : list){
			FotoDAO daoFoto = FotoDAO.getInstance(getContext());
			List<PragaFoto> fotos = daoFoto.recuperarFotosDaPraga(p.getId());
			p.setFotos(fotos);
		}
		//ordena lista de pragas por ordem alfabetica
		Collections.sort(list, new ComparatorPraga());
		return list;
	}




	public Map<Integer, Integer> recuperarMapPragasExistentes() {
		String sql = "SELECT " + COLUNA_ID + ", " + COLUNA_VERSAO_PUBLICACAO
				+ " FROM " + NOME_TABELA;
		Cursor cursor = dataBase.rawQuery(sql, null);
		Map<Integer, Integer> map = new HashMap<Integer,Integer>();
		if (cursor.moveToFirst()) {
			do {
				int key = cursor.getInt(0);
				int versao = cursor.getInt(1);
				map.put(key, versao);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return map;
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
	public ContentValues entidadeParaContentValues(Praga praga) {
		ContentValues values = new ContentValues();
		values.put(COLUNA_ID, praga.getId());
		//values.put(COLUNA_AUTOR, praga.getAutor().getId());
		values.put(COLUNA_NOME, praga.getNome());
		values.put(COLUNA_NOME_CIENTIFICO, praga.getNomeCientifico());
		if (praga.getDataCriacao() != null)
			values.put(COLUNA_DATA_CRICACAO, praga.getDataCriacao().getTime());
		else
			values.put(COLUNA_DATA_CRICACAO, 0);
		if (praga.getDataAlteracao() != null)
			values.put(COLUNA_DATA_ALTERACAO, praga.getDataAlteracao()
					.getTime());
		else
			values.put(COLUNA_DATA_ALTERACAO, 0);
		values.put(COLUNA_DESCRICAO, praga.getDescricao());
		values.put(COLUNA_BIOLOGIA, praga.getBiologia());
		values.put(COLUNA_COMPORTAMENTO, praga.getComportamento());
		values.put(COLUNA_DANOS, praga.getDanos());
		values.put(COLUNA_LOCALIZACAO, praga.getLocalizacao());
		values.put(COLUNA_CONTROLE, praga.getControle());
		values.put(COLUNA_BIBLIOGRAFIA, praga.getBibliografia());
		if(praga.getStrThumbnail()!=null)
			praga.setThumbnail( Base64.decode(praga.getStrThumbnail(), Base64.DEFAULT) );
		values.put(COLUNA_THUMBNAIL, praga.getThumbnail());
		values.put(COLUNA_VERSAO_PUBLICACAO, praga.getVersaoPublicacao());
		return values;
	}

	@Override
	public Praga contentValuesParaEntidade(ContentValues valores) {
		Praga praga = new Praga();
		praga.setId(valores.getAsInteger(COLUNA_ID));

		// recupera autor
		//AutorDAO daoAutor = AutorDAO.getInstance(getContext());
		//Autor autor = daoAutor.recuperarPorID(valores
		//		.getAsInteger(COLUNA_AUTOR));
		//praga.setAutor(autor);

		// recupera categoria
		CategoriaDAO daoCategoria = CategoriaDAO.getInstance(getContext());
		ArrayList<Categoria> categorias = daoCategoria
				.recuperarCategorias(praga.getId());
		praga.setCategorias(categorias);

		if (valores.containsKey(COLUNA_NOME))
			praga.setNome(valores.getAsString(COLUNA_NOME));
		if (valores.containsKey(COLUNA_NOME_CIENTIFICO))
			praga.setNomeCientifico(valores.getAsString(COLUNA_NOME_CIENTIFICO));
		if (valores.containsKey(COLUNA_DATA_CRICACAO))
			praga.setDataCriacao(new Date(valores
					.getAsLong(COLUNA_DATA_CRICACAO)));
		if (valores.containsKey(COLUNA_DATA_ALTERACAO))
			praga.setDataAlteracao(new Date(valores
					.getAsLong(COLUNA_DATA_ALTERACAO)));
		if (valores.containsKey(COLUNA_DESCRICAO))
			praga.setDescricao(valores.getAsString(COLUNA_DESCRICAO));
		if (valores.containsKey(COLUNA_BIOLOGIA))
			praga.setBiologia(valores.getAsString(COLUNA_BIOLOGIA));
		if (valores.containsKey(COLUNA_COMPORTAMENTO))
			praga.setComportamento(valores.getAsString(COLUNA_COMPORTAMENTO));
		if (valores.containsKey(COLUNA_DANOS))
			praga.setDanos(valores.getAsString(COLUNA_DANOS));
		if (valores.containsKey(COLUNA_LOCALIZACAO))
			praga.setLocalizacao(valores.getAsString(COLUNA_LOCALIZACAO));
		if (valores.containsKey(COLUNA_CONTROLE))
			praga.setControle(valores.getAsString(COLUNA_CONTROLE));
		if (valores.containsKey(COLUNA_BIBLIOGRAFIA))
			praga.setBibliografia(valores.getAsString(COLUNA_BIBLIOGRAFIA));
		if (valores.containsKey(COLUNA_VERSAO_PUBLICACAO))
			praga.setVersaoPublicacao(valores
					.getAsInteger(COLUNA_VERSAO_PUBLICACAO));
		if(valores.containsKey(COLUNA_THUMBNAIL))
			praga.setThumbnail(valores.getAsByteArray(COLUNA_THUMBNAIL));

		// busca fotos
		FotoDAO daoFoto = FotoDAO.getInstance(getContext());
		ArrayList<PragaFoto> fotos;
		if (valores.containsKey(COLUNA_DESCRICAO)) {// significa que quer todas
													// as informações
			// recupera fotos
			fotos = new ArrayList<PragaFoto>(
					daoFoto.recuperarFotosDaPraga(praga.getId()));
		}
		return praga;
	}

}
