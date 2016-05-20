package app.athleteunbound.RESTapiUtils;

/**
 * Created by Mal on 14-05-2016.
 */
public class IPFactory {
    //final String baseUrlKU = "http://192.168.0.115:8081/";
    //final String baseUrlHome = "http://192.168.0.104:8081/";
    //final String androidAPbaseUrl = "http://192.168.43.152:8081/";

    //android device needs to be connected to same network as the laptop hosting the backend
    public static String getBaseUrlHome() {
        return "http://192.168.0.103:8081/";
    }
    public static String getAndroidAPbaseUrl() {
        return "http://192.168.0.101:8081/";
    }
    public static String getBaseUrlKU() {
        return "http://192.168.0.115:8081/";
    }

}
