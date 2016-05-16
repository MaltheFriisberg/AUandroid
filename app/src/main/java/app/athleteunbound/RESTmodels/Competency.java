package app.athleteunbound.RESTmodels;

/**
 * Created by Mal on 14-05-2016.
 */
public class Competency {
    private int primaryKey;
    private String name;
    private String competencyId;

    public Competency() {

    }

    public String getCompetencyId() {
        return competencyId;
    }

    public void setCompetencyId(String competencyId) {
        this.competencyId = competencyId;
    }

    public Competency(int primaryKey, String name, String category, String competencyId) {
        this.primaryKey = primaryKey;
        this.competencyId = competencyId;
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
