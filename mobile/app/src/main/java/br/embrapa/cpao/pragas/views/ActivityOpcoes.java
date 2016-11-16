package br.embrapa.cpao.pragas.views;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import br.embrapa.cpao.pragas.controllers.PragasController;
import br.embrapa.cpao.pragas.interfaces.ResultTask;
import br.embrapa.cpao.pragas.Adapters.AdapterListOpcoes;
import br.embrapa.cpao.pragas.utils.ItemListView;
import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.utils.K;
import br.embrapa.cpao.pragas.utils.UpgradeSystem;

/**
 * Atividade contêm lista de opções e configurações da aplicação.
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class ActivityOpcoes extends ActivityApp{

    /**
     * Lista que exibe opções da aplicação
     */
    private ListView lvOpcoes;


    /**
     * É responsável por carregar o menu XML e inflar na Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        //altera titulo
        String mTitle = getString(R.string.opcoes);
        updateCustomActionBar(mTitle);

        //infla lista com xml
        lvOpcoes = (ListView) findViewById(R.id.lvLista);
        //inicial lista
        carregarLista();
        //inicia listeners
        initListeners();
    }

    /**
     * Método responsável por inicializar listview com dados estáticos
     */
    private void carregarLista() {
        ArrayList<ItemListView> itens = new ArrayList<ItemListView>();
        itens.add(new ItemListView(getString(R.string.atualizar_bd), getString(R.string.atualizar_bd_descricao), R.drawable.ic_download));
        itens.add(new ItemListView(getString(R.string.enviar_praga), getString(R.string.enviar_praga_descricao), R.drawable.ic_send));
        itens.add(new ItemListView(getString(R.string.fale_conosco), getString(R.string.fale_conosco_descricao), R.drawable.ic_talk));
        itens.add(new ItemListView(getString(R.string.ajuda), getString(R.string.ajuda_descricao), R.drawable.ic_help3));
        AdapterListOpcoes adapter = new AdapterListOpcoes(this, itens);
        lvOpcoes.setAdapter(adapter);
    }

    /**
     * Função nativa do android executa quando botão de voltar do smartphone for precionado,
     * antes da ação de voltar acontecer é aberto atividade Main '{@link ActivityMain}' e em seguida esta atividade ActivityOpcoes é finalizada.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * É responsável por carregar o menu XML e inflar na Activity.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simples, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Função responsável por tratar eventos de clique no menu da Activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método responsável por conter todos os listeners dos itens do listview de opções
     */
    private void initListeners() {
        lvOpcoes.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //atualizar bd
                if (position == 0)
                    atualizarBancoDeDados();
                //enviar praga desconhecida
                if (position == 1)
                    startActivity(new Intent(getApplicationContext(), ActivityEnviarPragaDesconhecida.class));
                //enviar feedback
                if (position == 2)
                    startActivity(new Intent(getApplicationContext(), ActivityFeedBack.class));
                //abrir pagina de ajuda
                if (position == 3) {
                    String url = K.ENDERECO_AJUDA;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }

            }
        });
    }


    /**
     * Método responsável por baixar novas pragas e atualizar banco de dados.
     */

    private void atualizarBancoDeDados() {
        Intent intent = new Intent(this, ActivityApresentacao.class);
        intent.putExtra("TIPO","UPDATE");
        startActivity(intent);

    }

    /**
     * Função responsável por inicializar processo de download para atualizar a base de dados, método chamado
     * apenas quando  opção de atualizar foi selecionado.
     */
    public void download() {
        final ProgressDialog progress = ProgressDialog.show
                (this,
                        getString(R.string.aguarde),
                        getString(R.string.atualizando_bd),
                        true, true);

        new UpgradeSystem(this,new ResultTask() {
            @Override
            public void publishProgess(int progess) {
                //progess
            }

            @Override
            public void completeTask() {
                String msg = "Atualização foi concluída com sucesso!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                progress.dismiss();
            }

            @Override
            public void failedTask() {
                String msg = "Falha, verifique sua conexão e tente novamente!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        }).execute();
    }

}
