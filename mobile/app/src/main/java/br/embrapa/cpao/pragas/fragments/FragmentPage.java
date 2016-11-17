package br.embrapa.cpao.pragas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.utils.K;



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
    private TextView txtView;

   private ScaleGestureDetector scaleGestureDetector;


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
        txtView = (TextView) rootView.findViewById(R.id.txtArea);
        txtView.setMovementMethod(new ScrollingMovementMethod());
        txtView.setText(Html.fromHtml(getArguments().getString(K.CONTEUDO)));
        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new simpleOnScaleGestureListener());


        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    scaleGestureDetector.onTouchEvent(event);
                }
                return true;
            }
        });
        return rootView;
    }


    public class simpleOnScaleGestureListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            float size = txtView.getTextSize();

            float factor = detector.getScaleFactor();
            float product = size*factor;
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);

            size = txtView.getTextSize();
            return true;
        }
    }
}
