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
    Button _3d,createMaterial_button;
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
        createMaterial_button = (Button)view.findViewById(R.id.createMaterial_button);
        _3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMain();
            }
        });
        createMaterial_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent().setClass(getActivity(), CreatematerialFragment.class);
                startActivity(mainIntent);
            }
        });
        return view;
    }

    private void callMain(){
        Intent mainIntent = new Intent().setClass(getActivity(), MainActivity.class);
        mainIntent.putExtra("path","path");
        startActivity(mainIntent);
    }

}
