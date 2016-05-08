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
 * Created by Mal on 08-05-2016.
 */
public class PostPutRequestAsync extends AsyncTask<String, String, String> {
    String baseUrl = "http://192.168.0.104:8081/";
    HttpURLConnection urlConnection = null;


    public AsyncResponse delegate = null;

    public PostPutRequestAsync(AsyncResponse delegate){
        this.delegate = delegate;
    }


    @Override
    public String doInBackground(String[] params) {
        String[] parameterArray = params;
        String subUrl = params[0];

        String restMethod = params[1];
        String jsonWebToken = params[2];
        String postData = params[3];
        try {
            JSONObject toPost = new JSONObject(params[3]);
            JSONObject obj = FacebookUtil.formatForPost(toPost);
            URL url = new URL(baseUrl+subUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(jsonWebToken.length() > 0 && !jsonWebToken.equals("")) {
                conn.setRequestProperty("x-access-token", params[2]);
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
