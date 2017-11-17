package android.app.printerapp.viewer;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.printerapp.R;
import android.app.printerapp.library.LibraryController;
import android.app.printerapp.model.ModelProfile;
import android.app.printerapp.util.ui.CustomEditableSlider;
import android.app.printerapp.util.ui.CustomPopupWindow;
import android.app.printerapp.util.ui.ListIconPopupWindowAdapter;
import android.app.printerapp.viewer.sidepanel.SidePanelHandler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devsmart.android.ui.HorizontalListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ViewerMainFragment extends Fragment {
    //Tabs
    private static final int NORMAL = 0;
    private static final int LAYER = 4;

    private static int mCurrentViewMode = 0;

    //Constants
    public static final int DO_SNAPSHOT = 0;
    public static final int DONT_SNAPSHOT = 1;
    public static final int PRINT_PREVIEW = 3;

    private static final float POSITIVE_ANGLE = 15;

    private static final int MENU_HIDE_OFFSET_SMALL = 20;
    private static final int MENU_HIDE_OFFSET_BIG = 1000;

    //Variables
    private static File mFile;

    private static ViewerSurfaceView mSurface;
    private static FrameLayout mLayout;

    //Advanced settings expandable panel
    private int mSettingsPanelMinHeight;

    //Buttons
    private static ImageButton mVisibilityModeButton;

    private static SeekBar mSeekBar;
    private boolean isKeyboardShown = false;

    private static List<DataStorage> mDataList = new ArrayList<DataStorage>();

    //Edition menu variables
    private static ProgressBar mProgress;

    private static Context mContext;
    private static View mRootView;

    private static LinearLayout mStatusBottomBar;
    private static FrameLayout mBottomBar;
    private static LinearLayout mRotationLayout;
    private static LinearLayout mScaleLayout;
    private static CustomEditableSlider mRotationSlider;
    private static ImageView mActionImage;

    private static EditText mScaleEditX;
    private static EditText mScaleEditY;
    private static EditText mScaleEditZ;
    private static ImageButton mUniformScale;

    private static ScaleChangeListener mTextWatcherX;
    private static ScaleChangeListener mTextWatcherY;
    private static ScaleChangeListener mTextWatcherZ;

    private static SlicingHandler mSlicingHandler;
    private static SidePanelHandler mSidePanelHandler;

    ;
    private static int[] mCurrentPlate = new int[]{WitboxFaces.WITBOX_LONG, WitboxFaces.WITBOX_WITDH, WitboxFaces.WITBOX_HEIGHT};
    ;

    private static LinearLayout mSizeText;
    private static int mCurrentAxis;


    //Empty constructor
    public ViewerMainFragment() {
        /*the automatically created constructor for the class*/
    }

    public static File getmFile() {
        return mFile;
    }

    public static void setmFile(File mFile) {
        ViewerMainFragment.mFile = mFile;
    }

    public static ViewerSurfaceView getmSurface() {
        return mSurface;
    }

    public static void setmSurface(ViewerSurfaceView mSurface) {
        ViewerMainFragment.mSurface = mSurface;
    }

    public static FrameLayout getmLayout() {
        return mLayout;
    }

    public static void setmLayout(FrameLayout mLayout) {
        ViewerMainFragment.mLayout = mLayout;
    }

    public static ImageButton getmVisibilityModeButton() {
        return mVisibilityModeButton;
    }

    public static void setmVisibilityModeButton(ImageButton mVisibilityModeButton) {
        ViewerMainFragment.mVisibilityModeButton = mVisibilityModeButton;
    }

    public static SeekBar getmSeekBar() {
        return mSeekBar;
    }

    public static void setmSeekBar(SeekBar mSeekBar) {
        ViewerMainFragment.mSeekBar = mSeekBar;
    }

    public static List<DataStorage> getmDataList() {
        return mDataList;
    }

    public static void setmDataList(List<DataStorage> mDataList) {
        ViewerMainFragment.mDataList = mDataList;
    }

    public static ProgressBar getmProgress() {
        return mProgress;
    }

    public static void setmProgress(ProgressBar mProgress) {
        ViewerMainFragment.mProgress = mProgress;
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        ViewerMainFragment.mContext = mContext;
    }

    public static View getmRootView() {
        return mRootView;
    }

    public static void setmRootView(View mRootView) {
        ViewerMainFragment.mRootView = mRootView;
    }

    public static LinearLayout getmStatusBottomBar() {
        return mStatusBottomBar;
    }

    public static void setmStatusBottomBar(LinearLayout mStatusBottomBar) {
        ViewerMainFragment.mStatusBottomBar = mStatusBottomBar;
    }

    public static FrameLayout getmBottomBar() {
        return mBottomBar;
    }

    public static void setmBottomBar(FrameLayout mBottomBar) {
        ViewerMainFragment.mBottomBar = mBottomBar;
    }

    public static LinearLayout getmRotationLayout() {
        return mRotationLayout;
    }

    public static void setmRotationLayout(LinearLayout mRotationLayout) {
        ViewerMainFragment.mRotationLayout = mRotationLayout;
    }

    public static LinearLayout getmScaleLayout() {
        return mScaleLayout;
    }

    public static void setmScaleLayout(LinearLayout mScaleLayout) {
        ViewerMainFragment.mScaleLayout = mScaleLayout;
    }

    public static CustomEditableSlider getmRotationSlider() {
        return mRotationSlider;
    }

    public static void setmRotationSlider(CustomEditableSlider mRotationSlider) {
        ViewerMainFragment.mRotationSlider = mRotationSlider;
    }

    public static ImageView getmActionImage() {
        return mActionImage;
    }

    public static void setmActionImage(ImageView mActionImage) {
        ViewerMainFragment.mActionImage = mActionImage;
    }

    public static EditText getmScaleEditX() {
        return mScaleEditX;
    }

    public static void setmScaleEditX(EditText mScaleEditX) {
        ViewerMainFragment.mScaleEditX = mScaleEditX;
    }

    public static EditText getmScaleEditY() {
        return mScaleEditY;
    }

    public static void setmScaleEditY(EditText mScaleEditY) {
        ViewerMainFragment.mScaleEditY = mScaleEditY;
    }

    public static EditText getmScaleEditZ() {
        return mScaleEditZ;
    }

    public static void setmScaleEditZ(EditText mScaleEditZ) {
        ViewerMainFragment.mScaleEditZ = mScaleEditZ;
    }

    public static ImageButton getmUniformScale() {
        return mUniformScale;
    }

    public static void setmUniformScale(ImageButton mUniformScale) {
        ViewerMainFragment.mUniformScale = mUniformScale;
    }

    public static ScaleChangeListener getmTextWatcherX() {
        return mTextWatcherX;
    }

    public static void setmTextWatcherX(ScaleChangeListener mTextWatcherX) {
        ViewerMainFragment.mTextWatcherX = mTextWatcherX;
    }

    public static ScaleChangeListener getmTextWatcherY() {
        return mTextWatcherY;
    }

    public static void setmTextWatcherY(ScaleChangeListener mTextWatcherY) {
        ViewerMainFragment.mTextWatcherY = mTextWatcherY;
    }

    public static ScaleChangeListener getmTextWatcherZ() {
        return mTextWatcherZ;
    }

    public static void setmTextWatcherZ(ScaleChangeListener mTextWatcherZ) {
        ViewerMainFragment.mTextWatcherZ = mTextWatcherZ;
    }

    /**
     * ****************************************************************************
     */
    public static SlicingHandler getmSlicingHandler() {
        return mSlicingHandler;
    }

    public static void setmSlicingHandler(SlicingHandler mSlicingHandler) {
        ViewerMainFragment.mSlicingHandler = mSlicingHandler;
    }

    public static SidePanelHandler getmSidePanelHandler() {
        return mSidePanelHandler;
    }

    public static void setmSidePanelHandler(SidePanelHandler mSidePanelHandler) {
        ViewerMainFragment.mSidePanelHandler = mSidePanelHandler;
    }

    public static int[] getmCurrentPlate() {
        return mCurrentPlate;
    }

    public static void setmCurrentPlate(int[] mCurrentPlate) {
        ViewerMainFragment.mCurrentPlate = mCurrentPlate;
    }

    public static LinearLayout getmSizeText() {
        return mSizeText;
    }

    public static void setmSizeText(LinearLayout mSizeText) {
        ViewerMainFragment.mSizeText = mSizeText;
    }

    public static int getmCurrentAxis() {
        return mCurrentAxis;
    }

    public static void setmCurrentAxis(int mCurrentAxis) {
        ViewerMainFragment.mCurrentAxis = mCurrentAxis;
    }

    public static PopupWindow getmActionModePopupWindow() {
        return mActionModePopupWindow;
    }

    public static void setmActionModePopupWindow(PopupWindow mActionModePopupWindow) {
        ViewerMainFragment.mActionModePopupWindow = mActionModePopupWindow;
    }

    public static PopupWindow getmCurrentActionPopupWindow() {
        return mCurrentActionPopupWindow;
    }

    public static void setmCurrentActionPopupWindow(PopupWindow mCurrentActionPopupWindow) {
        ViewerMainFragment.mCurrentActionPopupWindow = mCurrentActionPopupWindow;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retain instance to keep the Fragment from destroying itself
        setRetainInstance(true);

        setmSlicingHandler(new SlicingHandler(getActivity()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Reference to View
        setmRootView(null);

        //If is not new
        if (savedInstanceState == null) {

            //Show custom option menu
            setHasOptionsMenu(true);

            //Inflate the fragment
            setmRootView(inflater.inflate(R.layout.print_panel_main,
                    container, false));

            setmContext(getActivity());


            //Register receiver
            getmContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            initUIElements();

//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            //Init slicing elements
            setmSidePanelHandler(new SidePanelHandler(getmSlicingHandler(), getActivity(), getmRootView()));
            setmCurrentPlate(new int[]{WitboxFaces.WITBOX_LONG, WitboxFaces.WITBOX_WITDH, WitboxFaces.WITBOX_HEIGHT});

            setmSurface(new ViewerSurfaceView(getmContext(), getmDataList(), NORMAL, DONT_SNAPSHOT, getmSlicingHandler()));
            draw();

            //Hide the action bar when editing the scale of the model
            getmRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    Rect r = new Rect();
                    getmRootView().getWindowVisibleDisplayFrame(r);

                    if (getmSurface().getEditionMode() == ViewerSurfaceView.SCALED_EDITION_MODE){

                        int[] location = new int[2];
                        int heightDiff = getmRootView().getRootView().getHeight() - (r.bottom - r.top);

                        if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...

                            if (!isKeyboardShown) {
                                isKeyboardShown = true;
                                getmActionModePopupWindow().getContentView().getLocationInWindow(location);

                                if (Build.VERSION.SDK_INT >= 19)
                                    getmActionModePopupWindow().update(location[0], location[1] - MENU_HIDE_OFFSET_SMALL);
                                else  getmActionModePopupWindow().update(location[0], location[1] + MENU_HIDE_OFFSET_BIG);
                            }
                        } else {
                            if (isKeyboardShown) {
                                isKeyboardShown = false;
                                getmActionModePopupWindow().getContentView().getLocationInWindow(location);

                                if (Build.VERSION.SDK_INT >= 19)
                                    getmActionModePopupWindow().update(location[0], location[1] + MENU_HIDE_OFFSET_SMALL);
                                else  getmActionModePopupWindow().update(location[0], location[1] - MENU_HIDE_OFFSET_BIG);

                            }

                        }
                    }

                }
            });
        }

        return getmRootView();

    }

    public static void resetWhenCancel() {


        //Crashes on printview
        try {
            getmDataList().remove(getmDataList().size() - 1);
            getmSurface().requestRender();

            mCurrentViewMode = NORMAL;
            getmSurface().configViewMode(mCurrentViewMode);
            setmFile(new File(getmSlicingHandler().getLastReference()));

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    /**
     * ********************** UI ELEMENTS *******************************
     */

    private void initUIElements() {


        /*final CheckBox expandPanelButton = (CheckBox) mRootView.findViewById(R.id.expand_button_checkbox);
        expandPanelButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Expand/collapse the expandable panel
                if (isChecked) ExpandCollapseAnimation.collapse(expandablePanel, mSettingsPanelMinHeight);
                else ExpandCollapseAnimation.expand(expandablePanel);
            }
        });*/

        //Set elements to handle the model
        setmSeekBar((SeekBar) getmRootView().findViewById(R.id.barLayer));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            getmSeekBar().getThumb().mutate().setAlpha(0);
        getmSeekBar().setVisibility(View.INVISIBLE);

        //Undo button bar

        setmLayout((FrameLayout) getmRootView().findViewById(R.id.viewer_container_framelayout));

        setmVisibilityModeButton((ImageButton) getmRootView().findViewById(R.id.visibility_button));
        getmVisibilityModeButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                showVisibilityPopUpMenu();
            }
        });

        getmSeekBar().setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getmDataList().get(0).setActualLayer(progress);
                getmSurface().requestRender();
            }
        });


        /*****************************
         * EXTRA
         *****************************/
        setmProgress((ProgressBar) getmRootView().findViewById(R.id.progress_bar));
        getmProgress().setVisibility(View.GONE);
        setmSizeText((LinearLayout) getmRootView().findViewById(R.id.axis_info_layout));
        setmActionImage((ImageView) getmRootView().findViewById(R.id.print_panel_bar_action_image));


        setmRotationSlider((CustomEditableSlider) getmRootView().findViewById(R.id.print_panel_slider));
        getmRotationSlider().setValue(12);
        getmRotationSlider().setShownValue(0);
        getmRotationSlider().setMax(24);
        getmRotationSlider().setShowNumberIndicator(true);
        getmRotationSlider().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){

                    case MotionEvent.ACTION_DOWN:

                        break;

                    case MotionEvent.ACTION_UP:

                        if (getmSurface().getEditionMode() == ViewerSurfaceView.ROTATION_EDITION_MODE)
                            getmSurface().refreshRotatedObject();

                        break;
                }


                return false;
            }
        });
        getmRotationSlider().setOnValueChangedListener(new CustomEditableSlider.OnValueChangedListener() {

            boolean lock = false;


            @Override
            public void onValueChanged(int i) {

                //Calculation on a 12 point seekbar
                float newAngle = (i - 12) * POSITIVE_ANGLE;

                getmRotationSlider().setShownValue((int)newAngle);

                try {


                    if (!lock) {

                        switch (getmCurrentAxis()) {

                            case 0:
                                getmSurface().rotateAngleAxisX(newAngle);
                                break;
                            case 1:
                                getmSurface().rotateAngleAxisY(newAngle);
                                break;
                            case 2:
                                getmSurface().rotateAngleAxisZ(newAngle);
                                break;
                            default:
                                return;

                        }

                    }

                    getmSurface().requestRender();


                } catch (ArrayIndexOutOfBoundsException e) {

                    e.printStackTrace();
                }


            }
        });

        setmStatusBottomBar((LinearLayout) getmRootView().findViewById(R.id.model_status_bottom_bar));
        setmRotationLayout((LinearLayout) getmRootView().findViewById(R.id.model_button_rotate_bar_linearlayout));
        setmScaleLayout((LinearLayout) getmRootView().findViewById(R.id.model_button_scale_bar_linearlayout));

        setmTextWatcherX(new ScaleChangeListener(0));
        setmTextWatcherY(new ScaleChangeListener(1));
        setmTextWatcherZ(new ScaleChangeListener(2));

        setmScaleEditX((EditText) getmScaleLayout().findViewById(R.id.scale_bar_x_edittext));
        setmScaleEditY((EditText) getmScaleLayout().findViewById(R.id.scale_bar_y_edittext));
        setmScaleEditZ((EditText) getmScaleLayout().findViewById(R.id.scale_bar_z_edittext));
        setmUniformScale((ImageButton) getmScaleLayout().findViewById(R.id.scale_uniform_button));
        getmUniformScale().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getmUniformScale().isSelected()){
                    getmUniformScale().setSelected(false);
                } else {
                    getmUniformScale().setSelected(true);
                }


            }
        });
        getmUniformScale().setSelected(true);

        getmScaleEditX().addTextChangedListener(getmTextWatcherX());
        getmScaleEditY().addTextChangedListener(getmTextWatcherY());
        getmScaleEditZ().addTextChangedListener(getmTextWatcherZ());

        getmStatusBottomBar().setVisibility(View.VISIBLE);
        setmBottomBar((FrameLayout) getmRootView().findViewById(R.id.bottom_bar));
        getmBottomBar().setVisibility(View.INVISIBLE);
        setmCurrentAxis(-1);

    }

    /**
     * Change the current rotation axis and update the text accordingly
     * <p/>
     * Alberto
     */
    public static void changeCurrentAxis(int currentAxis) {

        setmCurrentAxis(currentAxis);

        float currentAngle = 12;

        switch (getmCurrentAxis()) {

            case 0:
                getmRotationSlider().setBackgroundColor(Color.GREEN);
                break;

            case 1:
                getmRotationSlider().setBackgroundColor(Color.RED);
                break;
            case 2:
                getmRotationSlider().setBackgroundColor(Color.BLUE);
                break;
            default:
                getmRotationSlider().setBackgroundColor(Color.TRANSPARENT);
                break;

        }

        getmSurface().setRendererAxis(getmCurrentAxis());

        getmRotationSlider().setValue((int) currentAngle);

    }


    /**
     * *************************************************************************
     */

    public static void initSeekBar(int max) {
        getmSeekBar().setMax(max);
        getmSeekBar().setProgress(max);
    }

    public static void configureProgressState(int v) {
        if (v == View.GONE) getmSurface().requestRender();
        else if (v == View.VISIBLE) getmProgress().bringToFront();

        getmProgress().setVisibility(v);
    }


    /**
     * ********************** OPTIONS MENU *******************************
     */
    //Create option menu and inflate viewer menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.print_panel_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()) {

            case R.id.viewer_open:
                FileBrowser.openFileBrowser(getActivity(), FileBrowser.VIEWER, getString(R.string.choose_file), ".stl", ".gcode");
                return true;

            case R.id.viewer_save:
                saveNewProject();
                return true;

            case R.id.viewer_restore:
                optionRestoreView();
                return true;

            case R.id.viewer_clean:

                optionClean();

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * ********************** FILE MANAGEMENT *******************************
     */


    /**
     * Restore the original view and discard the modifications by clearing the data list
     */
    public void optionRestoreView() {


        if (getmDataList().size() > 0) {
            String pathStl = getmDataList().get(0).getPathFile();
            getmDataList().clear();

            openFile(pathStl);
        }


    }

    /**
     * Clean the print panel and delete all references
     */
    public static void optionClean() {

        //Delete slicing reference
        //DatabaseController.handlePreference("Slicing", "Last", null, false);

        getmDataList().clear();
        setmFile(null);
        
        if (getmSlicingHandler() !=null){

            getmSlicingHandler().setOriginalProject(null);
            getmSlicingHandler().setLastReference(null);
            getmSeekBar().setVisibility(View.INVISIBLE);
            getmSurface().requestRender();
            showProgressBar(0,0);
        }


    }

    /**
     * Open a dialog if it's a GCODE to warn the user about unsaved data loss
     *
     * @param filePath
     */
    public static void openFileDialog(final String filePath) {

        if (LibraryController.hasExtension(0, filePath)) {

            if (!StlFile.checkFileSize(new File(filePath), getmContext())) {
                new MaterialDialog.Builder(getmContext())
                        .title(R.string.warning)
                        .content(R.string.viewer_file_size)
                        .negativeText(R.string.cancel)
                        .negativeColorRes(R.color.body_text_2)
                        .positiveText(R.string.ok)
                        .positiveColorRes(R.color.theme_accent_1)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                openFile(filePath);

                            }
                        })
                        .build()
                        .show();

            } else {
                openFile(filePath);
            }
        } else if (LibraryController.hasExtension(1, filePath)) {

            new MaterialDialog.Builder(getmContext())
                    .title(R.string.warning)
                    .content(R.string.viewer_open_gcode_dialog)
                    .negativeText(R.string.cancel)
                    .negativeColorRes(R.color.body_text_2)
                    .positiveText(R.string.ok)
                    .positiveColorRes(R.color.theme_accent_1)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            openFile(filePath);
                        }
                    })
                    .build()
                    .show();
        }


    }


    //Select the last object added
    public static void doPress(){

        getmSurface().doPress(getmDataList().size() - 1);

    }


    public static void openFile(String filePath) {
        DataStorage data = null;
        //Open the file
        if (LibraryController.hasExtension(0, filePath)) {

            data = new DataStorage();

            getmVisibilityModeButton().setVisibility(View.VISIBLE);
            setmFile(new File(filePath));
            StlFile.openStlFile(getmContext(), getmFile(), data, DONT_SNAPSHOT);
            mCurrentViewMode = NORMAL;

        } else if (LibraryController.hasExtension(1, filePath)) {

            data = new DataStorage();
            if (!filePath.contains("/temp")) {
                getmVisibilityModeButton().setVisibility(View.GONE);
                optionClean();
            }
            setmFile(new File(filePath));
            GcodeFile.openGcodeFile(getmContext(), getmFile(), data, DONT_SNAPSHOT);
            mCurrentViewMode = LAYER;

        }

        getmDataList().add(data);



        //Adding original project //TODO elsewhere?
        if (getmSlicingHandler() != null)
            if (getmSlicingHandler().getOriginalProject() == null){
                getmSlicingHandler().setOriginalProject(getmFile().getParentFile().getParent());
            } else {
                if (!getmFile().getAbsolutePath().contains("/temp")){
                    getmSlicingHandler().setOriginalProject(getmFile().getParentFile().getParent());
                }
            }




    }

    private void changeStlViews(int state) {

        //Handle the special mode: LAYER
        if (state == LAYER) {
            File tempFile = new File(LibraryController.getParentFolder() + "/temp/temp.gco");
            if (tempFile.exists()) {

                //It's the last file


            } else {
                Toast.makeText(getActivity(), R.string.viewer_slice_wait, Toast.LENGTH_SHORT).show();

            }
        }
        //Handle TRANSPARENT, NORMAL and OVERHANG modes
        else {
            if (getmFile() != null) {
                if (!getmFile().getPath().endsWith(".stl") && !getmFile().getPath().endsWith(".STL")) {


                    if (openStlFile()) {

                        mCurrentViewMode = state;


                    }
                    ;
                } else {

                    getmSurface().configViewMode(state);
                    mCurrentViewMode = state;
                }


            } else {
                Toast.makeText(getActivity(), R.string.viewer_toast_not_available_2, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean openStlFile() {

        //Name didn't work with new gcode creation so new stuff!
        //String name = mFile.getName().substring(0, mFile.getName().lastIndexOf('.'));

        String pathStl;

        if (getmSlicingHandler().getLastReference() != null) {

            pathStl = getmSlicingHandler().getLastReference();
            openFile(pathStl);

            return true;

        } else {

            //Here's the new stuff!
            pathStl = //LibraryController.getParentFolder().getAbsolutePath() + "/Files/" + name + "/_stl/";
                    getmFile().getParentFile().getParent() + "/_stl/";
            File f = new File(pathStl);

            //Only when it's a project
            if (f.isDirectory() && f.list().length > 0) {
                openFile(pathStl + f.list()[0]);

                return true;

            } else {
                Toast.makeText(getActivity(), R.string.devices_toast_no_stl, Toast.LENGTH_SHORT).show();

                return false;
            }
        }


    }

    public static void draw() {
        //Once the file has been opened, we need to refresh the data list. If we are opening a .gcode file, we need to ic_action_delete the previous files (.stl and .gcode)
        //If we are opening a .stl file, we need to ic_action_delete the previous file only if it was a .gcode file.
        //We have to do this here because user can cancel the opening of the file and the Print Panel would appear empty if we clear the data list.

        String filePath = "";
        if (getmFile() != null) filePath = getmFile().getAbsolutePath();

        if (LibraryController.hasExtension(0, filePath)) {
            if (getmDataList().size() > 1) {
                if (LibraryController.hasExtension(1, getmDataList().get(getmDataList().size() - 2).getPathFile())) {
                    getmDataList().remove(getmDataList().size() - 2);
                }
            }
            Geometry.relocateIfOverlaps(getmDataList());
            getmSeekBar().setVisibility(View.INVISIBLE);

        } else if (LibraryController.hasExtension(1, filePath)) {
            if (getmDataList().size() > 1)
                while (getmDataList().size() > 1) {
                    getmDataList().remove(0);
                }
            getmSeekBar().setVisibility(View.VISIBLE);
        }

        //Add the view
        getmLayout().removeAllViews();
        getmLayout().addView(getmSurface(), 0);
        getmLayout().addView(getmSeekBar(), 1);
        getmLayout().addView(getmSizeText(), 2);

//      mLayout.addView(mUndoButtonBar, 3);
//      mLayout.addView(mEditionLayout, 2);
    }

    /**
     * ********************** SAVE FILE *******************************
     */
    private void saveNewProject() {
        View createProjectDialog = LayoutInflater.from(getmContext()).inflate(R.layout.dialog_save_model, null);
        final EditText proyectNameText = (EditText) createProjectDialog.findViewById(R.id.model_name_textview);

        final RadioGroup radioGroup = (RadioGroup) createProjectDialog.findViewById(R.id.save_mode_radiogroup);

        proyectNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                proyectNameText.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }
        });

        String dialogTitle;

        if (getmFile() != null)
            dialogTitle = getString(R.string.save) + " - " + getmFile().getName();
        else
            dialogTitle = getString(R.string.save);

        final MaterialDialog.Builder createFolderDialog = new MaterialDialog.Builder(getActivity());
        createFolderDialog.title(dialogTitle)
                .customView(createProjectDialog, true)
                .positiveColorRes(R.color.theme_accent_1)
                .positiveText(R.string.save)
                .negativeColorRes(R.color.body_text_2)
                .negativeText(R.string.discard)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        int selected = radioGroup.getCheckedRadioButtonId();

                        switch (selected) {

                            case R.id.save_model_stl_checkbox:

                                if (getmFile() != null) {
                                    if (LibraryController.hasExtension(0, getmFile().getName())) {
                                        if (StlFile.checkIfNameExists(proyectNameText.getText().toString()))
                                            proyectNameText.setError(getmContext().getString(R.string.proyect_name_not_available));
                                        else {
                                            if (StlFile.saveModel(getmDataList(), proyectNameText.getText().toString(), null))
                                                dialog.dismiss();
                                            else {
                                                Toast.makeText(getmContext(), R.string.error_saving_invalid_model, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getmContext(), R.string.devices_toast_no_stl, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                } else {

                                    Toast.makeText(getmContext(), R.string.error_saving_invalid_model, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                                break;

                            case R.id.save_model_gcode_checkbox:

                                final File fileFrom = new File(LibraryController.getParentFolder() + "/temp/temp.gco");


                                //if there is a temporary sliced gcode
                                if (fileFrom.exists()) {

                                    //Get original project
                                    final File actualFile = new File(getmSlicingHandler().getOriginalProject());

                                    //Save gcode
                                    File fileTo = new File(actualFile + "/_gcode/" + proyectNameText.getText().toString().replace(" ", "_") + ".gcode");

                                    //Delete file if success
                                    try {
                                        fileCopy(fileFrom,fileTo);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if (getmFile().getName().equals(fileFrom.getName()))
                                        openFile(fileTo.getAbsolutePath());

                                    //if (fileFrom.delete()) {}


                                /**
                                 * Use an intent because it's an asynchronous static method without any reference (yet)
                                 */
                                Intent intent = new Intent("notify");
                                intent.putExtra("message", "Files");
                                LocalBroadcastManager.getInstance(getmContext()).sendBroadcast(intent);

                        }else{
                            Toast.makeText(getActivity(), R.string.viewer_slice_wait, Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();

                        break;

                        case R.id.save_model_overwrite_checkbox:

                        Toast.makeText(getActivity(), R.string.option_unavailable, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                        break;

                        default:

                        dialog.dismiss();

                        break;

                    }

                }

        @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.cancel();
                        dialog.dismiss();
                    }

                })
                .show();

    }

    //Copy a file to another location
    public void fileCopy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * ********************** SURFACE CONTROL *******************************
     */
    //This method will set the visibility of the surfaceview so it doesn't overlap
    //with the video grid view
    public void setSurfaceVisibility(int i) {

        if (getmSurface() != null) {
            switch (i) {
                case 0:
                    getmSurface().setVisibility(View.GONE);
                    break;
                case 1:
                    getmSurface().setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private static PopupWindow mActionModePopupWindow;
    private static PopupWindow mCurrentActionPopupWindow;

    /**
     * ********************** ACTION MODE *******************************
     */

    /**
     * Show a pop up window with the available actions of the item
     */
    public static void showActionModePopUpWindow() {

        hideCurrentActionPopUpWindow();

        getmSizeText().setVisibility(View.VISIBLE);

        if (getmActionModePopupWindow() == null) {

            //Get the content view of the pop up window
            final LinearLayout popupLayout = (LinearLayout) ((Activity) getmContext()).getLayoutInflater()
                    .inflate(R.layout.item_edit_popup_menu, null);
            popupLayout.measure(0, 0);

            //Set the behavior of the action buttons
            int imageButtonHeight = 0;
            for (int i = 0; i < popupLayout.getChildCount(); i++) {
                View v = popupLayout.getChildAt(i);
                if (v instanceof ImageButton) {
                    ImageButton ib = (ImageButton) v;
                    imageButtonHeight = ib.getMeasuredHeight();
                    ib.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onActionItemSelected((ImageButton) view);
                        }
                    });
                }
            }

            //Show the pop up window in the correct position
            int[] viewerContainerCoordinates = new int[2];
            getmLayout().getLocationOnScreen(viewerContainerCoordinates);
            int popupLayoutPadding = (int) getmContext().getResources().getDimensionPixelSize(R.dimen.content_padding_normal);
            int popupLayoutWidth = popupLayout.getMeasuredWidth();
            int popupLayoutHeight = popupLayout.getMeasuredHeight();
            final int popupLayoutX = viewerContainerCoordinates[0] + getmLayout().getWidth() - popupLayoutWidth;
            final int popupLayoutY = viewerContainerCoordinates[1] + imageButtonHeight + popupLayoutPadding;

            setmActionModePopupWindow((new CustomPopupWindow(popupLayout, popupLayoutWidth,
                    popupLayoutHeight, R.style.SlideRightAnimation).getPopupWindow()));

            getmActionModePopupWindow().showAtLocation(getmSurface(), Gravity.NO_GRAVITY,
                    popupLayoutX, popupLayoutY);

        }
    }

    /**
     * Hide the action mode pop up window
     */
    public static void hideActionModePopUpWindow() {
        if (getmActionModePopupWindow() != null) {
            getmActionModePopupWindow().dismiss();
            getmSurface().exitEditionMode();
            getmRotationLayout().setVisibility(View.GONE);
            getmScaleLayout().setVisibility(View.GONE);
            getmStatusBottomBar().setVisibility(View.VISIBLE);
            getmBottomBar().setVisibility(View.INVISIBLE);
            setmActionModePopupWindow(null);
            getmSurface().setRendererAxis(-1);
        }

        //Hide size text
        if (getmSizeText() != null)
            if (getmSizeText().getVisibility() == View.VISIBLE) getmSizeText().setVisibility(View.INVISIBLE);

        //hideCurrentActionPopUpWindow();
    }

    /**
     * Hide the current action pop up window if it is showing
     */
    public static void hideCurrentActionPopUpWindow() {
        if (getmCurrentActionPopupWindow() != null) {
            getmCurrentActionPopupWindow().dismiss();
            setmCurrentActionPopupWindow(null);
        }
        hideSoftKeyboard();
    }

    public static void hideSoftKeyboard() {
        try{
            InputMethodManager inputMethodManager = (InputMethodManager)  getmContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) getmContext()).getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e){

        }

    }

    /**
     * Perform the required action depending on the pressed button
     *
     * @param item Action button that has been pressed
     */
    public static void onActionItemSelected(final ImageButton item) {

        getmStatusBottomBar().setVisibility(View.VISIBLE);
        getmSurface().setRendererAxis(-1);
        getmRotationLayout().setVisibility(View.GONE);
        getmScaleLayout().setVisibility(View.GONE);
        getmBottomBar().setVisibility(View.INVISIBLE);
        getmSizeText().setVisibility(View.VISIBLE);

        selectActionButton(item.getId());

        switch (item.getId()) {
            case R.id.move_item_button:
                hideCurrentActionPopUpWindow();
                getmSurface().setEditionMode(ViewerSurfaceView.MOVE_EDITION_MODE);
                break;
            case R.id.rotate_item_button:

                if (getmCurrentActionPopupWindow() == null) {
                    final String[] actionButtonsValues = getmContext().getResources().getStringArray(R.array.rotate_model_values);
                    final TypedArray actionButtonsIcons = getmContext().getResources().obtainTypedArray(R.array.rotate_model_icons);
                    showHorizontalMenuPopUpWindow(item, actionButtonsValues, actionButtonsIcons,
                            null, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    changeCurrentAxis(Integer.parseInt(actionButtonsValues[position]));
                                    getmBottomBar().setVisibility(View.VISIBLE);
                                    getmRotationLayout().setVisibility(View.VISIBLE);
                                    getmSurface().setEditionMode(ViewerSurfaceView.ROTATION_EDITION_MODE);
                                    hideCurrentActionPopUpWindow();
                                    item.setImageResource(actionButtonsIcons.getResourceId(position, -1));
                                    getmActionImage().setImageDrawable(getmContext().getResources().getDrawable(actionButtonsIcons.getResourceId(position, -1)));
                                }
                            });
                } else {
                    hideCurrentActionPopUpWindow();
                }
                break;
            case R.id.scale_item_button:
                hideCurrentActionPopUpWindow();
                getmBottomBar().setVisibility(View.VISIBLE);
                getmScaleLayout().setVisibility(View.VISIBLE);
                getmSurface().setEditionMode(ViewerSurfaceView.SCALED_EDITION_MODE);
                getmActionImage().setImageDrawable(getmContext().getResources().getDrawable(R.drawable.ic_action_scale));
                displayModelSize(getmSurface().getObjectPresed());
                break;
                /*case R.id.mirror:
                    mSurface.setEditionMode(ViewerSurfaceView.MIRROR_EDITION_MODE);
                    mSurface.doMirror();

                    slicingCallback();
                    break;*/
            case R.id.multiply_item_button:
                hideCurrentActionPopUpWindow();
                showMultiplyDialog();
                break;
            case R.id.delete_item_button:
                hideCurrentActionPopUpWindow();
                getmSurface().deleteObject();
                hideActionModePopUpWindow();
                break;
        }

    }


    /**
     * Set the state of the selected action button
     *
     * @param selectedId Id of the action button that has been pressed
     */
    public static void selectActionButton(int selectedId) {

        if (getmActionModePopupWindow() != null) {
            //Get the content view of the pop up window
            final LinearLayout popupLayout = (LinearLayout) getmActionModePopupWindow().getContentView();

            //Set the behavior of the action buttons
            for (int i = 0; i < popupLayout.getChildCount(); i++) {
                View v = popupLayout.getChildAt(i);
                if (v instanceof ImageButton) {
                    ImageButton ib = (ImageButton) v;
                    if (ib.getId() == selectedId)
                        ib.setBackgroundDrawable(getmContext().getResources().getDrawable(R.drawable.oval_background_green));
                    else
                        ib.setBackgroundDrawable(getmContext().getResources().getDrawable(R.drawable.action_button_selector_dark));
                }
            }
        }
    }

    /**
     * Show a pop up window with the visibility options: Normal, overhang, transparent and layers.
     */
    public void showVisibilityPopUpMenu() {

        //Hide action mode pop up window to show the new menu
        hideActionModePopUpWindow();


        //Show a menu with the visibility options
        if (getmCurrentActionPopupWindow() == null) {
            final String[] actionButtonsValues = getmContext().getResources().getStringArray(R.array.models_visibility_values);
            final TypedArray actionButtonsIcons = getmContext().getResources().obtainTypedArray(R.array.models_visibility_icons);
            showHorizontalMenuPopUpWindow(getmVisibilityModeButton(), actionButtonsValues, actionButtonsIcons,
                    Integer.toString(mCurrentViewMode), new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            //Change the view mode of the model
                            changeStlViews(Integer.parseInt(actionButtonsValues[position]));
                            hideCurrentActionPopUpWindow();
                        }
                    });
        } else {
            hideCurrentActionPopUpWindow();
        }

    }

    /**
     * Show a pop up window with a horizontal list view as a content view
     */
    public static void showHorizontalMenuPopUpWindow(View currentView, String[] actionButtonsValues,
                                                     TypedArray actionButtonsIcons,
                                                     String selectedOption,
                                                     AdapterView.OnItemClickListener onItemClickListener) {

        HorizontalListView landscapeList = new HorizontalListView(getmContext(), null);
        ListIconPopupWindowAdapter listAdapter = new ListIconPopupWindowAdapter(getmContext(), actionButtonsValues, actionButtonsIcons, selectedOption);
        landscapeList.setOnItemClickListener(onItemClickListener);
        landscapeList.setAdapter(listAdapter);

        landscapeList.measure(0, 0);

        int popupLayoutHeight = 0;
        int popupLayoutWidth = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View mView = listAdapter.getView(i, null, landscapeList);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            popupLayoutHeight = mView.getMeasuredHeight();
            popupLayoutWidth += mView.getMeasuredWidth();
        }

        //Show the pop up window in the correct position
        int[] actionButtonCoordinates = new int[2];
        currentView.getLocationOnScreen(actionButtonCoordinates);
        int popupLayoutPadding = (int) getmContext().getResources().getDimensionPixelSize(R.dimen.content_padding_normal);
        final int popupLayoutX = actionButtonCoordinates[0] - popupLayoutWidth - popupLayoutPadding / 2;
        final int popupLayoutY = actionButtonCoordinates[1];

        setmCurrentActionPopupWindow((new CustomPopupWindow(landscapeList, popupLayoutWidth,
                popupLayoutHeight + popupLayoutPadding, R.style.SlideRightAnimation).getPopupWindow()));

        getmCurrentActionPopupWindow().showAtLocation(getmSurface(), Gravity.NO_GRAVITY, popupLayoutX, popupLayoutY);
    }

    /**
     * ********************** MULTIPLY ELEMENTS *******************************
     */

    public static void showMultiplyDialog() {
        View multiplyModelDialog = LayoutInflater.from(getmContext()).inflate(R.layout.dialog_multiply_model, null);
        final NumberPicker numPicker = (NumberPicker) multiplyModelDialog.findViewById(R.id.number_copies_numberpicker);
        numPicker.setMaxValue(10);
        numPicker.setMinValue(0);

        final int count = numPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numPicker)).setColor(getmContext().getResources().getColor(R.color.theme_primary_dark));
                    ((EditText) child).setTextColor(getmContext().getResources().getColor(R.color.theme_primary_dark));

                    Field[] pickerFields = NumberPicker.class.getDeclaredFields();
                    for (Field pf : pickerFields) {
                        if (pf.getName().equals("mSelectionDivider")) {
                            pf.setAccessible(true);
                            try {
                                pf.set(numPicker, getmContext().getResources().getDrawable(R.drawable.separation_line_horizontal));
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }

                    numPicker.invalidate();
                } catch (NoSuchFieldException e) {
                    //Log.w("setNumberPickerTextColor", e.toString());
                } catch (IllegalAccessException e) {
                    //Log.w("setNumberPickerTextColor", e.toString());
                } catch (IllegalArgumentException e) {
                    //Log.w("setNumberPickerTextColor", e.toString());
                }
            }
        }

        //Remove soft-input from number picker
        numPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        final MaterialDialog.Builder createFolderDialog = new MaterialDialog.Builder(getmContext());
        createFolderDialog.title(R.string.viewer_menu_multiply_title)
                .customView(multiplyModelDialog, true)
                .positiveColorRes(R.color.theme_accent_1)
                .positiveText(R.string.dialog_continue)
                .negativeColorRes(R.color.body_text_2)
                .negativeText(R.string.cancel)
                .autoDismiss(true)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        drawCopies(numPicker.getValue());
                    }
                })
                .show();

    }

    private static void drawCopies(int numCopies) {
        int model = getmSurface().getObjectPresed();
        int num = 0;

        while (num < numCopies) {
            final DataStorage newData = new DataStorage();
            newData.copyData(getmDataList().get(model));
            getmDataList().add(newData);

            /**
             * Check if the piece is out of the plate and stop multiplying
             */
            if (!Geometry.relocateIfOverlaps(getmDataList())) {

                Toast.makeText(getmContext(), R.string.viewer_multiply_error, Toast.LENGTH_LONG).show();
                getmDataList().remove(newData);
                break;

            }

            num++;
        }

        draw();
    }

