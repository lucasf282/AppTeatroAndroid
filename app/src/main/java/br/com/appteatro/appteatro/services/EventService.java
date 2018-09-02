package br.com.appteatro.appteatro.services;

import java.util.List;
import java.util.Map;

import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.model.EventoFilter;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by hc2mac31 on 26/03/18.
 */

public interface EventService {

    @GET("eventos")
    Call<List<Evento>> buscarEventos();

    @GET("eventos/listar/{uid}")
    Call<List<Evento>> buscarEventosComUsuarioLogado(@Path("uid") String uid);

    @GET("eventos/favoritos/{uid}")
    Call<List<Evento>> buscarEventosFavoritoPorUsuario(@Path("uid") String uid);

    @GET("eventos/{uid}?filtrar") //i.e https://api.test.com/Search?
    Call<List<Evento>> buscarEventosFiltrados(@Path("uid") String uid, @QueryMap Map<String, Object> eventoFilter);
}
