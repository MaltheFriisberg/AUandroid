package app.athleteunbound.DatabaseHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mal on 07-06-2016.
 */
public class Database extends SQLiteOpenHelper {
    //getApplicationContext().deleteDatabase("AthleteManager"); //DELETES the DB only use when changing the schema (DB MUST BE CLOSED)


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name


    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
            return super.getReadableDatabase();
    }

    public Database(Context context) {
        super(context, DbStrings.DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // creating required tables
            db.execSQL(DbStrings.CREATE_TABLE_SPORT);
            db.execSQL(DbStrings.CREATE_TABLE_COMPETENCIES);
            db.execSQL(DbStrings.CREATE_TABLE_SPORT_COMPETENCIES);
            db.execSQL(DbStrings.CREATE_TABLE_ATHLETE);
            db.execSQL(DbStrings.CREATE_TABLE_ATHLETE_COMPETENCIES);
            db.execSQL(DbStrings.CREATE_TABLE_APPUSERS);

        }catch (Exception e) {
            Log.d("onCreate ", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        try {
            db.execSQL("DROP TABLE IF EXISTS " + DbStrings.CREATE_TABLE_SPORT);
            db.execSQL("DROP TABLE IF EXISTS " + DbStrings.CREATE_TABLE_COMPETENCIES);
            db.execSQL("DROP TABLE IF EXISTS " + DbStrings.CREATE_TABLE_SPORT_COMPETENCIES);
        }catch (Exception e) {
            Log.d("onUpgrade", e.toString());
            e.printStackTrace();
        }


        // create new tables
        onCreate(db);
    }
}