/**
 * **************************** PROGRESS BAR FOR SLICING ******************************************
 */

    /**
     * Static method to show the progress bar by sending an integer when receiving data from the socket
     *
     * @param i either -1 to hide the progress bar, 0 to show an indefinite bar, or a normal integer
     */
    public static void showProgressBar(int status, int i) {


        if (getmRootView() !=null){


            ProgressBar pb = (ProgressBar) getmRootView().findViewById(R.id.progress_slice);
            TextView tv = (TextView) getmRootView().findViewById(R.id.viewer_text_progress_slice);
            TextView tve = (TextView) getmRootView().findViewById(R.id.viewer_text_estimated_time);
            TextView tve_title = (TextView) getmRootView().findViewById(R.id.viewer_estimated_time_textview);

            if ( getmSlicingHandler().getLastReference()!= null) {

                tve_title.setVisibility(View.VISIBLE);
                pb.setVisibility(View.VISIBLE);

                switch (status) {

                }

            }else {

                pb.setVisibility(View.INVISIBLE);
                tve_title.setVisibility(View.INVISIBLE);
                tv.setText(null);
                tve.setText(null);
                getmRootView().invalidate();



            }
        }



    }

    /**
     * Display model width, depth and height when touched
     */
    public static void displayModelSize(int position) {
        try {
            //TODO RANDOM CRASH ArrayIndexOutOfBoundsException
            DataStorage data = getmDataList().get(position);

            //Set point instead of comma
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(',');

            //Define new decimal format to display only 2 decimals
            DecimalFormat df = new DecimalFormat("##.##", otherSymbols);

            String width = df.format((data.getMaxX() - data.getMinX()));
            String depth = df.format((data.getMaxY() - data.getMinY()));
            String height = df.format((data.getMaxZ() - data.getMinZ()));

            //Display size of the model
            //mSizeText.setText("W = " + width + " mm / D = " + depth + " mm / H = " + height + " mm");
            //mSizeText.setText(String.format(mContext.getResources().getString(R.string.viewer_axis_info), Double.parseDouble(width), Double.parseDouble(depth), Double.parseDouble(height)));

            //Log.i("Scale","Vamos a petar " + width);
            ((TextView) getmSizeText().findViewById(R.id.print_panel_x_size)).setText(width);
            ((TextView) getmSizeText().findViewById(R.id.print_panel_y_size)).setText(depth);
            ((TextView) getmSizeText().findViewById(R.id.print_panel_z_size)).setText(height);

            if (getmScaleLayout().getVisibility() == View.VISIBLE){

                getmScaleEditX().removeTextChangedListener(getmTextWatcherX());
                getmScaleEditY().removeTextChangedListener(getmTextWatcherY());
                getmScaleEditZ().removeTextChangedListener(getmTextWatcherZ());

                getmScaleEditX().setText(width);
                getmScaleEditX().setSelection(getmScaleEditX().getText().length());
                getmScaleEditY().setText(depth);
                getmScaleEditY().setSelection(getmScaleEditY().getText().length());
                getmScaleEditZ().setText(height);
                getmScaleEditZ().setSelection(getmScaleEditZ().getText().length());

                getmScaleEditX().addTextChangedListener(getmTextWatcherX());
                getmScaleEditY().addTextChangedListener(getmTextWatcherY());
                getmScaleEditZ().addTextChangedListener(getmTextWatcherZ());
            }

        } catch (ArrayIndexOutOfBoundsException e) {

            e.printStackTrace();
        }


    }

    /**
     * Receives the "download complete" event asynchronously
     */
    public BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {



        }
    };

    /**
     * Notify the side panel adapters, check for null if they're not available yet (rare case)
     */
    public void notifyAdapter() {

        try {
            if (getmSidePanelHandler().profileAdapter != null)
                getmSidePanelHandler().profileAdapter.notifyDataSetChanged();

            getmSidePanelHandler().reloadProfileAdapter();

        } catch (NullPointerException e) {

            e.printStackTrace();
        }


    }

    //Refresh printers when the fragmetn is shown
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }


    /**
     * *********************************  SIDE PANEL *******************************************************
     */

    public static File getFile() {
        return getmFile();
    }

    public static int[] getCurrentPlate() {
        return getmCurrentPlate();
    }

    public static void changePlate(String resource) throws NullPointerException {

        JSONObject profile = ModelProfile.retrieveProfile(getmContext(), resource, ModelProfile.TYPE_P);

        try {
            JSONObject volume = profile.getJSONObject("volume");
            setmCurrentPlate(new int[]{volume.getInt("width") / 2, volume.getInt("depth") / 2, volume.getInt("height")});

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getmSurface().changePlate(getmCurrentPlate());
        getmSurface().requestRender();
    }

    public static void setSlicingPosition(float x, float y) {

        JSONObject position = new JSONObject();
        try {

            //mPreviousOffset = new Geometry.Point(x,y,0);

            position.put("x", (int) x + getmCurrentPlate()[0]);
            position.put("y", (int) y + getmCurrentPlate()[1]);

            getmSlicingHandler().setExtras("position", position);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /********************************* RESTORE PANEL *************************/

    public static void displayErrorInAxis(int axis){
/*
        if (mScaleLayout.getVisibility() == View.VISIBLE){
            switch (axis){

                case 0: mScaleEditX.setError(mContext.getResources().getString(R.string.viewer_error_bigger_plate,mCurrentPlate[0] * 2));
                    break;

                case 1: mScaleEditY.setError(mContext.getResources().getString(R.string.viewer_error_bigger_plate,mCurrentPlate[1] * 2));
                    break;

            }
        }
*/


    }


    private class ScaleChangeListener implements TextWatcher{

        int mAxis;

        private ScaleChangeListener(int axis){

            mAxis = axis;

        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            getmScaleEditX().setError(null);
            getmScaleEditY().setError(null);
            getmScaleEditZ().setError(null);

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            boolean valid = true;


            //Check decimals
           if (editable.toString().endsWith(".")){
               valid = false;

            }


            if (valid)
            try{
                switch (mAxis){

                    case 0:
                        getmSurface().doScale(Float.parseFloat(editable.toString()), 0, 0, getmUniformScale().isSelected());
                        break;

                    case 1:
                        getmSurface().doScale(0, Float.parseFloat(editable.toString()), 0, getmUniformScale().isSelected());
                        break;

                    case 2:
                        getmSurface().doScale(0, 0, Float.parseFloat(editable.toString()), getmUniformScale().isSelected());
                        break;

                }
            } catch (NumberFormatException e){

                e.printStackTrace();

            }



        }
    }


}
