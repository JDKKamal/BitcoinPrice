package com.jdkgroup.view;

import com.jdkgroup.baseclass.BaseView;
import com.jdkgroup.model.callapi.close.MainClose;

public interface CloseView extends BaseView {
    void callClose(MainClose response);
}
