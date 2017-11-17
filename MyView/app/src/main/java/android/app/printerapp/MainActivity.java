package android.app.printerapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.printerapp.history.HistoryDrawerAdapter;
import android.app.printerapp.history.SwipeDismissListViewTouchListener;
import android.app.printerapp.library.LibraryController;
import android.app.printerapp.library.LibraryFragment;
import android.app.printerapp.library.detail.DetailViewFragment;
import android.app.printerapp.util.ui.AnimationHelper;
import android.app.printerapp.viewer.ViewerMainFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alberto-baeza on 1/21/15.
 */
public class MainActivity extends ActionBarActivity {

    //List of Fragments
    private LibraryFragment mLibraryFragment; //Storage fragment
    private ViewerMainFragment mViewerFragment; //Print panel fragment @static for model load

    //Class specific variables
    private static Fragment mCurrent; //The current shown fragment @static
    private static FragmentManager mManager; //Fragment manager to handle transitions @static

    private static TabHost mTabHost;

    //Drawer
    private static DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private HistoryDrawerAdapter mDrawerAdapter;
    private static ActionBarDrawerToggle mDrawerToggle;
    private static FragmentTransaction mFragmentTransaction;

    Logger logger = Logger.getLogger(MainActivity.class.getName());


    public static Fragment getmCurrent() {
        return mCurrent;
    }

    public static void setmCurrent(Fragment mCurrent) {
        MainActivity.mCurrent = mCurrent;
    }

    public static FragmentManager getmManager() {
        return mManager;
    }

    public static void setmManager(FragmentManager mManager) {
        MainActivity.mManager = mManager;
    }

    public static TabHost getmTabHost() {
        return mTabHost;
    }

    public static void setmTabHost(TabHost mTabHost) {
        MainActivity.mTabHost = mTabHost;
    }

    public static DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    public static void setmDrawerLayout(DrawerLayout mDrawerLayout) {
        MainActivity.mDrawerLayout = mDrawerLayout;
    }

    public static ActionBarDrawerToggle getmDrawerToggle() {
        return mDrawerToggle;
    }

    public static void setmDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
        MainActivity.mDrawerToggle = mDrawerToggle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * Since API level 11, thread policy has changed and now does not allow network operation to
         * be executed on UI thread (NetworkOnMainThreadException), so we have to add these lines to
         * permit it.
         */
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setmTabHost((TabHost) findViewById(R.id.tabHost));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Initialize variables
        setmManager(getFragmentManager());

        //Initialize fragments
        mLibraryFragment = (LibraryFragment) getFragmentManager().findFragmentByTag(ListContent.ID_LIBRARY);
        mViewerFragment = (ViewerMainFragment) getFragmentManager().findFragmentByTag(ListContent.ID_VIEWER);


        initDrawer();


        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mAdapterNotification,
                new IntentFilter("notify"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);


        getmManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                /*invalid operation in this method*/
                throw new UnsupportedOperationException("Invalid operation.");
            }
        });

        //Init gcode cache
    
        //Set tab host for the view
        setTabHost();

    }

    public static void performClick(int i){

        getmTabHost().setCurrentTab(i);

    }

    //Initialize history drawer
    public void initDrawer(){

        setmDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout));
        getmDrawerLayout().setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        setmDrawerToggle(new ActionBarDrawerToggle(
                this,                   /* host Activity */
                getmDrawerLayout(),                /* DrawerLayout object */
                R.string.add,            /* "open drawer" description */
                R.string.cancel         /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //offset in 0.01
                double epsilon = 0.1;
                if (Math.abs(1.0 - slideOffset) < epsilon){
                    mDrawerAdapter.notifyDataSetChanged();
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        });

        // Set the drawer toggle as the DrawerListener
        getmDrawerToggle().setDrawerIndicatorEnabled(true);
        getmDrawerLayout().setDrawerListener(getmDrawerToggle());



        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setSelector(getResources().getDrawable(R.drawable.selectable_rect_background_green));

        View drawerListEmptyView = findViewById(R.id.history_empty_view);
        mDrawerList.setEmptyView(drawerListEmptyView);

        LayoutInflater inflater = getLayoutInflater();
        mDrawerList.addHeaderView(inflater.inflate(R.layout.history_drawer_header, null));
        mDrawerAdapter = new HistoryDrawerAdapter(this, LibraryController.getHistoryList());

        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                getmDrawerLayout().closeDrawers();
                requestOpenFile(LibraryController.getHistoryList().get(i - 1).path);

            }
        });

        mDrawerList.setOnTouchListener(new SwipeDismissListViewTouchListener(mDrawerList, new SwipeDismissListViewTouchListener.DismissCallbacks() {

            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {

                    Toast.makeText(MainActivity.this,getString(R.string.delete) + " " + LibraryController.getHistoryList().get(position - 1).model,Toast.LENGTH_SHORT).show();
                    mDrawerAdapter.removeItem(position - 1);


                }
            }
        }));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getmDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        getmDrawerToggle().onConfigurationChanged(newConfig);
    }


    public void setTabHost() {

        getmTabHost().setup();

        //Models tab
        TabHost.TabSpec spec = getmTabHost().newTabSpec("Library");
        spec.setIndicator(getTabIndicator(getResources().getString(R.string.fragment_models)));
        spec.setContent(R.id.maintab1);
        getmTabHost().addTab(spec);

        //Print panel tab
        spec = getmTabHost().newTabSpec("Panel");
        spec.setIndicator(getTabIndicator(getResources().getString(R.string.fragment_print)));
        spec.setContent(R.id.maintab2);
        getmTabHost().addTab(spec);


        getmTabHost().setCurrentTab(0);
        onItemSelected(0);


        getmTabHost().getTabWidget().setDividerDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        getmTabHost().setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                View currentView = getmTabHost().getCurrentView();
                AnimationHelper.inFromRightAnimation(currentView);

                onItemSelected(getmTabHost().getCurrentTab());


            }
        });

    }

    //handle action bar menu open
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (getmDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }


    /**
     * Return the custom view of the tab
     *
     * @param title Title of the tab
     * @return Custom view of a tab layout
     */
    private View getTabIndicator(String title) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.main_activity_tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tab_title_textview);
        tv.setText(title);
        return view;
    }

    public void onItemSelected(int id) {

        if (id!= 1) {

            ViewerMainFragment.hideActionModePopUpWindow();
            ViewerMainFragment.hideCurrentActionPopUpWindow();
        }

        //start transaction
        FragmentTransaction fragmentTransaction = getmManager().beginTransaction();


        //Pop backstack to avoid having bad references when coming from a Detail view
        getmManager().popBackStack();

        //If there is a fragment being shown, hide it to show the new one
        if (getmCurrent() != null) {
            try {
                fragmentTransaction.hide(getmCurrent());
            } catch (NullPointerException e) {
                String msg = "unexpected nullpointer error";
                logger.log(Level.SEVERE, msg, e);
            }
        }

        //Select fragment
        switch (id) {
            //Check if we already created the Fragment to avoid having multiple instances
            case 0: libFrag(fragmentTransaction);
                break;
            //Check if we already created the Fragment to avoid having multiple instances
            case 1: viewFrag(fragmentTransaction);
                break;
            //by default it close the view if no other messages
            default : throw new UnsupportedOperationException("no id found error");
        }

        if (mViewerFragment != null) {
            if (getmCurrent() != mViewerFragment) {
                //Make the surface invisible to avoid frame overlapping
                mViewerFragment.setSurfaceVisibility(0);
            } else {
                //Make the surface visible when we press
                mViewerFragment.setSurfaceVisibility(1);
            }
        }

        //Show current fragment
        if (getmCurrent() != null) {
            fragmentTransaction.show(getmCurrent()).commit();
            getmDrawerToggle().setDrawerIndicatorEnabled(true);
        }


    }
    private void libFrag(FragmentTransaction fragmentTransaction){
        closePrintView();
        if (getFragmentManager().findFragmentByTag(ListContent.ID_LIBRARY) == null) {
            mLibraryFragment = new LibraryFragment();
            fragmentTransaction.add(R.id.maintab1, mLibraryFragment, ListContent.ID_LIBRARY);
        }
        setmCurrent(mLibraryFragment);
    }

    private void viewFrag(FragmentTransaction fragmentTransaction){
        closePrintView();
        closeDetailView();

        if (getFragmentManager().findFragmentByTag(ListContent.ID_VIEWER) == null) {
            mViewerFragment = new ViewerMainFragment();
            fragmentTransaction.add(R.id.maintab2, mViewerFragment, ListContent.ID_VIEWER);
        }
        setmCurrent(mViewerFragment);
    }

    public static void refreshDevicesCount(){

        getmCurrent().setMenuVisibility(false);




    }


    private static void closePrintView(){
        //Refresh printview fragment if exists

        if (getmCurrent() !=null)
            getmCurrent().setMenuVisibility(true);
    }

    public static void closeDetailView(){
        //Refresh printview fragment if exists
        Fragment fragment = getmManager().findFragmentByTag(ListContent.ID_DETAIL);
        if (fragment != null)
            ((DetailViewFragment) fragment).removeRightPanel();
    }

    /**
     * Override to allow back navigation on the Storage fragment.
     */
    @Override
    public void onBackPressed() {

        //Update the actionbar to show the up carat/affordance


        if (getmCurrent() != null) {
            Fragment fragment = getmManager().findFragmentByTag(ListContent.ID_SETTINGS);

            if (fragment != null){

                closePrintView();

                if (getmManager().popBackStackImmediate()){

                    getmDrawerToggle().setDrawerIndicatorEnabled(true);
                    getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                    //Basically refresh printer count if all were deleted in Settings mode


                }else super.onBackPressed();
            } else super.onBackPressed();


        } else {
            super.onBackPressed();
        }
    }

    /**
     * Send a file to the Viewer to display
     *
     * @param path File path
     */
    public static void requestOpenFile(final String path) {

        //This method will simulate a click and all its effects
        performClick(1);

        //Handler will avoid crash
        Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {

                if (path!=null) {
                    ViewerMainFragment.openFileDialog(path);
                }
            }
        });

    }


    //notify ALL adapters every time a notification is received
    private BroadcastReceiver mAdapterNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String message = intent.getStringExtra("message");

            if (message!=null)
                if ("Devices".equals(message)){


                    //Refresh printview fragment if exists
                    throw new UnsupportedOperationException("no device error");

                } else if ("Profile".equals(message)&&mViewerFragment!=null){
                        mViewerFragment.notifyAdapter();


                } else if ("Files".equals(message)&&mLibraryFragment != null) {
                        mLibraryFragment.refreshFiles();

                }

        }
    };

    /*
Close app on locale change
 */


    @Override
    protected void onDestroy() {

        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mAdapterNotification);

        super.onDestroy();
    }

    @Override
    protected void onResume() {


        super.onResume();

    }

    @Override
    protected void onPause() {



        super.onPause();

    }
}
