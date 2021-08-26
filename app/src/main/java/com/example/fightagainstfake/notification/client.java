package com.example.fightagainstfake.notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class client {

    private  static Retrofit retrofit=null;
    public static Retrofit getClient(String uri){
        if(retrofit==null){
            retrofit =new Retrofit.Builder().baseUrl(uri)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;


    }

}
