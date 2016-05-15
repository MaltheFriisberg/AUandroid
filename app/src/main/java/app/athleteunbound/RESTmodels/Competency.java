package app.athleteunbound.RESTmodels;

/**
 * Created by Mal on 14-05-2016.
 */
public class Competency {
    private int primaryKey;
    private String name;

    public Competency() {

    }

    public Competency(int primaryKey, String name, String category) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.category = category;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
