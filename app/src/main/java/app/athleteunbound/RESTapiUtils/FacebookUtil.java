package app.athleteunbound.RESTapiUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mal on 27-04-2016.
 */
public class FacebookUtil {

    public static List<String> facebookPermissions() {
        List<String> toReturn = new ArrayList<String>();

        toReturn.add("name");
        toReturn.add("email");
        return toReturn;
    }
    public static JSONObject formatForPost(JSONObject user) {
        JSONObject appUser = new JSONObject();
        try {
            String FBid = user.getString("id");
            String username = user.getString("name");
            String email = user.getString("email");
            appUser.put("username", username);
            appUser.put("email", email);
            appUser.put("password", FBid); //this is NOT good
            user.put("FacebookId", FBid);
            user.put("gender", user.getString("gender"));
        }catch (Exception e) {

        }
        return appUser;
    }
}
