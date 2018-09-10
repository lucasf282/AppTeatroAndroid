package br.com.appteatro.appteatro.utils;

import br.com.appteatro.appteatro.services.EventService;
import br.com.appteatro.appteatro.services.FavoriteService;
import br.com.appteatro.appteatro.services.LocalService;
import br.com.appteatro.appteatro.services.UserService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by hc2mac31 on 26/03/18.
 */

public class RetrofitConfig {

    private final Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                //.baseUrl("https://teatro-api.herokuapp.com/")
                .baseUrl("http://192.168.0.10:8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public EventService getEventService() {
        return this.retrofit.create(EventService.class);
    }

    public UserService getUserService() {
        return this.retrofit.create(UserService.class);
    }

    public FavoriteService getFavoriteService() {
        return this.retrofit.create(FavoriteService.class);
    }

    public LocalService getLocalService() {
        return this.retrofit.create(LocalService.class);
    }

}
