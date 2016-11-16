package br.embrapa.cpao.pragas.models;

import java.io.Serializable;

import br.embrapa.cpao.pragas.dao.EntidadePersistivel;

/**
 * Classe responsável por abstrair informações da Categoria da praga
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class Categoria implements EntidadePersistivel, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * nome da categoria, ex.: soja, milho,etc.
     */
    private String nome;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
