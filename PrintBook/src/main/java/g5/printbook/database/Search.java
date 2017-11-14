package g5.printbook.database;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geek on 11/13/2017.
 */
public class Search extends AsyncTask<String, String, String[]> {
    JSONParser jsonParser = new JSONParser();
    String url;
    Config config;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    public Search (List<NameValuePair> params, String url){
        this.params = params;
        this.url = url;
    }
    @Override
    protected String[] doInBackground(String... strings) {
        String printing_id [] = new String[1];
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
                        .getJSONArray("printJob");
                JSONObject product = productObj.getJSONObject(0);
                printing_id[0] = product.getString("printing_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            printing_id[0] = "-101";
        }
        return printing_id;
    }

    @Override
    protected void onPreExecute() { super.onPreExecute(); }


}
