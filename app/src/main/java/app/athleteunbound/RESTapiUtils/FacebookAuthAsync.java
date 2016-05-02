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
public class FacebookAuthAsync extends AsyncTask<JSONObject, String, JSONObject>{
    HttpURLConnection urlConnection = null;


    public AsyncResponse delegate = null;

    public FacebookAuthAsync(AsyncResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected JSONObject doInBackground(JSONObject... params) {

        try {

            URL url = new URL("http://192.168.0.104:8081/api/appuser/authenticate/facebook");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("facebookId", params[0].getString("id"));
            conn.setRequestProperty("username", params[0].getString("name"));
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
                //String test = params[0].toString()+" "+br.readLine();
                JSONObject successStatus;

                successStatus = new JSONObject(response);
                successStatus.put("AppUser", params[0]);




                return successStatus;
                /*while ((br.readLine()) != null) {
                    sb.append(br.readLine() + "\n");
                }*/
                //br.close();

                //System.out.println(""+sb.toString());
                //return sb.toString();


            }else{
                //System.out.println(urlConnection.getResponseMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject obj) {
        try {
            //JSONObject obj = new JSONObject(s);
            delegate.processFinish(obj); //kald tilbage til LoginActivity
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
