package br.embrapa.cpao.pragas.resource;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import br.embrapa.cpao.pragas.http.HttpResponse;
import br.embrapa.cpao.pragas.http.HttpSync;
import br.embrapa.cpao.pragas.models.PragaFoto;
import br.embrapa.cpao.pragas.utils.K;

/**
 * Created by franco on 27/03/16.
 */
public class FotoResource {
    private static final String ENDERECO = K.ENDERECO + "resource/img/fotos/";

    public static void download(Context ctxt, PragaFoto foto) throws IOException {
        String endereco = ENDERECO+foto.getFileName();

        URL url = new URL(endereco);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.connect();

        InputStream in = httpConn.getInputStream();
        byte[] image = IOUtils.toByteArray(in);

        foto.setFoto(image);

    }
}
