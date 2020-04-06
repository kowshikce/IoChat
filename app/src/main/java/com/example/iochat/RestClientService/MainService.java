package com.example.iochat.RestClientService;

import com.example.iochat.RestClientService.BaseResponse.BaseResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainService {

    private static final String URL = "http://192.168.1.104:3000";
    private static ApiService apiService = null;

    private MainService() {
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public interface ApiService {

        @GET("/api")
        Call<BaseResponse> getBaseResponse();
    }
}
