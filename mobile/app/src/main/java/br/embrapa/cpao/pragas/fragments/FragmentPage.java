package br.embrapa.cpao.pragas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.utils.K;
import br.embrapa.cpao.pragas.utils.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Fragmento exibe determinada informação da Praga, exemplo, descrição, biologia, localização, etc.
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class FragmentPage extends Fragment {


    /**
     * Retorna uma instancia da pagina
     */
    public static FragmentPage newInstance(String titulo, String conteudo) {
        FragmentPage fragment = new FragmentPage();
        //pacote de informações do fragmento
        Bundle args = new Bundle();
        //adiciona titulo da pagina e conteudo que será exibido
        args.putString(K.TITULO, titulo);
        args.putString(K.CONTEUDO, conteudo);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Representa conteúdo html que será exibido na pagina
     */
    private WebView wvConteudo;


    /**
     * Construtorda pagina
     */
    public FragmentPage() {

    }

    /**
     * Método chamado no momento que a view está sendo criada
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_text, container, false);
        //recupera webview do fragmento
        wvConteudo = (WebView) rootView.findViewById(R.id.webView);
        //TODO: zoom, pagina das informações das pragas
        //WebSettings webSettings = wvConteudo.getSettings();
        //webSettings.setSupportZoom(true);
        //webSettings.setBuiltInZoomControls(true);
        //webSettings.setDisplayZoomControls(false);
        // disable scroll on touch
        /*wvConteudo.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });*/
        //adiciona conteudo que será exibido na pagina
        String conteudo = getHeader();
        conteudo += getArguments().getString(K.CONTEUDO);
        addConteudo(conteudo);

        return rootView;
    }

    /**
     * Adiciona conteudo no webview que sera exibido na pagina
     *
     * @param conteudo
     */
    private void addConteudo(String conteudo) {
        try {
            wvConteudo.loadData(
                    URLEncoder.encode(conteudo, Util.getCharset()).replaceAll("\\+", " "),
                    "text/html",
                    Util.getCharset()
            );
        } catch (UnsupportedEncodingException e) {
            try {
                wvConteudo.loadData(
                        URLEncoder.encode(getString(R.string.erro_exibir_conteudo), Util.getCharset()).replaceAll("\\+", " "),
                        "text/html",
                        Util.getCharset()
                );
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * Retorna cabeçalho do html que será adicionado no WebView
     *
     * @return
     */
    private String getHeader() {
        return "<html><head> <meta charset='" + Util.getCharset() + "'></head><body> ";
    }


}