package br.embrapa.cpao.pragas.controllers;

import android.content.Context;
import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.embrapa.cpao.pragas.dao.PragaDAO;
import br.embrapa.cpao.pragas.http.HttpResponse;
import br.embrapa.cpao.pragas.http.HttpSync;
import br.embrapa.cpao.pragas.interfaces.ResultTask;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.resource.PragaResource;

/**
 * Created by franco on 14/03/16.
 */
public class PragasController extends AppController{

    public PragasController(Context context){
        super(context);
    }

    public Praga get(int id){
        return PragaDAO.getInstance(context).recuperarPorID(id);
    }

    public List<Praga> listar(){
        return PragaDAO.getInstance(context).recuperarTodos();
    }
}
