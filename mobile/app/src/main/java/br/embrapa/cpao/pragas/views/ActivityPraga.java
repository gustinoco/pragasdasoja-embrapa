package br.embrapa.cpao.pragas.views;

import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import br.embrapa.cpao.pragas.Adapters.AdapterGaleria;
import br.embrapa.cpao.pragas.controllers.FotosController;
import br.embrapa.cpao.pragas.controllers.PragasController;
import br.embrapa.cpao.pragas.fragments.Fragmento_ListaPragas;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.models.PragaFoto;
import br.embrapa.cpao.pragas.widget.TouchImageView;
import br.embrapa.cpao.pragas.R;

/**
 * Atividade responsável por mostrar todas as informações de uma praga que foi selecionada
 * no fragmento {@link Fragmento_ListaPragas} da atividade main {@link ActivityMain}.
 * Serão exibido uma galeria de fotos da praga, uma lista de botões que abrem uma nova atividade que mostra
 * informaçẽos da praga de acordo com o botão clicado.
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class ActivityPraga extends ActivityApp {
    private Button btOque, btBiologia, btComportamento, btDanos, btLocalizacao,
            btControle, btBibliografia, btAutor;
    private TextView tvNome, tvNomeCientifico;
    private RecyclerView rvGaleria;
    private ArrayList<TouchImageView> listaImagens;
    private Praga praga;
    private PragasController pragasController = new PragasController(this);


    /**
     * É a primeira função a ser executada na Activity.
     * É responsável por carregar os layouts XML e outras operações de inicialização.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praga);

        tvNome = (TextView) findViewById(R.id.tvNome);
        tvNomeCientifico = (TextView) findViewById(R.id.tvNomeCientifico);
        listaImagens = new ArrayList<TouchImageView>();
        btOque = (Button) findViewById(R.id.btOque);
        btBiologia = (Button) findViewById(R.id.btBiologia);
        btComportamento = (Button) findViewById(R.id.btComportamento);
        btDanos = (Button) findViewById(R.id.btDanos);
        btLocalizacao = (Button) findViewById(R.id.btLocalizacao);
        btControle = (Button) findViewById(R.id.btControle);
        btBibliografia = (Button) findViewById(R.id.btBibliografia);
        //btAutor = (Button) findViewById(R.id.btAutor);

        // preenche os dados da praga no layout
        carregaDadosPraga();


        // altera titulo da actionbar
        updateCustomActionBar(praga.getNome());
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/
        //paintToolbarSystem();

        // Inicia listeners
        initListeners();

    }

    /**
     * Função nativa do android executa quando botão de voltar do smartphone for precionado,
     * antes da ação de voltar acontecer é feito uma limpeza na memório manualmente e depois é aberto atividade Main '{@link ActivityMain}'
     */
    @Override
    public void onBackPressed() {
        limparMemoria();
        //Intent intent = new Intent(this, ActivityMain.class);
        //startActivity(intent);
        finish();
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
     * Método responsável por carregar dados da praga
     */
    private void carregaDadosPraga() {
        int id_praga = getIntent().getExtras().getInt("ID");

        praga = pragasController.get(id_praga);
        tvNomeCientifico.setText(praga.getNomeCientifico());
        carregarGaleria();
    }


    /**
     * Método responsável por criar galeria com imageviews criado em código java
     */
    private void carregarGaleria() {
        //recupera do banco de dados local as fotos da praga
        FotosController fotosController = new FotosController(this);
        List<PragaFoto> fotos = fotosController.listar(praga.getId());
        praga.setFotos(fotos);

        rvGaleria= (RecyclerView)findViewById(R.id.rv_galeria);

        if(fotos.isEmpty())
            rvGaleria.setVisibility(View.GONE);
        else {
            rvGaleria.setHasFixedSize(true);
            int orientation = getResources().getConfiguration().orientation;
            int land= android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            int port = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            int other = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvGaleria.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            }else
                rvGaleria.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvGaleria.setAdapter(new AdapterGaleria(this, praga));
        }

    }

    /**
     * Método responsável por limpar memória
     */
    public void limparMemoria() {
        for (PragaFoto f : praga.getFotos()) {
            f.setFoto(null);
            f = null;
        }
        praga = null;
    }


    /**
     * LISTENERS dos componentes desta atividade
     */
    private void initListeners() {
        final Class activity = ActivityInfo.class;
        //DESCRIÇÃO
        btOque.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(0);
            }
        });
        //BIOLOGIA
        btBiologia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(1);
            }
        });
        //COMPORTAMENTO
        btComportamento.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(2);
            }
        });
        //DANOS
        btDanos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(3);
            }
        });
        //LOCALIZAÇÃO
        btLocalizacao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(4);
            }
        });
        //CONTROLE
        btControle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(5);
            }
        });
        //BIBLIOGRAFIA
        btBibliografia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(6);
            }
        });
        //AUTOR
        /*btAutor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(7);
            }
        });*/

    }
    /** FIM LISTENERS */

    /**
     * Abre atividade que exibe conteudo sobre a praga
     *
     * @param position
     */
    private void openActivity(int position) {
        Intent intent = new Intent(this, ActivityInfo.class);
        intent.putExtra("titulo", praga.getNome());
        //parametro correspondente a qual pagina deve ser exibida na proxima activty
        intent.putExtra("position", position);
        //conteudo das paginas
        intent.putExtra("oque", praga.getDescricao());
        intent.putExtra("biologia", praga.getBiologia());
        intent.putExtra("comportamento", praga.getComportamento());
        intent.putExtra("danos", praga.getDanos());
        intent.putExtra("localizacao", praga.getLocalizacao());
        intent.putExtra("controle", praga.getControle());
        intent.putExtra("bibliografia", praga.getBibliografia());
        //intent.putExtra("autor", praga.getAutor().getId());
        //intent.putExtra("autor",praga.);
        startActivity(intent);
    }
}
/**
 * FIM
 */
