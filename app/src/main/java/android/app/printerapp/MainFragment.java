package android.app.printerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    Button _3d;
    View view;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_main, container, false);
        _3d = (Button)view.findViewById(R.id.visualize_button);
        _3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMain();
            }
        });
        return view;
    }

    private void callMain(){
        Intent mainIntent = new Intent().setClass(getActivity(), MainActivity.class);
        startActivity(mainIntent);
    }

}
