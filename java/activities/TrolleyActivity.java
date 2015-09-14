package activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.spleat.R;
import com.vinaygaba.creditcardview.CreditCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Product;
import managers.ActivityManager;
import managers.CollectionManager;
import managers.UserManager;
import recycler.ItemTrolleyCard;
import recycler.ProductCard;
import recycler.RecyclerViewAdapter;
import recycler.RecyclerViewAdapterTrolley;
import stripe.PaymentManager;

/**
 * Created by guillaumeagis on 21/04/15.
 * display the list of venues, once loaded.
 * All the venues are sorted by the distance from the user.
 * It is possible to pull to refresh the list if the user is not connected to internet when he arrives here
 */
public class TrolleyActivity extends Activity {

    private RecyclerViewAdapterTrolley _adapter;
    private LinearLayoutManager _layoutManager;
    private RecyclerView _recyclerView;
    private RelativeLayout _rlPay;
    private TextView _title;

    Dialog dialog = null;
    // current list of item
    private ArrayList<ItemTrolleyCard> _objectsList = new ArrayList<>();

    private int _totalPrice = 0;

    public static TrolleyActivity trolleyActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_view);

        trolleyActivity = this;
        this.initializeRecyclerView();
        this.createListObject();
        this.init();
        this.update();
        dialog = createDialog();
    }


    /**
     * Initialize the recycler view and the floating button in same time
     * When the user scrolls the view, the button goes down
     */
private void initializeRecyclerView()
{
    this._recyclerView = (RecyclerView)this.findViewById(R.id.recyclerView);

    this._rlPay = (RelativeLayout)this.findViewById(R.id.rlPay);

    this._title = (TextView)this.findViewById(R.id.tvTitle);
    this._title.setText("Your trolley");

    paiementInfoBox();
}


    private void paiementInfoBox()
    {
        this._rlPay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                if (dialog != null && UserManager.getInstance().getItems().size() > 0)
                    dialog.show();
            }
        });
    }

    public void displayDialog()
    {
        dialog = createDialog();
        dialog.show();
    }

    private Dialog createDialog()
    {

        dialog = new Dialog(this);

        View v = LayoutInflater.from(this).inflate(R.layout.credit_card, null);
        dialog.setContentView(v);

        Log.e("Dialog", "createDialog : " + "0" + UserManager.expiryMonth + "/" + (UserManager.expiryYear - 2000));
        Log.e("Dialog", "createDialog : " + "0" + UserManager._cardNumber);
        dialog.setContentView(R.layout.credit_card);

        LinearLayout llScan =  (LinearLayout) dialog.findViewById(R.id.llScan);

        ImageView scan =  (ImageView) dialog.findViewById(R.id.scanButton);
        scan.setVisibility(View.VISIBLE);

        CreditCardView creditCardView = (CreditCardView)dialog.findViewById(R.id.card1);

        TextView totalPrice = (TextView)dialog.findViewById(R.id.tvPrice);
        totalPrice.setText("Pay " + _totalPrice + " £");
        creditCardView.setIsEditable(true);
        creditCardView.setIsCardNumberEditable(true);
        creditCardView.setIsExpiryDateEditable(true);
        if (UserManager._cardNumber != null)
        creditCardView.setCardNumber(UserManager._cardNumber);
        if (UserManager.expiryMonth != 0) {
            Log.e("", "credit card modify expire date");
            creditCardView.setExpiryDate("0" + UserManager.expiryMonth + "/" + (UserManager.expiryYear - 2000));
        }
        Log.e("", "set credit card expire date :" +creditCardView.getExpiryDate());
        scan.getDrawable().setColorFilter(this.getResources().getColor(R.color.custom_green), PorterDuff.Mode.MULTIPLY);

        v.invalidate();

        llScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.push(TrolleyActivity.this, ActivityManager.eType.SCAN);
                dialog.dismiss();
            }
        });

        RelativeLayout pay = (RelativeLayout) dialog.findViewById(R.id.rlPay);
        // if button is clicked, close the custom dialog
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance().clear();
                PaymentManager.makePayment(_totalPrice, TrolleyActivity.this);
                PaymentManager.sendSmsConfirmation();
                dialog.dismiss();
                SearchActivity.searchActivity.refreshTrolley();
                finish();

            }
        });

        return dialog;
    }

    public void removeItem(final int position, final int price) {
        if (_objectsList.size() > position + 1)
        {
            _objectsList.remove(position);
            _adapter.notifyItemRemoved(position);
            _totalPrice -= price;
            updateButton();
        }
    }

    /**
     * Add all venues in the list
     *  Clear the object list and sort the venue list by the distance from the user, also called when pull to refresh action is done
     */
    private void createListObject()
    {
        this._objectsList.clear();
        CollectionManager.getInstance().cleanList();

        int size = UserManager.getInstance().getItems().size();
        for (int position = 0; position < size ; position++)
        {
            JSONObject jo = UserManager.getInstance().getItems().get(position);
            try {
                String price = jo.getString("price");
                price = price.substring(1);
                _totalPrice += Integer.valueOf(price);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            this._objectsList.add( new ItemTrolleyCard(jo, this, position));
        }
    }


    private void updateButton()
    {
        if (UserManager.getInstance().getItems().size() > 0)
        {
            _rlPay.setBackgroundColor(0);
            _rlPay.setBackgroundResource(R.drawable.button_green_white);
        }
        else
        {
            _rlPay.setBackgroundResource(0);
            _rlPay.setBackgroundColor(getResources().getColor(R.color.cant_click));
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
        this._adapter = new RecyclerViewAdapterTrolley(_objectsList);
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
