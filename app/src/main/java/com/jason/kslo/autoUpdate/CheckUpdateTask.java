package com.jason.kslo.autoUpdate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import com.jason.kslo.R;

import static com.jason.kslo.main.activity.MainActivity.*;


/**
 * @author feicien (ithcheng@gmail.com)
 * @since 2016-07-05 19:21
 */
@SuppressWarnings("deprecation")
@SuppressLint("StaticFieldLeak")
class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    @SuppressWarnings("deprecation")
    private ProgressDialog dialog;
    @SuppressLint("StaticFieldLeak")
    private final Context mContext;
    private final int mType;
    private final boolean mShowProgressDialog;
    private static final String url = Constants.UPDATE_URL;

    @SuppressWarnings("deprecation")
    CheckUpdateTask(Context context, int type, boolean showProgressDialog) {

        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;

    }


    @SuppressWarnings("deprecation")
    protected void onPreExecute() {
        if (mShowProgressDialog) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getString(R.string.android_auto_update_dialog_checking));
            dialog.show();
        }
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {

            JSONObject obj = new JSONObject(result);
            String updateMessage = obj.getString(Constants.APK_UPDATE_CONTENT);

            String abi;

            String apkUrl;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                abi = Build.CPU_ABI;
            } else {
                abi = Build.SUPPORTED_ABIS[0];
            }
            switch (abi){
                case "armeabi":
                case "armeabi-v7a":
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL_arm);
                    Log.d("CheckUpdate", "apk Architecture: " + "arm (32-bit)");
                    break;
                case "arm64-v8a":
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL_arm64);
                    Log.d("CheckUpdate", "apk Architecture: " + "arm64 (64-bit)");
                    break;
                case "x86":
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL_x86);
                    Log.d("CheckUpdate", "apk Architecture: " + "x86 (32-bit)");
                    break;
                case "x86_64": 
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL_x86_64);
                    Log.d("CheckUpdate", "apk Architecture: " + "x86_64 (64-bit)");
                    break;
                default:
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
                    Log.d("CheckUpdate", "apk Architecture: " + "Unknown (using universal apk)");
                    break;
            }

            int apkCode = obj.getInt(Constants.APK_VERSION_CODE);

            int versionCode = AppUtils.getVersionCode(mContext);

            if (apkCode > versionCode) {
                if (mType == Constants.TYPE_NOTIFICATION) {
                    new NotificationHelper(mContext).showNotification(updateMessage, apkUrl);
                } else if (mType == Constants.TYPE_DIALOG) {
                    showDialog(mContext, updateMessage, apkUrl);
                }
            } else if (mShowProgressDialog) {
                final Snackbar snackbar = Snackbar.make(getView(),
                        mContext.getString(R.string.android_auto_update_toast_no_new_update), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(getPrimary())
                        .setAction("OK", view -> snackbar.dismiss())
                        .show();
            }

        } catch (JSONException e) {
            Log.e(Constants.TAG, "parse json error");
        }
    }


    /**
     * Show dialog
     */
    @SuppressWarnings("unused")
    private void showDialog(Context context, String content, String apkUrl) {
            UpdateDialog.show(context,content,apkUrl);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected String doInBackground(Void... args) {
        return HttpUtils.get(url);
    }
}
