package g5.printbook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import g5.printbook.database.Insert;
import g5.printbook.database.Config;
import g5.printbook.database.Search;

public class SearchFragment extends Fragment {
    View view;
    Button search_by_slm_button;
    EditText search_by_slm_EditText, searched_result_printingid;
    String submitted_slm_id;
    View focusView = null;
    Config config;
    Search search;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
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
        search_by_slm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return view;
    }

    private void search_by_slm(String slm_id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(config.PRINTING_slm_id, slm_id));
        search = new Search(params, config.url_search);
        try {
            String printing_id [] = search.execute().get();
            if(printing_id[0] == "-101"){
                Toast.makeText(view.getContext(),"No SLM FOUND", Toast.LENGTH_LONG).show();
            }
            else {
                search_Result(printing_id[0]);
                searched_result_printingid.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search_Result(String printing_id) {
        searched_result_printingid.setText(printing_id);
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
        search_by_slm_button = (Button) view.findViewById(R.id.search_by_slm_button);
        search_by_slm_EditText = (EditText)view.findViewById(R.id.search_by_slm_EditText);
        searched_result_printingid = (EditText)view.findViewById(R.id.searched_result_printingid);
        searched_result_printingid.setVisibility(View.GONE);
    }
}
