package br.embrapa.cpao.pragas.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import br.embrapa.cpao.pragas.Adapters.SectionsPagerAdapter;
import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.fragments.FragmentPage;
import br.embrapa.cpao.pragas.fragments.FragmentPageAutor;
import br.embrapa.cpao.pragas.utils.Util;

/**
 * Atividade que exibe as informações, como descrição, localização, bibliografia e etc.
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class ActivityInfo extends ActivityApp {

    /**
     * Adapter do ViewPager, possui e gerencia as paginas que sera exibidas no ViewPager
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * ViewPager é o componente de paginas dessa Activity
     */
    ViewPager mViewPager;

    /**
     * Menu de abas para escolher pagina do componente {@link ViewPager}
     */
    PagerSlidingTabStrip mTabs;


    /**
     * cria view
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //atualiza action bar
        updateCustomActionBar(getIntent().getStringExtra("titulo"));

        //inicia componentes
        initActionBar();
        initAdapter();
        initViewPager();
        initTabs();

    }

    private void initActionBar(){
        getSupportActionBar().setElevation(0);
    }
    /**
     * Inicial adapter do viewPager, responsavel por conter e gerenciar as paginas que serao exibidas
     */
    private void initAdapter() {
        //Cria adapter do ViewPager, responsável por controlar as paginas do ViewPager
        List<Fragment> pages = loadPages();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), pages);
    }

    /**
     * Inicia viewpager da activity
     */
    private void initViewPager() {
        // Recupera ViewPager
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
    }

    /**
     * Inicia componente de abas que controla o ViewPager
     */
    private void initTabs() {
        //recupera abas para navegação no ViewPager
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //passa view pager para o componente de abas poder gerencialo
        mTabs.setViewPager(mViewPager);
        //adiciona efeito de sombra
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            mTabs.setElevation(0);
        }
    }

    /**
     * Trata dados que foi passado para essa activty por Intent,
     * e cria paginas que serão exibidas no ViewPager, passando o conteudo e o titulo para cada pagina,
     * o retorno sera uma lista de fragmentos, que devem ser inflados no ViewPager da atividade.
     *
     * @return pages
     */
    private List<Fragment> loadPages() {
        List<Fragment> pages = new ArrayList<Fragment>();
        //intent para recuperar dados que foi passado para essa activty
        Intent i = getIntent();
        pages.add(FragmentPage.newInstance("O que é", i.getStringExtra("oque")));
        pages.add(FragmentPage.newInstance("Biologia", i.getStringExtra("biologia")));
        pages.add(FragmentPage.newInstance("Comportamento", i.getStringExtra("comportamento")));
        pages.add(FragmentPage.newInstance("Danos", i.getStringExtra("danos")));
        pages.add(FragmentPage.newInstance("Localização", i.getStringExtra("localizacao")));
        pages.add(FragmentPage.newInstance("Controle", i.getStringExtra("controle")));
        pages.add(FragmentPage.newInstance("Literatura recomendada", i.getStringExtra("bibliografia")));

        //pages.add(FragmentPageAutor.newInstance("Autor",i.getIntExtra("autor",0)));
        return pages;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simples, menu);
        return true;
    }

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
