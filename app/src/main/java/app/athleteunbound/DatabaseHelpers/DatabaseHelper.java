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
public class DatabaseHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    private Database myDataBase;
    private SQLiteDatabase readableDb;
    private SQLiteDatabase writeableDb;

    //constructor for unit testing
    public DatabaseHelper(Database db) {
        this.myDataBase = db;
        this.readableDb = db.getReadableDatabase();
        this.writeableDb = db.getWritableDatabase();
    }

    public DatabaseHelper(Context context) {

        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myDataBase = new Database(context);
        this.writeableDb = myDataBase.getWritableDatabase();
        this.readableDb = myDataBase.getReadableDatabase();
    }

    public DatabaseHelper(Database db, SQLiteDatabase readableDb, SQLiteDatabase writeableDb) {
        this.myDataBase = db;
        this.readableDb = readableDb;
        this.writeableDb = writeableDb;
    }

    public synchronized long createSport(JSONObject sport) {
        SQLiteDatabase db = myDataBase.getWritableDatabase();
        //Database db1 = new Database(getContext());

        try {

            ContentValues values = new ContentValues();

            values.put(DbStrings.KEY_SPORT_NAME, sport.get("name").toString());
            //insert row
            long sport_id = db.insertOrThrow(DbStrings.TABLE_SPORTS, null, values);
            //handle the competencies on the sport object, insert in joined table
            createSportCompetencies(sport, sport_id);
            //db.close();
            return sport_id;
        }catch (Exception e) {
            Log.d("Exception CreateSport", e.toString());
            e.printStackTrace();
            //createSportCompetencies(sport, sport_id);
            try {
                String query = "SELECT "+DbStrings.KEY_ID+" FROM "+DbStrings.TABLE_SPORTS +" WHERE name='"+sport.getString("name")+"'";
                Cursor c = db.rawQuery(query, null);
                long sportId;
                if (c.moveToFirst()) {
                    do {
                        sportId = c.getLong(c.getColumnIndex(DbStrings.KEY_ID));
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
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = myDataBase.getWritableDatabase();

        List<Integer> competencyPKs = new ArrayList<>();
        try {
            JSONArray arr = sport.getJSONArray("competencies");
            Date d = new Date(System.currentTimeMillis());
            for(int i = 0; i < arr.length(); i++) {
                //save as competencies
                JSONObject competency = arr.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DbStrings.KEY_COMPETENCY_NAME, competency.getString("name").toString());
                //competencyID from the backend
                values.put(DbStrings.KEY_COMPETENCY_ID, competency.getString("_competencyId"));
                String query = "IF NOT EXISTS (SELECT * from competencies " +
                        "WHERE name = "+competency.getString("name")+
                        "INSERT INTO Competencies VALUES("+"";
                long competency_id = db.insertOrThrow(DbStrings.TABLE_COMPETENCIES, null, values);

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
        db.close();

    }
    public  synchronized  void createSportCompetency(List<Integer> competencyPKs, long sportId) {
        SQLiteDatabase db = myDataBase.getWritableDatabase();
        try {
            //SQLiteDatabase db = this.getWritableDatabase();
            for(Integer competencyPK : competencyPKs) {
                ContentValues values = new ContentValues();
                values.put(DbStrings.KEY_SPORT_ID, sportId);
                values.put(DbStrings.KEY_COMPETENCY_ID, competencyPK);
                db.insertOrThrow(DbStrings.TABLE_SPORT_COMPETENCIES, null, values);

            }
        }catch (Exception e) {
            Log.d("exception ",e.toString());
            db.close();
        }
        db.close();
    }
    public List<Sport> getAllSports() {
        List<Sport> sports = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DbStrings.TABLE_SPORTS;

        Log.e(LOG, selectQuery);

        //SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db = myDataBase.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Sport sport = new Sport();
                sport.setPrimaryKey(c.getInt(c.getColumnIndex(DbStrings.KEY_ID)));
                sport.setName(c.getString(c.getColumnIndex(DbStrings.KEY_SPORT_NAME)));
                sport.setCreatedAt(c.getString(c.getColumnIndex(DbStrings.KEY_CREATED_AT)));
                sports.add(sport);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return sports;
    }
    public void deleteAllSports() {

        String deleteQuery = "DELETE FROM " + DbStrings.TABLE_SPORTS;

        Log.e(LOG, deleteQuery);

        //SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db = myDataBase.getReadableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }
    public List<Competency> getCompetencies(String sportId) {
        List<Competency> toReturn = new ArrayList<>();
        SQLiteDatabase db = myDataBase.getReadableDatabase();
        Log.d("GetCompetencies ", sportId);
        try {

            String query = "SELECT * FROM "+DbStrings.TABLE_SPORT_COMPETENCIES+ " LEFT JOIN "+ DbStrings.TABLE_COMPETENCIES +
                    " ON  sportCompetencies.competencyId = competencies.id WHERE sportId="+sportId; //WHERE sportCompetencies.sportId="+sportId
            Cursor c = db.rawQuery(query, null);

            if (c.moveToFirst()) {
                do {
                    String sportId1 =c.getString(c.getColumnIndex("sportId"));
                    Competency competency = new Competency();
                    competency.setPrimaryKey(c.getInt(c.getColumnIndex(DbStrings.KEY_ID)));
                    competency.setCompetencyId(c.getString(c.getColumnIndex(DbStrings.KEY_COMPETENCY_ID)));
                    competency.setName(c.getString(c.getColumnIndex(DbStrings.KEY_COMPETENCY_NAME)));
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

        SQLiteDatabase db = myDataBase.getWritableDatabase();

        try {
            JSONObject athleteObj = new JSONObject(athlete);
            ContentValues values = new ContentValues();
            values.put(DbStrings.appUserId, athleteObj.getString(DbStrings.appUserId));
            values.put(DbStrings.KEY_GOAL, athleteObj.getString("goal"));
            values.put(DbStrings.KEY_SPORT, athleteObj.getString("sport"));
            //values.put(KEY_SPORT_NAME, athleteObj.getString("name"));
            //values.put(KEY_USERNAME, athleteObj.getString("username"));
            //values.put()
            //insert row
            long athlete_id = db.insertOrThrow(DbStrings.TABLE_ATHLETES, null, values);
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
        SQLiteDatabase db = myDataBase.getWritableDatabase();
        try {
            for (int i = 0; i < competencies.size(); i++) {
                Competency competency = competencies.get(i);
                ContentValues values = new ContentValues();
                values.put("name", competency.getName());
                values.put("competencyId", competency.getCompetencyId());
                long id = db.insert(DbStrings.TABLE_ATHLETE_COMPETENCIES, null, values);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Competency> getAthleteCompetencies() {

        List<Competency> toReturn = new ArrayList<>();
        SQLiteDatabase db = myDataBase.getReadableDatabase();
        String query = "SELECT * FROM AthleteCompetencies";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    Competency competency = new Competency();

                    competency.setName(c.getString(c.getColumnIndex(DbStrings.KEY_COMPETENCY_NAME)));
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
        SQLiteDatabase db = myDataBase.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("backendId", user.getBackendId());
        values.put("email", user.getEmail());
        values.put("username", user.getUsername());
        values.put("gender", user.getGender());
        long id = db.insertOrThrow(DbStrings.TABLE_APPUSER, null, values);
        db.close();
        return id;
    }
    public AppUser getAppUser() {
        String query = "SELECT * from AppUsers";
        AppUser toReturn;
        SQLiteDatabase db = myDataBase.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {

            int primaryKey = c.getInt(c.getColumnIndex(DbStrings.KEY_ID));
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
        SQLiteDatabase db = myDataBase.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DbStrings.TABLE_ATHLETES;
        try {
            Log.e(LOG, selectQuery);


            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Athlete athlete = new Athlete();
                    athlete.set_id(c.getInt(c.getColumnIndex(DbStrings.KEY_ID)));
                    //athlete.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                    athlete.setGoal(c.getString(c.getColumnIndex("goal")));
                    // String goal = c.
                    athlete.setSport(c.getString(c.getColumnIndex(DbStrings.KEY_SPORT)));
                    athlete.setAppUserId(c.getString(c.getColumnIndex(DbStrings.appUserId)));
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
