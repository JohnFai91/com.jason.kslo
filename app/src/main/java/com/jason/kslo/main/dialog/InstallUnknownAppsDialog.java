package com.jason.kslo.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.R;

public class InstallUnknownAppsDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getString(R.string.Thanks))
                .setMessage(getString(R.string.AllowPermission))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Go), (dialogInterface, i) -> {
                    Context getContext = MainActivity.getContextOfApplication();
                    Intent intent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        intent = new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:com.jason.kslo"));
                    }
                    assert intent != null;
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext.startActivity(intent);
                });
        return builder.create();
    }
}