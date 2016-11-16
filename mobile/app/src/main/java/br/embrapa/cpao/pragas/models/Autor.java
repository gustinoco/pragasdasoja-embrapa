package br.embrapa.cpao.pragas.models;

import java.io.Serializable;

import br.embrapa.cpao.pragas.dao.EntidadePersistivel;

/**
 * Classe responsável por abstrair informações do Autor
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class Autor implements EntidadePersistivel, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private String telefone2;
    private String descricao;
    private byte[] foto;
    private String strFoto;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getStrFoto() {
        return strFoto;
    }

    public void setStrFoto(String strFoto) {
        this.strFoto = strFoto;
    }


}
