package br.embrapa.cpao.pragas.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Classe responsável por disponibilizar acesso as fontes externas de texto.
 *
 * @author Juarez Arce Franco Junior
 * @version 1.0
 */
public class FontChange {

    public static Typeface setUbuntuRegular(Context context) {

        return Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-R.ttf");
    }

    public static Typeface setUbuntuBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-B.ttf");
    }
}
