package com.jdkgroup.baseclass;

//TODO DEVELOPED BY KAMLESH LAKHANI

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.interacter.disposablemanager.DisposableManager;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.utils.Logging;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.jdkgroup.utils.AppUtils.appUtilsInstance;

public abstract class BaseFragment extends Fragment {

    private Dialog progressDialog;
    private HashMap<String, String> params;
    private Calendar calendar;

    private LinearLayoutManager layoutManager;
    private int recyclerViewLinearLayout = 0, recyclerViewGridLayout = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        DisposableManager.dispose();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void isData(LinearLayout llDataPresent, LinearLayout llDataNo, int status) {
        switch (status) {
            case 0:
                llDataPresent.setVisibility(View.GONE);
                llDataNo.setVisibility(View.VISIBLE);
                break;

            case 1:
                llDataPresent.setVisibility(View.VISIBLE);
                llDataNo.setVisibility(View.GONE);
                break;
        }
    }

    protected void hideSoftKeyboard() {
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //SCREEN SIZE
    public static int[] getScreenSize(Activity activity) {
        Point size = new Point();
        WindowManager w = activity.getWindowManager();

        w.getDefaultDisplay().getSize(size);
        return new int[]{size.x, size.y};
    }

    protected void showKeyboard( AppCompatEditText appCompatEditText) {
        try {
            if (getActivity() != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(appCompatEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception e) {
            Logging.e("Exception on show " + e.toString());
        }
    }

    public void requestEditTextFocus(AppCompatEditText view) {
        view.requestFocus();
        showKeyboard(view);
    }

    /*TODO PROGRESSBAR*/
    public void showProgressDialog(boolean show) {
        //Show Progress bar here
        if (show) {
            showProgressBar();
        } else {
            hideProgressDialog();
        }
    }

    //SHOW PROGRESSBAR
    protected final void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new Dialog(getActivity());
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.progressbar_dialog, null, false);

        AppCompatImageView imageView1 = view.findViewById(R.id.appIvProgressBar);
        Animation a1 = AnimationUtils.loadAnimation(getActivity(), R.anim.progress_anim);
        a1.setDuration(1500);
        imageView1.startAnimation(a1);

        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(view);
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), android.R.color.transparent));
            //window.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        }
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    //HIDE PROGRESSBAR
    protected final void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showProgressToolBar(boolean show, View view) {
        // ((BaseActivity)getActivity()).showProgressToolBar(show,view);
    }

    public void onAuthenticationFailure(String msg) {
        // logoutUser(msg);
    }

    public static void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public HashMap<String, String> getDefaultParameter() {
        params = new HashMap<>();
        return params;
    }

    public HashMap<String, String> getDefaultParamWithIdAndToken() {
        params = getDefaultParameter();
        return params;
    }

    protected Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> entryIterator = map.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, String> pairs = entryIterator.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

    //TODO RECYCLERVIEW
    protected LinearLayoutManager setRecyclerView(RecyclerView recyclerView, int spanCount, int no) {
        switch (no) {
            case 0:
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                break;

            case 1:
                layoutManager = new GridLayoutManager(getActivity(), spanCount);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                break;
        }

        return layoutManager;
    }

    //TODO GSON
    protected String getToJson(List<?> alData) {
        return new Gson().toJson(alData);
    }

    protected String getToJsonClass(Object src) {
        return new Gson().toJson(src);
    }

    protected <T> T getFromJson(String str, Class<T> classType) {
        return new Gson().fromJson(str, classType);
    }

    protected <T> T fromJson(File file, Class<T> clazz) throws Exception {
        T data = new Gson().fromJson(new FileReader(file.getAbsoluteFile()), clazz);
        return data;
    }

    protected Gson switchGson(int param) {
        switch (param) {
            case 1:
                return new GsonBuilder().create();

            case 2: //FIRST CHARACTER UPPER CAMEL
                return new GsonBuilder().
                        disableHtmlEscaping().
                        setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).
                        setPrettyPrinting().serializeNulls().
                        create();
            default:
                break;
        }
        return null;
    }

    /* TODO LAUNCH FRAGMENT */
    protected <T> T getParcelable(String bundleName) {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(bundleName));
    }

    protected void launch(Class<?> classType, final Bundle bundle, int addFlag) {
        switch (addFlag) {
            case 1: //NO BUNDLE AND NO CLEAR
                startActivity(new Intent(getActivity(), classType).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;

            case 2: //NO BUNDLE AND CLEAR ALL HISTORY
                startActivity( new Intent(getActivity(), classType).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case 3: //BUNDLE AND NO CLEAR
                startActivity( new Intent(getActivity(), classType).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("bundle", bundle));
                break;

            case 4: //BUNDLE AND CLEAR ALL HISTORY
                startActivity( new Intent(getActivity(), classType).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("bundle", bundle));
                break;
        }
    }

    public InputFilter decimalPointAfterBeforeAmount(final int maxDigitsBeforeDecimalPoint, final int maxDigitsAfterDecimalPoint) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source.subSequence(start, end).toString());
                if (!builder.toString().matches("(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?")) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }
                return null;
            }
        };

        return filter;
    }

    protected void intentOpenBrowser(final String url) {
        if (isInternet()) {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
        } else {
            appUtilsInstance().showToast(getActivity(), getString(R.string.no_internet_message));
        }
    }

    protected boolean isInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnectedOrConnecting())) {
            appUtilsInstance().showToast(getActivity(), getActivity().getString(R.string.no_internet_message));
            return false;
        }
        return true;
    }

    protected UUID getUUIDRandom() {
        return UUID.randomUUID();
    }

    protected Date getCurrentDate() {
        return new Date();
    }

    private JSONObject mapConvertToJSON(final Map map) {
        JSONObject jsonobject = new JSONObject();
        try {
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();

                jsonobject.put(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
            }
        } catch (Exception ex) {
        }

        return jsonobject;
    }

    public void DatePicker(AppCompatTextView appCompatTextView) {
        calendar = Calendar.getInstance();
        int mYear, mMonth, mDay;
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> timePicker(dayOfMonth, (monthOfYear + 1), year, appCompatTextView), mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    protected void timePicker(int selectDayOfMonth, int selectMonthOfYear, int selectYear, AppCompatTextView appCompatTextView) {
        calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                (view, hourOfDay, minute) -> {
                    String ampm;
                    if (hourOfDay < 12 && hourOfDay >= 0) {
                        ampm = " AM";
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                    } else {
                        hourOfDay -= 12;
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        ampm = " PM";
                    }
                    appCompatTextView.setText(new StringBuilder().append(selectDayOfMonth + "-" + selectMonthOfYear + "-" + selectYear + " " + pad(hourOfDay)).append(":").append(pad(minute)) + ampm);
                }, mHour, mMinute, false);
        timePickerDialog.updateTime(mHour, mHour);
        timePickerDialog.show();
    }

    private String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    protected boolean IsHasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    //For take screenshot with status bar return Bitmap
    protected Bitmap nbGetScreenShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenSize(activity)[0];
        int height = getScreenSize(activity)[1];
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    //For take screenshot without status bar return Bitmap
    protected Bitmap nbGetScreenShotWithoutStatusBar() {
        View view = getActivity().getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        view.destroyDrawingCache();
        return Bitmap.createBitmap(bmp, 0, statusBarHeight, getScreenSize( getActivity())[0], getScreenSize( getActivity())[1] - statusBarHeight);
    }

    //For Get the screen dimensions
    private int[] getScreenSize() {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        return new int[]{size.x, size.y};
    }

    //DISABLE SCREEN CAPTURE
    protected void disableScreenshotFunctionality() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    protected String getDateDifference(Date startDate, Date endDate) {
        try {
            long different = endDate.getTime() - startDate.getTime();
            if (different <= 0) {
                return "0";
            }
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            String mSec = "";
            if (elapsedSeconds <= 9)
                mSec = "0";
            return " 0" + elapsedMinutes + " : " + mSec + elapsedSeconds + "";
            // return elapsedMinutes + " min " + elapsedSeconds + " Sec";

        } catch (Exception e) {
            return "0";
        }
    }
}