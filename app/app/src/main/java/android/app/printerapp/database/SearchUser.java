package android.app.printerapp.database;

/**
 * Created by Geek on 11/24/2017.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchUser extends AsyncTask<String, String, String[]> {
    String returned [] = new String[3];
    JSONParser jsonParser = new JSONParser();
    String url;
    Config config;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    public SearchUser(List<NameValuePair> params, String url){
        this.params = params;
        this.url = url;
    }
    @Override
    protected String[] doInBackground(String... strings) {
        JSONObject json = jsonParser.makeHttpRequest(
                url, "GET", params);
        Log.d("SEARCH VALUE", "" +json);
        int success = 0;
        try {
            success = json.getInt(config.TAG_SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (success == 1) {
            JSONArray productObj = null;
            try {
                productObj = json
                        .getJSONArray("users");
                returned[0] = "101";
                returned = gather_returned_files(productObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            returned[0] = "-101";
        }
        return returned;
    }

    private String [] gather_returned_files(JSONArray productObj) {
        JSONObject product = null;
        try {
            Log.d("array size", productObj+ "");
            product = productObj.getJSONObject(0);
            returned[0] = product.getString(config.SIGNUP_user_name);
            returned[1] = product.getString(config.SIGNUP_first_name);
            returned[2] = product.getString(config.SIGNUP_last_name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returned;
    }

    @Override
    protected void onPreExecute() { super.onPreExecute(); }


}
