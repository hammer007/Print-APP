package g5.printbook;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import g5.printbook.database.Insert;
import g5.printbook.database.Config;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPrintJobFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Calendar myCalendar = Calendar.getInstance();
    Config config;
    Insert insert;
    String slm_id;
    Button printingSave_Button, preprintingSave_Button, submit_Button, preprinting_expand_button, printing_expand_button, posprinting_expand_button, postprintingSave_Button;
    EditText projectID_editText, partnumber_editText, buildId_editText, numberofparts_editText, printingparameters_editText, comment_editText;
    EditText slmid_editText, starttime_editText, endtime_editText, date_editText, operator_editText, agingComment_editText;
    EditText typeofmachine_editText, powerweight_editText, powerweightatEnd_editText, powderwaste_editText, material_editText;
    EditText buildplatform_editText, printTune_editText, powderused_editText, reused_times_editText, agingNumberofCycles_editText;
    EditText numberofLayers_editText, dpcFactor_editText, minExposureTime_editText, printingComments_editText, hardeningComment_editText;
    EditText postID_editText, urlphoto_editText, WEDMcomments_editText, blastingType_editText, blastingTime_editText, hardeningTime_editText;
    EditText blastingComment_editText, stressTemp_editText, stressTime_editText, stressComment_editText, hardeningTemp_editText;
    EditText temperingTemp_editText, temperingTime_editText, temperingNumberofCycles_editText, temperingComment_editText, solutionTreatmentTemp_editText;
    EditText solutionTreatmentTime_editText, solutionTreatmentComment_editText, agingTemp_editText, agingTime_editText;
    TextView solutionTreatmentTime_Text, solutionTreatmentComment_Text, agingTreatment_Text, agingTemp_Text, agingTime_Text, agingComment_Text;
    TextView temperingTemp_Text, temperingTime_Text, temperingNumberofCycles_Text, temperingComment_Text, solutionTreatment_Text;
    TextView stressComment_Text, hardening_Text, hardeningTemp_Text, hardeningTime_Text, hardeningComment_Text, tempering_Text, agingNumberofCycles_Text;
    TextView blastingComment_Text, heatTreatment_Text, stressRelieving_Text, stressTemp_Text, stressTime_Text, stressShieldingGas_Text;
    TextView postID_Text, urlphoto_Text, supportremoval_Text, WEDM_Text, WEDMcomments_Text, blasting_Text, blastingType_Text, blastingTime_Text;
    TextView projectID_Text, partnumber_Text, numberofparts_Text, printingparameters_Text, comment_Text, if_textView, solutionTreatmentTemp_Text;
    TextView slmid_Text, starttime_Text, endtime_Text, date_Text, operator_Text, typeofmachine_Text, powerweight_Text;
    TextView powerweightatEnd_Text, powderwaste_Text,material_Text, buildplatform_Text, printTune_Text, powderCondition_Text, numberofLayers_Text;
    TextView dpcFactor_Text, minExposureTime_Text, printingComments_Text, buildId_textView, powderused_textView;
    Spinner spinner, supportremovalSpinner, WEDMSpinner, blastingSpinner, shieldingSpinner;
    boolean expanded_preprinting = false, expanded_printing = false, expanded_posprinting = false;

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
        slm_id = getArguments().getString("slm_id");
        initialize(view);
        slmid_editText.setText(slm_id);
        slmid_editText.setEnabled(false);
        spinner.setOnItemSelectedListener(this);
        supportremovalSpinner.setOnItemSelectedListener(this);
        WEDMSpinner.setOnItemSelectedListener(this);
        blastingSpinner.setOnItemSelectedListener(this);
        shieldingSpinner.setOnItemSelectedListener(this);
        submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int success = createProject();
                int success_pre_printing = -1, success_printing = -1, success_postprinting = -1;
                success_pre_printing = insert_to_pre_printing();
                success_printing = insert_to_printing();
                success_postprinting = insert_to_postprinting();
                if(success == 1 & success_postprinting == 1 & success_pre_printing ==1 & success_printing ==1){
                    Toast.makeText(getContext(), "SUCCESSFUL", Toast.LENGTH_LONG).show();
                }
                else if(success != 1 & success_postprinting != 1 & success_pre_printing !=1 & success_printing !=1){
                    Toast.makeText(getContext(), "Nothing was Successful", Toast.LENGTH_LONG).show();
                }
                else if(success != 1) Toast.makeText(getContext(), "Insertion to Project was Successful", Toast.LENGTH_LONG).show();
                else if(success_postprinting != 1) Toast.makeText(getContext(), "Insertion to Post printing was Successful", Toast.LENGTH_LONG).show();
                else if(success_pre_printing != 1) Toast.makeText(getContext(), "Insertion to Pre printing was Successful", Toast.LENGTH_LONG).show();
                else if(success_printing != 1) Toast.makeText(getContext(), "Insertion to Printing was Successful", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
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
        date_editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;
                        date_editText.setText("" + selectedyear + "/" + selectedmonth + "/" + selectedday);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        starttime_editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        starttime_editText.setText( selectedHour + ":" + selectedMinute);
                        Log.d(TAG, "START TIME 2" + starttime_editText.getText().toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        endtime_editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endtime_editText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        powderwaste_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(TextUtils.isEmpty(powerweight_editText.getText().toString()) || TextUtils.isEmpty(powerweightatEnd_editText.getText().toString())))
                {
                    powderused_editText.setText( "" + (Integer.parseInt(powerweight_editText.getText().toString()) - Integer.parseInt(powerweightatEnd_editText.getText().toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(TextUtils.isEmpty(powerweight_editText.getText().toString()) || TextUtils.isEmpty(powerweightatEnd_editText.getText().toString())))
                {
                    powderused_editText.setText( "" + (Integer.parseInt(powerweight_editText.getText().toString()) - Integer.parseInt(powerweightatEnd_editText.getText().toString())));
                }
            }
        });

        return view;

    }

    private int insert_to_postprinting() {
        String blastingTime = blastingTime_editText.getText().toString();
        if(TextUtils.isEmpty(blastingTime)) blastingTime = "null";
        int success = -1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //AUTO INCREMENT params.add(new BasicNameValuePair(config.PRINTING_printing_id, 2 + ""));
        params.add(new BasicNameValuePair(config.POSTPRINTING_slm_id, slmid_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.POSTPRINTING_support_removal, supportremovalSpinner.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(config.POSTPRINTING_wedm, WEDMSpinner.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(config.POSTPRINTING_wedm_comment, WEDMcomments_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.POSTPRINTING_blasting, blastingSpinner.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(config.POSTPRINTING_blasting_time, blastingTime));
        params.add(new BasicNameValuePair(config.POSTPRINTING_blasting_type, blastingType_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.POSTPRINTING_blasting_comment, blastingComment_editText.getText().toString()));
        insert = new Insert(params, config.TAG_INSERT_POSTPRINTING);
        try {
            success = insert.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return success;
    }
    private int insert_to_printing() {
        String starttime = starttime_editText.getText().toString();
        String endtime = endtime_editText.getText().toString();
        String date = date_editText.getText().toString();
        String powerweight = powerweight_editText.getText().toString();
        String powderatend = powerweightatEnd_editText.getText().toString();
        String powderused=null;
        String powderwaste = powderwaste_editText.getText().toString();
        String buildplatform = buildplatform_editText.getText().toString();
        String printTime = printTune_editText.getText().toString();
        String reused_times = reused_times_editText.getText().toString();
        String numberofLayers = numberofLayers_editText.getText().toString();
        String dpcFactor = dpcFactor_editText.getText().toString();
        String minExposureTime = minExposureTime_editText.getText().toString();
        if(TextUtils.isEmpty(starttime)) starttime = "null";
        if(TextUtils.isEmpty(endtime)) endtime = "null";
        if(TextUtils.isEmpty(date)) date = "null";
        if(!(TextUtils.isEmpty(powerweight) || TextUtils.isEmpty(powderatend)))
        {
            powderused = "" + (Integer.parseInt(powerweight_editText.getText().toString()) - Integer.parseInt(powerweightatEnd_editText.getText().toString()));
        }
        if(TextUtils.isEmpty(powerweight))
        {
            powerweight = "null";
            powderused = "null";
        }
        if(TextUtils.isEmpty(powderatend)){
            powderatend = "null";
            powderused = "null";
        }
        if(TextUtils.isEmpty(powderwaste)) powderwaste = "null";
        if(TextUtils.isEmpty(buildplatform)) buildplatform = "null";
        if(TextUtils.isEmpty(printTime)) printTime = "null";
        if(TextUtils.isEmpty(reused_times)) reused_times = "null";
        if(TextUtils.isEmpty(numberofLayers)) numberofLayers = "null";
        if(TextUtils.isEmpty(dpcFactor)) dpcFactor = "null";
        if(TextUtils.isEmpty(minExposureTime)) minExposureTime = "null";
        int success = -1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //AUTO INCREMENT params.add(new BasicNameValuePair(config.PRINTING_printing_id, 2 + ""));
        params.add(new BasicNameValuePair(config.PRINTING_slm_id, slmid_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_start_time, starttime));
        params.add(new BasicNameValuePair(config.PRINTING_end_time, endtime));
        params.add(new BasicNameValuePair(config.PRINTING_date, date));
        params.add(new BasicNameValuePair(config.PRINTING_operator, operator_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_machine_type, typeofmachine_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_powder_weight_start, powerweight));
        params.add(new BasicNameValuePair(config.PRINTING_powder_weight_end, powderatend));
        params.add(new BasicNameValuePair(config.PRINTING_powder_used, powderused));
        params.add(new BasicNameValuePair(config.PRINTING_powder_waste_weight, powderwaste));
        params.add(new BasicNameValuePair(config.PRINTING_material_id, material_editText.getText().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_build_platform_weight, buildplatform));
        params.add(new BasicNameValuePair(config.PRINTING_print_time, printTime));
        params.add(new BasicNameValuePair(config.PRINTING_powder_condition, spinner.getSelectedItem().toString()));
        params.add(new BasicNameValuePair(config.PRINTING_reused_times, reused_times));
        params.add(new BasicNameValuePair(config.PRINTING_number_of_layers, numberofLayers));
        params.add(new BasicNameValuePair(config.PRINTING_dpc_factor, dpcFactor));
        params.add(new BasicNameValuePair(config.PRINTING_exposure_time, minExposureTime));
        params.add(new BasicNameValuePair(config.PRINTING_comments, printingComments_editText.getText().toString()));
        insert = new Insert(params, config.TAG_INSERT_PRINTING);
        try {
            success = insert.execute().get();
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
        String projectId = projectID_editText.getText().toString();
        String slm_id_project = slmid_editText.getText().toString();
        if(projectId.isEmpty()) projectId = "null";
        params.add(new BasicNameValuePair(config.PROJECT_number, projectId));
        params.add(new BasicNameValuePair(config.PROJECT_slm_id, slm_id_project));
        params.add(new BasicNameValuePair(config.PROJECT_name, "dummy value"));
        insert = new Insert(params, config.TAG_CREATE_PROJECT);
        try {
            success = insert.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return success;

    }
    private int insert_to_pre_printing(){
        String slm_id = slmid_editText.getText().toString();
        String buildId = buildId_editText.getText().toString();
        String numberofparts = numberofparts_editText.getText().toString();
        String printingparameters = printingparameters_editText.getText().toString();
        String comment = comment_editText.getText().toString();
        if(TextUtils.isEmpty(buildId)) buildId = "null";
        if(TextUtils.isEmpty(numberofparts)) numberofparts = "null";
        if(TextUtils.isEmpty(printingparameters)) printingparameters = "null";
        if(TextUtils.isEmpty(comment)) comment = "null";
        int success = -1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        int id = (int)(100*Math.random());
        params.add(new BasicNameValuePair(config.PROJECT_slm_id, slm_id));
        params.add(new BasicNameValuePair(config.PREPRINTING_build_id, buildId));
        params.add(new BasicNameValuePair(config.PREPRINTING_no_parts, numberofparts));
        params.add(new BasicNameValuePair(config.PREPRINTING_printing_parameter, printingparameters));
        params.add(new BasicNameValuePair(config.PREPRINTING_comment, comment));
        Log.d(TAG, "params" + params);
        insert = new Insert(params, config.TAG_INSERT_PREPRINTING);
        try {
            success = insert.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return success;
    }
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

        blastingSpinner = (Spinner) view.findViewById(R.id.blasting_editText);
        ArrayAdapter<CharSequence> blastingSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.blasting_string, android.R.layout.simple_spinner_item);
        blastingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blastingSpinner.setAdapter(blastingSpinnerAdapter);

        shieldingSpinner = (Spinner) view.findViewById(R.id.shielding_editText);
        ArrayAdapter<CharSequence> shieldingSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.blasting_string, android.R.layout.simple_spinner_item);
        shieldingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shieldingSpinner.setAdapter(shieldingSpinnerAdapter);

        preprinting_expand_button = (Button)view.findViewById(R.id.preprinting_expand_button);
        projectID_editText = (EditText)view.findViewById(R.id.projectID_editText);
        partnumber_editText = (EditText)view.findViewById(R.id.partnumber_editText);
        buildId_editText = (EditText)view.findViewById(R.id.buildId_editText);
        buildId_textView = (TextView) view.findViewById(R.id.buildId_textView);
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
        operator_editText = (EditText)view.findViewById(R.id.search_result_operator_editText);
        typeofmachine_editText = (EditText)view.findViewById(R.id.typeofmachine_editText);
        powerweight_editText = (EditText)view.findViewById(R.id.powderweight_editText);
        powerweightatEnd_editText = (EditText)view.findViewById(R.id.powderweightatEnd_editText);
        powderused_editText = (EditText)view.findViewById(R.id.powderused_editText);
        powderused_textView = (TextView)view.findViewById(R.id.powderused_textView);
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
        powerweight_Text = (TextView)view.findViewById(R.id.powderweight_textView);
        powerweightatEnd_Text = (TextView)view.findViewById(R.id.powderweightatEnd_textView);
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
        submit_Button = (Button)view.findViewById(R.id.submit_Button);

        //------ Post-Printing Fields ------//
        posprinting_expand_button = (Button)view.findViewById(R.id.posprinting_expand_button);
        postID_Text = (TextView)view.findViewById(R.id.postID_textView);
        postID_editText = (EditText)view.findViewById(R.id.postID_editText);
        urlphoto_Text = (TextView)view.findViewById(R.id.urlphoto_textView);
        urlphoto_editText = (EditText)view.findViewById(R.id.urlphoto_editText);
        supportremoval_Text = (TextView)view.findViewById(R.id.supportRemoval_textView);
        WEDM_Text = (TextView)view.findViewById(R.id.WEDM_textView);
        WEDMcomments_Text = (TextView)view.findViewById(R.id.WEDMcomments_textView);
        WEDMcomments_editText = (EditText) view.findViewById(R.id.WEDMcomments_editText);
        blasting_Text = (TextView)view.findViewById(R.id.blasting_textView);
        blastingType_Text = (TextView) view.findViewById(R.id.blastingType_textView);
        blastingType_editText = (EditText)view.findViewById(R.id.blastingType_editText);
        blastingTime_Text = (TextView)view.findViewById(R.id.blastingTime_textView);
        blastingTime_editText = (EditText)view.findViewById(R.id.blastingTime_editText);
        blastingComment_Text = (TextView)view.findViewById(R.id.blastingComment_textView);
        blastingComment_editText = (EditText)view.findViewById(R.id.blastingComment_editText);
        heatTreatment_Text = (TextView)view.findViewById(R.id.heatTreatment_textView);
        stressRelieving_Text = (TextView)view.findViewById(R.id.stressRelieving_textView);
        stressTemp_Text = (TextView)view.findViewById(R.id.stressTemp_textView);
        stressTemp_editText = (EditText)view.findViewById(R.id.stressTemp_editText);
        stressTime_Text = (TextView)view.findViewById(R.id.stressTime_textView);
        stressTime_editText = (EditText)view.findViewById(R.id.stressTime_editText);
        stressShieldingGas_Text = (TextView) view.findViewById(R.id.stressShieldingGas_textView);
        stressComment_Text = (TextView) view.findViewById(R.id.stressComment_textView);
        stressComment_editText = (EditText)view.findViewById(R.id.stressComment_editText);
        hardening_Text = (TextView)view.findViewById(R.id.hardening_textView);
        hardeningTemp_Text = (TextView)view.findViewById(R.id.hardeningTemp_textView);
        hardeningTemp_editText = (EditText)view.findViewById(R.id.hardeningTemp_editText);
        hardeningTime_Text = (TextView)view.findViewById(R.id.hardeningTime_textView);
        hardeningTime_editText = (EditText)view.findViewById(R.id.hardeningTime_editText);
        hardeningComment_Text = (TextView)view.findViewById(R.id.hardeningComment_textView);
        hardeningComment_editText = (EditText)view.findViewById(R.id.hardeningComment_editText);
        tempering_Text = (TextView)view.findViewById(R.id.tempering_textView);
        temperingTemp_Text = (TextView)view.findViewById(R.id.temperingTemp_textView);
        temperingTemp_editText = (EditText)view.findViewById(R.id.temperingTemp_editText);
        temperingTime_Text = (TextView)view.findViewById(R.id.temperingTime_textView);
        temperingTime_editText = (EditText)view.findViewById(R.id.temperingTime_editText);
        temperingNumberofCycles_Text = (TextView)view.findViewById(R.id.temperingNumberofCycles_textView);
        temperingNumberofCycles_editText = (EditText)view.findViewById(R.id.temperingNumberofCycles_editText);
        temperingComment_Text = (TextView)view.findViewById(R.id.temperingComment_textView);
        temperingComment_editText = (EditText)view.findViewById(R.id.temperingComment_editText);
        solutionTreatment_Text = (TextView)view.findViewById(R.id.solutionTreatment_textView);
        solutionTreatmentTemp_Text = (TextView)view.findViewById(R.id.solutionTreatmentTemp_textView);
        solutionTreatmentTemp_editText = (EditText)view.findViewById(R.id.solutionTreatmentTemp_editText);
        solutionTreatmentTime_Text = (TextView)view.findViewById(R.id.solutionTreatmentTime_textView);
        solutionTreatmentTime_editText = (EditText)view.findViewById(R.id.solutionTreatmentTime_editText);
        solutionTreatmentComment_Text = (TextView)view.findViewById(R.id.solutionTreatmentComment_textView);
        solutionTreatmentComment_editText = (EditText)view.findViewById(R.id.solutionTreatmentComment_editText);
        agingTreatment_Text = (TextView)view.findViewById(R.id.agingTreatment_textView);
        agingTemp_Text = (TextView)view.findViewById(R.id.agingTemp_textView);
        agingTemp_editText = (EditText)view.findViewById(R.id.agingTemp_editText);
        agingTime_Text = (TextView)view.findViewById(R.id.agingTime_textView);
        agingTime_editText = (EditText)view.findViewById(R.id.agingTime_editText);
        agingNumberofCycles_Text = (TextView)view.findViewById(R.id.agingNumberofCycles_textView);
        agingNumberofCycles_editText = (EditText)view.findViewById(R.id.agingNumberofCycles_editText);
        agingComment_Text = (TextView)view.findViewById(R.id.agingComment_textView);
        agingComment_editText = (EditText)view.findViewById(R.id.agingComment_editText);
        postprintingSave_Button = (Button)view.findViewById(R.id.postprintingSave_Button);

        hide_preprining();
        hide_prining();
        hide_posprinting();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinners = (Spinner) parent;;
        if(spinners.getId() == R.id.powderCondition_editText) {
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
        else if(spinners.getId() == R.id.supportRemoval_editText) {
        }
        else if(spinners.getId() == R.id.WEDM_editText) {
        }
        else if(spinners.getId() == R.id.blasting_editText) {
        }
        else if(spinners.getId() == R.id.shielding_editText) {
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void hide_preprining(){
        projectID_editText.setVisibility(View.GONE);
        partnumber_editText.setVisibility(View.GONE);
        buildId_textView.setVisibility(View.GONE);
        buildId_editText.setVisibility(View.GONE);
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
        buildId_textView.setVisibility(View.VISIBLE);
        buildId_editText.setVisibility(View.VISIBLE);
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
        powderused_editText.setVisibility(View.GONE);
        powderused_textView.setVisibility(View.GONE);
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
        powderused_editText.setVisibility(View.VISIBLE);
        powderused_textView.setVisibility(View.VISIBLE);
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
        WEDMcomments_Text.setVisibility(View.GONE);
        WEDMcomments_editText.setVisibility(View.GONE);
        blasting_Text.setVisibility(View.GONE);
        blastingSpinner.setVisibility(View.GONE);
        blastingType_Text.setVisibility(View.GONE);
        blastingType_editText.setVisibility(View.GONE);
        blastingTime_Text.setVisibility(View.GONE);
        blastingTime_editText.setVisibility(View.GONE);
        blastingComment_Text.setVisibility(View.GONE);
        blastingComment_editText.setVisibility(View.GONE);
        heatTreatment_Text.setVisibility(View.GONE);
        stressRelieving_Text.setVisibility(View.GONE);
        stressTemp_Text.setVisibility(View.GONE);
        stressTemp_editText.setVisibility(View.GONE);
        stressTime_Text.setVisibility(View.GONE);
        stressTime_editText.setVisibility(View.GONE);
        stressShieldingGas_Text.setVisibility(View.GONE);
        shieldingSpinner.setVisibility(View.GONE);
        stressComment_Text.setVisibility(View.GONE);
        stressComment_editText.setVisibility(View.GONE);
        hardening_Text.setVisibility(View.GONE);
        hardeningTemp_Text.setVisibility(View.GONE);
        hardeningTemp_editText.setVisibility(View.GONE);
        hardeningTime_Text.setVisibility(View.GONE);
        hardeningTime_editText.setVisibility(View.GONE);
        hardeningComment_Text.setVisibility(View.GONE);
        hardeningComment_editText.setVisibility(View.GONE);
        tempering_Text.setVisibility(View.GONE);
        temperingTemp_Text.setVisibility(View.GONE);
        temperingTemp_editText.setVisibility(View.GONE);
        temperingTime_Text.setVisibility(View.GONE);
        temperingTime_editText.setVisibility(View.GONE);
        temperingNumberofCycles_Text.setVisibility(View.GONE);
        temperingNumberofCycles_editText.setVisibility(View.GONE);
        temperingComment_Text.setVisibility(View.GONE);
        temperingComment_editText.setVisibility(View.GONE);
        solutionTreatment_Text.setVisibility(View.GONE);
        solutionTreatmentTemp_Text.setVisibility(View.GONE);
        solutionTreatmentTemp_editText.setVisibility(View.GONE);
        solutionTreatmentTime_Text.setVisibility(View.GONE);
        solutionTreatmentTime_editText.setVisibility(View.GONE);
        solutionTreatmentComment_Text.setVisibility(View.GONE);
        solutionTreatmentComment_editText.setVisibility(View.GONE);
        agingTreatment_Text.setVisibility(View.GONE);
        agingTemp_Text.setVisibility(View.GONE);
        agingTemp_editText.setVisibility(View.GONE);
        agingTime_Text.setVisibility(View.GONE);
        agingTime_editText.setVisibility(View.GONE);
        agingNumberofCycles_Text.setVisibility(View.GONE);
        agingNumberofCycles_editText.setVisibility(View.GONE);
        agingComment_Text.setVisibility(View.GONE);
        agingComment_editText.setVisibility(View.GONE);
        postprintingSave_Button.setVisibility(View.GONE);

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
        WEDMcomments_Text.setVisibility(View.VISIBLE);
        WEDMcomments_editText.setVisibility(View.VISIBLE);
        blasting_Text.setVisibility(View.VISIBLE);
        blastingSpinner.setVisibility(View.VISIBLE);
        blastingType_Text.setVisibility(View.VISIBLE);
        blastingType_editText.setVisibility(View.VISIBLE);
        blastingTime_Text.setVisibility(View.VISIBLE);
        blastingTime_editText.setVisibility(View.VISIBLE);
        blastingComment_Text.setVisibility(View.VISIBLE);
        blastingComment_editText.setVisibility(View.VISIBLE);
        heatTreatment_Text.setVisibility(View.VISIBLE);
        stressRelieving_Text.setVisibility(View.VISIBLE);
        stressTemp_Text.setVisibility(View.VISIBLE);
        stressTemp_editText.setVisibility(View.VISIBLE);
        stressTime_Text.setVisibility(View.VISIBLE);
        stressTime_editText.setVisibility(View.VISIBLE);
        stressShieldingGas_Text.setVisibility(View.VISIBLE);
        shieldingSpinner.setVisibility(View.VISIBLE);
        stressComment_Text.setVisibility(View.VISIBLE);
        stressComment_editText.setVisibility(View.VISIBLE);
        hardening_Text.setVisibility(View.VISIBLE);
        hardeningTemp_Text.setVisibility(View.VISIBLE);
        hardeningTemp_editText.setVisibility(View.VISIBLE);
        hardeningTime_Text.setVisibility(View.VISIBLE);
        hardeningTime_editText.setVisibility(View.VISIBLE);
        hardeningComment_Text.setVisibility(View.VISIBLE);
        hardeningComment_editText.setVisibility(View.VISIBLE);
        tempering_Text.setVisibility(View.VISIBLE);
        temperingTemp_Text.setVisibility(View.VISIBLE);
        temperingTemp_editText.setVisibility(View.VISIBLE);
        temperingTime_Text.setVisibility(View.VISIBLE);
        temperingTime_editText.setVisibility(View.VISIBLE);
        temperingNumberofCycles_Text.setVisibility(View.VISIBLE);
        temperingNumberofCycles_editText.setVisibility(View.VISIBLE);
        temperingComment_Text.setVisibility(View.VISIBLE);
        temperingComment_editText.setVisibility(View.VISIBLE);
        solutionTreatment_Text.setVisibility(View.VISIBLE);
        solutionTreatmentTemp_Text.setVisibility(View.VISIBLE);
        solutionTreatmentTemp_editText.setVisibility(View.VISIBLE);
        solutionTreatmentTime_Text.setVisibility(View.VISIBLE);
        solutionTreatmentTime_editText.setVisibility(View.VISIBLE);
        solutionTreatmentComment_Text.setVisibility(View.VISIBLE);
        solutionTreatmentComment_editText.setVisibility(View.VISIBLE);
        agingTreatment_Text.setVisibility(View.VISIBLE);
        agingTemp_Text.setVisibility(View.VISIBLE);
        agingTemp_editText.setVisibility(View.VISIBLE);
        agingTime_Text.setVisibility(View.VISIBLE);
        agingTime_editText.setVisibility(View.VISIBLE);
        agingNumberofCycles_Text.setVisibility(View.VISIBLE);
        agingNumberofCycles_editText.setVisibility(View.VISIBLE);
        agingComment_Text.setVisibility(View.VISIBLE);
        agingComment_editText.setVisibility(View.VISIBLE);
        postprintingSave_Button.setVisibility(View.VISIBLE);

        posprinting_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shrink, 0);

    }
}
