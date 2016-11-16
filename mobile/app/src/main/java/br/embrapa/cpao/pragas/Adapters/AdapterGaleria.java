package br.embrapa.cpao.pragas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.models.PragaFoto;
import br.embrapa.cpao.pragas.utils.ImageHelper;
import br.embrapa.cpao.pragas.utils.K;
import br.embrapa.cpao.pragas.views.ActivityPraga;
import br.embrapa.cpao.pragas.views.ActivityShowImage;

/**
 * Adapter gerencia imagens da galeria de fotos das pragas
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class AdapterGaleria extends RecyclerView.Adapter<AdapterGaleria.CustomViewHolder> {
    ActivityPraga activity;
    List<PragaFoto> fotos;
    int idPraga;
    String nomePraga;
    int screenWidth,screenHeight;

    public AdapterGaleria(ActivityPraga activity,Praga praga){
        this.activity = activity;
        this.idPraga = praga.getId();
        this.fotos = praga.getFotos();
        this.nomePraga= praga.getNome();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_galeria, viewGroup, false);
        CustomViewHolder vh = new CustomViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder vh, int position) {

        vh.update(fotos.get(position));
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private CardView mItem;
        private ImageView ivFoto;
        private ImageHelper ivHelper;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ivFoto = (ImageView)itemView.findViewById(R.id.iv_foto);
            mItem = (CardView)itemView;
            mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ActivityShowImage.class);
                    intent.putExtra("ID_PRAGA",idPraga);
                    intent.putExtra(K.TITULO,nomePraga);
                    intent.putExtra("ID_FOTO", (int) v.getTag());
                    activity.startActivity(intent);
                    activity.limparMemoria();
                    activity.finish();

                }
            });
        }

        public void update(PragaFoto foto){
            Context ctxt = mItem.getContext();
            mItem.setTag(foto.getId());

            if(ivHelper!=null){
                ivHelper.cancel(true);
                ivHelper = null;
            }

            ivFoto.setImageBitmap(null);
            ivFoto.setImageResource(R.color.white);

            if (ivHelper == null || !ivHelper.isRunning()) {
                if(screenWidth<300)
                    ivHelper = new ImageHelper(ctxt, ivFoto, foto.getUri(ctxt),40, 40);
                else if(screenWidth<500)
                    ivHelper = new ImageHelper(ctxt, ivFoto, foto.getUri(ctxt),50, 50);
                else if(screenWidth>1200)
                    ivHelper = new ImageHelper(ctxt, ivFoto, foto.getUri(ctxt),125, 125);
                else
                    ivHelper = new ImageHelper(ctxt, ivFoto, foto.getUri(ctxt),90, 90);
                ivHelper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

}
