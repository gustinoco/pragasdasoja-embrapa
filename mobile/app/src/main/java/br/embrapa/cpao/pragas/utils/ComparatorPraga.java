package br.embrapa.cpao.pragas.utils;

import java.text.Collator;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.Locale;

import br.embrapa.cpao.pragas.models.Praga;

/**
 *
 * Interface para algoritimo de comparação, esta interface tem finalidade de verificar a ordem alfabetica dos nome de
 * 2 pragas.
 * Created by franco on 13/10/15.
 */
public class ComparatorPraga implements Comparator<Praga> {
    @Override
    public int compare(Praga p1, Praga p2) {
        String str1 = removerAcentos(p1.getNome());
        String str2 = removerAcentos(p2.getNome());
        return str1.compareToIgnoreCase(str2);

    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

    }
}
