package br.com.appteatro.appteatro.services;

import java.util.List;

import br.com.appteatro.appteatro.domain.model.Local;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Windows 10 on 19/08/2018.
 */

public interface LocalService {
    @GET("locais")
    Call<List<Local>> buscarLocais();
}
