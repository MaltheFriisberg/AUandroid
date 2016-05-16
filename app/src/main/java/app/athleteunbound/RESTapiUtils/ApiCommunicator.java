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
public class ApiCommunicator {

    public static String PostPutRequest() {
        return "";
    }

}
