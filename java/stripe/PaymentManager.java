package stripe;

import android.app.Activity;
import android.widget.Toast;



import Utils.PaymentTask;
import Utils.SmsTask;

/**
 * Created by agisg on 13/09/15.
 */
public class PaymentManager {

    public static void sendSmsConfirmation() {
        new SmsTask().execute();

    }

    public static void makePayment(final int amount, final Activity activity)
    {
        new PaymentTask(amount).execute();
        Toast.makeText( activity, "Card Charged , Paid : "  + amount + " £",  Toast.LENGTH_LONG).show();
    }
}
