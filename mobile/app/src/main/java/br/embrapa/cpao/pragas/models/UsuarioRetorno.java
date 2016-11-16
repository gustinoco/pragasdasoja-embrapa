package br.embrapa.cpao.pragas.models;

import java.util.ArrayList;

/**
 * Classe responsável por abstrair informações de dados de um possível retorno que o usuario poder dar.
 * Enviando dados de pragas desconhecidas
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class UsuarioRetorno {

    private Integer id;
    private String nome;
    private String email;
    private String descricao;
    private ArrayList<UsuarioRetornoFoto> fotos;

    public UsuarioRetorno() {
        fotos = new ArrayList<UsuarioRetornoFoto>();
        fotos.add(new UsuarioRetornoFoto());
        fotos.add(new UsuarioRetornoFoto());
    }

    public UsuarioRetorno(String nome, String email, String descricao) {
        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setFotos(ArrayList<UsuarioRetornoFoto> usuarioRetornoFotos) {
        this.fotos = usuarioRetornoFotos;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDescricao() {
        return descricao;
    }

    public ArrayList<UsuarioRetornoFoto> getFotos() {
        return fotos;
    }
}


