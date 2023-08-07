package com.example.jobify.WebService;

import com.example.jobify.Utils.UtilService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitApi retrofitApi;
    private static Retrofit retrofit;
    //       private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .connectTimeout(1, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(25, TimeUnit.SECONDS)
//            .build();
    public static Retrofit retrofit(){
        if(retrofit==null){
             retrofit = new Retrofit.Builder()
                    .baseUrl(UtilService.BASE_URL)
//                     .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static RetrofitApi getRetrofitApiService() {
        if(retrofitApi == null ) {
            retrofitApi = retrofit().create(RetrofitApi.class);
        }
        return retrofitApi;
    }

}
