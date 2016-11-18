package br.embrapa.cpao.pragas.models;

import java.io.Serializable;

import br.embrapa.cpao.pragas.dao.EntidadePersistivel;

/**
 * Created by Tinoco on 18/11/2016.
 */

public class Apresentacao implements Serializable, EntidadePersistivel {
    private static final long serialVersionUID = 1L;

    private String texto;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }
}
