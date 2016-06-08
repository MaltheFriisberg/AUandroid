package app.athleteunbound.DatabaseHelpers;

/**
 * Created by Mal on 07-06-2016.
 */
public class DbStrings {
    public static final String DATABASE_NAME = "AthleteManager";

    // Table Names
    public final static String TABLE_APPUSER = "AppUsers";
    public static final String TABLE_ATHLETES = "Athletes";
    public static final String TABLE_SPORTS = "Sports";
    public static final String TABLE_TODO_SPORTCOMPETENCIES = "SportCompetencies";
    public static final String TABLE_ATHLETE_COMPETENCIES = "AthleteCompetencies";
    public static final String TABLE_MESSAGES = "Messages";
    public static final String TABLE_COMPETENCY_RATINGS = "CompetencyRatings";
    public static final String TABLE_COMPETENCIES = "Competencies";

    //joined table
    public static final String TABLE_SPORT_COMPETENCIES = "SportCompetencies";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";

    //APPUSER column names
    public static final String appUserId = "appUserId";
    public static final String username = "username";
    public static final String name = "name";
    public static final String last_name = "last_name";
    public static final String email = "email";
    public static final String gender = "gender";

    //ATHLETE comumn names
    public static final String KEY_SPORT = "sport";
    public static final String KEY_GOAL = "goal";
    public static final String KEY_USERNAME = "username";
    //SPORT table - column names
    public static final String KEY_SPORT_NAME = "name";

    //COMPETENCIES table - column names
    public static final String KEY_COMPETENCY_NAME = "name";

    //SPORT_COMPETENCIES table - column names (joined table)
    public static final String KEY_SPORT_ID = "sportId";
    public static final String KEY_COMPETENCY_ID = "competencyId";
    //private static final String KEY_COMPETENCY_BACKEND_ID = "backendId";

    //sport table create statement
    public static String CREATE_TABLE_SPORT = "CREATE TABLE "+ TABLE_SPORTS
            + "("+KEY_ID+ " INTEGER PRIMARY KEY," + KEY_SPORT_NAME + " TEXT UNIQUE,"
            +KEY_CREATED_AT + " date default CURRENT_DATE"+")"; // UNIQUE(name) ON CONFLICT REPLACE
    public static final String CREATE_TABLE_COMPETENCIES = "CREATE TABLE "+ TABLE_COMPETENCIES
            + "("+KEY_ID+ " INTEGER PRIMARY KEY,"+" competencyId TEXT UNIQUE, " + KEY_COMPETENCY_NAME + " TEXT UNIQUE,"
            +KEY_CREATED_AT + " DATETIME" + ")";
    public static final String CREATE_TABLE_SPORT_COMPETENCIES = "CREATE TABLE "+ TABLE_SPORT_COMPETENCIES
            + "("+KEY_ID+ " INTEGER PRIMARY KEY," + KEY_SPORT_ID + " INTEGER," + KEY_COMPETENCY_ID + " TEXT UNIQUE,"
            +KEY_CREATED_AT + " DATETIME" + ")";
    public static final String CREATE_TABLE_ATHLETE = "CREATE TABLE "+ TABLE_ATHLETES + "("+KEY_ID+ " INTEGER PRIMARY KEY,"+
            appUserId+" TEXT, "+KEY_USERNAME + " TEXT, "+KEY_SPORT+" TEXT, "+KEY_GOAL+" TEXT, "+KEY_CREATED_AT+" DATETIME"+")";
    public static final String CREATE_TABLE_ATHLETE_COMPETENCIES = "CREATE TABLE "+TABLE_ATHLETE_COMPETENCIES
            +"("+KEY_ID+ " INTEGER PRIMARY KEY,"+ KEY_COMPETENCY_NAME+" TEXT,"+"competencyId TEXT"+")";

    public static final String CREATE_TABLE_APPUSERS = "CREATE TABLE "+ TABLE_APPUSER
            +"("+KEY_ID+ " INTEGER PRIMARY KEY, backendId TEXT, email TEXT, username TEXT, gender TEXT)";
}
