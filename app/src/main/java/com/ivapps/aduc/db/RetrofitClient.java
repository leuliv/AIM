package com.ivapps.aduc.db;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ivapps.aduc.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.137.1/aduc/";
    private static final String BASE_URL2 = "http://192.168.43.210/aduc/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized  RetrofitClient getInstance(){
        if (mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }

}
