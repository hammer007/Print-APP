package g5.printbook;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import g5.printbook.database.Config;
import g5.printbook.database.Insert;
import g5.printbook.database.Search;
import g5.printbook.database.SearchUser;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class NewProjectMandatoryFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    Insert insert;
    String returned[] = new String[3];
    private Button magic_upload_button, create_printjob_continue, create_printjob_save;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ImageView imageView;
    EditText slm_id;
    TextView jobType_Text, users_Text;
    Spinner jobTypeSpinner, usersSpinner;
    Bitmap bitmap;
    View view;
    private String submitted_slmId;
    ProgressDialog progressDialog;
    String ImageName = "slm_id", ImagePath = "image_path" ;
    Config config;
    boolean check = true;
    View focusView = null;
    String SLM_FOUND = "";
    SearchUser search;

    public NewProjectMandatoryFragment() {
        // Required empty public constructor
    }
    public static NewProjectMandatoryFragment newInstance(int slm_id) {
        NewProjectMandatoryFragment myFragment = new NewProjectMandatoryFragment();

        Bundle args = new Bundle();
        args.putInt("slm_id", slm_id);
        myFragment.setArguments(args);

        return myFragment;
    }
    public int getShowSlmId() {
        return getArguments().getInt("slm_id", 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_new_project_mandatory, container, false);

        initialize();

        jobTypeSpinner.setOnItemSelectedListener(this);
        usersSpinner.setOnItemSelectedListener(this);


        magic_upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        create_printjob_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitted_slmId = slm_id.getText().toString();
                if (TextUtils.isEmpty(submitted_slmId)) {
                    slm_id.setError(getString(R.string.error_field_required));
                    focusView = slm_id;
                    focusView.requestFocus();
                }else {
                    String uploaded = ImageUploadToServerFunction();
                    String success_message = "SLM ALREADY EXISTS";
                    if (uploaded.contains(success_message))
                        Toast.makeText(view.getContext(), "SLM ALREADY EXISTS", Toast.LENGTH_LONG).show();
                    else if(uploaded.contains("PROBLEM IN UPLOADING")){
                        Toast.makeText(view.getContext(), "PROBLEM IN UPLOADING", Toast.LENGTH_LONG).show();
                    }
                    else {
                        insert_to_access_table();
                        NewPrintJobFragment fragment = new NewPrintJobFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("slm_id", submitted_slmId);
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();
                    }
                }
                Toast.makeText(getContext(), "Image selected : " + imageView.isSelected(),Toast.LENGTH_LONG).show();

            }
        });
        create_printjob_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitted_slmId = slm_id.getText().toString();
                if (TextUtils.isEmpty(submitted_slmId)) {
                    slm_id.setError(getString(R.string.error_field_required));
                    focusView = slm_id;
                    focusView.requestFocus();
                }else {
                    String uploaded = ImageUploadToServerFunction();
                    String success_message = "SLM ALREADY EXISTS";
                    if (uploaded.contains(success_message))
                        Toast.makeText(view.getContext(), "SLM ALREADY EXISTS", Toast.LENGTH_LONG).show();
                    else if(uploaded.contains("PROBLEM IN UPLOADING")){
                        Toast.makeText(view.getContext(), "PROBLEM IN UPLOADING", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        return view;
    }

    private int insert_to_access_table() {
        int success = -1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //AUTO INCREMENT params.add(new BasicNameValuePair(config.PRINTING_printing_id, 2 + ""));
        params.add(new BasicNameValuePair("slm_id", slm_id.getText().toString()));
        params.add(new BasicNameValuePair("username", usersSpinner.getSelectedItem().toString()));
        insert = new Insert(params, config.TAG_INSERT_ACCESS);
        try {
            success = insert.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {

            case 0: usersSpinner.setVisibility(View.GONE);
                    users_Text.setVisibility(View.GONE);
                break;

            case 1: usersSpinner.setVisibility(View.VISIBLE);
                    users_Text.setVisibility(View.VISIBLE);
                break;


        }
    }

    private String ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(),"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Log.d("SERVER UPLOAD",string1);

                // Setting image as transparent after done uploading.
                imageView.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageName, submitted_slmId);

                HashMapParams.put(ImagePath, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(config.ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        try {
            SLM_FOUND = AsyncTaskUploadClassOBJ.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return SLM_FOUND;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initialize(){
        magic_upload_button = (Button)view.findViewById(R.id.magic_upload_button);
        imageView = (ImageView)view.findViewById(R.id.magic_image_view);
        create_printjob_continue = (Button)view.findViewById(R.id.create_printjob_continue);
        create_printjob_save = (Button)view.findViewById(R.id.create_printjob_save);
        slm_id = (EditText)view.findViewById(R.id.SLM_ID_EDITTEXT);

        jobTypeSpinner = (Spinner) view.findViewById(R.id.jobType_editText);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.jobType_string, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTypeSpinner.setAdapter(adapter);



        List <NameValuePair> params = new ArrayList <NameValuePair>();
        params.add(new BasicNameValuePair("user", "user"));
        search = new SearchUser(params, config.url_get_users);
        try {

            returned = search.execute().get();
            Log.d("Users fetched ", returned + "");

        }

        catch (Exception e) {

            e.printStackTrace();

        }
        usersSpinner = (Spinner) view.findViewById(R.id.users_editText);
        ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, returned);
        //ArrayAdapter<CharSequence> usersAdapter = ArrayAdapter.createFromResource(getContext(),
        //      R.array.users_string, android.R.layout.simple_spinner_item);
        usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersSpinner.setAdapter(usersAdapter);


        jobType_Text = (TextView)view.findViewById(R.id.jobType_textView);
        users_Text = (TextView)view.findViewById(R.id.users_TextView);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                if (Build.VERSION.SDK_INT >= 23)
                {
                    if (checkPermission())
                    {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);

                        imageView.setImageBitmap(bitmap);
                    } else {
                        int x = requestPermission();
                        if (x == 1){
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Log.d("Image", "" + bitmap);
                imageView.setImageBitmap(bitmap);

                        imageView.setImageBitmap(bitmap);}
                    }
                }
                else
                {

                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);

                    imageView.setImageBitmap(bitmap);
                }



                Log.d("empty", " " + data);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private int requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Log.d("fail", "Write External Storage permission allows us to do store images. Please allow this permission in App Settings");
        return 1;
        } else {
            Log.d("true", "Write External Storage permission allows us to do store images. Please allow this permission in App Settings");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return 1;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.d("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}
