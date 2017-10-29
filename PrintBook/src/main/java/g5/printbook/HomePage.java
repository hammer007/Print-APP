package g5.printbook;

/**
 Created by mao & nan on 2017-10-20
**/
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class HomePage extends ActionBarActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private Button fetch, upload, visual, check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();


        fetch = (Button)findViewById(R.id.fetch);
        upload = (Button)findViewById(R.id.upload);
        visual = (Button)findViewById(R.id.visual);
        check = (Button)findViewById(R.id.button4);

        //The clicklistener for buttons
        fetch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                  // Code here executes on main thread after user presses button
                Toast.makeText(HomePage.this, "fetch the model!", Toast.LENGTH_SHORT).show();
            }
        });
        //The clicklistener for buttons
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Toast.makeText(HomePage.this, "upload the model!", Toast.LENGTH_SHORT).show();
            }
        });
        //The clicklistener for buttons
        visual.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Toast.makeText(HomePage.this, "view the model!", Toast.LENGTH_SHORT).show();
            }
        });
        //The clicklistener for buttons
        check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Toast.makeText(HomePage.this, "check the model!", Toast.LENGTH_SHORT).show();
            }
        });

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {
        String[] osArray = { "New Project", "Settings", "About", "4th" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(HomePage.this, "something here", Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 0: {
                        ((RelativeLayout) findViewById(R.id.HomePage_RelativeLayout)).removeAllViews();

                        NewProjectFragment fragmentNp = new NewProjectFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction()
                                .replace(R.id.HomePage_RelativeLayout, fragmentNp)
                                .commit();
                        break;
                    }
                    case 1: {
                        Toast.makeText(HomePage.this, "turn to about page", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 2: {
                        Toast.makeText(HomePage.this, "turn to settings page", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    default:Toast.makeText(HomePage.this, "nothing!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}