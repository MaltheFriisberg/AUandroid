package app.athleteunbound.RESTapiUtils;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import app.athleteunbound.Interfaces.AsyncResponse;

/**
 * Created by Mal on 30-04-2016.
 */
public class GetAthleteAsync extends AsyncTask<JSONObject, String, String> {
    HttpURLConnection urlConnection = null;


    public AsyncResponse delegate = null;

    public GetAthleteAsync(AsyncResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected String doInBackground(JSONObject... params) {

        JSONObject user = params[0];
        try {
            JSONObject obj = FacebookUtil.formatForPost(user);
            URL url = new URL("http://192.168.0.104:8081/api/athlete");
            String toPost = obj.toString();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            int HttpResult =conn.getResponseCode();
            int x = 7;

            conn.connect();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                StringBuilder sb = new StringBuilder();
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
        super.onPostExecute(s);
    }
}
