package br.embrapa.cpao.pragas.views;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.embrapa.cpao.pragas.R;


/**
 * Activity pai de todas, todas as activitys devem ser herdadas desta, qualquer modificação em comum
 * deve ser feita nessa activity. Manter este padrão para futuras modificações
 *
 * Created by franco on 13/03/16.
 */
public abstract class ActivityApp extends AppCompatActivity {


    /**
     * Atualiza actionbar
     *
     * @param titulo
     */
    public void updateActionBar(String titulo) {
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayShowTitleEnabled(true);
        toolbar.setTitle(titulo);
        toolbar.setDisplayHomeAsUpEnabled(true);
        paintToolbarSystem();

    }

    public void updateCustomActionBar(String titulo){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);

        TextView tituloToolbar=(TextView) toolbar.findViewById(R.id.toolbar_title);
        tituloToolbar.setText(titulo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        //altera cor da seta de voltar
        if (Build.VERSION.SDK_INT >= 23 ){
            Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            // mudar para
            //Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }else{
            Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
        paintToolbarSystem();
    }

    /**
     * Pinta barra de status do sistema, apenas para versões igual ou acima da Lollipop
     *
     */
    protected void paintToolbarSystem() {

        //configura parametros para versão do android LOLLIPOP ou >
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //cor da barra de status do sistema
            window.setStatusBarColor(getResources().getColor(R.color.primaryDark));
        }
    }

}