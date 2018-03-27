package br.com.appteatro.appteatro.utils;

import br.com.appteatro.appteatro.services.EventService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by hc2mac31 on 26/03/18.
 */

public class RetrofitConfig {

    private final Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://teatro-api.herokuapp.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public EventService getEventService() {
        return this.retrofit.create(EventService.class);
    }

}
