package br.embrapa.cpao.pragas.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.embrapa.cpao.pragas.dao.EntidadePersistivel;

/**
 * Classe responsável por abstrair informações da Praga
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class Praga implements EntidadePersistivel, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Autor autor;
    private String nome;
    private String nomeCientifico;
    private Date dataCriacao;
    private Date dataAlteracao;
    private String descricao;
    private String biologia;
    private String comportamento;
    private String danos;
    private String localizacao;
    private String controle;
    private String bibliografia;
    private Integer versaoPublicacao;
    private byte[] thumbnail;
    private String strThumbnail;
    private List<PragaFoto> pragaFotos;
    private List<Categoria> categorias;

    //CONTRUCTOR
    public Praga() {
    }

    public Praga(Integer id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    //GETTERS AND SETTERS
    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeCientifico() {
        return nomeCientifico;
    }

    public void setNomeCientifico(String nomeCientifico) {
        this.nomeCientifico = nomeCientifico;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getBiologia() {
        return biologia;
    }

    public void setBiologia(String biologia) {
        this.biologia = biologia;
    }

    public String getComportamento() {
        return comportamento;
    }

    public void setComportamento(String comportamento) {
        this.comportamento = comportamento;
    }

    public String getDanos() {
        return danos;
    }

    public void setDanos(String danos) {
        this.danos = danos;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setStrThumbnail(String strThumbnail) {
        this.strThumbnail = strThumbnail;
    }

    public String getStrThumbnail() {
        return strThumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public String getControle() {
        return controle;
    }

    public void setControle(String controle) {
        this.controle = controle;
    }

    public String getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(String bibliografia) {
        this.bibliografia = bibliografia;
    }

    public Integer getVersaoPublicacao() {
        return versaoPublicacao;
    }

    public void setVersaoPublicacao(Integer versaoPublicacao) {
        this.versaoPublicacao = versaoPublicacao;
    }

    public List<PragaFoto> getFotos() {
        return pragaFotos;
    }

    public void setFotos(List<PragaFoto> fotos) {
        this.pragaFotos = fotos;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }



    @Override
    public String toString() {
        return "id: " + id +
                "\n" + nome +
                "\n" + nomeCientifico +
                "\nAutor: " + autor.getNome() + "\n" +
                "\nVersao: " + versaoPublicacao;
    }


}
