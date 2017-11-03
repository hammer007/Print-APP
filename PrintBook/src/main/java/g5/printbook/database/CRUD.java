package g5.printbook.database;

import android.os.AsyncTask;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * Created by Geek on 10/21/2017.
 */

public class CRUD extends AsyncTask<String, String, String> {
    Config config = new Config();
    JSONParser jsonParser = new JSONParser();
    String do_what = config.TAG_CREATE_NOTHING;
    List<NameValuePair> params;
    public CRUD(List<NameValuePair> param, String do_what){
        this.params = param;
        this.do_what = do_what;
    }
    @Override
    protected String doInBackground(String... strings) {
        int success = -1;
        if(do_what == config.TAG_CREATE_PROJECT)  success = insertToDB(config.url_create_project);
        else if (do_what == config.TAG_INSERT_TO_PREPRINTING) success = insertToDB(config.url_insert_preprinting);
        if (success == 1) System.out.print("success");
        else System.out.print("fail");
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    protected void onPostExecute(String file_url) {}
    private int insertToDB(String doWhat){
        System.out.print(" params " +params);
        JSONObject json = jsonParser.makeHttpRequest(doWhat, "POST", params);
        int success = -1;
        try {
            System.out.print( "" + json);
            success = json.getInt(config.TAG_SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }

}
