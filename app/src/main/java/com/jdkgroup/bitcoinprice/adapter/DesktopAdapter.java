package com.jdkgroup.bitcoinprice.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.customview.recyclerview.BaseRecyclerView;
import com.jdkgroup.customview.recyclerview.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

public class DesktopAdapter extends BaseRecyclerView<String> {

    private LayoutInflater inflater;
    private List<String> listDesktop;

    private ItemListener listener;

    public DesktopAdapter(Context context, List<String> listDesktop) {
        this.inflater = LayoutInflater.from(context);
        this.listDesktop = listDesktop;
    }

    public void setOnListener(ItemListener listener) {
        this.listener = listener;
    }

    @Override
    protected String getItem(int position) {
        return listDesktop.get(position);
    }

    @Override
    public BaseViewHolder<String> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.itemview_desktop, parent, false));
    }

    @Override
    public int getItemCount() {
        return listDesktop.size();
    }

    class ViewHolder extends BaseViewHolder<String> {
        @BindView(R.id.appIvTitle)
        AppCompatImageView appIvTitle;
        @BindView(R.id.appTvTitle)
        AppCompatTextView appTvTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> listener.onClickDesktop(listDesktop.get(getAdapterPosition())));
        }

        @Override
        public void populateItem(String str) {
            appTvTitle.setText(str + "");
        }
    }

    public interface ItemListener {
        void onClickDesktop(final String str);
    }
}
