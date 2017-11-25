package br.com.appteatro.appteatro.domain.service;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.utils.HttpHelper;

public class EventoService {
    // https://teatro-api.herokuapp.com/eventos
    // https://teatro-api.herokuapp.com/locais
    // https://teatro-api.herokuapp.com/eventos?paginado&size=3&page=0
    // https://teatro-api.herokuapp.com/eventos?peagle&size=3&page=0

    private static final String URL = "https://teatro-api.herokuapp.com/eventos";

    public static List<Evento> getEventos() {
        List<Evento> eventos = new ArrayList<>();

        try {
            eventos = getEventosFromWebService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventos;
    }

    // Faz a requisição HTTP, cria a lista de eventos
    public static List<Evento> getEventosFromWebService() throws IOException {
        String url = URL;
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);
        List<Evento> carros = parserJSON(json);
        return carros;
    }

    private static List<Evento> parserJSON(String json) throws IOException {
        List<Evento> eventos = new ArrayList<>();
        try {
            JSONArray jsonEventos = new JSONArray(json);

            // Insere cada evento na lista
            for (int i = 0; i < jsonEventos.length(); i++) {
                JSONObject jsonEvento = jsonEventos.getJSONObject(i);
                Evento e = new Evento();
                // Lê as informações de cada evento
                e.nome = jsonEvento.optString("nome");
                e.urlFoto = jsonEvento.optString("imagem");
                e.genero = jsonEvento.optString("genero");

                eventos.add(e);
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return eventos;
    }
}
