package br.com.appteatro.appteatro.domain.model;

import java.io.Serializable;

/**
 * Created by Windows 10 on 31/03/2018.
 */

public class Favorito implements Serializable {
    public Long id;

    public String uid;

    public Evento evento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

}
