package app.athleteunbound.RESTapiUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import app.athleteunbound.RESTmodels.RestApi;
import app.athleteunbound.RESTmodels.TokenResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Mal on 25-04-2016.
 */
public class RetroFitExample {

    public static final String baseUrl = "http://192.168.0.104:8081/api/";

    public static void test() {
        OkHttpClient httpClient = new OkHttpClient();
        /*httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("test", "test").build();
                return chain.proceed(request);
            }
        });*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //RestApi apiService = ServiceGenerator.c
        RestApi service = retrofit.create(RestApi.class);
        Call<TokenResponse> call = service.getAuthToken();
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                try {
                    Log.d("success ", response.body().success.toString());
                    Log.d("message ", response.body().message);
                    Log.d("token ", response.body().token);
                }catch (Exception e){
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });


    }
}
