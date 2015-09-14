package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spleat.R;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import managers.UserManager;

/**
 * Created by guillaumeagis on 21/04/15.
 * display the list of venues, once loaded.
 * All the venues are sorted by the distance from the user.
 * It is possible to pull to refresh the list if the user is not connected to internet when he arrives here
 */
public class ScanActivity extends Activity {
    final String TAG = getClass().getName();

    private Button scanButton;
    private TextView resultTextView;

    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        scanButton = (Button) findViewById(R.id.scanButton);

        onScanPress();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CardIOActivity.canReadCardWithCamera()) {
            scanButton.setText("Scan a credit card with card.io");
        } else {
            scanButton.setText("Enter credit card information");
        }
    }

    public void onScanPress() {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = "Card Number: " + scanResult.cardNumber + "\n";

            Log.e("", "CreditCard info :" +resultStr);

            UserManager._cardNumber = scanResult.cardNumber;
            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                UserManager.expiryMonth = scanResult.expiryMonth;
                UserManager.expiryYear = scanResult.expiryYear;
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                UserManager.CVV = scanResult.cvv;
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n";
            }
        } else {
            resultStr = "Scan was canceled.";
        }
        resultTextView.setText(resultStr);
        TrolleyActivity.trolleyActivity.displayDialog();
        finish();
    }
}
