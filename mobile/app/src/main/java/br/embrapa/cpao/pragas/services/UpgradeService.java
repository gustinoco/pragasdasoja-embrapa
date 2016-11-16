package br.embrapa.cpao.pragas.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.dao.FotoDAO;
import br.embrapa.cpao.pragas.dao.PragaDAO;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.models.PragaFoto;
import br.embrapa.cpao.pragas.resource.FotoResource;
import br.embrapa.cpao.pragas.resource.PragaResource;
import br.embrapa.cpao.pragas.utils.K;
import br.embrapa.cpao.pragas.views.ActivityApresentacao;

/**
 * Created by juarez on 06/06/16.
 */
public class UpgradeService extends IntentService  {
    private boolean canceled=false;
    private Context context;
    private boolean finish=false;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyManager;
    private int progress;
    private boolean iniciado=false;

    private final IBinder mBinder = new LocalBinder();


    public void cancel() {
        canceled=true;
    }

    public class LocalBinder extends Binder {

        public UpgradeService getService() {
            // Return this instance of LocalService so clients can call public methods
            return UpgradeService.this;
        }

    }



    public UpgradeService() {
        super("UpgradeService");
        context=this;
    }

    public void cancelNotification(){
        mNotifyManager.cancelAll();

    }

    public int getProgress(){
        return progress;
    }

    /**
     * Ids das pragas que foram baixadas por este serviço
     */
    private List<Integer> idsPragasBaixadas;

    private boolean debug =false;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public void setActivityInNotification(Class<?> cls){
        Intent notificationIntent = new Intent(context, cls);
        PendingIntent padingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        if(mBuilder!=null)
            mBuilder.setContentIntent(padingIntent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("UpgradeService", "Serviço de download iniciado");
        idsPragasBaixadas = null;
        mBuilder =  new NotificationCompat.Builder(this);
        mNotifyManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setContentTitle("Pragas da Soja")
                .setContentText("atualizando base de dados")
                .setSmallIcon(getNotificationIcon(false))
                .setOngoing(true);



        iniciado=true;
        canceled=false;
        if(download()){
            finish=true;
        }else{
            mNotifyManager.cancelAll();
            PragaDAO.getInstance(this).deletar(idsPragasBaixadas);
            //PragaDAO.getInstance(this).deletarTodos();
        }
    }

    private int getNotificationIcon(boolean complete) {
        int idDrawable = complete ? R.drawable.ic_cloud_done_black_24dp : R.drawable.ic_cloud_download_black_24dp;

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? idDrawable : R.mipmap.ic_launcher;
    }

    @Override
    public void onDestroy() {

        Log.e("UpgradeService","Serviço destruido");
        if(iniciado && mNotifyManager!=null && progress<100 || canceled) {
            Log.e("onDestroy.Progresso"," val is "+progress);
            PragaDAO.getInstance(this).deletar(idsPragasBaixadas);
            //PragaDAO.getInstance(this).deletarTodos();
        }
        if(mNotifyManager!=null && progress<100)
            mNotifyManager.cancelAll();
        stopSelf();
        super.onDestroy();
    }

    private void sendProgess(int cont, int total){
        if(canceled) {
            this.progress = 0;
            return;
        }

        if(total>0)
            this.progress = (cont*100)/total;
        if(total==0)
            this.progress=100;
        if(total<0)
            this.progress=-1;
        //informa tipo de intenção
        Intent intent = new Intent(K.BROADCAST_DOWNLOAD);
        //informa progresso de download na intenção
        if(total==0)
            intent.putExtra(K.PROGRESS_DOWNLOAD, 200);//base ja esta atualizada
        else
            intent.putExtra(K.PROGRESS_DOWNLOAD, this.progress);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        mBuilder.setProgress(100, this.progress, false);

        if(this.progress>=100) {
            mBuilder.setContentText("Download completo!")
                    .setProgress(0, 0, false)
                    .setOngoing(false)
                    .setSmallIcon(getNotificationIcon(true));
        }
        mNotifyManager.notify(0, mBuilder.build());
    }

    private boolean download(){
        this.progress=1;
        PragaDAO daoPraga = PragaDAO.getInstance(context);
        FotoDAO daoFoto = FotoDAO.getInstance(context);
        idsPragasBaixadas = new ArrayList<>();

        List<Praga> novasPragas;
        boolean sucesso = false;
        int cont=0;

        //primeiro baixa lista de todos os ids de pragas que existem no banco externo
        //faz isso para verificar se alguma praga do banco local foi excluido no banco externo
        List<Integer> idsPragasExistentesExterno = PragaResource.listaIdsPragasExistentes(context);
        if(idsPragasExistentesExterno==null) {
            sendProgess(0,-1);
            return false;
        }

        Map<Integer,Integer> pragasExistentesInterno = daoPraga.recuperarMapPragasExistentes();
        //percorre ids das pragas internas
        for(int idPragaInt : pragasExistentesInterno.keySet()){
            boolean existe = false;
            //verifica se o id existe ainda na listagem de pragas externas
            for(int idPragaExt : idsPragasExistentesExterno){
                //se existe então ela nao pode ser excluida
                if(idPragaInt == idPragaExt){
                    existe = true;
                    break;
                }
            }
            //se id da praga interna não exste na listagem de ids das pragas externas, significa que ela foi excluida
            if(!existe){
                daoPraga.deletar(new Praga(idPragaInt));
            }
        }

        //baixa quantidade de pragas que será baixada
        int total = PragaResource.countNovas(context, pragasExistentesInterno);
        if(debug)
            total =2;
        if(total==0) {
            sendProgess(cont, total);
            return true;
        }
        if(total==-1){
            sendProgess(0,-1);
            return false;
        }




        // baixar pragas e salvar no banco
        // o download é feito dentro do laço, pois o processo é feito em
        // partes
        // só chega ao fim quando a lista retornada é vazia, significando que
        // todos os objetos foram baixados
        do {
            //verifica se upgrade foi cancelado
            if(canceled){
                return false;
            }
            novasPragas = null;//libera memória

            //faz o download das pragas, exceto das que ja existem.
            try {
                novasPragas = PragaResource.downloadPragas(context,
                        daoPraga.recuperarMapPragasExistentes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                sendProgess(0,-1);
                return false;
            }

            //verifica falha ou cancelamento
            if (novasPragas == null || canceled) {
                return false;
            }

            //salva pragas no banco de dados
            if (!novasPragas.isEmpty()) {
                //faz download das fotos da praga
                for(Praga p : novasPragas){



                    for(PragaFoto f : p.getFotos()){


                        try {
                            //faz download da foto
                            FotoResource.download(context,f);
                            daoFoto.salvar(f);
                            f.setFoto(null);//libera memoria
                        } catch (IOException e) {
                            Log.d("ERRO DOWNLOAD", "não foi possível fazer download da foto " + f.getId() + " da praga " + p.getId(), e);
                            e.printStackTrace();
                            sendProgess(0,-1);
                            return false;
                        }
                    }
                    daoPraga.salvar(p);
                    //salva foto no banco de dados
                    idsPragasBaixadas.add(p.getId()-0);
                }
            }else
                sucesso= true;

            cont ++;
            //envia progresso do download para quem implementou ResultTask
            sendProgess(cont,total);
            if(cont==2 && debug)
                return true;
        } while (!novasPragas.isEmpty());
        novasPragas = null;//libera memória,acredite para telefones antigos com pouca memoria ram isso resolveu
        return sucesso;
    }





}
