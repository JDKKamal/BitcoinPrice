package com.jdkgroup.presenter;

import com.jdkgroup.baseclass.BasePresenter;
import com.jdkgroup.interacter.InterActorCallback;
import com.jdkgroup.model.callapi.close.MainClose;
import com.jdkgroup.view.CloseView;

public class ClosePresenter extends BasePresenter<CloseView> {
    public void apiClose() {

            //TODO NETWORK CHECK
            /*if(hasInternet())
            {

            }*/

            getAppInteractor().callApiClose(getView().getActivity(), new InterActorCallback<MainClose>() {
                @Override
                public void onStart() {
                    getView().showProgressDialog(true);
                }

                @Override
                public void onResponse(MainClose response) {
                    getView().callClose(response);
                }

                @Override
                public void onFinish() {
                    getView().showProgressDialog(false);
                }

                @Override
                public void onError(String message) {
                    getView().onFailure(message);
                }

            });
    }
}