package g5.printbook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
public class NewPrintJobFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Config config;
    CRUD crud;
    Button printingSave_Button, preprintingSave_Button, preprinting_expand_button, printing_expand_button, posprinting_expand_button;
    EditText projectID_editText, partnumber_editText, numberofparts_editText, printingparameters_editText, comment_editText;
    EditText slmid_editText, starttime_editText, endtime_editText, date_editText, operator_editText;
    EditText typeofmachine_editText, powerweight_editText, powerweightatEnd_editText, powderwaste_editText, material_editText;
    EditText buildplatform_editText, printTune_editText, powderCondition_editText, reused_times_editText;
    EditText numberofLayers_editText, dpcFactor_editText, minExposureTime_editText, printingComments_editText;
    EditText postID_editText, urlphoto_editText;
    TextView postID_Text, urlphoto_Text, supportremoval_Text, WEDM_Text;
    TextView projectID_Text, partnumber_Text, numberofparts_Text, printingparameters_Text, comment_Text, if_textView;
    TextView slmid_Text, starttime_Text, endtime_Text, date_Text, operator_Text, typeofmachine_Text, powerweight_Text;
    TextView powerweightatEnd_Text, powderwaste_Text,material_Text, buildplatform_Text, printTune_Text, powderCondition_Text, numberofLayers_Text;
    TextView dpcFactor_Text, minExposureTime_Text, printingComments_Text;
    Spinner spinner, supportremovalSpinner, WEDMSpinner;
    boolean expanded_preprinting = false,expanded_printing = false,expanded_posprinting = false;

    public NewPrintJobFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_printjob, container, false);
        initialize(view);
        spinner.setOnItemSelectedListener(this);
        supportremovalSpinner.setOnItemSelectedListener(this);
        WEDMSpinner.setOnItemSelectedListener(this);

        printingSave_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(projectID_editText.getText().toString())) projectID_editText.setError( "Id is Required!" );
                else {
                    int success = createProject();
                    int success_pre_printing = -1;
                    int success_printing = -1;
                    if(success == 1) {
                        success_pre_printing = insert_to_pre_printing();
                        if(success_pre_printing == 1){
                            success_printing = insert_to_printing();
                            Toast.makeText(getContext(), "Am here" , Toast.LENGTH_LONG);
                            if(success_printing == 1) Toast.makeText(getContext(), "SUCCESS; THE PROJECT HAS BEEN INSERTED", Toast.LENGTH_LONG);
                            else Toast.makeText(getContext(), "SOME PROBLEM WHEN INSERTING TO PRINTING TABLE", Toast.LENGTH_LONG);
                        }else Toast.makeText(getContext(), "SOME PROBLEM INSERTING TO PRE PRINTING, PRE PRINTING TABLE REJECTING", Toast.LENGTH_LONG);
                    }
                    else Toast.makeText(getContext(), "SOME PROBLEM WITH PROJECT CREATION, PROJECT TABLE REJECTING", Toast.LENGTH_LONG);
                    success = -1;
                    success_pre_printing = -1;
                    success_printing = -1;
                }

            }
        });
        preprinting_expand_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expanded_preprinting) {
                    hide_preprining();
                    expanded_preprinting = false;
                }
                else{
                    show_preprining();
                    expanded_preprinting = true;
                }

            }
        });
        printing_expand_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expanded_printing) {
                    hide_prining();
                    expanded_printing = false;
                }
                else{
                    show_prining();
                    expanded_printing = true;
                }

            }
        });

        posprinting_expand_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expanded_posprinting) {
                    hide_posprinting();
                    expanded_posprinting = false;
                }
                else{
                    show_posprinting();
                    expanded_posprinting = true;
                }

            }
        });

        return view;

    }

    private int insert_to_printing() {
        int success = -1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //AUTO INCREMENT params.add(new BasicNameValuePair(config.PRINTING_printing_id, 2 + ""));
        params.add(new BasicNameValuePair(config.PRINTING_slm_id, slmid_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_start_time, starttime_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_end_time, endtime_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_date, date_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_operator, operator_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_machine_type, typeofmachine_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_powder_weight_start, powerweight_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_powder_weight_end, powerweightatEnd_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_powder_used, ((Integer.parseInt(powerweight_editText.getText().toString()) - Integer.parseInt(powerweightatEnd_editText.getText().toString())) + "")));
        params.add(new BasicNameValuePair(config.PRINTING_powder_waste_weight, powderwaste_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_material_id, material_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_build_platform_weight, buildplatform_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_print_time, printTune_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_powder_condition, powderCondition_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_reused_times, reused_times_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_number_of_layers, numberofLayers_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_dpc_factor, dpcFactor_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_exposure_time, minExposureTime_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_comments, printingComments_editText.getText().toString()));
        crud = new CRUD(params, config.TAG_INSERT_PRINTING);
        try {
            success = crud.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Am here too" + params);
        return success;
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

    private int insert_to_pre_printing(){
        int success = -1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(config.PREPRINTING_pre_printing_id, "4"));
        params.add(new BasicNameValuePair(config.PREPRINTING_project_id, projectID_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PREPRINTING_build_id, projectID_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PREPRINTING_no_parts, numberofparts_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PREPRINTING_printing_parameter, printingparameters_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PREPRINTING_comment, comment_editText.getText().toString()));
        Log.d(TAG, "params" + params);
        crud = new CRUD(params, config.TAG_INSERT_PREPRINTING);
        try {
            success = crud.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return success;
    }

    //TODO: Insert Post-Printing fields into database

    private void initialize(View view) {
        spinner = (Spinner) view.findViewById(R.id.powderCondition_editText);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.powder_condition_string, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        supportremovalSpinner = (Spinner) view.findViewById(R.id.supportRemoval_editText);
        ArrayAdapter<CharSequence> supportremovalAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.support_removal_string, android.R.layout.simple_spinner_item);
        supportremovalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supportremovalSpinner.setAdapter(supportremovalAdapter);

        WEDMSpinner = (Spinner) view.findViewById(R.id.WEDM_editText);
        ArrayAdapter<CharSequence> WEDMSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.WEDM_string, android.R.layout.simple_spinner_item);
        WEDMSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        WEDMSpinner.setAdapter(WEDMSpinnerAdapter);

        preprinting_expand_button = (Button)view.findViewById(R.id.preprinting_expand_button);
        projectID_editText = (EditText)view.findViewById(R.id.projectID_editText);
        partnumber_editText = (EditText)view.findViewById(R.id.partnumber_editText);
        numberofparts_editText =  (EditText)view.findViewById(R.id.numberofparts_editText);
        printingparameters_editText = (EditText)view.findViewById(R.id.printingparameters_editText);
        comment_editText = (EditText)view.findViewById(R.id.comment_editText);

        projectID_Text = (TextView) view.findViewById(R.id.projectID_textView);
        partnumber_Text = (TextView)view.findViewById(R.id.partnumber_textView);
        numberofparts_Text =  (TextView)view.findViewById(R.id.numberparts_textView);
        printingparameters_Text = (TextView)view.findViewById(R.id.prinringparameters_textView);
        comment_Text = (TextView)view.findViewById(R.id.coment_textView);

        printing_expand_button = (Button)view.findViewById(R.id.Printing_expand_button);
        slmid_editText = (EditText)view.findViewById(R.id.slmid_editText);
        starttime_editText = (EditText)view.findViewById(R.id.starttime_editText);
        endtime_editText =  (EditText)view.findViewById(R.id.endtime_editText);
        date_editText = (EditText)view.findViewById(R.id.date_editText);
        operator_editText = (EditText)view.findViewById(R.id.operator_editText);
        typeofmachine_editText = (EditText)view.findViewById(R.id.typeofmachine_editText);
        powerweight_editText = (EditText)view.findViewById(R.id.powerweight_editText);
        powerweightatEnd_editText = (EditText)view.findViewById(R.id.powerweightatEnd_editText);
        powderwaste_editText = (EditText)view.findViewById(R.id.powderwaste_editText);
        material_editText = (EditText)view.findViewById(R.id.material_editText);
        buildplatform_editText = (EditText)view.findViewById(R.id.buildplatform_editText);
        printTune_editText = (EditText)view.findViewById(R.id.printTune_editText);
        if_textView = (TextView)view.findViewById(R.id. if_textView);
        //powderCondition_editText = (EditText)view.findViewById(R.id.powderCondition_editText);
        reused_times_editText = (EditText)view.findViewById(R.id.if_editText);
        numberofLayers_editText = (EditText)view.findViewById(R.id.numberofLayers_editText);
        dpcFactor_editText = (EditText)view.findViewById(R.id.dpcFactor_editText);
        minExposureTime_editText = (EditText)view.findViewById(R.id.minExposureTime_editText);
        printingComments_editText = (EditText)view.findViewById(R.id.printingComments_editText);

        slmid_Text = (TextView)view.findViewById(R.id.slmid_textView);
        starttime_Text = (TextView)view.findViewById(R.id.starttime_textView);
        endtime_Text =  (TextView)view.findViewById(R.id.endtime_textView);
        date_Text = (TextView)view.findViewById(R.id.date_textView);
        operator_Text = (TextView)view.findViewById(R.id.operator_textView);
        typeofmachine_Text = (TextView)view.findViewById(R.id.typeofmachine_textView);
        powerweight_Text = (TextView)view.findViewById(R.id.powerweight_textView);
        powerweightatEnd_Text = (TextView)view.findViewById(R.id.powerweightatEnd_textView);
        powderwaste_Text = (TextView)view.findViewById(R.id.powderwaste_textView);
        material_Text = (TextView)view.findViewById(R.id.material_textView);
        buildplatform_Text = (TextView)view.findViewById(R.id.buildplatform_textView);
        printTune_Text = (TextView)view.findViewById(R.id.printTune_textView);
        powderCondition_Text = (TextView)view.findViewById(R.id.powderCondition_textView);
        numberofLayers_Text = (TextView)view.findViewById(R.id.numberofLayers_textView);
        dpcFactor_Text = (TextView)view.findViewById(R.id.dpcFactor_textView);
        minExposureTime_Text = (TextView)view.findViewById(R.id.minExposureTime_textView);
        printingComments_Text = (TextView)view.findViewById(R.id.printingComments_textView);

        printingSave_Button = (Button)view.findViewById(R.id.printingSave_Button);
        preprintingSave_Button = (Button)view.findViewById(R.id.preprintingSave_Button);
        //------ Post-Printing Fields ------//
        posprinting_expand_button = (Button)view.findViewById(R.id.posprinting_expand_button);
        postID_Text = (TextView)view.findViewById(R.id.postID_textView);
        postID_editText = (EditText)view.findViewById(R.id.postID_editText);
        urlphoto_Text = (TextView)view.findViewById(R.id.urlphoto_textView);
        urlphoto_editText = (EditText)view.findViewById(R.id.urlphoto_editText);
        supportremoval_Text = (TextView)view.findViewById(R.id.supportRemoval_textView);
        WEDM_Text = (TextView)view.findViewById(R.id.WEDM_textView);

        hide_preprining();
        hide_prining();
        hide_posprinting();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                reused_times_editText.setVisibility(View.GONE);
                if_textView.setVisibility(View.GONE);
                break;
            case 1:
                reused_times_editText.setVisibility(View.VISIBLE);
                if_textView.setVisibility(View.VISIBLE);
                break;
            case 2:
                reused_times_editText.setVisibility(View.GONE);
                if_textView.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void hide_preprining(){
        projectID_editText.setVisibility(View.GONE);
        partnumber_editText.setVisibility(View.GONE);
        numberofparts_editText.setVisibility(View.GONE);
        printingparameters_editText.setVisibility(View.GONE);
        comment_editText.setVisibility(View.GONE);
        preprintingSave_Button.setVisibility(View.GONE);
        projectID_Text.setVisibility(View.GONE);
        partnumber_Text.setVisibility(View.GONE);
        numberofparts_Text.setVisibility(View.GONE);
        printingparameters_Text.setVisibility(View.GONE);
        comment_Text.setVisibility(View.GONE);
        preprinting_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
    }
    private void show_preprining(){
        projectID_editText.setVisibility(View.VISIBLE);
        partnumber_editText.setVisibility(View.VISIBLE);
        numberofparts_editText.setVisibility(View.VISIBLE);
        printingparameters_editText.setVisibility(View.VISIBLE);
        comment_editText.setVisibility(View.VISIBLE);
        preprintingSave_Button.setVisibility(View.VISIBLE);
        preprinting_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shrink, 0);
        projectID_Text.setVisibility(View.VISIBLE);
        partnumber_Text.setVisibility(View.VISIBLE);
        numberofparts_Text.setVisibility(View.VISIBLE);
        printingparameters_Text.setVisibility(View.VISIBLE);
        comment_Text.setVisibility(View.VISIBLE);
    }

    private void hide_prining() {
        slmid_editText.setVisibility(View.GONE);
        starttime_editText.setVisibility(View.GONE);
        endtime_editText.setVisibility(View.GONE);
        date_editText.setVisibility(View.GONE);
        operator_editText.setVisibility(View.GONE);
        typeofmachine_editText.setVisibility(View.GONE);
        powerweight_editText.setVisibility(View.GONE);
        powerweightatEnd_editText.setVisibility(View.GONE);
        powderwaste_editText.setVisibility(View.GONE);
        material_editText.setVisibility(View.GONE);
        buildplatform_editText.setVisibility(View.GONE);
        printTune_editText.setVisibility(View.GONE);
        if_textView.setVisibility(View.GONE);
        reused_times_editText.setVisibility(View.GONE);
        numberofLayers_editText.setVisibility(View.GONE);
        dpcFactor_editText.setVisibility(View.GONE);
        minExposureTime_editText.setVisibility(View.GONE);
        printingComments_editText.setVisibility(View.GONE);
        printingSave_Button.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);

        slmid_Text.setVisibility(View.GONE);
        starttime_Text.setVisibility(View.GONE);
        endtime_Text.setVisibility(View.GONE);
        date_Text.setVisibility(View.GONE);
        operator_Text.setVisibility(View.GONE);
        typeofmachine_Text.setVisibility(View.GONE);
        powerweight_Text.setVisibility(View.GONE);
        powerweightatEnd_Text.setVisibility(View.GONE);
        powderwaste_Text.setVisibility(View.GONE);
        material_Text.setVisibility(View.GONE);
        buildplatform_Text.setVisibility(View.GONE);
        printTune_Text.setVisibility(View.GONE);
        powderCondition_Text.setVisibility(View.GONE);
        numberofLayers_Text.setVisibility(View.GONE);
        dpcFactor_Text.setVisibility(View.GONE);
        minExposureTime_Text.setVisibility(View.GONE);
        printingComments_Text.setVisibility(View.GONE);
        printing_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand, 0);

    }

    private void show_prining() {
        slmid_editText.setVisibility(View.VISIBLE);
        starttime_editText.setVisibility(View.VISIBLE);
        endtime_editText.setVisibility(View.VISIBLE);
        date_editText.setVisibility(View.VISIBLE);
        operator_editText.setVisibility(View.VISIBLE);
        typeofmachine_editText.setVisibility(View.VISIBLE);
        powerweight_editText.setVisibility(View.VISIBLE);
        powerweightatEnd_editText.setVisibility(View.VISIBLE);
        powderwaste_editText.setVisibility(View.VISIBLE);
        material_editText.setVisibility(View.VISIBLE);
        buildplatform_editText.setVisibility(View.VISIBLE);
        printTune_editText.setVisibility(View.VISIBLE);
        numberofLayers_editText.setVisibility(View.VISIBLE);
        dpcFactor_editText.setVisibility(View.VISIBLE);
        minExposureTime_editText.setVisibility(View.VISIBLE);
        printingComments_editText.setVisibility(View.VISIBLE);
        printingSave_Button.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        slmid_Text.setVisibility(View.VISIBLE);
        starttime_Text.setVisibility(View.VISIBLE);
        endtime_Text.setVisibility(View.VISIBLE);
        date_Text.setVisibility(View.VISIBLE);
        operator_Text.setVisibility(View.VISIBLE);
        typeofmachine_Text.setVisibility(View.VISIBLE);
        powerweight_Text.setVisibility(View.VISIBLE);
        powerweightatEnd_Text.setVisibility(View.VISIBLE);
        powderwaste_Text.setVisibility(View.VISIBLE);
        material_Text.setVisibility(View.VISIBLE);
        buildplatform_Text.setVisibility(View.VISIBLE);
        printTune_Text.setVisibility(View.VISIBLE);
        powderCondition_Text.setVisibility(View.VISIBLE);
        numberofLayers_Text.setVisibility(View.VISIBLE);
        dpcFactor_Text.setVisibility(View.VISIBLE);
        minExposureTime_Text.setVisibility(View.VISIBLE);
        printingComments_Text.setVisibility(View.VISIBLE);
        printing_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shrink, 0);
    }

    private void hide_posprinting(){

        postID_Text.setVisibility(View.GONE);
        postID_editText.setVisibility(View.GONE);
        //TODO: add Project ID here ----
        urlphoto_Text.setVisibility(View.GONE);
        urlphoto_editText.setVisibility(View.GONE);
        supportremoval_Text.setVisibility(View.GONE);
        supportremovalSpinner.setVisibility(View.GONE);
        WEDM_Text.setVisibility(View.GONE);
        WEDMSpinner.setVisibility(View.GONE);



        posprinting_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand, 0);

    }

    private void show_posprinting(){

        postID_Text.setVisibility(View.VISIBLE);
        postID_editText.setVisibility(View.VISIBLE);
        //TODO: add Project ID here ----
        urlphoto_Text.setVisibility(View.VISIBLE);
        urlphoto_editText.setVisibility(View.VISIBLE);
        supportremoval_Text.setVisibility(View.VISIBLE);
        supportremovalSpinner.setVisibility(View.VISIBLE);
        WEDM_Text.setVisibility(View.VISIBLE);
        WEDMSpinner.setVisibility(View.VISIBLE);



        posprinting_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shrink, 0);

    }
}
