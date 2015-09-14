package Utils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import activities.SearchActivity;
import activities.TrolleyActivity;
import managers.CollectionManager;


/**
 * Created by guillaumeagis on 21/04/15.
 * Asynctask to get the venues list from the server
 */
public class PaymentTask extends AsyncTask<String, Void, String> {



    private int _amount = 0;

    public PaymentTask(final int amount)
    {
        this._amount = amount;
    }


    /**
     * Call the URL in background_effect , to load all the venue
     * @param params no given parameter
     * @return file load, under string format
     */
    @Override
    protected String doInBackground(String... params) {

        Stripe.apiKey = "sk_test_XIpWbBX1xdHzi6GYqlffKPZW";
        Log.e("PaymentManager", "Make makePayment");
        final Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("number", "4242424242424242");
        cardParams.put("exp_month", 03);
        cardParams.put("exp_year", 2021);
        cardParams.put("cvc", "111");
        cardParams.put("name", "Guillaume AGIS");
        cardParams.put("address_line1", "piccadilly circus");
        cardParams.put("address_line2", "1th Floor");
        cardParams.put("address_city", "London");
        cardParams.put("address_zip", "94105");
        cardParams.put("address_state", "UK");
        cardParams.put("address_country", "UK");

        final Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", _amount * 100);
        chargeParams.put("currency", "GBP");
        chargeParams.put("card", cardParams);

        final Charge charge;
        try {
            Log.e("Payment", "ready to pay");
            charge = Charge.create(chargeParams);
            Log.e("Payment", charge.toString());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            Log.e("Payment", "AuthenticationException failed" + e);
        } catch (InvalidRequestException e) {
            e.printStackTrace();
            Log.e("Payment", "InvalidRequestException failed" + e);
        } catch (APIConnectionException e) {
            e.printStackTrace();
            Log.e("Payment", "API connection failed" + e);
        } catch (CardException e) {
            e.printStackTrace();
            Log.e("Payment", "card failed" + e);
        } catch (APIException e) {
            e.printStackTrace();
            Log.e("Payment", "api exp : " + e);
        }
         return null;
    }


    /**
     * Post action, called after the doInBackground
     * If the parameter is empty, the view wont be refreshed
     */
    @Override
    protected void onPostExecute(String result) {

    }


    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}