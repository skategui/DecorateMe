package managers;

/**
 * Created by guillaumeagis on 21/04/15.
 * Information related to the user, with his current lat and long
 */
public class UserProfile {


    private static UserProfile _instance = null;

    private UserProfile() {
    }

    /**
     * Singleton
     * @return instance of this class
     */
    public static UserProfile getInstance() {
        if(_instance == null) {
            _instance = new UserProfile();
        }
        return _instance;
    }

}
