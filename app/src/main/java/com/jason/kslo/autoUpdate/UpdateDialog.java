package com.jason.kslo.autoUpdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import com.jason.kslo.R;

class UpdateDialog {

    static void show(final Context context, String content, final String downloadUrl) {
        if (isContextValid(context)) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.android_auto_update_dialog_title)
                    .setMessage(content)
                    .setPositiveButton(R.string.android_auto_update_dialog_btn_download, (dialog, id) -> goToDownload(context, downloadUrl))

                    .setNegativeButton(R.string.android_auto_update_dialog_btn_cancel, (dialog, id) -> {
                    })
                    .setCancelable(true)
                    .show();
        }
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }


    private static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }
}
