package br.embrapa.cpao.pragas.controllers;

import android.content.Context;

import java.util.List;

import br.embrapa.cpao.pragas.dao.FotoDAO;
import br.embrapa.cpao.pragas.models.PragaFoto;

/**
 * Created by franco on 14/03/16.
 */
public class FotosController extends AppController{

    public FotosController(Context context) {
        super(context);
    }

    public PragaFoto get(int idFoto){
        return FotoDAO.getInstance(context).recuperarPorID(idFoto);
    }

    public List<PragaFoto> listar(int idPraga){
        return FotoDAO.getInstance(context).recuperarFotosDaPraga(idPraga);
    }
}
