package g5.printbook.database;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geek on 10/21/2017.
 */

public class CRUD extends AsyncTask<String, String, String> {
    Config config = new Config();
    JSONParser jsonParser = new JSONParser();
    @Override
    protected String doInBackground(String... strings) {
        // Project Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", "1"));
        params.add(new BasicNameValuePair("name", "test_1"));
        JSONObject json = jsonParser.makeHttpRequest(config.url_create_project,
                "POST", params);
        try {
            Log.i("tagconvertstr", "["+json+"]");
            int success = json.getInt(config.TAG_SUCCESS);

            if (success == 1) {
                // successfully created project
                System.out.print("success");
            } else {
                // failed to create project
                System.out.print("fail");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after getting all products

    }

}
