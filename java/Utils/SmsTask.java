package Utils;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;

import activities.SearchActivity;
import managers.CollectionManager;


/**
 * Created by guillaumeagis on 21/04/15.
 * Asynctask to get the venues list from the server
 */
public class SmsTask extends AsyncTask<String, Void, String> {


    // file to load
    private final String SERVERAPI = "http://boiling-crag-1340.herokuapp.com/payments";


    /**
     * Call the URL in background_effect , to load all the venue
     * @param params no given parameter
     * @return file load, under string format
     */
    @Override
    protected String doInBackground(String... params) {

        String url = SERVERAPI;

        Log.e("GetProductsTask", "SmsTask " + url);

        HttpGet HttpGet = new HttpGet(url);
        String result = "";

        //passes the results to a string builder/entity
        DefaultHttpClient httpclient = new DefaultHttpClient();
        //sets a request header so the page receving the request
        //will know what to do with it
        HttpGet.setHeader("Accept", "application/json");
        HttpGet.setHeader("Content-type", "application/json");
        HttpResponse res = null;
        try {
            res = httpclient.execute(HttpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

         return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {}
}