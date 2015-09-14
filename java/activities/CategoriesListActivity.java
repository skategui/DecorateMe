package activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.spleat.R;

import java.util.ArrayList;

import Utils.GetProductsTask;
import managers.ActivityManager;
import managers.CollectionManager;
import recycler.ProductCard;
import recycler.RecyclerViewAdapter;

/**
 * Created by guillaumeagis on 21/04/15.
 * display the list of venues, once loaded.
 * All the venues are sorted by the distance from the user.
 * It is possible to pull to refresh the list if the user is not connected to internet when he arrives here
 */
public class CategoriesListActivity extends Activity {

    private RecyclerViewAdapter _adapter;
    private LinearLayoutManager _layoutManager;
    private RecyclerView _recyclerView;
    private RelativeLayout _noResultBlock;
    private TextView _title;

    // current list of item
    private ArrayList<ProductCard> _objectsList = new ArrayList<ProductCard>();

    private FloatingActionButton _button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        this.initializeRecyclerView();
        this.updateView(CollectionManager._hasInternet);
        this.createListObject();
        this.init();
        this.update();

    }



    /**
     * Check if there is there is any item in the list , if not : display "No result" message
     */
    private void updateView(boolean internet)
    {
        if (internet == false)
        {
            this._noResultBlock.setVisibility(View.VISIBLE);
            this._recyclerView.setVisibility(View.GONE);
            this._title.setVisibility(View.GONE);
            this._button.setVisibility(View.GONE);
        }
        else
        {
            this._noResultBlock.setVisibility(View.GONE);
            this._recyclerView.setVisibility(View.VISIBLE);
        }
    }



    /**
     * Initialize the recycler view and the floating button in same time
     * When the user scrolls the view, the button goes down
     */
private void initializeRecyclerView()
{
    this._recyclerView = (RecyclerView)this.findViewById(R.id.recyclerView);
    this._noResultBlock = (RelativeLayout) this.findViewById(R.id.noResult);

    _button = (FloatingActionButton) findViewById(R.id.fab);
    _button.setVisibility(View.GONE);

    this._title = (TextView)this.findViewById(R.id.tvTitle);
    this._title.setText("INTERESTED IN?");

}


    /**
     * Add all venues in the list
     *  Clear the object list and sort the venue list by the distance from the user, also called when pull to refresh action is done
     */
    private void createListObject()
    {

        this._objectsList.clear();
        CollectionManager.getInstance().cleanList();

        int size = CollectionManager.getInstance().getCategories().size();
        for (int position = 0; position < size ; position++)
        {
            this._objectsList.add( new ProductCard(position, this, true));
        }
    }


    /**
     * get items in the root view related to the components
     */
    private void init()
    {
        this._layoutManager = new LinearLayoutManager(this);
        this._recyclerView.setLayoutManager(_layoutManager);
        this._recyclerView.setItemAnimator(new DefaultItemAnimator());
        this._adapter = new RecyclerViewAdapter(_objectsList);
        this._recyclerView.setAdapter(_adapter);
    }

    /**
     * call all the update views of each item in the adapter
     */
    private void update()
    {
        if (this._adapter != null)
            this._adapter.notifyDataSetChanged();
    }



    // kill app when user press back button
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
