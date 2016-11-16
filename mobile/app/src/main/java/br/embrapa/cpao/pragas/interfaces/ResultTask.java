package br.embrapa.cpao.pragas.interfaces;

/**
 * Created by franco on 14/03/16.
 */
public interface ResultTask {

    void publishProgess(int progess);

    void completeTask();

    void failedTask();

}
