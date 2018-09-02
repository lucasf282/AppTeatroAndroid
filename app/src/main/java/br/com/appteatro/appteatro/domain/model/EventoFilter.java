package br.com.appteatro.appteatro.domain.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Windows 10 on 01/09/2018.
 */

public class EventoFilter implements Serializable, QueryPojo {

    private String nome;

    private String descricao;

    private Genero genero;

    private String nomeLocal;

    private String estadoLocal;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getEstadoLocal() {
        return estadoLocal;
    }

    public void setEstadoLocal(String estadoLocal) {
        this.estadoLocal = estadoLocal;
    }

    @Override
    public Map<String, Object> build() {
        Map<String, Object> mapa = new HashMap<>();

        if(this.getNome() != null){
            mapa.put("nome", this.getNome());
        }

        if(this.getGenero() != null){
            mapa.put("genero", this.getGenero());
        }

        if(this.getNomeLocal() != null){
            mapa.put("nomeLocal", this.getNomeLocal());
        }
        return mapa;
    }
}
