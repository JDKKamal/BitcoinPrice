package com.jdkgroup.connection;

import android.content.Context;

import com.jdkgroup.utils.PreferenceUtils;

public class TokenManagerImpl implements TokenManager {
    private Context context;

    public TokenManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getToken() {
        //TOKEN SET
        return "bearer ac65df43b1a76c8672f3f4da2c282f822a7bf39c40b47de7af930dc21110f0f4";
    }

    @Override
    public boolean hasToken() {
        //TOKEN CHECK
        if (PreferenceUtils.getInstance(context).isToken()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clearToken() {
      //TOKEN CLEAR
    }

    @Override
    public synchronized void refreshToken() {
      //TOKEN REFRESH
    }
}
