package br.embrapa.cpao.pragas.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.embrapa.cpao.pragas.BuildConfig;
import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.models.UsuarioRetorno;
import br.embrapa.cpao.pragas.models.UsuarioRetornoFoto;
import br.embrapa.cpao.pragas.resource.FeedBackResource;
import br.embrapa.cpao.pragas.utils.ImageHelper;
import br.embrapa.cpao.pragas.utils.K;

/**
 * Ativiadade responsável por enviar dados de pragas desconhecidas
 * Atividade contêm formulário de dados para usuario preencher, esses dados são nome,email, descrição da praga, e fotos.
 * O usuario envia esses dados para nosso webservice.
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class ActivityEnviarPragaDesconhecida extends ActivityApp {
    /**
     * Compenentes graficos
     */
    private EditText etNome, etEmail, etDescricao;
    /**
     * Compenentes graficos
     */
    private ImageView ivFoto1, ivFoto2;

    /**
     * Dados do usuario que enviara praga desconhecida
     */
    private UsuarioRetorno user;

    /**
     * Botão que envia foto
     */
    Button btEnviar;


    /**
     * Uri da imagem capturada pela camera
     */
    Uri uriImage1Camera;
    Uri uriImage2Camera;
    String tag;


    /**
     * É a primeira função a ser executada na Activity.
     * É responsável por carregar os layouts XML e outras operações de inicialização.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_pragadesconhecida);
        String mTitle = getString(R.string.title_enviarpraga);
        updateCustomActionBar(mTitle);

        //recupera componentes graficos do xml
        user = new UsuarioRetorno();
        etNome = (EditText) findViewById(R.id.etNome);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etDescricao = (EditText) findViewById(R.id.etDescricao);
        ivFoto1 = (ImageView) findViewById(R.id.iv1);
        ivFoto2 = (ImageView) findViewById(R.id.iv2);
        btEnviar = (Button) findViewById(R.id.btEnviar);

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
     * Listener responsável por evento de clique no <i>ImageView</i> para selecionar da galeria uma foto ou tirar uma foto
     *
     * @param v(ImageView)
     */
    public void selecionarFoto1(View v) {
        selecionarFoto(v);
    }

    /**
     * Listener responsável por evento de clique no <i>ImageView</i> para selecionar da galeria uma foto ou tirar uma foto
     *
     * @param v(ImageView)
     */
    public void selecionarFoto2(View v) {
        selecionarFoto(v);
    }

    /**
     * Método chamado pelo listener do imageView responsável por mostrar foto de uma praga desconhecida, ao clicar no imageview,
     * esse método é chamado como função de adicionar nova imagem.
     */
    private void selecionarFoto(View v) {
        tag = v.getTag().toString();
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.selecionar_opcao);
        build.setMessage("");
        build.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirCamera("0");
            }
        });
        build.setNegativeButton(R.string.galeria, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirGaleria("1");
            }
        });
        AlertDialog alerta = build.create();
        alerta.show();
    }


    /**
     * chama atividade da camera do smartphone
     */
    private void abrirCamera(String tag) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Crashlytics.logException(e);
            }
            if (photoFile != null) {
                uriImage1Camera = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage1Camera);
                startActivityForResult(intent, Integer.parseInt(tag));
            }
        }
    }

    /**
     * Cria arquivo onde imagem capturada pela camera será salvo.
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    /**
     * chama atividade para abrir aplicativo padrão de galeria de fotos do smartphone
     */
    private void abrirGaleria(String tag) {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), Integer.parseInt(tag));
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(intent, Integer.parseInt(tag));
        }
    }

    /**
     * Método responsável por receber as fotos das atividades da camera ou da galeria
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 0 || requestCode == 1) && resultCode == RESULT_OK) {
            new TarefaLoadFoto(
                    this,
                    data,
                    ivFoto1,
                    ivFoto2,
                    btEnviar,
                    requestCode
            ).execute();
        }
    }

    /**
     * Método responsável por enviar dados que usario entrou para webservice
     */
    public void enviar(View v) {
        if (!verificaCamposVazio())
            return;

        user.setNome(etNome.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setDescricao(etDescricao.getText().toString());
        //executa terefa de envio
        TarefaEnviar tarefaEnviar = new TarefaEnviar(this, user);
        tarefaEnviar.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * Método responsável por limpar todos os campos dos componentes gráficos, é chamado após envio de informações para o <i>webservice</i> ser concluído
     */
    private void limpaCampos() {
        if (etNome != null)
            etNome.setText("");
        if (etEmail != null)
            etEmail.setText("");
        if (etDescricao != null)
            etDescricao.setText("");
        if (ivFoto1 != null)
            ivFoto1.setImageResource(R.drawable.ic_add_photo);
        if (ivFoto2 != null)
            ivFoto2.setImageResource(R.drawable.ic_add_photo);
        user = null;
        user = new UsuarioRetorno();

    }

    /**
     * Método responsável por fazer verificação dos campos antes de enviar os dados para o webservice
     */
    private boolean verificaCamposVazio() {
        String msg = "";
        if (etNome.getText().toString().equals("")) {
            msg = "Preencha seu nome.\n";
        }
        if (etEmail.getText().toString().equals("")) {
            msg += "Preencha seu email.\n";
        }
        if (etDescricao.getText().toString().equals("")) {
            msg += "Preencha uma descrição.";
        }
        if (msg.equals(""))
            return true;
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * Responsável por enviar dados da praga desconhecida para o servidor
     */
    class TarefaEnviar extends AsyncTask<Void, Void, String> {
        Context context;
        ProgressDialog progress;
        Toast sucesso, falha;
        UsuarioRetorno user;

        FeedBackResource fbc;

        public TarefaEnviar(Context context, UsuarioRetorno user) {
            this.context = context;
            this.user = user;
        }

        //inicializa componentes
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setMessage(getString(R.string.aguarde_descricao));
            progress.setCancelable(false);
            progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (fbc != null)
                        fbc.cancelarSincronismo();
                    progress.dismiss();
                    falha = Toast.makeText(context, R.string.cancela_envio, Toast.LENGTH_LONG);
                }
            });
            progress.show();
            sucesso = Toast.makeText(context, R.string.sucesso_enviar_praga_desconhecida, Toast.LENGTH_LONG);
            falha = Toast.makeText(context, R.string.falha_enviar_praga_desconhecida, Toast.LENGTH_LONG);
        }

        //método execultado em uma thread separada da principal para acesso a internet
        @Override
        protected String doInBackground(Void... params) {
            fbc = new FeedBackResource(getApplicationContext());
            String result = fbc.enviarPragaDesconhecida(user);
            return result;
        }

        //atualiza componentes graficos
        @Override
        protected void onPostExecute(String result) {
            if (result != null && result.equals(K.SUCESSO)) {
                sucesso.show();
            } else
                falha.show();
            progress.dismiss();
            limpaCampos();
        }
    }


    //Classe responsável por carregar foto retornada da camera ou galeria e exibir na tela
    class TarefaLoadFoto extends AsyncTask<Void, Void, Void> {
        Context context;
        Intent data;
        ImageView ivFoto1, ivFoto2;
        Bitmap bitmap;
        int requestCode;
        Button btAction;

        public TarefaLoadFoto(Context context, Intent data, ImageView ivFoto1, ImageView ivFoto2, Button btAction, int requestCode) {
            this.context = context;
            this.data = data;
            this.ivFoto1 = ivFoto1;
            this.ivFoto2 = ivFoto2;
            this.btAction = btAction;
            this.requestCode = requestCode;
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                publishProgress();
                //imagem da camera
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int screenWidth = displaymetrics.widthPixels;
                int screenHeight = displaymetrics.heightPixels;

                if (requestCode == 0) {
                    if (screenWidth < 500)
                        bitmap = ImageHelper.decodeBitmapFromUri(context, uriImage1Camera, 200, 200);
                    else
                        bitmap = ImageHelper.decodeBitmapFromUri(context, uriImage1Camera, screenWidth / 2, screenWidth / 2);
                } else {//imagem da galeria de imagens
                    //      Uri imageUriGaleria = data.getData();
                    Uri imageUriGaleria = data.getData();
                    if (screenWidth < 500)
                        bitmap = ImageHelper.decodeBitmapFromUri(context, imageUriGaleria, 200, 200);
                    else
                        bitmap = ImageHelper.decodeBitmapFromUri(context, imageUriGaleria, screenWidth / 2, screenWidth / 2);
                }

                //salva foto
                if (bitmap != null) {
                    String imageBase64 = ImageHelper.encodeToBase64(bitmap);
                    user.getFotos().set(requestCode, new UsuarioRetornoFoto(imageBase64));
                }

            } catch (IOException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            btAction.setEnabled(false);
            btAction.setText("Aguarde carregamento da foto");
            if (ivFoto1.getTag().equals(tag + ""))
                ivFoto1.setImageResource(R.drawable.clock_black);
            if (ivFoto2.getTag().equals(tag + ""))
                ivFoto2.setImageResource(R.drawable.clock_black);
        }

        @Override
        protected void onPostExecute(Void param) {
            //exibe imagem no componente grafico na tela
            if (bitmap != null) {
                if (ivFoto1.getTag().equals(tag + ""))
                    ivFoto1.setImageBitmap(bitmap);
                else
                    ivFoto2.setImageBitmap(bitmap);
            } else {
                //algum erro então volte a imagem default
                if (ivFoto1.getTag().equals(tag + ""))
                    ivFoto1.setImageResource(R.drawable.ic_add_photo);
                else
                    ivFoto2.setImageResource(R.drawable.ic_add_photo);
            }
            btAction.setText("Enviar");
            btAction.setEnabled(true);
        }
    }

}
