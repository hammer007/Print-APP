package g5.printbook.database;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Geek on 10/21/2017.
 */

public class Insert extends AsyncTask<String, String, Integer> {
    int success = -1;
    Config config = new Config();
    JSONParser jsonParser = new JSONParser();
    String do_what = config.TAG_CREATE_NOTHING;
    List<NameValuePair> params;
    public Insert(List<NameValuePair> param, String do_what){
        this.params = param;
        this.do_what = do_what;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        if(do_what == config.TAG_CREATE_PROJECT)  success = insertToDB(config.url_create_project);
        else if (do_what == config.TAG_INSERT_PREPRINTING) success = insertToDB(config.url_insert_preprinting);
        else if (do_what == config.TAG_INSERT_PRINTING) success = insertToDB(config.url_insert_printing);
        else if (do_what == config.TAG_INSERT_SIGNUP) success = insertToDB(config.url_insert_sign_up);
        return success;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    protected int onPostExecute(int success) {return success;}
    private int insertToDB(String doWhat){
        JSONObject json = jsonParser.makeHttpRequest(doWhat, "POST", params);
        try {
            success = json.getInt(config.TAG_SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }
}
