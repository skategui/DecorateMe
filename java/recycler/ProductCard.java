package recycler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.spleat.R;

import Utils.Product;
import managers.ActivityManager;
import managers.CollectionManager;
import Utils.ImageUtils;
import Utils.ScreenHelper;

/**
 * Created by guillaumeagis on 21/04/15.
 * Offer item in the list (recycler view)
 */
public class ProductCard {

    // current activity
    private Activity _act;

    // venue to display
    private Product _current;

    private boolean _hasAction = false;

    /**
     * constructor
     * get the venue by the given position in parameter
     * @param position position in the list
     * @param act current activity
     */
    public ProductCard(int position, Activity act) {
        this._act = act;
        _hasAction = false;
        this._current = CollectionManager.getInstance().getCollectionByPosition(position);
    }

    public ProductCard(int position, Activity act, boolean hasAction) {
        this._act = act;
        this._hasAction = hasAction;
        this._current = CollectionManager.getInstance().getCategoryByPosition(position);
    }

    public static class itemAdapter extends RecyclerViewAdapter.ViewHolder {

        // background_effect img
        private ImageView _ivBackground;

        // title
        private TextView _tvTitle;

        public itemAdapter(View v) {
            super(v);
            this._tvTitle = (TextView)v.findViewById(R.id.tvName);
            this._ivBackground = (ImageView) v.findViewById(R.id.ivBackground);
        }
    }

    /***
     * Update the view
     * @param holder current placeHolder
     */
    public void updateView(final RecyclerViewAdapter.ViewHolder holder) {

        ((itemAdapter)holder)._tvTitle.setText(_current.getTitle());


        // 3 items per view, with some margin between them
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((itemAdapter)holder)._ivBackground.getLayoutParams();
        params.height = ((ScreenHelper.getHeightScreen(_act) ) / 3 -  (9 * 3)) - 65;
        ((itemAdapter)holder)._ivBackground.setLayoutParams(params);
        ImageUtils.loadImage(_current.getDrawable(), ((itemAdapter) holder)._ivBackground, _act);
        if (_hasAction == true)
        {
            ((itemAdapter)holder)._ivBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityManager.push(_act, ActivityManager.eType.SEARCH);
                }
            });
        }
    }


    /**
     * Inflate the view related to the venue
     * @param parent parent
     * @return view inflated
     */
    public static View getView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return v;
    }

}