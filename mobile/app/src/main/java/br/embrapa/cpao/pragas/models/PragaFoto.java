package br.embrapa.cpao.pragas.models;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.Serializable;

import br.embrapa.cpao.pragas.dao.EntidadePersistivel;
import br.embrapa.cpao.pragas.utils.Util;

/**
 * Classe responsável por abstrair informações de uma Foto de uma Praga{@link Praga}
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class PragaFoto implements EntidadePersistivel, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer id_praga;
    private byte[] foto;
    private String fotoBase64;
    private String fotografo;
    private String descricao;

    public PragaFoto() {
    }

    public String getFileName(){
        return this.id_praga+"-"+this.id+".jpg";
    }

    public Uri getUri(Context ctxt){
        File file = new File(Util.getFilePath(ctxt)+"/"+getFileName());
        return Uri.fromFile(file);
    }
    public PragaFoto(int id, int id_praga){
        this.id = id;
        this.id_praga = id_praga;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Integer getIdPraga() {
        return id_praga;
    }

    public void setIdPraga(Integer id_praga) {
        this.id_praga = id_praga;
    }

    public String getFotografo() {
        return fotografo;
    }

    public void setFotografo(String fotografo) {
        this.fotografo = fotografo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }


}
