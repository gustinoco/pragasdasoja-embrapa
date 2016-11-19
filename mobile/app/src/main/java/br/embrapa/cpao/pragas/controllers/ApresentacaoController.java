package br.embrapa.cpao.pragas.controllers;

import android.content.Context;

import java.util.List;

import br.embrapa.cpao.pragas.dao.ApresentacaoDAO;
import br.embrapa.cpao.pragas.dao.FotoDAO;
import br.embrapa.cpao.pragas.models.Apresentacao;
import br.embrapa.cpao.pragas.models.PragaFoto;

/**
 * Created by franco on 14/03/16.
 */
public class ApresentacaoController extends AppController{

    public ApresentacaoController(Context context) {
        super(context);
    }

    public Apresentacao getApresentacaoTexto(){
        return ApresentacaoDAO.getInstance(context).recuperarUm();
    }

 }
