package app.athleteunbound.RESTapiUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
//import com.loopj.android.http.Req
import com.loopj.android.http.AsyncHttpClient;
import android.app.DownloadManager;
import android.util.Log;
import android.widget.Toast;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.methods.RequestBuilder;

/**
 * Created by Mal on 25-04-2016.
 */
public class ApiCommunicatorAsync {
    //the virtual machine can not connect to API on "localhost"
    //When testing on localhost, lookup /cmd/ipconfig
    String baseUrl = "http://192.168.0.102:8081";
    JsonObject toReturn;
    public JsonObject Login(String username, String password) {
        RequestParams params = new RequestParams();
        params.setForceMultipartEntityContentType(true);
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("username", username);
        client.addHeader("password", password);

        client.get("http://192.168.0.102:8081/api/appuser/authenticate", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String breakpoint;
                Log.d("onSuccess", new String(responseBody));
                try {
                    JsonParser parser = new JsonParser();
                    //JsonElement element = new JsonPrimitive(new String(responseBody));
                    toReturn = parser.parse(new String(responseBody)).getAsJsonObject();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                JsonParser parser = new JsonParser();
                //JsonElement element = new JsonPrimitive(new String(responseBody));
                Log.d("onFailure", new String(responseBody));
                toReturn = parser.parse(new String(responseBody)).getAsJsonObject();

            }
        });
        return toReturn;
    }
    /*public static String Login(String username, String password) {
        AsyncHttpClient client=new AsyncHttpClient();
        Request request = client.pr
    }*/

}
