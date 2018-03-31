package br.com.appteatro.appteatro.services;

import br.com.appteatro.appteatro.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Windows 10 on 29/03/2018.
 */

public interface UserService {

    @POST("usuarios")
    Call<Usuario> inserirUsuario(@Body Usuario usuario);

    @GET("usuarios?filtrar")
    Call<Usuario> buscarUsuarioFiltro(@Query("uid") String uid);
}
