package br.embrapa.cpao.pragas.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import br.embrapa.cpao.pragas.Adapters.AdapterListPraga;
import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.controllers.PragasController;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.views.ActivityApresentacao;
import br.embrapa.cpao.pragas.views.ActivityPraga;

/**
 * Fragmento contêm lista com todas as pragas.
 * Cada item da lista é uma informação de uma praga, ao clicar em um item será aberta a ActivityPraga{@link br.embrapa.cpao.pragas.views.ActivityPraga} com as informações de
 * acordo com a praga clicada.
 * @author Juarez Arce Franco Junior
 * @category fragmento
 * @version 1.0
 *
 */
public class Fragmento_ListaPragas extends Fragment{

    /** Array com todas as pragas do app, este array será referenciado pelo ListView lvLista*/
    private List<Praga> listaPragas = new ArrayList<Praga>();
    /** ListView com todas as pragas que o app contêm*/
    private ListView lvLista;

    /**
     * Adapter para o lista view, responsável por tratar os dados que serão exibidos no lista view
     */
    private AdapterListPraga adapterPragas;

    private PragasController pragasController;

    private boolean firstRun =true;
    /**
     * É a primeira função a ser executada quando fragmento é criado.
     * É responsável por carregar os layouts XML e outras operações de inicialização do fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_pragas, container,false);

        pragasController = new PragasController(getActivity());

        //casting do listaview que exibirá todas as pragas
        lvLista = (ListView)view.findViewById(R.id.lvLista);
        //cria listeners
        initListeners();

        //carrega lista com todas as pragas
        carregaLista();
        return view;
    }

    /**
     * Método responsável por iniciar listeners
     */
    private void initListeners(){
        //clique no item da lista
        lvLista.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cliqueItemDaLista(parent, view, position, id);
            }
        });
    }

    /**
     * Método é chamada quando um item da lista é selecionado.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    private void cliqueItemDaLista(AdapterView<?> parent, View view, int position, long id){
        Praga praga = (Praga) lvLista.getItemAtPosition(position);
        if(praga.getId()==-1)
            return;
        Intent intent = new Intent(getActivity(), ActivityPraga.class);
        intent.putExtra("ID", praga.getId());
        startActivity(intent);
        //getActivity().finish();
    }


    /**
     * carrega dados no listaview
     */
    private void carregaLista(){
        listaPragas = pragasController.listar();

        if(listaPragas==null || listaPragas.isEmpty()){
            Intent intent =new Intent(getActivity(),ActivityApresentacao.class);
            startActivity(intent);
            return;
        }
        adapterPragas = new AdapterListPraga(getActivity(), listaPragas);
        lvLista.setAdapter(adapterPragas);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("Fragmento_ListPragas","onReumse");

    }

    public void refreshList(){
        if(!firstRun) {
            listaPragas.clear();
            listaPragas.addAll(pragasController.listar());
            adapterPragas.notifyDataSetChanged();
            Log.d("Fragmento_ListPragas","Lista de pragas atualizado");
        }
        firstRun=false;
    }

    /**
     * Atualiza ListView de acordo com o que foi passado por parametro
     * @param query
     */
    public void filtrarListaPragas(String query) {
        adapterPragas.executeQuery(query);
    }

}

