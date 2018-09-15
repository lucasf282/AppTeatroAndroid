package br.com.appteatro.appteatro.domain.model;

import java.io.Serializable;

/**
 * Created by Windows 10 on 01/09/2018.
 */

public enum Genero implements Serializable {

    TODOS("Todos"),
    COMEDIA("Comédia"),
    DRAMA("Drama"),
    INFANTIL("Infantil"),
    MONOLOGO("Monólogo"),
    MUSICAL("Musical"),
    PALESTRA("Palestra"),
    SHOW("Show"),
    STANDUP("Standup");

    private String descricao;

    Genero(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
