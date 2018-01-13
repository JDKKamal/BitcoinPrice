package com.jdkgroup.bitcoinprice.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.customview.recyclerview.BaseRecyclerView;
import com.jdkgroup.customview.recyclerview.BaseViewHolder;
import com.jdkgroup.model.callapi.close.ModelBpiDetail;

import java.util.List;

import butterknife.BindView;

public class CloseAdapter extends BaseRecyclerView<ModelBpiDetail> {

    private LayoutInflater inflater;
    private List<ModelBpiDetail> listBpiDetail;

    public CloseAdapter(Context context, List<ModelBpiDetail> listBpiDetail) {
        this.inflater = LayoutInflater.from(context);
        this.listBpiDetail = listBpiDetail;
    }

    @Override
    protected ModelBpiDetail getItem(int position) {
        return listBpiDetail.get(position);
    }

    @Override
    public BaseViewHolder<ModelBpiDetail> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.itemview_close, parent, false));
    }

    @Override
    public int getItemCount() {
        return listBpiDetail.size();
    }

    class ViewHolder extends BaseViewHolder<ModelBpiDetail> {
        @BindView(R.id.appIvIndex)
        AppCompatTextView appIvIndex;
        @BindView(R.id.appTvRate)
        AppCompatTextView appTvRate;
        @BindView(R.id.appTvDate)
        AppCompatTextView appTvDate;

        public ViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void populateItem(ModelBpiDetail modelBpiDetail) {
            appIvIndex.setText(getAdapterPosition() + "");
            appTvRate.setText(modelBpiDetail.getRate() + "");
            appTvDate.setText(modelBpiDetail.getDate() + "");
        }
    }
}
