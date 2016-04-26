package app.athleteunbound;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import app.athleteunbound.RESTapiUtils.ApiCommunicatorAsync;

/**
 * Created by Mal on 19-04-2016.
 */
public class LoginActivity extends Activity {

    ProgressDialog progressDialog;
    TextView textField_username;
    TextView textField_password;
    //LoginButton fbLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        //fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        //fbLoginButton.setReadPermissions("email");
        textField_username = (TextView) findViewById(R.id.textField_username);
        textField_password = (TextView) findViewById(R.id.textField_password);
        JsonObject loginResult;
    }

    public void onClickLoginBtn(View v) {
        //getApplicationContext();
        if(v.getId()== R.id.btn_login) {

            String username = textField_username.getText().toString();
            String password = textField_password.getText().toString();
            Log.d("username", username);
            Log.d("password", password);
            //progressDialog.
            //new AuthenticateTask().execute(username, password);
            ApiCommunicatorAsync apiCommunicatorAsync = new ApiCommunicatorAsync();
            apiCommunicatorAsync.Login(username, password);
            String breakpoint;
        }

    }
    private class AuthenticateTask extends AsyncTask<String, String, JsonObject> {

        @Override
        protected JsonObject doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            ApiCommunicatorAsync apiCommunicatorAsync = new ApiCommunicatorAsync();
            return apiCommunicatorAsync.Login(username, password);
        }
        protected void onProgressUpdate(String... progress) {
            //progressDialog.setProgressPercent(progress[0]);
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        protected void onPostExecute(Long result) {
            progressDialog.dismiss();
        }
    }
}
