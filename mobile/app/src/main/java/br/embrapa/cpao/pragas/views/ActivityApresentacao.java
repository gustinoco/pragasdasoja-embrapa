package br.embrapa.cpao.pragas.views;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.embrapa.cpao.pragas.controllers.PragasController;
import br.embrapa.cpao.pragas.interfaces.ResultTask;
import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.models.Praga;
import br.embrapa.cpao.pragas.services.DownloadBroadcastReceiver;
import br.embrapa.cpao.pragas.services.UpgradeService;
import br.embrapa.cpao.pragas.utils.K;

/**
 * Atividade de apresentação. Nela contém textos de boas vindas e trata de executar download da base de dados.
 * Classe responsável por atividade inicial da aplicação, quando é recem instalado.
 * @interface ServiceConnection implementação para iniciar comunicação com serviço de segundo plano
 * @interface ResultTask implementação para receber msgs do serviço de segundo plano
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class ActivityApresentacao extends Activity implements ResultTask, ServiceConnection{

    /**
     * Variaveis indicam estado do botão btAction.
     */
    private int BUTTON_STATUS= 2;
    private final int BT_CANCELADO=1;
    private final int BT_TENTAR_NOVAMENTE =2;
    private final int BT_CONCLUIDO=3;



    /**
     * Botao para Cancelar/TentarNovamente/Concluir (3 em 1)
     */
    private Button btAction;

    /**
     * Componentes indica progresso do download das pragas
     */
    private ProgressBar progressBar, progressBar2;

    /**
     * Componentes de informações do estado da Activity
     */
    private TextView tvInfo2, tvInfo;


    /**
     * Instancia do serviço que será executado em segundo plano para fazer download de dados
     */
    private UpgradeService upgradeService;

    /**
     * Status se classe está conectada com o serviço em segundo plano
     */
    private boolean mBound = false;

    //status broadcast
    private boolean statusBroadcast = false;
    private static boolean UP=true;
    private static boolean DOWN=false;

    private int progress=0;

    /**
     * Responsável por receber progresso do download do serviço de segundo plano que faz o download.
     */
    private DownloadBroadcastReceiver downloadBroadcastReceiver;

    /**
     * Status se serviço está em execução ou não
     */
    private boolean runService =false;

    private boolean UPDATE=false;

    /**
     * É a primeira função a ser executada na Activity.
     * É responsável por carregar os layouts XML e outras operações de inicialização.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Remove titulo da actionBar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //infla layout
        setContentView(R.layout.activity_apresentacao);

        btAction        = (Button) findViewById(R.id.btAction);
        progressBar     = (ProgressBar) findViewById(R.id.progressBar);
        progressBar2    = (ProgressBar) findViewById(R.id.progressBarra);
        tvInfo          = (TextView) findViewById(R.id.tvInfo);
        tvInfo2         = (TextView) findViewById(R.id.tvInfo2);


         String tipo = getIntent().getStringExtra("TIPO");
        //fazer atualização
        if(tipo != null && tipo.equals("UPDATE")){
            UPDATE=true;
            BUTTON_STATUS = BT_TENTAR_NOVAMENTE;
            tvInfo.setText("Atualize agora!");
            tvInfo2.setText("");
            btAction.setText("Download");
            btAction.setBackgroundColor(getResources().getColor(R.color.positvo));
            progressBar.setVisibility(View.INVISIBLE);
            progressBar2.setVisibility(View.INVISIBLE);
        }else{
        //fazer primeira atualização da base de dados
            paintButton(BT_CANCELADO);
            List<Praga> listaPragas = new PragasController(this).listar();
            if(listaPragas!=null && !listaPragas.isEmpty()){
                startActivity(new Intent(this, ActivitySplash.class));
                finish();
                return;
            }
            //inicia download das pragas
            upService();
        }
    }

    /**
     * chamado ao encerrar atividade
     */
    @Override
    protected void onDestroy(){

        if(runService) {
            cancelService();
        }
        super.onDestroy();
    }


    /**
     * chamado Ao minimizar tela
     */
    @Override
    protected void onPause(){
        Log.i("onPause ","paused");
        downBroadcastReceiver();
        super.onPause();
    }

    /**
     * Implementação do botão voltar
     */
    @Override
    public void onBackPressed(){
        if(UPDATE)
            super.onBackPressed();
        else
            moveTaskToBack(true);
    }


    @Override
    protected  void onResume(){
        super.onResume();
        upBroadcastReceiver();
        if(upgradeService!=null)
            publishProgess(upgradeService.getProgress());
        Log.i("onResume ","resumed");

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onSart ","started");
        //cria comunicação com o serviço se ele estiver rodando
        if(runService)
            bindService(getIntentUpgradeService(), this, 0);
    }

    @Override
    protected void onStop() {
        Log.e("onStop ","stoped");
        //encerra comunicação da classe com o serviço
        if (mBound) {
            unbindService(this);
            mBound = false;
        }
        super.onStop();
    }


    /**
     * Listener do botão cancelar/tentar novamente
     * @param btView
     */
    public void btActionListener(View btView){
        if(!btClickable())
            return;
        switch (BUTTON_STATUS){
            case BT_CANCELADO:
                btActionCancelarListener();
                break;
            case BT_TENTAR_NOVAMENTE:
                btActionTentarNovamenteListener();
                break;
            case BT_CONCLUIDO:
                btActionConcluirListener();
                break;
        }
    }

    /**
     * Guarda tempo do ultimo clique no botão btAction.
     * (Evita que usuario clique varias vezes simultanea com intuito de travar o sistema)
     */
    private long lastClickTime=0;

    /**
     * Define intervalo de tempo que o botao pode ser clicado, evitando duplos cliques
     * @return
     */
    private boolean btClickable(){
        if (SystemClock.elapsedRealtime() - lastClickTime < 2000){
            return false;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return true;
    }
    /**
     * - Cancela serviço de download das pragas
     * - altera o estilo do botão para exibir tentar novamente
     */
    private void btActionCancelarListener() {
        paintButton(BT_TENTAR_NOVAMENTE);
        //cancela serviço
        cancelService();
    }

    /**
     * Inicia aplicação
     */
    private void btActionConcluirListener(){

        downService();
        Intent intent = new Intent(this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * - inicia o serviço de download.
     * - altera o estilo do botão para exibir cancelar
     */
    private void btActionTentarNovamenteListener(){
        paintButton(BT_CANCELADO);
        //inicia serviço
        upService();

    }

    /**
     * Derruba serviço em segundo plano responsável por fazer download da base de dados
     */
    private void downService(){
        if(upgradeService!=null)
            upgradeService.cancelNotification();
        if(mBound) {
            unbindService(this);
            mBound = false;
        }
        stopService(getIntentUpgradeService());
        runService =false;
    }

    private void cancelService(){
        upgradeService.cancel();
        downService();
    }


    /**
     * Método responsável por preparar informações para fazer download de dados do webservice,
     * Método precisa de um update, substituir implementação de Thread por AsyncTask
     */
    private void upService(){
        runService =true;
        //cria o serviço de segundo plano para fazer download das pragas
        startService(getIntentUpgradeService());
        bindService(getIntentUpgradeService(), this, 0);
    }


    /**
     * Inicia aplicação, só deve ser iniciado quando download da base de dados for concluída
     */
    private void completeServiceUpdate(){
            paintButton(BT_CONCLUIDO);
            downService();
    }


    /**
     * Cria um BroadCaster que vai fazer a comunicação entre serviço de segundo plano com a activity
     * Esse broadcaster tem a função de atualizar o progresso do download
     */
    private void upBroadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter(K.BROADCAST_DOWNLOAD);
        //cria broadcastreceiver responsável por receber informações do serviço de upgrade de segundo plano
        if (downloadBroadcastReceiver == null)
            downloadBroadcastReceiver = new DownloadBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                downloadBroadcastReceiver,
                intentFilter
        );
        statusBroadcast=UP;
    }



    private void downBroadcastReceiver(){
        //downloadBroadcastReceiver.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadBroadcastReceiver);
    }


    private Intent getIntentUpgradeService(){
        return new Intent(this,UpgradeService.class);
    }




    /**
     * Método atualiza progresso do download das pragas
     * @param progess
     */
    @Override
    public void publishProgess(int progess ){
        this.progress=progess;
        if(progess<=0) {
            paintButton(BT_TENTAR_NOVAMENTE);
        }else if(progess>0 && progess<100){
            paintButton(BT_CANCELADO);
        }else if(progress==200){
            paintButton(BT_CONCLUIDO);
            tvInfo.setText("Sistema já está atualizado!");
        }else{
            paintButton(BT_CONCLUIDO);
        }
    }

    /**
     * Método é chamado quando download das pragas é concluido com sucesso
     */
    @Override
    public void completeTask() {
        completeServiceUpdate();
    }

    /**
     * Método é chamado quando ocorre alguma falha no download das pragas
     */
    @Override
    public void failedTask() {
        Log.e("ResultTask","failedTask");
        Toast.makeText(getApplicationContext(),
                "Falha ao fazer download, verifique sua conexão e tente novamente!",
                Toast.LENGTH_LONG).show();
        paintButton(BT_TENTAR_NOVAMENTE);
        downService();
    }

    private void paintButton(int status){

        switch (status){
            case BT_CANCELADO:
                BUTTON_STATUS = BT_CANCELADO;
                btAction.setText("Cancelar");
                btAction.setBackgroundColor(getResources().getColor(R.color.cancel));
                tvInfo.setText("Atualizando base de dados");
                tvInfo2.setText("Aguarde alguns instantes...");
                progressBar.setVisibility(View.VISIBLE);
                progressBar2.setVisibility(View.VISIBLE);
                progressBar2.setProgress(progress);
                break;
            case BT_TENTAR_NOVAMENTE:
                BUTTON_STATUS = BT_TENTAR_NOVAMENTE;
                btAction.setText("Tentar novamente");
                btAction.setBackgroundColor(getResources().getColor(R.color.positvo));
                tvInfo.setText("Não foi possível concluir download");
                tvInfo2.setText("tente novamente");
                progressBar.setVisibility(View.INVISIBLE);
                progressBar2.setVisibility(View.INVISIBLE);
                progressBar2.setProgress(0);
                break;
            case BT_CONCLUIDO:
                BUTTON_STATUS = BT_CONCLUIDO;
                tvInfo.setText("Atualização concluída!");
                tvInfo2.setText("");
                progressBar.setVisibility(View.INVISIBLE);
                progressBar2.setVisibility(View.INVISIBLE);
                btAction.setText("OK");
                btAction.setBackgroundColor(getResources().getColor(R.color.verde));
                break;
        }

    }
    /**
     * Conecta classe com o serviço se estiver ativo
     * @param componentName
     * @param service
     */
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        UpgradeService.LocalBinder binder = (UpgradeService.LocalBinder) service;
        upgradeService = binder.getService();
        if(upgradeService!=null) {
            upgradeService.setActivityInNotification(ActivityApresentacao.class);
            progress = upgradeService.getProgress();

            //download em progress
            if (progress > 0 && progress < 100) {
                paintButton(BT_CANCELADO);
            //download concluido
            }else if(progress>=100){
                paintButton(BT_CONCLUIDO);
                completeServiceUpdate();
            }else{
            //download interrompido
                paintButton(BT_TENTAR_NOVAMENTE);
            }

        }
        mBound = true;
    }

    /**
     * Desconecta classe do serviço
     * @param componentName
     */
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBound = false;
    }




}
