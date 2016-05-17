package app.athleteunbound.RESTmodels;

/**
 * Created by Mal on 17-05-2016.
 */
public class AppUser {
    private int primaryKey;
    private String backendId;
    private String email;
    private String username;
    private String gender;
    //public String facebookId;

    public AppUser() {
    }

    public AppUser(int primaryKey, String gender, String username, String email, String backendId) {
        this.primaryKey = primaryKey;
        this.gender = gender;
        this.username = username;
        this.email = email;
        this.backendId = backendId;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getBackendId() {
        return backendId;
    }

    public void setBackendId(String backendId) {
        this.backendId = backendId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
