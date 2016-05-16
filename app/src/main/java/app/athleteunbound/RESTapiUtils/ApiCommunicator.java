package app.athleteunbound.RESTapiUtils;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.LoginActivity;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Mal on 25-04-2016.
 */
public class ApiCommunicator {

    public static String PostPutRequest(String postData, String subUrl, String restMethod, String jsonWebToken) {
        try {
            JSONObject toPost = new JSONObject(postData);

            URL url = new URL(IPFactory.getBaseUrlHome()+subUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(jsonWebToken.length() > 0 && !jsonWebToken.equals("")) {
                conn.setRequestProperty("x-access-token", jsonWebToken);
            }
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(restMethod);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData);
            writer.flush();
            writer.close();
            os.close();
            int HttpResult =conn.getResponseCode();
            int x = 7;

            conn.connect();
            if(HttpResult ==HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),"utf-8"));
                //String line;
                String test = br.readLine();

                return test;



            }else{
                //System.out.println(urlConnection.getResponseMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String GetRequest(HashMap<String, String> headers, String subUrl) {

        try {

            URL url = new URL(IPFactory.getBaseUrlHome()+subUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            for (Map.Entry<String, String> entry : headers.entrySet())
            {
                conn.setRequestProperty(entry.getKey(), entry.getValue());

            }
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //conn.setDoOutput(true);
            int HttpResult =conn.getResponseCode();
            conn.connect();
            if(HttpResult ==HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),"utf-8"));
                //String line;
                String response = br.readLine();

                return response;
            }else{
                //System.out.println(urlConnection.getResponseMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
