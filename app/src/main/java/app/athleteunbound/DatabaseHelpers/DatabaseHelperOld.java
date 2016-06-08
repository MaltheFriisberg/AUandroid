package app.athleteunbound.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import app.athleteunbound.RESTmodels.AppUser;
import app.athleteunbound.RESTmodels.Athlete;
import app.athleteunbound.RESTmodels.Competency;
import app.athleteunbound.RESTmodels.Sport;

/**
 * Created by Mal on 15-05-2016.
 */
public class DatabaseHelperOld extends SQLiteOpenHelper {
    //getApplicationContext().deleteDatabase("AthleteManager"); //DELETES the DB only use when changing the schema (DB MUST BE CLOSED)
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

    //ATHLETE comumn names
    private static final String KEY_SPORT = "sport";
    private static final String KEY_GOAL = "goal";
    private static final String KEY_USERNAME = "username";
    //SPORT table - column names
    private static final String KEY_SPORT_NAME = "name";

    //COMPETENCIES table - column names
    private static final String KEY_COMPETENCY_NAME = "name";

    //SPORT_COMPETENCIES table - column names (joined table)
    private static final String KEY_SPORT_ID = "sportId";
    private static final String KEY_COMPETENCY_ID = "competencyId";
    //private static final String KEY_COMPETENCY_BACKEND_ID = "backendId";

    //sport table create statement
    private static final String CREATE_TABLE_SPORT = "CREATE TABLE "+ TABLE_SPORTS
            + "("+KEY_ID+ " INTEGER PRIMARY KEY," + KEY_SPORT_NAME + " TEXT UNIQUE,"
            +KEY_CREATED_AT + " date default CURRENT_DATE"+")"; // UNIQUE(name) ON CONFLICT REPLACE
    private static final String CREATE_TABLE_COMPETENCIES = "CREATE TABLE "+ TABLE_COMPETENCIES
            + "("+KEY_ID+ " INTEGER PRIMARY KEY,"+" competencyId TEXT UNIQUE, " + KEY_COMPETENCY_NAME + " TEXT UNIQUE,"
            +KEY_CREATED_AT + " DATETIME" + ")";
    private static final String CREATE_TABLE_SPORT_COMPETENCIES = "CREATE TABLE "+ TABLE_SPORT_COMPETENCIES
            + "("+KEY_ID+ " INTEGER PRIMARY KEY," + KEY_SPORT_ID + " INTEGER," + KEY_COMPETENCY_ID + " TEXT UNIQUE,"
            +KEY_CREATED_AT + " DATETIME" + ")";
    private static final String CREATE_TABLE_ATHLETE = "CREATE TABLE "+ TABLE_ATHLETES + "("+KEY_ID+ " INTEGER PRIMARY KEY,"+
            appUserId+" TEXT, "+KEY_USERNAME + " TEXT, "+KEY_SPORT+" TEXT, "+KEY_GOAL+" TEXT, "+KEY_CREATED_AT+" DATETIME"+")";
    private static final String CREATE_TABLE_ATHLETE_COMPETENCIES = "CREATE TABLE "+TABLE_ATHLETE_COMPETENCIES
            +"("+KEY_ID+ " INTEGER PRIMARY KEY,"+ KEY_COMPETENCY_NAME+" TEXT,"+"competencyId TEXT"+")";

    private static final String CREATE_TABLE_APPUSERS = "CREATE TABLE "+ TABLE_APPUSER
            +"("+KEY_ID+ " INTEGER PRIMARY KEY, backendId TEXT, email TEXT, username TEXT, gender TEXT)";


