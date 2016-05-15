package app.athleteunbound.RESTapiUtils;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.Interfaces.AsyncResponse1;

/**
 * Created by Mal on 02-05-2016.
 */
public class ApiRequestAsync extends AsyncTask<String, Integer, String> {
    HttpURLConnection urlConnection = null;
    public AsyncResponse1 delegate = null;
    final String baseUrlKU = "http://192.168.0.115:8081/";
    final String baseUrlHome = "http://192.168.0.102:8081/";
    final String androidAPbaseUrl = "http://192.168.43.152:8081/";
    private ProgressBar spinner;
    public ApiRequestAsync(AsyncResponse1 delegate, ProgressBar spinner) {
        this.delegate = delegate;
        this.spinner = spinner;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        //spinner.star
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //super.onProgressUpdate(values);
        spinner.setProgress(values[0]);
    }

    @Override
    protected String doInBackground(String... params) {
        String[] parameterArray = params;
        String subUrl = params[0];

        String restMethod = params[1];
        String jsonWebToken = params[2];
        String postData = params[3];
        try {

            URL url = new URL(IPFactory.getBaseUrlHome()+subUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setRequestProperty("fb-access-token", params[0]); //token in the header
            conn.setRequestProperty("x-access-token", params[2]);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(restMethod);
             conn.setDoInput(true);
            if(restMethod.equals("POST")) {

                conn.setDoOutput(true);
            }

            int HttpResult =conn.getResponseCode();
            conn.connect();
            if(HttpResult ==HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),"utf-8"));
                //String line;
                String response = br.readLine();


                


                return response;
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
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        spinner.setVisibility(View.GONE);
        delegate.processFinish(s);

    }
}
