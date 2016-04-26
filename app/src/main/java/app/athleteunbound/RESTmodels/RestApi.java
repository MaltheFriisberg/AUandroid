package app.athleteunbound.RESTmodels;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mal on 25-04-2016.
 */
public interface RestApi {

    @GET("/api/appuser/authenticate")
    Call<TokenResponse> getAuthToken();
}
