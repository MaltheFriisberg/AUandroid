package app.athleteunbound;

import android.database.sqlite.SQLiteDatabase;
import android.test.mock.MockContext;
import android.util.Log;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import app.athleteunbound.DatabaseHelpers.Database;
import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.DatabaseHelpers.DbStrings;
import app.athleteunbound.RESTmodels.Athlete;

/**
 * Created by Mal on 08-06-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class DatabaseHelperUnitTest {
    private DatabaseHelper databaseHelper;
    @Mock
    private Database database;
    SQLiteDatabase readAble;
    SQLiteDatabase writeAble;

    @Before
    public void setUp() {
        //this.database =  new Database(new MockContext());
        this.database = Mockito.mock(Database.class);
        //SQLiteDatabase db = this.database.getReadableDatabase();
        //SQLiteDatabase db1 = this.database.getReadableDatabase();
        this.readAble = Mockito.mock(SQLiteDatabase.class);
        this.writeAble = Mockito.mock(SQLiteDatabase.class);
        this.databaseHelper = new DatabaseHelper(this.database, this.readAble, this.writeAble);
    }
    @Test
    public void testCreateSport() {
        try {
            //JSONObject sport = new JSONObject();
            //DatabaseHelper mock = Mockito.mock(DatabaseHelper.class);
            JSONObject obj = Mockito.mock(JSONObject.class);
            Mockito.when(obj.getString("name").toString()).thenReturn("name");
            databaseHelper.createSport(obj);

            // Mockito.when(databaseHelper.createSport(Mockito.any(JSONObject.class)));
            //mock.createSport(obj);
           Mockito.verify(writeAble, Mockito.times(100)).insertOrThrow("", null, null);
            Mockito.verify(writeAble).insertOrThrow("foooo", null, null);
            Mockito.verify(writeAble, Mockito.never()).insertOrThrow("", null, null);
            Mockito.verify(writeAble).rawQuery("", null);
            Mockito.verify(writeAble.insertOrThrow("fooo", null, null));
            Mockito.verify(writeAble, Mockito.never()).close();
            List mockedList = Mockito.mock(List.class);

// using mock object - it does not throw any "unexpected interaction" exception
            mockedList.add("three");
            mockedList.clear();

// selective, explicit, highly readable verification
            Mockito.verify(mockedList).add("one");
            Mockito.verify(mockedList).clear();
        } catch (Exception e) {
            int x = 7;
            e.printStackTrace();
        }

    }
    @Test
    public void test() {
        List mockedList = Mockito.mock(List.class);

// using mock object - it does not throw any "unexpected interaction" exception
        mockedList.add("one");
        mockedList.clear();

// selective, explicit, highly readable verification
        Mockito.verify(mockedList).add("one");
        Mockito.verify(mockedList).clear();
    }
}
