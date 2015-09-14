package managers;

import com.spleat.R;

import java.util.ArrayList;

import Utils.Product;

/**
 * Created by guillaumeagis on 21/04/15.
 * Manage all the actions related to the venues
 * This class is serializable because the json is stored locally in order to get access to the list of venues offline !
 */
public class CollectionManager {

    public static boolean _hasInternet = true;
    /**
     * list of venues
     */
    private  ArrayList<Product> _collectionList = new ArrayList<>();
    private  ArrayList<Product> _categoriesList = new ArrayList<>();

    private static CollectionManager _instance = null;

    private CollectionManager() {

        _categoriesList.add(new Product("Living room", R.drawable.living));
        _categoriesList.add(new Product("Bedroom", R.drawable.bedroom));
        _categoriesList.add(new Product("Kitchen", R.drawable.kitchen));
        _categoriesList.add(new Product("Bathroom", R.drawable.bath));
    }

    /**
     * Singleton
     * @return instance of this class
     */
    public static CollectionManager getInstance() {
        if(_instance == null) {
            _instance = new CollectionManager();
        }
        return _instance;
    }

    public ArrayList<Product> getCategories()
    {
        return _categoriesList;
    }

    public Product getCategoryByPosition(int position)
    {
        return _categoriesList.get(position);
    }

    public void cleanList()
    {
        _collectionList.clear();
    }

    public Product getCollectionByPosition(int position)
    {
        return _collectionList.get(position);
    }

    public void addCollection(String json)
    {
    }
}
