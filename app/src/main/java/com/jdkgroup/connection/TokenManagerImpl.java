package com.jdkgroup.connection;

import android.content.Context;

import static com.jdkgroup.utils.Preference.preferenceInstance;

public class TokenManagerImpl implements TokenManager {
    private Context context;

    public TokenManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getToken() {
        //TOKEN SET bearer ac65df43b1a76c8672f3f4da2c282f822a7bf39c40b47de7af930dc21110f0f4
        return preferenceInstance(context).getDeviceToken();
    }

    @Override
    public boolean hasToken() {
        //TOKEN CHECK
        if (preferenceInstance(context).isToken()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clearToken() {
        //TOKEN CLEAR
        preferenceInstance(context).setDeviceToken("");
    }

    @Override
    public synchronized void refreshToken() {
        //TOKEN REFRESH
    }
}