    public DatabaseHelperOld(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String test = CREATE_TABLE_SPORT;
        String test1 = CREATE_TABLE_COMPETENCIES;
        String test2 = CREATE_TABLE_SPORT_COMPETENCIES;
        try {
            // creating required tables
            db.execSQL(CREATE_TABLE_SPORT);
            db.execSQL(CREATE_TABLE_COMPETENCIES);
            db.execSQL(CREATE_TABLE_SPORT_COMPETENCIES);
            db.execSQL(CREATE_TABLE_ATHLETE);
            db.execSQL(CREATE_TABLE_ATHLETE_COMPETENCIES);
            db.execSQL(CREATE_TABLE_APPUSERS);

        }catch (Exception e) {
            Log.d("onCreate ", e.toString());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String test = CREATE_TABLE_SPORT;
        String test1 = CREATE_TABLE_COMPETENCIES;
        String test2 = CREATE_TABLE_SPORT_COMPETENCIES;
        // on upgrade drop older tables
        try {
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SPORT);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_COMPETENCIES);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SPORT_COMPETENCIES);
        }catch (Exception e) {
            Log.d("onUpgrade", e.toString());
            e.printStackTrace();
        }


        // create new tables
        onCreate(db);
    }
    public synchronized long createSport(JSONObject sport) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();

            values.put(KEY_SPORT_NAME, sport.get("name").toString());
            //insert row
            long sport_id = db.insertOrThrow(TABLE_SPORTS, null, values);
            //handle the competencies on the sport object, insert in joined table
            createSportCompetencies(sport, sport_id);
            //db.close();
            return sport_id;
        }catch (Exception e) {
            Log.d("Exception CreateSport", e.toString());
            e.printStackTrace();
            //createSportCompetencies(sport, sport_id);
            try {
                String query = "SELECT "+KEY_ID+" FROM "+TABLE_SPORTS +" WHERE name='"+sport.getString("name")+"'";
                Cursor c = db.rawQuery(query, null);
                long sportId;
                if (c.moveToFirst()) {
                    do {
                        sportId = c.getLong(c.getColumnIndex(KEY_ID));
                        createSportCompetencies(sport, sportId);
                    } while (c.moveToNext());
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        db.close();
        return 0;

    }

    public synchronized void createSportCompetencies(JSONObject sport, long sport_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Integer> competencyPKs = new ArrayList<>();
        try {
            JSONArray arr = sport.getJSONArray("competencies");
            Date d = new Date(System.currentTimeMillis());
            for(int i = 0; i < arr.length(); i++) {
                //save as competencies
                JSONObject competency = arr.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(KEY_COMPETENCY_NAME, competency.getString("name").toString());
                //competencyID from the backend
                values.put(KEY_COMPETENCY_ID, competency.getString("_competencyId"));
                String query = "IF NOT EXISTS (SELECT * from competencies " +
                        "WHERE name = "+competency.getString("name")+
                        "INSERT INTO Competencies VALUES("+"";
                long competency_id = db.insertOrThrow(TABLE_COMPETENCIES, null, values);

                //save the primary keys for each created competency in list
                competencyPKs.add((int)competency_id);
            }
            //add all the list primary keys in the joined table SPORT_COMPETENCIES
            createSportCompetency(competencyPKs, sport_id);
            //db.close();
        }catch (Exception e) {
            Log.d("exception ", e.toString());
            //db.close();
        }

    }
    public  synchronized  void createSportCompetency(List<Integer> competencyPKs, long sportId) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for(Integer competencyPK : competencyPKs) {
                ContentValues values = new ContentValues();
                values.put(KEY_SPORT_ID, sportId);
                values.put(KEY_COMPETENCY_ID, competencyPK);
                db.insertOrThrow(TABLE_SPORT_COMPETENCIES, null, values);
            }
        }catch (Exception e) {
            Log.d("exception ",e.toString());
        }
    }
    public List<Sport> getAllSports() {
        List<Sport> sports = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SPORTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Sport sport = new Sport();
                sport.setPrimaryKey(c.getInt(c.getColumnIndex(KEY_ID)));
                sport.setName(c.getString(c.getColumnIndex(KEY_SPORT_NAME)));
                sport.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                sports.add(sport);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return sports;
    }
    public void deleteAllSports() {

        String deleteQuery = "DELETE FROM " + TABLE_SPORTS;

        Log.e(LOG, deleteQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }
    public List<Competency> getCompetencies(String sportId) {
        List<Competency> toReturn = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("GetCompetencies ", sportId);
        try {

            String query = "SELECT * FROM "+TABLE_SPORT_COMPETENCIES+ " LEFT JOIN "+ TABLE_COMPETENCIES +
                    " ON  sportCompetencies.competencyId = competencies.id WHERE sportId="+sportId; //WHERE sportCompetencies.sportId="+sportId
            Cursor c = db.rawQuery(query, null);

            if (c.moveToFirst()) {
                do {
                    String sportId1 =c.getString(c.getColumnIndex("sportId"));
                    Competency competency = new Competency();
                    competency.setPrimaryKey(c.getInt(c.getColumnIndex(KEY_ID)));
                    competency.setCompetencyId(c.getString(c.getColumnIndex(KEY_COMPETENCY_ID)));
                    competency.setName(c.getString(c.getColumnIndex(KEY_COMPETENCY_NAME)));
                    //sport.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                    toReturn.add(competency);
                } while (c.moveToNext());
            }
            c.close();
        }catch (Exception e) {
            Log.d("getCompetencies ", e.toString());
        }
        db.close();
        return toReturn;
    }
    public long saveAthlete(String athlete) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            JSONObject athleteObj = new JSONObject(athlete);
            ContentValues values = new ContentValues();
            values.put(appUserId, athleteObj.getString(appUserId));
            values.put(KEY_GOAL, athleteObj.getString("goal"));
            values.put(KEY_SPORT, athleteObj.getString("sport"));
            //values.put(KEY_SPORT_NAME, athleteObj.getString("name"));
            //values.put(KEY_USERNAME, athleteObj.getString("username"));
            //values.put()
            //insert row
            long athlete_id = db.insertOrThrow(TABLE_ATHLETES, null, values);
            //handle the competencies on the sport object, insert in joined table
            //saveAthleteCompetencies(athleteObj.getJSONArray("competencies"));
            db.close();
            return athlete_id;
        }catch (Exception e) {
            //Log.d("Exception CreateSport", e.toString());
            e.printStackTrace();
        }
        db.close();
        return 0;
    }
    public long saveAthleteCompetencies(List<Competency> competencies) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < competencies.size(); i++) {
                Competency competency = competencies.get(i);
                ContentValues values = new ContentValues();
                values.put("name", competency.getName());
                values.put("competencyId", competency.getCompetencyId());
                long id = db.insert(TABLE_ATHLETE_COMPETENCIES, null, values);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Competency> getAthleteCompetencies() {

        List<Competency> toReturn = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM AthleteCompetencies";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    Competency competency = new Competency();

                    competency.setName(c.getString(c.getColumnIndex(KEY_COMPETENCY_NAME)));
                    competency.setCompetencyId(c.getString(c.getColumnIndex("competencyId")));

                    toReturn.add(competency);
                } while (c.moveToNext());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        db.close();
        return toReturn;
    }
    public long createAppUser(AppUser user) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("backendId", user.getBackendId());
        values.put("email", user.getEmail());
        values.put("username", user.getUsername());
        values.put("gender", user.getGender());
        long id = db.insertOrThrow(TABLE_APPUSER, null, values);
        db.close();
        return id;
    }
    public AppUser getAppUser() {
        String query = "SELECT * from AppUsers";
        AppUser toReturn;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {

            int primaryKey = c.getInt(c.getColumnIndex(KEY_ID));
            String backendId = c.getString(c.getColumnIndex("backendId"));
            String email = c.getString(c.getColumnIndex("email"));
            String username = c.getString(c.getColumnIndex("username"));
            String gender = c.getString(c.getColumnIndex("gender"));
            toReturn = new AppUser(primaryKey, backendId, email, username, gender);
            return toReturn;

        } while (c.moveToNext());
        db.close();
        return new AppUser();

    }
    public List<Athlete> getAllAthletes() {
        List<Athlete> athletes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ATHLETES;
        try {
            Log.e(LOG, selectQuery);


            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Athlete athlete = new Athlete();
                    athlete.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
                    //athlete.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                    athlete.setGoal(c.getString(c.getColumnIndex("goal")));
                    // String goal = c.
                    athlete.setSport(c.getString(c.getColumnIndex(KEY_SPORT)));
                    athlete.setAppUserId(c.getString(c.getColumnIndex(appUserId)));
                    //athlete.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                    athletes.add(athlete);
                } while (c.moveToNext());
            }
            c.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return athletes;

    }
}
