package br.embrapa.cpao.pragas.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.embrapa.cpao.pragas.models.Feedback;
import br.embrapa.cpao.pragas.resource.FeedBackResource;
import br.embrapa.cpao.pragas.utils.K;
import br.embrapa.cpao.pragas.R;

/**
 * Atividade responsável por enviar um feedback ao nosso webservice.
 * Nesta atividade contêm um formulario onde o usuario deve preencher, nome, email, descrição do feedback (elogios, criticas, etc.)
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class ActivityFeedBack extends ActivityApp {
    private EditText etEmail, etFeedBack, etNome;

    /**
     * É a primeira função a ser executada na Activity.
     * É responsável por carregar os layouts XML e outras operações de inicialização.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        String mTitle = getString(R.string.fale_conosco);
        updateCustomActionBar(mTitle);

        etNome = (EditText) findViewById(R.id.etNome);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etFeedBack = (EditText) findViewById(R.id.etFeedBack);

    }

    /**
     * É responsável por carregar o menu XML e inflar na Activity.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simples, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Função responsável por tratar eventos de clique no menu da Activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Método responsável por criar uma thread e enviar o feedback para nosso webservice
     */
    public void enviarFeedBack(View v) {
        if (etEmail.getText().toString().equals("") || etFeedBack.getText().toString().equals("")
                || etNome.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.campos_incompleto), Toast.LENGTH_SHORT).show();
            return;
        }else if (!isEmailValid(etEmail.getText().toString())) {
            Toast.makeText(this, "Email inválido.", Toast.LENGTH_LONG).show();
            return;
            }
        Feedback fb = new Feedback(
                etNome.getText().toString(),
                etEmail.getText().toString(),
                etFeedBack.getText().toString());

        new PostFeedBack(this, fb).execute();

    }
    public boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void limpaCampos(){
        etNome.setText("");
        etEmail.setText("");
        etFeedBack.setText("");
    }

    public class PostFeedBack extends AsyncTask<Void,Void,String>{
        ProgressDialog dialog;
        AlertDialog sucesso, falhaConexao;
        Feedback fb;
        Context ctxt;
        public PostFeedBack(Context ctxt, Feedback fb) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ctxt);
            builder1.setMessage("O seu email foi enviado com sucesso. Agradecemos sua contribuição em breve entraremos em contato.");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            limpaCampos();
                        }
                    });
            AlertDialog alert11 = builder1.create();

            AlertDialog.Builder builder2 = new AlertDialog.Builder(ctxt);
            builder2.setMessage("Erro ao enviar Feedback,  verifique sua conexão com a internet");
            builder2.setCancelable(false);
            builder2.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert22 = builder1.create();
            sucesso = alert11;
            falhaConexao = alert22;
            this.fb = fb;
            this.ctxt = ctxt;
        }
        @Override
        protected void onPreExecute(){
            dialog = new ProgressDialog(ctxt);
            dialog.setTitle("Aguarde");
            dialog.setMessage("Enviando Feedback...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = new FeedBackResource(getApplicationContext()).enviarFeedBack(fb);
            return result;
        }

        @Override
        protected void onPostExecute(String param){
            if(param.equals(K.SUCESSO)){
                sucesso.show();
                limpaCampos();
            }else{
                falhaConexao.show();
            }
            if(dialog.isShowing())
                dialog.dismiss();
        }
    }
}
