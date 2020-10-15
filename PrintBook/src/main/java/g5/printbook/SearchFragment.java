package g5.printbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import g5.printbook.database.Insert;
import g5.printbook.database.Config;
import g5.printbook.database.Search;

public class SearchFragment extends Fragment {
    String returned [] = new String[19];
    View view;
    Button search_by_slm_button;
    EditText search_by_slm_EditText, searched_result_printingid;
    String submitted_slm_id;
    View focusView = null;
    Config config;
    Search search;
    Button  submit_Button, preprinting_expand_button, printing_expand_button, posprinting_expand_button;
    EditText projectID_editText, partnumber_editText, numberofparts_editText, printingparameters_editText, comment_editText;
    EditText slmid_editText, starttime_editText, endtime_editText, date_editText, operator_editText, agingComment_editText;
    EditText typeofmachine_editText, powerweight_editText, powerweightatEnd_editText, powderwaste_editText, material_editText;
    EditText buildplatform_editText, printTune_editText, powderCondition_editText, reused_times_editText, agingNumberofCycles_editText;
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
    TextView starttime_Text, endtime_Text, date_Text, operator_Text, typeofmachine_Text, powerweight_Text;
    TextView powerweightatEnd_Text, powderwaste_Text,material_Text, buildplatform_Text, printTune_Text, powderCondition_Text, numberofLayers_Text;
    TextView dpcFactor_Text, minExposureTime_Text, printingComments_Text;
    Spinner spinner, supportremovalSpinner, WEDMSpinner, blastingSpinner, shieldingSpinner;
    boolean expanded_preprinting = false, expanded_printing = false, expanded_posprinting = false;
    Button edit_key;
    boolean edit_key_pressed = false;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView imageview;
    Bitmap bitmap;
    private Button download_button;
    private byte[] picByteArray;

    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.search_fragment, container, false);
        initialize();
        initialize_returned();
        hide_all();
        search_by_slm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                hide_all();
                submitted_slm_id = search_by_slm_EditText.getText().toString();
                if (TextUtils.isEmpty(submitted_slm_id)) {
                    search_by_slm_EditText.setError(getString(R.string.error_field_required));
                    focusView = search_by_slm_EditText;
                    focusView.requestFocus();
                }else {
                        search_by_slm(submitted_slm_id);
                }
            }
        });
        edit_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_key_pressed) {
                    make_editable_uneditable(!edit_key_pressed);
                    edit_key_pressed = false;
                }
                else {
                    make_editable_uneditable(!edit_key_pressed);
                    edit_key_pressed = true;
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

    private void search_by_slm(String slm_id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(config.PRINTING_slm_id, slm_id));
        search = new Search(params, config.url_search);
        try {
            returned = search.execute().get();
            if(returned[0] == "-101"){
                Toast.makeText(view.getContext(),"No SLM FOUND", Toast.LENGTH_LONG).show();
            }
            else {
                show_all();
                make_editable_uneditable(false);
                search_Result(returned);
                searched_result_printingid.setVisibility(View.VISIBLE);
                show_preview();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search_Result(String returned []) {
        search_result_printing(returned);
    }

    private void show_preview() throws MalformedURLException {
       try {
           bitmap = BitmapFactory.decodeStream((InputStream) new URL(Config.url_image_preview).getContent());
           ByteArrayOutputStream bytes = new ByteArrayOutputStream();
           Bitmap preview = bitmap.copy(bitmap.getConfig(), true);
           ByteArrayOutputStream pictureByteStream = new ByteArrayOutputStream();
           preview.compress(Bitmap.CompressFormat.PNG, 100, pictureByteStream);
           picByteArray = pictureByteStream.toByteArray();

           bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);

           imageview.setImageBitmap(bitmap);
       }catch (IOException e){
           e.printStackTrace();
       }

    }

    private void search_result_printing(String returned []){
        starttime_editText.setText(returned[1]);
        endtime_editText.setText(returned[2]);
        date_editText.setText(returned[3]);
        operator_editText.setText(returned[4]);
        typeofmachine_editText.setText(returned[5]);
        powerweight_editText.setText(returned[6]);
        powerweightatEnd_editText.setText(returned[7]);
        powderwaste_editText.setText(returned[8]);
        material_editText.setText(returned[10]);
        buildplatform_editText.setText(returned[11]);
        printTune_editText.setText(returned[12]);
        //if_textView.setText(returned[]);
        reused_times_editText.setText(returned[14]);
        numberofLayers_editText.setText(returned[15]);
        dpcFactor_editText.setText(returned[16]);
        minExposureTime_editText.setText(returned[17]);
        printingComments_editText.setText(returned[18]);
        /*
        //slmid_Text.setText(returned[]);
        starttime_Text.setText(returned[]);
        endtime_Text.setText(returned[]);
        date_Text.setText(returned[]);
        operator_Text.setText(returned[]);
        typeofmachine_Text.setText(returned[]);
        powerweight_Text.setText(returned[]);
        powerweightatEnd_Text.setText(returned[]);
        powderwaste_Text.setText(returned[]);
        material_Text.setText(returned[]);
        buildplatform_Text.setText(returned[]);
        printTune_Text.setText(returned[]);
        powderCondition_Text.setText(returned[]);
        numberofLayers_Text.setText(returned[]);
        dpcFactor_Text.setText(returned[]);
        minExposureTime_Text.setText(returned[]);
        printingComments_Text.setText(returned[]); */
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void initialize(){
        edit_key = (Button) view.findViewById(R.id.edit_key);
        search_by_slm_button = (Button) view.findViewById(R.id.search_by_slm_button);
        search_by_slm_EditText = (EditText)view.findViewById(R.id.search_by_slm_EditText);
    }
    private void initialize_returned() {
        spinner = (Spinner) view.findViewById(R.id.searched_result_powderCondition_editText);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.powder_condition_string, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        supportremovalSpinner = (Spinner) view.findViewById(R.id.searched_result_supportRemoval_editText);
        ArrayAdapter<CharSequence> supportremovalAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.support_removal_string, android.R.layout.simple_spinner_item);
        supportremovalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supportremovalSpinner.setAdapter(supportremovalAdapter);

        WEDMSpinner = (Spinner) view.findViewById(R.id.searched_result_WEDM_editText);
        ArrayAdapter<CharSequence> WEDMSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.WEDM_string, android.R.layout.simple_spinner_item);
        WEDMSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        WEDMSpinner.setAdapter(WEDMSpinnerAdapter);

        blastingSpinner = (Spinner) view.findViewById(R.id.searched_result_blasting_editText);
        ArrayAdapter<CharSequence> blastingSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.blasting_string, android.R.layout.simple_spinner_item);
        blastingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blastingSpinner.setAdapter(blastingSpinnerAdapter);

        shieldingSpinner = (Spinner) view.findViewById(R.id.searched_result_shielding_editText);
        ArrayAdapter<CharSequence> shieldingSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.blasting_string, android.R.layout.simple_spinner_item);
        shieldingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shieldingSpinner.setAdapter(shieldingSpinnerAdapter);

        preprinting_expand_button = (Button)view.findViewById(R.id.searched_result_preprinting_expand_button);
        projectID_editText = (EditText)view.findViewById(R.id.searched_result_projectID_editText);
        partnumber_editText = (EditText)view.findViewById(R.id.searched_result_partnumber_editText);
        numberofparts_editText =  (EditText)view.findViewById(R.id.searched_result_numberofparts_editText);
        printingparameters_editText = (EditText)view.findViewById(R.id.searched_result_printingparameters_editText);
        comment_editText = (EditText)view.findViewById(R.id.searched_result_comment_editText);

        projectID_Text = (TextView) view.findViewById(R.id.searched_result_projectID_textView);
        partnumber_Text = (TextView)view.findViewById(R.id.searched_result_partnumber_textView);
        numberofparts_Text =  (TextView)view.findViewById(R.id.searched_result_numberparts_textView);
        printingparameters_Text = (TextView)view.findViewById(R.id.searched_result_prinringparameters_textView);
        comment_Text = (TextView)view.findViewById(R.id.searched_result_coment_textView);

        printing_expand_button = (Button)view.findViewById(R.id.searched_result_Printing_expand_button);
        starttime_editText = (EditText)view.findViewById(R.id.searched_result_starttime_editText);
        endtime_editText =  (EditText)view.findViewById(R.id.searched_result_endtime_editText);
        date_editText = (EditText)view.findViewById(R.id.searched_result_date_editText);
        operator_editText = (EditText)view.findViewById(R.id.searched_result_operator_editText);
        typeofmachine_editText = (EditText)view.findViewById(R.id.searched_result_typeofmachine_editText);
        powerweight_editText = (EditText)view.findViewById(R.id.searched_result_powderweight_editText);
        powerweightatEnd_editText = (EditText)view.findViewById(R.id.searched_result_powderweightatEnd_editText);
        powderwaste_editText = (EditText)view.findViewById(R.id.searched_result_powderwaste_editText);
        material_editText = (EditText)view.findViewById(R.id.searched_result_material_editText);
        buildplatform_editText = (EditText)view.findViewById(R.id.searched_result_buildplatform_editText);
        printTune_editText = (EditText)view.findViewById(R.id.searched_result_printTune_editText);
        if_textView = (TextView)view.findViewById(R.id.searched_result_if_textView);
        reused_times_editText = (EditText)view.findViewById(R.id.searched_result_if_editText);
        numberofLayers_editText = (EditText)view.findViewById(R.id.searched_result_numberofLayers_editText);
        dpcFactor_editText = (EditText)view.findViewById(R.id.searched_result_dpcFactor_editText);
        minExposureTime_editText = (EditText)view.findViewById(R.id.searched_result_minExposureTime_editText);
        printingComments_editText = (EditText)view.findViewById(R.id.searched_result_printingComments_editText);

        starttime_Text = (TextView)view.findViewById(R.id.searched_result_starttime_textView);
        endtime_Text =  (TextView)view.findViewById(R.id.searched_result_endtime_textView);
        date_Text = (TextView)view.findViewById(R.id.searched_result_date_textView);
        operator_Text = (TextView)view.findViewById(R.id.searched_result_operator_textView);
        typeofmachine_Text = (TextView)view.findViewById(R.id.searched_result_typeofmachine_textView);
        powerweight_Text = (TextView)view.findViewById(R.id.searched_result_powderweight_textView);
        powerweightatEnd_Text = (TextView)view.findViewById(R.id.searched_result_powderweightatEnd_textView);
        powderwaste_Text = (TextView)view.findViewById(R.id.searched_result_powderwaste_textView);
        material_Text = (TextView)view.findViewById(R.id.searched_result_material_textView);
        buildplatform_Text = (TextView)view.findViewById(R.id.searched_result_buildplatform_textView);
        printTune_Text = (TextView)view.findViewById(R.id.searched_result_printTune_textView);
        powderCondition_Text = (TextView)view.findViewById(R.id.searched_result_powderCondition_textView);
        numberofLayers_Text = (TextView)view.findViewById(R.id.searched_result_numberofLayers_textView);
        dpcFactor_Text = (TextView)view.findViewById(R.id.searched_result_dpcFactor_textView);
        minExposureTime_Text = (TextView)view.findViewById(R.id.searched_result_minExposureTime_textView);
        printingComments_Text = (TextView)view.findViewById(R.id.searched_result_printingComments_textView);

        submit_Button = (Button)view.findViewById(R.id.searched_result_submit_Button);

        //------ Post-Printing Fields ------//
        posprinting_expand_button = (Button)view.findViewById(R.id.searched_result_posprinting_expand_button);
        postID_Text = (TextView)view.findViewById(R.id.searched_result_postID_textView);
        postID_editText = (EditText)view.findViewById(R.id.searched_result_postID_editText);
        urlphoto_Text = (TextView)view.findViewById(R.id.searched_result_urlphoto_textView);
        urlphoto_editText = (EditText)view.findViewById(R.id.searched_result_urlphoto_editText);
        supportremoval_Text = (TextView)view.findViewById(R.id.searched_result_supportRemoval_textView);
        WEDM_Text = (TextView)view.findViewById(R.id.searched_result_WEDM_textView);
        WEDMcomments_Text = (TextView)view.findViewById(R.id.searched_result_WEDMcomments_textView);
        WEDMcomments_editText = (EditText) view.findViewById(R.id.searched_result_WEDMcomments_editText);
        blasting_Text = (TextView)view.findViewById(R.id.searched_result_blasting_textView);
        blastingType_Text = (TextView) view.findViewById(R.id.searched_result_blastingType_textView);
        blastingType_editText = (EditText)view.findViewById(R.id.searched_result_blastingType_editText);
        blastingTime_Text = (TextView)view.findViewById(R.id.searched_result_blastingTime_textView);
        blastingTime_editText = (EditText)view.findViewById(R.id.searched_result_blastingTime_editText);
        blastingComment_Text = (TextView)view.findViewById(R.id.searched_result_blastingComment_textView);
        blastingComment_editText = (EditText)view.findViewById(R.id.searched_result_blastingComment_editText);
        heatTreatment_Text = (TextView)view.findViewById(R.id.searched_result_heatTreatment_textView);
        stressRelieving_Text = (TextView)view.findViewById(R.id.searched_result_stressRelieving_textView);
        stressTemp_Text = (TextView)view.findViewById(R.id.searched_result_stressTemp_textView);
        stressTemp_editText = (EditText)view.findViewById(R.id.searched_result_stressTemp_editText);
        stressTime_Text = (TextView)view.findViewById(R.id.searched_result_stressTime_textView);
        stressTime_editText = (EditText)view.findViewById(R.id.searched_result_stressTime_editText);
        stressShieldingGas_Text = (TextView) view.findViewById(R.id.searched_result_stressShieldingGas_textView);
        stressComment_Text = (TextView) view.findViewById(R.id.searched_result_stressComment_textView);
        stressComment_editText = (EditText)view.findViewById(R.id.searched_result_stressComment_editText);
        hardening_Text = (TextView)view.findViewById(R.id.searched_result_hardening_textView);
        hardeningTemp_Text = (TextView)view.findViewById(R.id.searched_result_hardeningTemp_textView);
        hardeningTemp_editText = (EditText)view.findViewById(R.id.searched_result_hardeningTemp_editText);
        hardeningTime_Text = (TextView)view.findViewById(R.id.searched_result_hardeningTime_textView);
        hardeningTime_editText = (EditText)view.findViewById(R.id.searched_result_hardeningTime_editText);
        hardeningComment_Text = (TextView)view.findViewById(R.id.searched_result_hardeningComment_textView);
        hardeningComment_editText = (EditText)view.findViewById(R.id.searched_result_hardeningComment_editText);
        tempering_Text = (TextView)view.findViewById(R.id.searched_result_tempering_textView);
        temperingTemp_Text = (TextView)view.findViewById(R.id.searched_result_temperingTemp_textView);
        temperingTemp_editText = (EditText)view.findViewById(R.id.searched_result_temperingTemp_editText);
        temperingTime_Text = (TextView)view.findViewById(R.id.searched_result_temperingTime_textView);
        temperingTime_editText = (EditText)view.findViewById(R.id.searched_result_temperingTime_editText);
        temperingNumberofCycles_Text = (TextView)view.findViewById(R.id.searched_result_temperingNumberofCycles_textView);
        temperingNumberofCycles_editText = (EditText)view.findViewById(R.id.searched_result_temperingNumberofCycles_editText);
        temperingComment_Text = (TextView)view.findViewById(R.id.searched_result_temperingComment_textView);
        temperingComment_editText = (EditText)view.findViewById(R.id.searched_result_temperingComment_editText);
        solutionTreatment_Text = (TextView)view.findViewById(R.id.searched_result_solutionTreatment_textView);
        solutionTreatmentTemp_Text = (TextView)view.findViewById(R.id.searched_result_solutionTreatmentTemp_textView);
        solutionTreatmentTemp_editText = (EditText)view.findViewById(R.id.searched_result_solutionTreatmentTemp_editText);
        solutionTreatmentTime_Text = (TextView)view.findViewById(R.id.searched_result_solutionTreatmentTime_textView);
        solutionTreatmentTime_editText = (EditText)view.findViewById(R.id.searched_result_solutionTreatmentTime_editText);
        solutionTreatmentComment_Text = (TextView)view.findViewById(R.id.searched_result_solutionTreatmentComment_textView);
        solutionTreatmentComment_editText = (EditText)view.findViewById(R.id.searched_result_solutionTreatmentComment_editText);
        agingTreatment_Text = (TextView)view.findViewById(R.id.searched_result_agingTreatment_textView);
        agingTemp_Text = (TextView)view.findViewById(R.id.searched_result_agingTemp_textView);
        agingTemp_editText = (EditText)view.findViewById(R.id.searched_result_agingTemp_editText);
        agingTime_Text = (TextView)view.findViewById(R.id.searched_result_agingTime_textView);
        agingTime_editText = (EditText)view.findViewById(R.id.searched_result_agingTime_editText);
        agingNumberofCycles_Text = (TextView)view.findViewById(R.id.searched_result_agingNumberofCycles_textView);
        agingNumberofCycles_editText = (EditText)view.findViewById(R.id.searched_result_agingNumberofCycles_editText);
        agingComment_Text = (TextView)view.findViewById(R.id.searched_result_agingComment_textView);
        agingComment_editText = (EditText)view.findViewById(R.id.searched_result_agingComment_editText);

        imageview = (ImageView)view.findViewById(R.id.image_preview);
        download_button = (Button)view.findViewById(R.id.download);
    }
    private void hide_all(){
        edit_key.setVisibility(View.GONE);
        posprinting_expand_button.setVisibility(View.GONE);
        preprinting_expand_button.setVisibility(View.GONE);
        printing_expand_button.setVisibility(View.GONE);
        submit_Button.setVisibility(View.GONE);
        imageview.setVisibility(View.GONE);
        download_button.setVisibility(View.GONE);
        hide_preprining();
        hide_prining();
        hide_posprinting();
    }
    private void show_all(){
        edit_key.setVisibility(View.VISIBLE);
        posprinting_expand_button.setVisibility(View.VISIBLE);
        preprinting_expand_button.setVisibility(View.VISIBLE);
        printing_expand_button.setVisibility(View.VISIBLE);
        imageview.setVisibility(View.VISIBLE);
        download_button.setVisibility(View.VISIBLE);
    }
    private void hide_preprining(){
        projectID_editText.setVisibility(View.GONE);
        partnumber_editText.setVisibility(View.GONE);
        numberofparts_editText.setVisibility(View.GONE);
        printingparameters_editText.setVisibility(View.GONE);
        comment_editText.setVisibility(View.GONE);
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
        preprinting_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shrink, 0);
        projectID_Text.setVisibility(View.VISIBLE);
        partnumber_Text.setVisibility(View.VISIBLE);
        numberofparts_Text.setVisibility(View.VISIBLE);
        printingparameters_Text.setVisibility(View.VISIBLE);
        comment_Text.setVisibility(View.VISIBLE);
    }
    private void hide_prining() {
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
        spinner.setVisibility(View.GONE);

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
        //slmid_editText.setVisibility(View.VISIBLE);
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
        spinner.setVisibility(View.VISIBLE);
        //slmid_Text.setVisibility(View.VISIBLE);
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
        posprinting_expand_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shrink, 0);
    }
    private void reset(){
        reset_printing();
    }
    private void reset_printing(){
        String set_to = null;
        starttime_editText.setText(set_to);
        endtime_editText.setText(set_to);
        date_editText.setText(set_to);
        operator_editText.setText(set_to);
        typeofmachine_editText.setText(set_to);
        powerweight_editText.setText(set_to);
        powerweightatEnd_editText.setText(set_to);
        powderwaste_editText.setText(set_to);
        material_editText.setText(set_to);
        buildplatform_editText.setText(set_to);
        printTune_editText.setText(set_to);
        reused_times_editText.setText(set_to);
        numberofLayers_editText.setText(set_to);
        dpcFactor_editText.setText(set_to);
        minExposureTime_editText.setText(set_to);
        printingComments_editText.setText(set_to);
    }

    private void make_editable_uneditable(boolean editable){
        if(editable) submit_Button.setVisibility(View.VISIBLE);
        else submit_Button.setVisibility(View.GONE);
        projectID_editText.setEnabled(editable);
        partnumber_editText.setEnabled(editable);
        numberofparts_editText.setEnabled(editable);
        printingparameters_editText.setEnabled(editable);
        comment_editText.setEnabled(editable);
        projectID_Text.setEnabled(editable);
        partnumber_Text.setEnabled(editable);
        numberofparts_Text.setEnabled(editable);
        printingparameters_Text.setEnabled(editable);
        comment_Text.setEnabled(editable);

        postID_Text.setEnabled(editable);
        postID_editText.setEnabled(editable);
        urlphoto_Text.setEnabled(editable);
        urlphoto_editText.setEnabled(editable);
        supportremoval_Text.setEnabled(editable);
        supportremovalSpinner.setEnabled(editable);
        WEDM_Text.setEnabled(editable);
        WEDMSpinner.setEnabled(editable);
        WEDMcomments_Text.setEnabled(editable);
        WEDMcomments_editText.setEnabled(editable);
        blasting_Text.setEnabled(editable);
        blastingSpinner.setEnabled(editable);
        blastingType_Text.setEnabled(editable);
        blastingType_editText.setEnabled(editable);
        blastingTime_Text.setEnabled(editable);
        blastingTime_editText.setEnabled(editable);
        blastingComment_Text.setEnabled(editable);
        blastingComment_editText.setEnabled(editable);
        heatTreatment_Text.setEnabled(editable);
        stressRelieving_Text.setEnabled(editable);
        stressTemp_Text.setEnabled(editable);
        stressTemp_editText.setEnabled(editable);
        stressTime_Text.setEnabled(editable);
        stressTime_editText.setEnabled(editable);
        stressShieldingGas_Text.setEnabled(editable);
        shieldingSpinner.setEnabled(editable);
        stressComment_Text.setEnabled(editable);
        stressComment_editText.setEnabled(editable);
        hardening_Text.setEnabled(editable);
        hardeningTemp_Text.setEnabled(editable);
        hardeningTemp_editText.setEnabled(editable);
        hardeningTime_Text.setEnabled(editable);
        hardeningTime_editText.setEnabled(editable);
        hardeningComment_Text.setEnabled(editable);
        hardeningComment_editText.setEnabled(editable);
        tempering_Text.setEnabled(editable);
        temperingTemp_Text.setEnabled(editable);
        temperingTemp_editText.setEnabled(editable);
        temperingTime_Text.setEnabled(editable);
        temperingTime_editText.setEnabled(editable);
        temperingNumberofCycles_Text.setEnabled(editable);
        temperingNumberofCycles_editText.setEnabled(editable);
        temperingComment_Text.setEnabled(editable);
        temperingComment_editText.setEnabled(editable);
        solutionTreatment_Text.setEnabled(editable);
        solutionTreatmentTemp_Text.setEnabled(editable);
        solutionTreatmentTemp_editText.setEnabled(editable);
        solutionTreatmentTime_Text.setEnabled(editable);
        solutionTreatmentTime_editText.setEnabled(editable);
        solutionTreatmentComment_Text.setEnabled(editable);
        solutionTreatmentComment_editText.setEnabled(editable);
        agingTreatment_Text.setEnabled(editable);
        agingTemp_Text.setEnabled(editable);
        agingTemp_editText.setEnabled(editable);
        agingTime_Text.setEnabled(editable);
        agingTime_editText.setEnabled(editable);
        agingNumberofCycles_Text.setEnabled(editable);
        agingNumberofCycles_editText.setEnabled(editable);
        agingComment_Text.setEnabled(editable);
        agingComment_editText.setEnabled(editable);

        //slmid_editText.setEnabled(editable);
        starttime_editText.setEnabled(editable);
        endtime_editText.setEnabled(editable);
        date_editText.setEnabled(editable);
        operator_editText.setEnabled(editable);
        typeofmachine_editText.setEnabled(editable);
        powerweight_editText.setEnabled(editable);
        powerweightatEnd_editText.setEnabled(editable);
        powderwaste_editText.setEnabled(editable);
        material_editText.setEnabled(editable);
        buildplatform_editText.setEnabled(editable);
        printTune_editText.setEnabled(editable);
        numberofLayers_editText.setEnabled(editable);
        dpcFactor_editText.setEnabled(editable);
        minExposureTime_editText.setEnabled(editable);
        printingComments_editText.setEnabled(editable);
        spinner.setEnabled(editable);
        //slmid_Text.setEnabled(editable);
        starttime_Text.setEnabled(editable);
        endtime_Text.setEnabled(editable);
        date_Text.setEnabled(editable);
        operator_Text.setEnabled(editable);
        typeofmachine_Text.setEnabled(editable);
        powerweight_Text.setEnabled(editable);
        powerweightatEnd_Text.setEnabled(editable);
        powderwaste_Text.setEnabled(editable);
        material_Text.setEnabled(editable);
        buildplatform_Text.setEnabled(editable);
        printTune_Text.setEnabled(editable);
        powderCondition_Text.setEnabled(editable);
        numberofLayers_Text.setEnabled(editable);
        dpcFactor_Text.setEnabled(editable);
        minExposureTime_Text.setEnabled(editable);
        printingComments_Text.setEnabled(editable);
    }
}
