package br.embrapa.cpao.pragas.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by franco on 13/03/16.
 */
public class ImageHelper extends AsyncTask<Void,Void,Bitmap>{
    private final int TIPO_BYTES=100;
    private final int TIPO_RESID=200;
    private final int TIPO_URI=300;
    final int tipoEntrada;

    ImageView ivImage;
    byte[] bytes;
    int resId;
    int width, height;
    Uri imageUri;
    Context context;

    /** flag se processamento está sendo executado ou não **/
    boolean running=false;

    public ImageHelper(ImageView iv, byte[] bytes){
        //seta tipo de entrada de dados
        tipoEntrada = TIPO_BYTES;

        this.ivImage = iv;
        this.bytes = bytes;
        setDimensao(0, 0);
    }
    public ImageHelper(ImageView iv, byte[] bytes, int reqWidth, int reqHeight){
        //seta tipo de entrada de dados
        tipoEntrada = TIPO_BYTES;

        this.ivImage = iv;
        this.bytes = bytes;
        setDimensao(reqWidth, reqHeight);
    }

    public ImageHelper(Context context,ImageView iv, Uri imageUri, int reqWidth, int reqHeight){
        //seta tipo de entrada de dados
        tipoEntrada = TIPO_URI;
        this.ivImage = iv;
        this.imageUri = imageUri;
        this.context = context;
        setDimensao(reqWidth, reqHeight);
    }

    public ImageHelper(ImageView iv, int resId, int reqWidth, int reqHeight){
        //seta tipo de entrada de dados
        tipoEntrada = TIPO_RESID;

        this.ivImage=iv;
        this.resId = resId;
        setDimensao(reqWidth, reqHeight);
    }

    private void setDimensao(int reqWidth, int reqHeight){
        this.width = reqWidth;
        this.height= reqHeight;
    }


    @Override
    protected Bitmap doInBackground(Void... objects) {
        running=true;

        Bitmap bmp = null;
        switch (tipoEntrada){
            case TIPO_BYTES:
                bmp = decodeBitmapFromByte(bytes,width,height);
                break;
            case TIPO_RESID:
                bmp = decodeBitmapFromResource(resId,width,height);
                break;
            case TIPO_URI:
                try {
                    bmp = decodeBitmapFromUri(context, imageUri,width,height);
                } catch (FileNotFoundException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
        }

        return bmp;
    }

    public boolean isRunning(){
        return running;
    }
    @Override
    protected  void onPostExecute(Bitmap bmp){
        if(bmp!=null)
            ivImage.setImageBitmap(bmp);
        running=false;
    }


    private static int calculateSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Método copiado da api do android e foi modificado por mim, Juarez, para
     * receber byte[] em vez resource e id.
     *
     * @param bytes
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromByte(byte[] bytes,
                                              int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        if(reqHeight==0 && reqWidth==0) {
            reqHeight = options.outHeight;
            reqWidth = options.outWidth;
            Log.e("TAM IMAGEM","WIDTH: "+reqWidth+" - HEIGHT: "+reqHeight);
        }
        options.inSampleSize = calculateSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        bytes=null;
        return bmp;
    }

    /**
     * Carrega em memória de forma otimizada imagem através de seu resource ID
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromResource(int resId,int reqWidth, int reqHeight){
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(Resources.getSystem(), resId, options);

        // Calculate inSampleSize
        if(reqHeight==0 && reqWidth==0) {
            reqHeight = options.outHeight;
            reqWidth = options.outWidth;
            Log.e("TAM IMAGEM","WIDTH: "+reqWidth+" - HEIGHT: "+reqHeight);
        }
        options.inSampleSize = calculateSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeResource(Resources.getSystem(),resId, options);
        return bmp;
    }

    public static Bitmap decodeBitmapFromUri(Context context, Uri uri,
                                             int reqWidth, int reqHeight) throws FileNotFoundException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);



        // Calculate inSampleSize
        options.inSampleSize = calculateSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
    }

    public static String encodeToBase64(Bitmap bmp) {
        // transforma foto em binario, salva em uma string e retorna um json da
        // imagem
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        // transforma em string a foto
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedImage;
    }

    public static String encodeToBase64_low(Bitmap bmp) {
        // transforma foto em binario, salva em uma string e retorna um json da
        // imagem
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();

        // transforma em string a foto
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedImage;
    }
}

