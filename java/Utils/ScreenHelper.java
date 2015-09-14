package Utils;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by guillaumeagis on 21/04/15.
 * Check the size of the phone screen
 */
public class ScreenHelper {

    /**
     * Get height of the screen
     * @param act current Activity
     * @return height
     */
    public static int getHeightScreen(Activity act)
    {
        Display display = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        return height - getStatusBarHeight(act);
    }


    /**
     * Check height of the status bar
     * @param act curreny activity
     * @return height of the status bar, 0 if not found
     */
    private static int getStatusBarHeight(Activity act) {
        int result = 0;
        int resourceId = act.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = act.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
