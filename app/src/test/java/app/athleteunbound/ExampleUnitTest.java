package app.athleteunbound;

import android.test.mock.MockContext;

import org.junit.Test;
import org.mockito.Mock;

import app.athleteunbound.DatabaseHelpers.Database;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Mock Database database = new Database(new MockContext());
    @Test
    public void addition_isCorrect() throws Exception {

        MockContext context = new MockContext();
        Database db = new Database(context);
        assertEquals(4, 2 + 2);
    }
}