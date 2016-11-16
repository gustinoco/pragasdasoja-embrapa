package br.embrapa.cpao.pragas.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import br.embrapa.cpao.pragas.interfaces.ResultTask;
import br.embrapa.cpao.pragas.utils.K;

/**
 * Created by juarez on 06/06/16.
 */
public class DownloadBroadcastReceiver extends BroadcastReceiver {
    ResultTask resultTask;

    public DownloadBroadcastReceiver(ResultTask resultTask) {
        setResultTask(resultTask);
    }

    public void setResultTask(ResultTask resultTask) {
        this.resultTask = resultTask;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BroadcastReceiver", "Nova intent de comunicação");

        //recupera progresso
        int progress = intent.getExtras().getInt(K.PROGRESS_DOWNLOAD);

        //envia progresso para resulttask
        if(resultTask!=null)
            if(progress==200)
                resultTask.publishProgess(progress);
            else if(progress>=100)
                resultTask.completeTask();
            else if(progress==-1)
                resultTask.failedTask();
            else
                resultTask.publishProgess(progress);

    }
}
