package app.athleteunbound.RESTapiUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import app.athleteunbound.Interfaces.AsyncResponse;

/**
 * Created by Mal on 28-04-2016.
 */
public class GraphRequester implements AsyncResponse  {


    public AsyncResponse delegate = null;

    public GraphRequester(AsyncResponse delegate) {
        this.delegate = delegate;
    }
    public void requestGraphApi(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.d("name ", object.getString("name"));
                            Log.d("email ", object.getString("email"));
                            //Log.d("token",object.getString("token"));
                            //Log.d("FbID ", object.getString("id"));
                            delegate.processFinish(object);
                            //ApiCommunicator.saveNewAppUser1(object);

                            //Intent intent = new Intent(this, SportActivity.class);

                            //txtState.setText("Hi, " + object.getString("name"));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void processFinish(JSONObject output) {

    }

}
