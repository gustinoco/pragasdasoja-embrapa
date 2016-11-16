package br.embrapa.cpao.pragas.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.embrapa.cpao.pragas.utils.K;

import java.util.List;

/**
 * Adapter do PagerView, responsável por gerenciar os dados de cada pagina do PagerView
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    /**
     * Lista de fragmentos que são exibidos como pagina no PagerView
     */
    List<Fragment> pages;

    /**
     * Construtor
     *
     * @param fm
     * @param pages
     */
    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> pages) {
        super(fm);
        this.pages = pages;
    }

    /**
     * Retorna um fragmento/pagina do PagerView
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    /**
     * Retorna quantidade de fragmento/paginas que possui
     *
     * @return
     */
    @Override
    public int getCount() {
        return pages.size();
    }

    /**
     * Retorna titulo do fragmento/pagina
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        Fragment page = pages.get(position);
        //recupera titulo que deve existir no pacote de argumentos do fragmento, o titulo deve ser passado no momento
        // criação da instancia da pagina
        return page.getArguments().getString(K.TITULO);
    }
}