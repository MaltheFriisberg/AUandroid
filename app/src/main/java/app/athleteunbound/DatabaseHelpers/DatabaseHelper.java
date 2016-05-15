package app.athleteunbound.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mal on 15-05-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AthleteManager";

    // Table Names
    private final static String TABLE_APPUSER = "AppUsers";
    private static final String TABLE_ATHLETES = "Athletes";
    private static final String TABLE_SPORTS = "Sports";
    private static final String TABLE_TODO_SPORTCOMPETENCIES = "SportCompetencies";
    private static final String TABLE_ATHLETE_COMPETENCIES = "AthleteCompetencies";
    private static final String TABLE_MESSAGES = "Messages";
    private static final String TABLE_COMPETENCY_RATINGS = "CompetencyRatings";
    private static final String TABLE_COMPETENCIES = "Competencies";
    //joined table
    private static final String TABLE_SPORT_COMPETENCIES = "SportCompetencies";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    //APPUSER column names
    private static final String appUserId = "appUserId";
    private static final String username = "username";
    private static final String name = "name";
    private static final String last_name = "last_name";
    private static final String email = "email";
    private static final String gender = "gender";

    //SPORT table - column names
    private static final String KEY_SPORT_NAME = "name";

    //COMPETENCIES table - column names
    private static final String KEY_COMPETENCY_NAME = "name";

    //SPORT_COMPETENCIES table - column names (joined table)
    private static final String KEY_SPORT_ID = "sportId";
    private static final String KEY_COMPETENCY_ID = "competencyId";

    // NOTES Table - column nmaes
    /*private static final String KEY_TODO = "todo";
    private static final String KEY_STATUS = "status";

    // TAGS Table - column names
    private static final String KEY_TAG_NAME = "tag_name";

    // NOTE_TAGS Table - column names
    private static final String KEY_TODO_ID = "todo_id";
    private static final String KEY_TAG_ID = "tag_id";*/

    // Table Create Statements
    // Todo table create statement
    /*private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_APPUSER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";*/
    //sport table create statement
    private static final String CREATE_TABLE_SPORT = "CREATE TABLE "+ TABLE_SPORTS
            + "("+KEY_ID+ " INTEGER PRIMARY KEY," + KEY_SPORT_NAME + " TEXT,"
            +KEY_CREATED_AT + " date default CURRENT_DATE" + ")";
    private static final String CREATE_TABLE_COMPETENCIES = "CREATE TABLE "+ TABLE_COMPETENCIES
            + "("+KEY_ID+ " INTEGER PRIMARY KEY" + KEY_COMPETENCY_NAME + " TEXT,"
            +KEY_CREATED_AT + " DATETIME" + ")";
    private static final String CREATE_TABLE_SPORT_COMPETENCIES = "CREATE TABLE "+ TABLE_SPORT_COMPETENCIES
            + "("+KEY_ID+ " INTEGER PRIMARY KEY" + KEY_SPORT_ID + " INTEGER," + KEY_COMPETENCY_ID + " INTEGER,"
            +KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SPORT);
        db.execSQL(CREATE_TABLE_COMPETENCIES);
        db.execSQL(CREATE_TABLE_SPORT_COMPETENCIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SPORT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_COMPETENCIES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SPORT_COMPETENCIES);

        // create new tables
        onCreate(db);
    }
    public long createSport(JsonObject sport) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SPORT_NAME, sport.get("name").toString());
        //insert row
        long sport_id = db.insert(TABLE_SPORTS, null, values);
        //handle the competencies on the sport object, insert in joined table
        createSportCompetencies(sport, sport_id);
        return sport_id;
    }

    public void createSportCompetencies(JsonObject sport, long sport_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //HashMap to hold the primary keys and values for the joined table
        List<Integer> competencyPKs = new ArrayList<>();
        JsonArray arr = sport.getAsJsonArray("competencies");
        Date d = new Date(System.currentTimeMillis());
        for(final JsonElement competency : arr) {
            //save as competencies
            ContentValues values = new ContentValues();
            values.put(KEY_COMPETENCY_NAME, competency.toString());

            long competency_id = db.insert(TABLE_COMPETENCIES, null, values);
            //save the primary keys for each created competency in list
            competencyPKs.add((int)competency_id);
        }
        //add all the list primary keys in the joined table SPORT_COMPETENCIES
        createSportCompetency(competencyPKs, sport_id);
    }
    public void createSportCompetency(List<Integer> competencyPKs, long sportId) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(Integer competencyPK : competencyPKs) {
            ContentValues values = new ContentValues();
            values.put(KEY_SPORT_ID, sportId);
            values.put(KEY_COMPETENCY_ID, competencyPK);
            db.insert(TABLE_SPORT_COMPETENCIES, null, values);
        }
        
    }


}