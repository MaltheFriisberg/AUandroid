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
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.LoginActivity;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Mal on 25-04-2016.
 */
public class ApiCommunicator extends AsyncTask<JSONObject, String, String> {
    HttpURLConnection urlConnection = null;


    public AsyncResponse delegate = null;

    public ApiCommunicator(AsyncResponse delegate){
        this.delegate = delegate;
    }

    public String saveNewAppUser(Gson user) {
        SyncHttpClient client = new SyncHttpClient();

        String result;
        client.post("http://192.168.0.104:8081/api/appuser", new JsonHttpResponseHandler() {
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {

            }
        });
        return "";
    }
    /*public static String saveNewAppUser1(JSONObject user) {

        try {
            JSONObject obj = FacebookUtil.formatForPost(user);
            URL url = new URL("http://192.168.0.104:8081/api/appuser/facebook");
            String toPost = obj.toString();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(obj.toString());
            writer.flush();
            writer.close();
            os.close();
            int HttpResult =conn.getResponseCode();
            int x = 7;


            conn.connect();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }*/

    @Override
    protected String doInBackground(JSONObject... params) {
        JSONObject user = params[0];
        try {
            JSONObject obj = FacebookUtil.formatForPost(user);
            URL url = new URL("http://192.168.0.104:8081/api/appuser/facebook");
            String toPost = obj.toString();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(obj.toString());
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
                /*while ((br.readLine()) != null) {
                    sb.append(br.readLine() + "\n");
                }*/
                //br.close();

                //System.out.println(""+sb.toString());
                //return sb.toString();


            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        //call back to the activity when the AsyncTask is done
        try {
            JSONObject obj = new JSONObject(s);
            delegate.processFinish(obj); //kald tilbage til LoginActivity
        }catch (Exception e) {

        }


    }
}
