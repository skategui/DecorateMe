package managers;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by agisg on 12/09/15.
 */
public class UserManager {

    private ArrayList<JSONObject> _itemsTrolley = new ArrayList<>();

    private static UserManager _instance = null;

    public static String _cardNumber = "";
    public static int expiryMonth = 0;
    public static int expiryYear = 0;
    public static String CVV = "";
    public static String FullName = "";
    /**
     * Singleton
     * @return instance of this class
     */
    public static UserManager getInstance() {
        if(_instance == null) {
            _instance = new UserManager();
        }
        return _instance;
    }

    public void clear()
    {
        _itemsTrolley.clear();
    }

    public void add(final JSONObject json)
    {
        _itemsTrolley.add(json);
    }

    public ArrayList<JSONObject> getItems()
    {
        return _itemsTrolley;
    }

    public void delete(final JSONObject json)
    {
        _itemsTrolley.remove(json);
    }

}
