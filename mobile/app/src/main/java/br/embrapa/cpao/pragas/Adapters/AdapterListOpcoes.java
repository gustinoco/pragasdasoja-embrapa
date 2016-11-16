package br.embrapa.cpao.pragas.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.utils.ItemListView;
import br.embrapa.cpao.pragas.views.ActivityOpcoes;

/**
 * Adapter do Listview da atividade de Opções {@link ActivityOpcoes}
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class AdapterListOpcoes extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ItemListView> itens;

    public AdapterListOpcoes(Context context, ArrayList<ItemListView> itens) {
        // Itens que preencheram o listview
        this.itens = itens;
        // responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemListView item = itens.get(position);
        ItemSuporte itemSuporte;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_config, null);
            itemSuporte = new ItemSuporte();
            itemSuporte.tvTitulo = (TextView) view.findViewById(R.id.tvTitulo);
            itemSuporte.tvDescricao = (TextView) view
                    .findViewById(R.id.tvDescricao);
            itemSuporte.ivFoto = (ImageView) view.findViewById(R.id.ivFoto);
            view.setTag(itemSuporte);
        } else {
            itemSuporte = (ItemSuporte) view.getTag();
        }
        itemSuporte.tvTitulo.setText(item.getTitulo());
        itemSuporte.tvDescricao.setText(item.getDescricao());
        itemSuporte.ivFoto.setImageResource(item.getIdFoto());

        return view;

    }

    private class ItemSuporte {
        TextView tvTitulo;
        TextView tvDescricao;
        ImageView ivFoto;
    }


}
