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
public class GetProductsTask extends AsyncTask<String, Void, String> {


    // file to load
    private final String SERVERAPI = "http://boiling-crag-1340.herokuapp.com/products.json";

    private final String PRODUCTAPI = "http://boiling-crag-1340.herokuapp.com/products/";

    private final String FORMAT = ".json";

    // current activity
    private SearchActivity _activity;

    // check if there is an action after loading the file
    private boolean _ping = false;

    public GetProductsTask(SearchActivity act, boolean ping)
    {
        this._activity = act;
        this._ping = ping;
    }


    /**
     * Call the URL in background_effect , to load all the venue
     * @param params no given parameter
     * @return file load, under string format
     */
    @Override
    protected String doInBackground(String... params) {

        String url = _ping ? SERVERAPI : PRODUCTAPI + params[0] + FORMAT;

        Log.e("GetProductsTask", "url " + url);

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
            Log.e("GetProductsTask", "code  : " + res.getStatusLine().getStatusCode());
           if (res.getStatusLine().getStatusCode() == 200)
           {
               result = EntityUtils.toString(res.getEntity());
               if (_ping == false)
               CollectionManager.getInstance().addCollection(result);
           }
        } catch (IOException e) {
            e.printStackTrace();
          //  Log.e("GetProductsTask", "errro  : " + res.getStatusLine().getStatusCode());
        }

         return result;
    }


    /**
     * Post action, called after the doInBackground
     * If the parameter is empty, the view wont be refreshed
     */
    @Override
    protected void onPostExecute(String result) {

        if (result == null)
            CollectionManager._hasInternet = false;
        else
            CollectionManager._hasInternet = true;

        if (_ping == false)
        {
            try {
                _activity.updateInfo(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}