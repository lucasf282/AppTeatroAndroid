package br.com.appteatro.appteatro.services;

import java.util.List;

import br.com.appteatro.appteatro.domain.model.Evento;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hc2mac31 on 26/03/18.
 */

public interface EventService {

    @GET("eventos/")
    Call<List<Evento>> buscarEventos();

    @GET("favorito/{codigo}")
    Call<List<Evento>> buscarEventosFavoritoPorUsuario(@Path("codigo") String codigo);
}
