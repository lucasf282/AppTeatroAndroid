package br.com.appteatro.appteatro.domain.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.model.Local;
import br.com.appteatro.appteatro.utils.HttpHelper;

public class EventoService {
    // https://teatro-api.herokuapp.com/eventos
    // https://teatro-api.herokuapp.com/locais
    // https://teatro-api.herokuapp.com/eventos?paginado&size=3&page=0
    // https://teatro-api.herokuapp.com/eventos?peagle&size=3&page=0

    private static final String URL = "https://teatro-api.herokuapp.com/eventos";

    private int tipo;

    public static List<Evento> getEventos(int tipo) {
        List<Evento> eventos = new ArrayList<>();

        try {
            eventos = getEventosFromWebService(tipo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventos;
    }

    // Faz a requisição HTTP, cria a lista de eventos
    public static List<Evento> getEventosFromWebService(int tipo) throws IOException {
        String url = montaURL(tipo);
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);
        List<Evento> eventos = parserJSON(json);
        return eventos;
    }

    private static String montaURL(int tipo) {
        if (tipo == 0) {
            // Aba 0 Eventos
            return URL;
        } else {
            // Aba 1 Destaque
            // Definido como destaque genero STANDUP
            return URL + "?filtrar&genero=STANDUP";
        }
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
                e.descricao = jsonEvento.optString("descricao");
                e.imagem = jsonEvento.optString("imagem");
                e.genero = jsonEvento.optString("genero");
                e.local = insereLocal(jsonEvento.getJSONObject("local"));

                eventos.add(e);
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return eventos;
    }

    private static Local insereLocal(JSONObject localJSON) {
        Local local = new Local();
        local.nome = localJSON.optString("nome");
        local.endereco = localJSON.optString("endereco");
        local.complemento = localJSON.optString("complemento");
        local.cidade = localJSON.optString("cidade");
        local.estado = localJSON.optString("estado");
        local.telefone = localJSON.optString("telefone");
        local.latitude = localJSON.optString("latitude");
        local.longitude = localJSON.optString("longitude");

        return local;
    }
}
