package br.embrapa.cpao.pragas.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.models.Constants;

public class ActivitySplash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread splash = new Splash();
        splash.start();
    }

   private class Splash extends Thread{
        @Override
        public void run(){
            try {
                sleep(Constants.timeSplash);
                iniciarSistema();
            } catch (InterruptedException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }
    }

    public void iniciarSistema(){
        Intent intent = new Intent(this, ActivityMain.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
