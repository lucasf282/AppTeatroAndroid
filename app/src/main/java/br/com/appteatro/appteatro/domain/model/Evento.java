package br.com.appteatro.appteatro.domain.model;

import java.io.Serializable;

public class Evento implements Serializable{

    public String nome;
    public String descricao;
    public String imagem;
    public String genero;
    //public List<Agenda> listaAgenda;
    public Local local;

}

