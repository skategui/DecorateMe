package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spleat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Utils.GetProductsTask;
import Utils.ImageUtils;
import managers.ActivityManager;
import managers.UserManager;
import stripe.PaymentManager;

/**
 * Created by guillaumeagis on 21/04/15.
 * display the list of venues, once loaded.
 * All the venues are sorted by the distance from the user.
 * It is possible to pull to refresh the list if the user is not connected to internet when he arrives here
 */
public class SearchActivity extends Activity {


    private TextView _title;
    private TextView _productName;
    private TextView _nbArticleView;
    private TextView _productPrice;
    private ImageView _productImage;
    private RelativeLayout _rlcheckout;

    private LinearLayout _dislike;
    private LinearLayout _addTrolley;

    private int step = 0;

    private HashMap<String, Integer> _decisions = new HashMap<>();


    private ArrayList<String> _titlesList = new ArrayList<String>();


    private JSONObject _current = null;

    public static SearchActivity searchActivity = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recommend);
        GetProductsTask my_task = new GetProductsTask(this,false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            my_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
        else
            my_task.execute("1");

        searchActivity = this;
        this._titlesList.add("Recommended product");
        this._titlesList.add("Fancy enough ?");
        this._titlesList.add("What about this one ?");
        this._titlesList.add("My favorite one !");
        this._titlesList.add("Check this out !");

        this.initializeRecyclerView();

    }

    /**
     * Initialize the recycler view and the floating button in same time
     * When the user scrolls the view, the button goes down
     */
private void initializeRecyclerView()
{
    this._title = (TextView)this.findViewById(R.id.tvTitle);

    final ImageView dislikeIcon = (ImageView)this.findViewById(R.id.ivDislikeIcon);
    dislikeIcon.getDrawable().setColorFilter(this.getResources().getColor(R.color.holo_red), PorterDuff.Mode.MULTIPLY );

    final ImageView addIcon = (ImageView)this.findViewById(R.id.addIcon);
    addIcon.getDrawable().setColorFilter(this.getResources().getColor(R.color.custom_green), PorterDuff.Mode.MULTIPLY );

    this._productName = (TextView)this.findViewById(R.id.tvTitleProduct);
    this._productPrice = (TextView)this.findViewById(R.id.tvPrice);
    this._productImage = (ImageView)this.findViewById(R.id.ivBackground);
    this._nbArticleView = (TextView)this.findViewById(R.id.tvNBArticle);

    _nbArticleView.setText("CHECKOUT");

    this._dislike = (LinearLayout)this.findViewById(R.id.ivDislike);
    this._addTrolley = (LinearLayout)this.findViewById(R.id.ivAdd);
    this._rlcheckout = (RelativeLayout)this.findViewById(R.id.rlcheckout);

    this._rlcheckout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (UserManager.getInstance().getItems().size() > 0)
                ActivityManager.push(SearchActivity.this, ActivityManager.eType.CHECKOUT);
        }
    });


    this._dislike.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {

            if (arg1.getAction() != MotionEvent.ACTION_DOWN) {
                _dislike.setBackgroundResource(R.drawable.circlebutton_red);
                dislikeIcon.getDrawable().setColorFilter(getResources().getColor(R.color.holo_red), PorterDuff.Mode.MULTIPLY);
            } else {
                _dislike.setBackgroundResource(R.drawable.circlebutton_red_pressed);
                dislikeIcon.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                displayDialog();
            }
            return true;
        }
    });

    this._addTrolley.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {

            if (arg1.getAction() != MotionEvent.ACTION_DOWN) {
                _addTrolley.setBackgroundResource(R.drawable.circlebutton_green);
                addIcon.getDrawable().setColorFilter(getResources().getColor(R.color.custom_green_trans), PorterDuff.Mode.MULTIPLY);
            } else {
                _addTrolley.setBackgroundResource(R.drawable.circlebutton_green_pressed);
                addIcon.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                updateButton();
                UserManager.getInstance().add(_current);


                new GetProductsTask(SearchActivity.this, false).execute("" + _decisions.get("like_id"));

                try {
                    if (_current != null)
                        Toast.makeText(SearchActivity.this, _current.getString("title") + " has been added to the trolley!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    });
}

    public void refreshTrolley()
    {
        UserManager.getInstance().getItems().clear();
        this._nbArticleView.setText("CHECKOUT");
    }



    private void displayDialog()
    {
        final CharSequence[] items = {
                "Too expensive", "I don't like the color", "I don't like the design"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("Why you don't like it ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                       int result = _decisions.get(String.valueOf(items[item]));
                new GetProductsTask(SearchActivity.this, false).execute("" + result);
                //  new GetProductsTask(SearchActivity.this, false).execute("" + _decisions.get(result));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateButton()
    {
        if (UserManager.getInstance().getItems().size() > 0)
        {
            _rlcheckout.setBackgroundColor(0);
            _rlcheckout.setBackgroundResource(R.drawable.button_green_white);

        }
        else
        {
            _rlcheckout.setBackgroundResource(0);
            _rlcheckout.setBackgroundColor(getResources().getColor(R.color.cant_click));
        }

        if (UserManager.getInstance().getItems().size() > 0)
            _nbArticleView.setText("CHECKOUT (" + UserManager.getInstance().getItems().size() + ")");
        else
            _nbArticleView.setText("CHECKOUT");

    }

    public void updateInfo(final String jsonReceived) throws JSONException {

        JSONObject jo = new JSONObject(jsonReceived);

        updateButton();

        _current = jo;

        Log.e("", "jo : " + jo);
        String name = jo.getString("title");
        String price = jo.getString("price");
        String imageUrl = jo.getString("pictureUrl");

        _decisions.clear();

        if (jo.isNull("like_id") == false)
            _decisions.put("like_id", jo.getInt("like_id"));
        if (jo.isNull("dontlike_id") == false)
            _decisions.put("dontlike_id", jo.getInt("dontlike_id"));
        if (jo.isNull("dontlikeexpensive_id") == false)
        _decisions.put("Too expensive", jo.getInt("dontlikeexpensive_id"));
        if (jo.isNull("dontlikecolour_id") == false)
        _decisions.put("I don't like the color", jo.getInt("dontlikecolour_id"));
        if (jo.isNull("dontlikesize_id") == false)
        _decisions.put("I don't like the design", jo.getInt("dontlikesize_id"));

        this._title.setText(_titlesList.get(step));
        this._productName.setText(name);
        this._productPrice.setText(price);
        ImageUtils.loadImageUrl(imageUrl, this._productImage, this);
        step++;
        if (step > 4)
            step = 4;
    }






    // kill app when user press back button
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
