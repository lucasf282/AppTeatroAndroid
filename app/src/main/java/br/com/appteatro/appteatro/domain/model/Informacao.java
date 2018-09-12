package br.com.appteatro.appteatro.domain.model;

import java.io.Serializable;

/**
 * Created by Windows 10 on 11/09/2018.
 */

public class Informacao implements Serializable {
    private String elenco;
    private String ficha;
    private String duracao;

    public String getElenco() {
        return elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }
}
