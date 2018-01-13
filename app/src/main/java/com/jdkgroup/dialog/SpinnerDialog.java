package com.jdkgroup.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.bitcoinprice.adapter.spinner.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpinnerDialog extends Dialog {
    @BindView(R.id.tvCancel)
    AppCompatTextView tvCancel;
    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private Activity activity;
    private WindowManager.LayoutParams lp;
    private OnItemClick itmeSelecte;
    private String dialogTitle = "Select";

    private SpinnerAdapter spinnerAdapter;
    private ArrayList arr_list = new ArrayList<>();

    @OnClick(R.id.tvCancel)
    public void onViewClicked() {
        this.cancel();
    }

    public SpinnerDialog(Activity activity, OnItemClick itmeSelecte, List<?> arr_list) {
        super(activity);
        this.activity = activity;
        this.itmeSelecte = itmeSelecte;
        this.arr_list.addAll(arr_list);
    }

    public SpinnerDialog(Activity activity, String title, OnItemClick itmeSelecte, List<?> arr_list) {
        super(activity);
        this.activity = activity;
        this.dialogTitle = title;
        this.itmeSelecte = itmeSelecte;
        this.arr_list.addAll(arr_list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        setContentView(R.layout.custom_dialog);
        ButterKnife.bind(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(activity));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //recyclerview.addItemDecoration(new LineRecycleView(activity));

        tvTitle.setText(dialogTitle);

        spinnerAdapter = new SpinnerAdapter(activity, arr_list, new OnItemClick() {
            @Override
            public void selectedItem(Object object) {
                dismiss();
                if (itmeSelecte != null)
                    itmeSelecte.selectedItem(object);
            }
        });
        recyclerview.setAdapter(spinnerAdapter);

        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        final int height = display.getHeight();
        lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());

        lp.width = (int) (width - (width * 0.15));
        this.recyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerview.getHeight() >= ((int) (height - (height * 0.20))))
                    lp.height = (int) (height - (height * 0.20));
                else
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                getWindow().setAttributes(lp);
            }
        }, 10);
    }

    public interface OnItemClick {
        void selectedItem(Object object);
    }
}
