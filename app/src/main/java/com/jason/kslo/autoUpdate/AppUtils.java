package com.jason.kslo.autoUpdate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.jason.kslo.BuildConfig;

/**
 * @author feicien (ithcheng@gmail.com)
 * @since 2016-07-05 17:41
 */

public class AppUtils {

    @SuppressWarnings("deprecation")
    public static int getVersionCode(Context mContext) {
        if (mContext != null) {
                return BuildConfig.VERSION_CODE;
        }
        return 0;
    }

    public static String getVersionName(Context mContext) {
        if (mContext != null) {
                return BuildConfig.VERSION_NAME;
        }

        return "";
    }
}
