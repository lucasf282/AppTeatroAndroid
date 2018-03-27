package br.com.appteatro.appteatro.services;

import java.util.List;

import br.com.appteatro.appteatro.domain.model.Evento;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hc2mac31 on 26/03/18.
 */

public interface EventService {

    @GET("eventos/")
    Call<List<Evento>> buscarEventos();
}
