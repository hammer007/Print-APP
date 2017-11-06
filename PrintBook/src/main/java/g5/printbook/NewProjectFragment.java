package g5.printbook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import g5.printbook.database.CRUD;
import g5.printbook.database.Config;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewProjectFragment extends Fragment {

    Config config;
    CRUD crud;
    Button printingSave_Button, preprintingSave_Button;
    EditText projectID_editText, partnumber_editText, numberofparts_editText, printingparameters_editText, comment_editText;
    EditText slmid_editText, starttime_editText, endtime_editText, date_editText, operator_editText;


    public NewProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_project, container, false);
        initialize(view);
        printingSave_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(projectID_editText.getText().toString())) projectID_editText.setError( "Id is Required!" );
                else {
                    int project_created = createProject();
                    if(project_created == 1){
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(config.PREPRINTING_pre_printing_id, "2"));
                        params.add(new BasicNameValuePair(config.PREPRINTING_project_id, projectID_editText.getText().toString()));
                        params.add(new BasicNameValuePair(config.PREPRINTING_build_id, projectID_editText.getText().toString()));
                        params.add(new BasicNameValuePair(config.PREPRINTING_no_parts, numberofparts_editText.getText().toString()));
                        params.add(new BasicNameValuePair(config.PREPRINTING_printing_parameter, printingparameters_editText.getText().toString()));
                        params.add(new BasicNameValuePair(config.PREPRINTING_comment, comment_editText.getText().toString()));
                        Log.d(TAG, "params" + params);
                        crud = new CRUD(params, config.TAG_INSERT_PREPRINTING);
                        crud.execute();
                    }
                    else Toast.makeText(getContext(), "SOME PROBLEM WITH PROJECT CREATION, PROJECT TABLE REJECTING", Toast.LENGTH_LONG);
                }

            }
        });
        return view;

    }

    private int createProject() {
        int success = -1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(config.PROJECT_id, projectID_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PROJECT_name, "dummy value"));
        crud = new CRUD(params, config.TAG_CREATE_PROJECT);
        try {
            success = crud.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return success;

    }

    private void initialize(View view) {
        projectID_editText = (EditText)view.findViewById(R.id.projectID_editText);
        partnumber_editText = (EditText)view.findViewById(R.id.partnumber_editText);
        numberofparts_editText =  (EditText)view.findViewById(R.id.numberofparts_editText);
        printingparameters_editText = (EditText)view.findViewById(R.id.printingparameters_editText);
        comment_editText = (EditText)view.findViewById(R.id.comment_editText);
        slmid_editText = (EditText)view.findViewById(R.id.comment_editText);
        starttime_editText = (EditText)view.findViewById(R.id.comment_editText);
        endtime_editText =  (EditText)view.findViewById(R.id.comment_editText);
        date_editText = (EditText)view.findViewById(R.id.comment_editText);
        operator_editText = (EditText)view.findViewById(R.id.comment_editText);
        printingSave_Button = (Button)view.findViewById(R.id.printingSave_Button);
        preprintingSave_Button = (Button)view.findViewById(R.id.preprintingSave_Button);
    }

}
