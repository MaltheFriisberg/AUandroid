package app.athleteunbound;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Mal on 02-05-2016.
 */
public class SwipeGestureDetector
        extends GestureDetector.SimpleOnGestureListener {
    // Swipe properties, you can change it to make the swipe
    // longer or shorter and speed
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 200;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    /*Activity activity;
    public SwipeGestureDetector(Activity activity) {
        this.activity = activity;
    }*/

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        try {
            float diffAbs = Math.abs(e1.getY() - e2.getY());
            float diff = e1.getX() - e2.getX();

            if (diffAbs > SWIPE_MAX_OFF_PATH)
                return false;

            // Left swipe
            if (diff > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onLeftSwipe();


                // Right swipe
            } else if (-diff > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onRightSwipe();
            }
        } catch (Exception e) {
            Log.e("YourActivity", "Error on gestures");
        }
        return false;
    }
    private void onLeftSwipe() {
        Log.d("swiped left", "");
    }
    private void onRightSwipe() {
        Log.d("swiped right", "");
    }
}

