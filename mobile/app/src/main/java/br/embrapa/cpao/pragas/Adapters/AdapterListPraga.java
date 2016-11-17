package br.embrapa.cpao.pragas.Adapters;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.models.PragaFoto;
import br.embrapa.cpao.pragas.utils.ImageHelper;

/**
 * Adapter do Listview da atividade Main {@link br.embrapa.cpao.pragas.views.ActivityMain}
 * ou no Fragmento {@link br.embrapa.cpao.pragas.fragments.Fragmento_ListaPragas}.
 * Responsável por adicionar foto, nome, nome cientifico no item da lista.
 *
 * OBS.: Neste adapter criei um mecanismo de filtro, este filtro exibe as pragas de acordo com o parametro
 * passado no método executeQuery
 *
 * @author Juarez Arce Franco Junior
 * @category adapter
 * @version 1.0
 *
 */
public class AdapterListPraga extends BaseAdapter {

    private LayoutInflater mInflater;

    /** itens do adapter, esse será dinamico pois poderá ser filtrado pelo nome da praga**/
    private List<Praga> itens;

    /** deve ser final, itensOriginal, é a cópia original com todas as pragas**/
    private final List<Praga> itensOriginal;

    private String query;
    private boolean flagQuery;

    public AdapterListPraga(Context context, List<Praga> itensPraga) {
        if(itensPraga == null)
            throw new NullPointerException("Itens do AdapterListPraga não pode ser NULL");

        // Itens que preencheram o listview
        this.itens = itensPraga;
        //guarda referencia dos itens originais
        itensOriginal = copiarRefItens(itensPraga);
        // responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    /** Guarda referencia de todos os ITENS em um List diferente**/
    private List<Praga> copiarRefItens(List<Praga> pragas){
        List<Praga> copyRefItem = new ArrayList<Praga>();
        for(Praga p : pragas){
            copyRefItem.add(p);
        }
        return copyRefItem;
    }

    /**
     * Método responsável por  filtrar itens de acordo com o parametros passado.
     * @param query string referente ao nome da praga que deseja ser filtrada
     * TODO: algoritimo de busca da praga.
     */
    public void executeQuery(String query){
        //remove todos os itens
        itens.clear();
        if(query==null || query.isEmpty())
            //se vazio então copia os itens originais (todos)
            itens = copiarRefItens(itensOriginal);
        else{
            String nome1 = removerAcentos(query).toLowerCase();
            //adiciona apenas itens que se igualam com a query
            for(Praga p : itensOriginal){
                //filtra os nomes que "contem "
                String nome2 = removerAcentos(p.getNome());
                if(nome2.toLowerCase().contains(nome1))
                    itens.add(p);
                /*if (nome2.toLowerCase().startsWith(nome1))
                    itens.add(p);*/
            }
        }
        //atualiza tela
        notifyDataSetChanged();
    }
    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

    }
    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Trata dados que serão exibidos na tela
     * @param position
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Praga p = itens.get(position);
        ItemSuporte itemSuporte;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_lista, null);
            itemSuporte = new ItemSuporte(view);
            view.setTag(itemSuporte);
        } else {
            itemSuporte = (ItemSuporte) view.getTag();
        }

        itemSuporte.update(p);

        return view;

    }

    /**
     * Responsável por fazer cast dos itens do xml e carregalo com informações
     */
    private class ItemSuporte {
        private TextView tvTitulo;
        private TextView tvDescricao;
        private ImageView ivFoto;
        private ImageHelper ivHelper;

        public ItemSuporte(View view){
            tvTitulo = (TextView) view.findViewById(R.id.tvTitulo);
            tvDescricao = (TextView) view.findViewById(R.id.tvDescricao);
            ivFoto = (ImageView) view.findViewById(R.id.ivFoto);
        }
        public void update(Praga p){
            tvTitulo.setText(p.getNome());
            //tvDescricao.setText(p.getNomeCientifico());
            tvDescricao.setText(Html.fromHtml("<i>" + p.getNomeCientifico() + "</i>"));


            if(ivHelper!=null){
                ivHelper.cancel(true);
                ivHelper = null;
            }
            //Previnido que a tela trave caso de um scroll muito rápido;
            ivFoto.setImageBitmap(null);
            ivFoto.setImageResource(R.drawable.ic_praga);

            //exibe imagem
            if (ivHelper == null || !ivHelper.isRunning()) {
                //recupera nome de uma foot
                if(p.getFotos()!=null && !p.getFotos().isEmpty() && p.getFotos().get(0)!=null && !p.getFotos().get(0).getFileName().isEmpty()) {
                    PragaFoto thumb = p.getFotos().get(0);
                    Uri uriImage = thumb.getUri(mInflater.getContext());

                    ivHelper = new ImageHelper(ivFoto.getContext(), ivFoto, uriImage, 38, 38);
                    ivHelper.execute();
                }
            }

        }
    }

}
