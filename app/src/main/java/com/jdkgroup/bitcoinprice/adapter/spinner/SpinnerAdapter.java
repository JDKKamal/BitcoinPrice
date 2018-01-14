package com.jdkgroup.bitcoinprice.adapter.spinner;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.dialog.SpinnerDialog;
import com.jdkgroup.model.supportedcurrencies.ModelCurrencyDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpinnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList alClear = new ArrayList<>();
    private ArrayList alList = new ArrayList<>();

    SpinnerDialog.OnItemClick itemSelecte;

    public SpinnerAdapter(Context context, ArrayList<?> alList, SpinnerDialog.OnItemClick itemSelecte) {
        this.context = context;
        this.alList = alList;
        this.alClear.addAll(alList);
        this.itemSelecte=itemSelecte;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_spinner, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return alList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.appTvTitle)
        AppCompatTextView appTvTitle;
        int pos;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.appTvTitle)
        public void onViewClicked() {
            if(itemSelecte != null)
                itemSelecte.selectedItem(alList.get(pos));
        }

        public void setData(int pos) {
            this.pos = pos;

            if (alList.get(pos) instanceof ModelCurrencyDetail) {
                ModelCurrencyDetail currencyDetail  = (ModelCurrencyDetail) alList.get(pos);
                appTvTitle.setText(currencyDetail.getCountry());
            }
           /* else if (arr_list.get(pos) instanceof State) {
                State model = (State) alList.get(pos);
                appTvTitle.setText(model.getState_name());
            } else if (arr_list.get(pos) instanceof Country) {
                Country countryModel = (Country) alList.get(pos);
                appTvTitle.setText(countryModel.getCountry_name());
            }*/

        }
    }

    public void filter(String text) {
        alClear.clear();
        if (text.isEmpty()) {
            alClear.addAll(alClear);
        } else {
            text = text.toLowerCase();

            if (alClear.get(0) instanceof ModelCurrencyDetail) {
                for (int i = 0; i < alClear.size(); i++) {
                    ModelCurrencyDetail countryModel = (ModelCurrencyDetail) alClear.get(i);
                    if (countryModel.getCurrency().toLowerCase().contains(text)) {
                        alList.add(countryModel);
                    }
                }
            } /*else if (list_clone.get(0) instanceof State) {
                for (int i = 0; i < list_clone.size(); i++) {
                    State stateModel = (State) alClear.get(i);
                    if (stateModel.getState_name().toLowerCase().contains(text)) {
                        alList.add(stateModel);
                    }
                }

            } else if (list_clone.get(0) instanceof Country) {
                for (int i = 0; i < list_clone.size(); i++) {
                    Country countryModel = (Country) alClear.get(i);
                    if (countryModel.getCountry_name().toLowerCase().contains(text)) {
                        alList.add(countryModel);
                    }
                }
            }*/
        }
        notifyDataSetChanged();
    }

}