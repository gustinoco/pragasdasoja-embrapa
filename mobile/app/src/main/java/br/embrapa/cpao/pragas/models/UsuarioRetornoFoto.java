package br.embrapa.cpao.pragas.models;


/**
 * Classe responsável por abstrair informações das Fotos de pragas deconhecidas que o usuario {@link UsuarioRetorno} envia
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class UsuarioRetornoFoto {


    private Integer id;
    private UsuarioRetorno usuarioRetorno;
    private byte[] foto;
    private String strFoto;

    public UsuarioRetornoFoto() {
    }

    public String getStrFoto() {
        return strFoto;
    }

    public void setStrFoto(String strFoto) {
        this.strFoto = strFoto;
    }

    public UsuarioRetornoFoto(byte[] foto) {
        this.foto = foto;
    }

    public UsuarioRetornoFoto(String strFoto) {
        this.strFoto = strFoto;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsuarioRetorno getUsuarioRetorno() {
        return this.usuarioRetorno;
    }

    public void setUsuarioRetorno(UsuarioRetorno usuarioRetorno) {
        this.usuarioRetorno = usuarioRetorno;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}


