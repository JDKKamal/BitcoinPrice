package com.jdkgroup.baseclass;

//TODO DEVELOPED BY KAMLESH LAKHANI
/* BASEAPPLICATION
   *  SET MULTIDEX, LOOGING, CALLINGGRAPHY (FONT)
   *  REALM (DATABASE)
*/

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.utils.Logging;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication baseApplication = null;

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        baseApplication = this;

        MultiDex.install(this);
        Logging.setDebugLogging(Logging.isDebugLogging());

        //Stetho.initializeWithDefaults(this);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(this.getString(R.string.regular_font))
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}