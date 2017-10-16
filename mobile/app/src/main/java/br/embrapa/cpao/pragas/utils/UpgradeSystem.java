package br.embrapa.cpao.pragas.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import br.embrapa.cpao.pragas.dao.FotoDAO;
import br.embrapa.cpao.pragas.dao.PragaDAO;
import br.embrapa.cpao.pragas.interfaces.ResultTask;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.models.PragaFoto;
import br.embrapa.cpao.pragas.resource.FotoResource;
import br.embrapa.cpao.pragas.resource.PragaResource;

/**
 * Created by franco on 21/03/16.
 */
public class UpgradeSystem extends AsyncTask<Void, Integer,Boolean> {
    private boolean canceled=false;
    private ResultTask resultTask;
    private Context context;

    public UpgradeSystem(Context context, ResultTask resultTask) {
        this.resultTask = resultTask;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        PragaDAO daoPraga = PragaDAO.getInstance(context);
        FotoDAO daoFoto = FotoDAO.getInstance(context);

        List<Praga> novasPragas;
        boolean sucesso = false;
        int cont=0;

        //primeiro baixa lista de todos os ids de pragas que existem no banco externo
        //faz isso para verificar se alguma praga do banco local foi excluido no banco externo
        List<Integer> idsPragasExistentesExterno = PragaResource.listaIdsPragasExistentes(context);
        if(idsPragasExistentesExterno==null)
            return false;
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
        if(total==0) {
            publishProgress(cont, total);
            return true;
        }



        // baixar pragas e salvar no banco
        // o download é feito dentro do laço, pois o processo é feito em
        // partes
        // só chega ao fim quando a lista retornada é vazia, significando que
        // todos os objetos foram baixados
        do {
            //verifica se upgrade foi cancelado
            if(canceled){
                sucesso=false;
                break;
            }
            novasPragas = null;//libera memória

            //faz o download das pragas, exceto das que ja existem.
            try {
                novasPragas = PragaResource.downloadPragas(context,
                        daoPraga.recuperarMapPragasExistentes());
            } catch (UnsupportedEncodingException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
                publishProgress(0);
                return false;
            }

            //verifica falha ou cancelamento
            if (novasPragas == null || canceled) {
                sucesso = false;
                break;
            }

            //salva pragas no banco de dados
            if (!novasPragas.isEmpty()) {
                //faz download das fotos da praga
                for(Praga p : novasPragas){
                    for(PragaFoto f : p.getFotos()){
                        try {
                            //faz download da foto
                            FotoResource.download(context,f);
                            //salva foto no banco de dados
                            daoFoto.salvar(f);
                            f.setFoto(null);//libera memoria
                        } catch (IOException e) {
                            Crashlytics.logException(e);
                            Log.d("ERRO DOWNLOAD", "não foi possível fazer download da foto " + f.getId() + " da praga " + p.getId(), e);
                            e.printStackTrace();
                        }
                    }
                    daoPraga.salvar(p);
                }
            }else
                sucesso= true;

            cont ++;
            //envia progresso do download para quem implementou ResultTask
            publishProgress(cont,total);
        } while (!novasPragas.isEmpty());
        novasPragas = null;//libera memória,acredite para telefones antigos com pouca memoria ram isso resolveu
        return sucesso;
    }

    @Override
    public void onProgressUpdate(Integer... param){
        int progress;
        if(param[1]!=0)
            progress = (param[0]*100)/param[1];
        else
            progress=100;
        resultTask.publishProgess(progress);
    }
    @Override
    public void onPostExecute(Boolean success){
        if(success) {
            resultTask.publishProgess(100);
            resultTask.completeTask();
        }else
            resultTask.failedTask();
    }

    public void cancel() {
        this.canceled = true;
    }
}

