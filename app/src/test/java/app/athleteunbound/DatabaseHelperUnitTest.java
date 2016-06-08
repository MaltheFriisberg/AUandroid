package app.athleteunbound;

import android.database.sqlite.SQLiteDatabase;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import app.athleteunbound.DatabaseHelpers.Database;
import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.RESTmodels.Athlete;

/**
 * Created by Mal on 08-06-2016.
 */
public class DatabaseHelperUnitTest {
    private DatabaseHelper databaseHelper;
    @Mock
    private Database database;

    @Before
    private void setUp() {
        //this.database =  new Database(new MockContext());
        this.database = Mockito.mock(Database.class);
        SQLiteDatabase db = this.database.getReadableDatabase();
        SQLiteDatabase db1 = this.database.getReadableDatabase();
        this.databaseHelper = new DatabaseHelper(this.database);
    }
    @Test
    private void test() {
        //database.
    }
}
