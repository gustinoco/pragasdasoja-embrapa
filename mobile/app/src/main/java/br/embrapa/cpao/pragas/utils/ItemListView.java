package br.embrapa.cpao.pragas.utils;

import br.embrapa.cpao.pragas.Adapters.AdapterListOpcoes;
import br.embrapa.cpao.pragas.views.ActivityOpcoes;

/**
 * Classe responsávem por abstrair itens do ListView da atividade de opções {@link ActivityOpcoes} e que também é
 * utilizado no adapter {@link AdapterListOpcoes}
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class ItemListView {
    private String titulo;
    private String descricao;
    private int idFoto;

    public ItemListView(String titulo, String descricao, int idFoto) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.idFoto = idFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getIdFoto() {
        return idFoto;
    }
}
