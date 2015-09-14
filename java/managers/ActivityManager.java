package managers;

import android.app.Activity;
import android.content.Intent;

import activities.CategoriesListActivity;
import activities.IntroActivity;
import activities.ScanActivity;
import activities.SearchActivity;
import activities.TrolleyActivity;

import java.util.ArrayList;


/**
 * Created by guillaumeagis on 21/04/15.
 * This class will handle all the ActivityInfo inside the app, doing transition and passing from one to an other
 */
public class ActivityManager
{
    public enum eType
    {
         // login page
        LOGIN,
         //  list venue page
        LIST,
        // google map activity
        CATEGORY,
        SEARCH,
        PAY,
        CHECKOUT,
        SCAN
    }

    static private ArrayList<Intent> stack = new ArrayList<Intent>();


    /**
     * Pop from the stack. If we are popping the last element, then push actStream onto the stack.
     */
    public static void pop(Activity activity)
    {
        if (stack.size() > 1)
        {
            stack.remove(stack.size() - 1);
            activity.startActivity(stack.get(stack.size() - 1)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    /**
     * push activity without parameters
     * @param type
     */
    public static boolean push(Activity activity, eType type)
    {
        if (activity == null)
            return false;
        // Set up startActivity intent
        Intent intent = getIntentByAct(activity, type);
        addInStackAndStartActivity(activity, intent);
        return true;
    }

    /**
     * add activity in stack and start it
     * @param intent intent of the activity
     */
    private static void addInStackAndStartActivity(Activity activity, Intent intent)
    {
        stack.add(intent);
        activity.startActivity(stack.get(stack.size() - 1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    static Intent getIntentByAct(Activity activity, eType type)
    {
        Intent intent;
        switch (type) {
            case LOGIN:
                intent = new Intent(activity, IntroActivity.class);
                break;
            case LIST:
                intent = new Intent(activity, CategoriesListActivity.class);
                break;
            case CATEGORY:
                intent = new Intent(activity, CategoriesListActivity.class);
                break;
            case SEARCH:
                intent = new Intent(activity, SearchActivity.class);
                break;
            case CHECKOUT:
                intent = new Intent(activity, TrolleyActivity.class);
                break;
            case SCAN:
                intent = new Intent(activity, ScanActivity.class);
                break;
            default:
                intent = new Intent(activity, CategoriesListActivity.class);
                break;
        }
        return intent;
    }

}