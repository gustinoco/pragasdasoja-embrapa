package br.embrapa.cpao.pragas.controllers;

import android.content.Context;

/**
 * Created by franco on 14/03/16.
 */
public abstract class AppController {
    protected Context context;

    public AppController(Context context) {
        this.context = context;
    }
}
    