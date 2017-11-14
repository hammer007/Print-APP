package g5.printbook.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import g5.printbook.R;
import g5.printbook.database.Config;
import g5.printbook.database.Insert;

import static android.content.ContentValues.TAG;

/**
 * Created by jobaer-pc on 13-Nov-17.
 */

public class Signup extends AppCompatActivity {
         Config config;
         Insert insert;
         EditText edit_first_name, edit_last_name, edit_user_name, edit_email_address, edit_password, edit_retype_password;
         Button signUpbutton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int success = 1;
        setContentView(R.layout.sign_up);
        initialize();

        signUpbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(false){ int x = 0;} //TextUtils.isEmpty(edit_password.getText().toString())) edit_password.setError( "Password is Required!" );
                else {
                    if(success == 1) {
                         insert_to_sign_up();
                          }
                    else Toast.makeText(getApplicationContext(), "SOME PROBLEM WITH PROJECT CREATION, PROJECT TABLE REJECTING", Toast.LENGTH_LONG);
                    //success = -1;
                }

            }
        });
    }

    private void initialize() {
        edit_first_name =(EditText)findViewById(R.id.your_first_name);
        edit_last_name =(EditText)findViewById(R.id.your_last_name);
        edit_user_name = (EditText)findViewById(R.id.your_user_name);
        edit_email_address = (EditText)findViewById(R.id.your_email_address);
        edit_password = (EditText)findViewById(R.id.create_new_password);
        edit_retype_password = (EditText)findViewById(R.id.new_retype_password);
        signUpbutton = findViewById(R.id.sign_up_button);
    }

    private int insert_to_sign_up(){
        int success = 1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //AUTO INCREMENT params.add(new BasicNameValuePair(config.PRINTING_printing_id, 2 + ""));
        params.add(new BasicNameValuePair(config.SIGNUP_first_name, edit_first_name.getText().toString()));
        params.add(new BasicNameValuePair(config.SIGNUP_last_name, edit_last_name.getText().toString()));
        params.add(new BasicNameValuePair(config.SIGNUP_user_name, edit_user_name.getText().toString()));
        params.add(new BasicNameValuePair(config.SIGNUP_email_address, edit_email_address.getText().toString()));
        params.add(new BasicNameValuePair(config.SIGNUP_password_name, edit_password.getText().toString()));
        params.add(new BasicNameValuePair(config.SIGNUP_retype_password_name, edit_retype_password.getText().toString()));
        insert = new Insert(params, config.TAG_INSERT_SIGNUP);
        try {
            success = insert.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "signup signup" + params);
        return success;
    }
}
