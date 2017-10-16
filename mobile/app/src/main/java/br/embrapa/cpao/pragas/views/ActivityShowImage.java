package br.embrapa.cpao.pragas.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.FileNotFoundException;

import br.embrapa.cpao.pragas.controllers.FotosController;
import br.embrapa.cpao.pragas.models.PragaFoto;
import br.embrapa.cpao.pragas.utils.CarregarEficienteBitmap;
import br.embrapa.cpao.pragas.utils.ImageHelper;
import br.embrapa.cpao.pragas.utils.K;
import br.embrapa.cpao.pragas.utils.Util;
import br.embrapa.cpao.pragas.widget.TouchImageView;
import br.embrapa.cpao.pragas.R;

import static br.embrapa.cpao.pragas.utils.ImageHelper.decodeBitmapFromByte;
import static br.embrapa.cpao.pragas.utils.ImageHelper.decodeBitmapFromUri;

/**
 * Atividade responsável por exibir uma imagem que foi selecionada da galeria de imagens da ActivityPraga{@link ActivityPraga}
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class ActivityShowImage extends ActivityApp {

    /**
     * Foto que será exibida
     */
    private TouchImageView ivFoto;

    /**
     * Crédito da foto
     */
    private TextView tvFotografo, tvDescricaoFoto;

    /**
     * layoute pai que contem tvFotografo e tvDescricaoFoto
     */
    private LinearLayout llCredito;

    /**
     * identificador da praga, para poder voltar para activity anterior
     */
    private int id_praga;

    private FotosController fotosController=new FotosController(this);
    /**
     * É a primeira função a ser executada na Activity.
     * É responsável por carregar os layouts XML e outras operações de inicialização.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_imagem);
        ivFoto = (TouchImageView) findViewById(R.id.img);
        tvFotografo = (TextView) findViewById(R.id.tv_fotografo);
        tvDescricaoFoto=(TextView) findViewById(R.id.tv_descricao_foto);
        llCredito = (LinearLayout) findViewById(R.id.ll_credito);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //recupera titulo que foi passado por intent para essa activity exibir
        String mTitle = getIntent().getExtras().getString(K.TITULO);
        updateCustomActionBar(mTitle);
        carregaDadosPraga();

    }

    /**
     * Função nativa do android executa quando botão de voltar do smartphone for precionado.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityPraga.class);
        intent.putExtra("ID", id_praga);
        startActivity(intent);
        finish();
    }

    /**
     * Recupera dados passado para intent da atividade e em seguida carrega foto de acordo com a qual foi selecionada na atividade anterior ({@link ActivityPraga})
     */
    private void carregaDadosPraga() {

        id_praga = getIntent().getExtras().getInt("ID_PRAGA");
        int id_foto = getIntent().getExtras().getInt("ID_FOTO");

        PragaFoto foto = fotosController.get(id_foto);

        //exibe creditos na foto
        if((foto.getFotografo()==null||foto.getFotografo().isEmpty()) &&
                (foto.getDescricao()==null || foto.getDescricao().isEmpty()))
            llCredito.setVisibility(View.GONE);
        else {
            if(foto.getFotografo()!=null || !foto.getFotografo().isEmpty())
                tvFotografo.setText("Foto: "+foto.getFotografo());
            if(foto.getDescricao()!=null || !foto.getDescricao().isEmpty())
                tvDescricaoFoto.setText(foto.getDescricao());
        }

        //trata imagem a ser exibida
        //captura largura e altura da tela
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        Log.e("Screen FONE","width: "+screenWidth+" - height: "+screenHeight);
        //processa imagem para ser exibida na tela
        try {
            Bitmap bmp;
            if(screenWidth<500)
                bmp = decodeBitmapFromUri(this, foto.getUri(this), 200, 200);
            else
                bmp = decodeBitmapFromUri(this, foto.getUri(this), screenWidth/2, screenWidth/2);
            ivFoto.setImageBitmap(bmp);
        } catch (FileNotFoundException e) {
            Crashlytics.logException(e);
            Log.d("Erro ao abrir imagem","não foi possível abrir imagem", e);
            Toast.makeText(this,"Não foi possível abrir imagem", Toast.LENGTH_SHORT).show();
            finish();
        }
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
}
