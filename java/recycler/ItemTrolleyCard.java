package recycler;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spleat.R;

import org.json.JSONException;
import org.json.JSONObject;

import Utils.ImageUtils;
import activities.TrolleyActivity;

/**
 * Created by guillaumeagis on 21/04/15.
 * Offer item in the list (recycler view)
 */
public class ItemTrolleyCard{

    // current activity
    private TrolleyActivity _act;

    // venue to display
    private JSONObject _current;

    private int position;


    /**
     * constructor
     * get the venue by the given position in parameter
     * @param act current activity
     * @param position
     */
    public ItemTrolleyCard(JSONObject object, TrolleyActivity act, int position) {
        this._act = act;
        this._current = object;
        this.position = position;
    }

    public static class itemAdapter extends RecyclerViewAdapterTrolley.ViewHolder {

        // background_effect img
        private ImageView _ivBackground;
        private ImageView _delete;
        // title
        private TextView _tvTitle;

        private TextView _tvPrice;

        public itemAdapter(View v) {
            super(v);
            this._tvTitle = (TextView)v.findViewById(R.id.tvTitle);
            this._tvPrice = (TextView)v.findViewById(R.id.tvPrice);
            this._delete = (ImageView)v.findViewById(R.id.ivDelete);
            this._ivBackground = (ImageView) v.findViewById(R.id.ivImage);

        }
    }

    /***
     * Update the view
     * @param holder current placeHolder
     */
    public void updateView(final RecyclerViewAdapterTrolley.ViewHolder holder) {

        try {
            ((itemAdapter)holder)._delete.getDrawable().setColorFilter(_act.getResources().getColor(R.color.custom_green),
                    PorterDuff.Mode.MULTIPLY );

            ((itemAdapter)holder)._tvTitle.setText(_current.getString("title"));
            ((itemAdapter)holder)._tvPrice.setText(_current.getString("price"));
            ImageUtils.loadImageUrl(_current.getString("pictureUrl"), ((itemAdapter) holder)._ivBackground, _act);
            ((itemAdapter)holder)._delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        // delete item from list
                    _act.removeItem(position, 123);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 3 items per view, with some margin between them

    }


    /**
     * Inflate the view related to the venue
     * @param parent parent
     * @return view inflated
     */
    public static View getView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return v;
    }

}