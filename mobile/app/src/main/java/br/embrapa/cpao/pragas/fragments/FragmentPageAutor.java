package br.embrapa.cpao.pragas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.dao.AutorDAO;
import br.embrapa.cpao.pragas.models.Autor;
import br.embrapa.cpao.pragas.utils.CarregarEficienteBitmap;
import br.embrapa.cpao.pragas.utils.ImageHelper;
import br.embrapa.cpao.pragas.utils.K;
import br.embrapa.cpao.pragas.utils.Util;

/**
 * Fragmento contem dados do pesquisador dos textos sobre a praga
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 * @deprecated
 */
public class FragmentPageAutor extends Fragment {


    /**
     * Componente gráfico para exibição de informações
     */
    private ImageView ivFotoAutor;
    /**
     * Componente gráfico para exibição de informações
     */
    private TextView tvNome, tvEmail,tvTelefone,tvTelefone2;
    /**
     * Componente gráfico para exibição de informações
     */
    private WebView wvDescricao;

    Autor autor;

    /**
     * Retorna uma instancia da pagina
     */
    public static FragmentPageAutor newInstance(String titulo, int id) {
        FragmentPageAutor fragment = new FragmentPageAutor();
        //pacote de informações do fragmento
        Bundle args = new Bundle();
        //adiciona titulo da pagina e conteudo que será exibido
        args.putString(K.TITULO, titulo);
        args.putInt(K.KEY, id);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Construtor da pagina
     */
    public FragmentPageAutor() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_autor, container, false);

        tvNome 		= (TextView)rootView.findViewById(R.id.tvNome);
        tvEmail 	= (TextView)rootView.findViewById(R.id.tvEmail);
        tvTelefone 	= (TextView)rootView.findViewById(R.id.tvTelefone1);
        tvTelefone2 = (TextView)rootView.findViewById(R.id.tvTelefone2);
        wvDescricao = (WebView)rootView.findViewById(R.id.wvDescricao);
        ivFotoAutor = (ImageView)rootView.findViewById(R.id.ivFotoAutor);

        carregaDadosAutor();
        return rootView;
    }

    /**
     * Carrega dados do pesquisador para exibir na tela
     */
    private void carregaDadosAutor() {

        int id = getArguments().getInt(K.KEY);

        AutorDAO dao = new AutorDAO(getActivity());
        autor = dao.recuperarPorID(id);

        if(autor==null){
            Toast.makeText(getActivity(), "Falha ao carregar dados do Autor", Toast.LENGTH_LONG).show();
            return;
        }
        if(autor.getNome()!=null)
            tvNome.setText(autor.getNome());
        if(autor.getEmail()!=null)
            tvEmail.setText(autor.getEmail());
        if(autor.getTelefone()!=null && !autor.getTelefone().equals(""))
            tvTelefone.setText("Telefone:  "+autor.getTelefone());
        if(autor.getTelefone2()!=null &&!autor.getTelefone2().equals(""))
            tvTelefone2.setText("Telefone:  "+autor.getTelefone2());
        if(autor.getDescricao()!=null&&!autor.getDescricao().equals("")){
            String conteudo = getHeader();
            conteudo += autor.getDescricao();
            try {
                wvDescricao.loadData(URLEncoder.encode(conteudo, Util.getCharset()).replaceAll("\\+"," "), "text/html", Util.getCharset());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(autor.getFoto()!=null)
            ivFotoAutor.setImageBitmap(ImageHelper.decodeBitmapFromByte(autor.getFoto(), 200, 200));

    }

    /**
     * retorna cabeçalho html para adicionar no WebView
     * @return
     */
    private String getHeader() {
        return "<html><head> <meta charset='" + Util.getCharset() + "'></head><body> ";
    }
}
