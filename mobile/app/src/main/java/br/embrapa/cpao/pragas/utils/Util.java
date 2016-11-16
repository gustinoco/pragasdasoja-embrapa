package br.embrapa.cpao.pragas.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.Normalizer;

import br.embrapa.cpao.pragas.R;

/**
 * Criado por Juarez Arce Franco Junior on 27/08/15.
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class Util {

    /**
     * Função retorna charset para ser usado no webview porque
     * antes de adicionar texto em uma WebView algumas versões do android estavam decodificando errado o texto,
     * foi então testado e concluido que para versões menores que o SDK 11 sejam utilizada um CHARSET utf-8 e maiores ou igual ao
     * SDK11 seja utilizado o CHARSET ISO-8859-1. Dessa forma foi resolvido problemas com acentuação e simbolos.
     *
     * @return charset
     */
    public static String getCharset() {
        if (Build.VERSION.SDK_INT < 11) {
            return "utf-8";
        } else {
            return "ISO-8859-1";
        }
    }

    public static void saveArrayToInternalStorage(Context ctxt, String fileName, byte[] imagem) throws IOException {

            FileOutputStream fos = ctxt.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(imagem);
            fos.flush();
            fos.close();

    }

    public static String getFilePath(Context ctxt){
        /*if(isExternalStorageWritable()){
            return ctxt.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }else{
            return ctxt.getFilesDir().getAbsolutePath();
        }*/
        return ctxt.getFilesDir().getAbsolutePath();
    }

    /**
     * Salvar array de bytes no cartão de memória, por enquanto esta função não está sendo utilizada
     * pois traz problemas de quando o usuario trocar de cartao de memoria, ai precisaria ser tratado
     * @param ctxt
     * @param fileName
     * @param imagem
     */
    public static void saveArrayToSDCard(Context ctxt, String fileName, byte[] imagem){
        File path = ctxt.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(path, fileName);
        try{
            OutputStream os = new FileOutputStream(file);
            os.write(imagem);
            os.close();
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing", e);
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
