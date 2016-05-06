package app.athleteunbound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import org.json.JSONObject;

import app.athleteunbound.Interfaces.AsyncResponse1;
import app.athleteunbound.RESTapiUtils.ApiRequestAsync;

public class StartActivity extends AppCompatActivity {
    ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        this.spinner = (ProgressBar) findViewById(R.id.progressBar2);
        if (!settings.getString("AthleteUnboundApiToken", ""/*default value*/).equals("") &&
                settings.getString("AthleteUnboundApiToken", ""/*default value*/) != null) {
            String token = settings.getString("AthleteUnboundApiToken", ""/*default value*/);
            //authenticate with api /api/athlete/ (get Req, token req)
            ApiRequestAsync athleteRequest = (ApiRequestAsync) new ApiRequestAsync(new AsyncResponse1() {
                @Override
                public void processFinish(String result) {
                    JSONObject obj;
                    try {
                        obj = new JSONObject(result);
                        // if athlete req successfull
                        if(obj.has("success") && obj.getBoolean("success")== true) {
                            runMainViewActivity(obj);
                        }

                    }catch (Exception e) {
                        goToLoginActivity();
                    }
                    goToLoginActivity();
                }
            }, this.spinner).execute("api/athlete", "GET", token, "");
        }
    }
    private void runMainViewActivity(JSONObject obj) {
        Intent intent = new Intent(this, MainViewActivity.class);
        intent.putExtra("AppUser",obj.toString());
        startActivity(intent);
    }
    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
