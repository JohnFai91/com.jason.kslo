package com.jason.kslo.crashActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.R;

import static com.jason.kslo.App.updateLanguage;

public final class customOnCrashActivity extends AppCompatActivity {

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is needed to avoid a crash if the developer has not specified
        //an app-level theme that extends Theme.AppCompat
        TypedArray a = obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (!a.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        }
        a.recycle();

        updateLanguage(this);
        setContentView(R.layout.activity_custom_activity_on_crash);

        errorConsists();

        //Close/restart button logic:
        //If a class if set, use restart.
        //Else, use close and just finish the app.
        //It is recommended that you follow this logic if implementing a custom error activity.
        Button restartButton = findViewById(R.id.customActivityOnCrash_error_activity_restart_button);

        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());

        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }

        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(R.string.restart_application);
            restartButton.setOnClickListener(v -> CustomActivityOnCrash.restartApplication(customOnCrashActivity.this, config));
        } else {
            restartButton.setOnClickListener(v -> CustomActivityOnCrash.closeApplication(customOnCrashActivity.this, config));
        }

        Button moreInfoButton = findViewById(R.id.customActivityOnCrash_error_activity_more_info_button);

        if (config.isShowErrorDetails()) {
            moreInfoButton.setOnClickListener(v -> {
                //We retrieve all the error data and show it

                AlertDialog dialog = new AlertDialog.Builder(customOnCrashActivity.this)
                        .setTitle(R.string.errorTitle)
                        .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(customOnCrashActivity.this, getIntent()))
                        .setNegativeButton(R.string.Cancel, null)
                        .setNeutralButton(R.string.Copy,
                                (dialog1, which) -> copyErrorToClipboard())
                        .show();
                TextView textView = dialog.findViewById(android.R.id.message);
                if (textView != null) {
                    textView.setTextSize(12);
                }
            });
        } else {
            moreInfoButton.setVisibility(View.GONE);
        }

        Integer defaultErrorActivityDrawableId = config.getErrorDrawable();
        ImageView errorImageView = findViewById(R.id.customActivityOnCrash_error_activity_image);

        if (defaultErrorActivityDrawableId != null) {
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), defaultErrorActivityDrawableId, getTheme()));
        }
    }

    private void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(customOnCrashActivity.this, getIntent());

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        RelativeLayout relativeLayout = findViewById(R.id.onCrashRelativeLayout);

        //Are there any devices without clipboard...?
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(getString(R.string.MoreInfo), errorInformation);
            clipboard.setPrimaryClip(clip);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Snackbar.make(relativeLayout, getString(R.string.copied_message_and_query), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.Go), view -> {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://signal.group/#CjQKIOvHZXoDBVldMPF9VqJJAt2JjSRiUptLsto_Rj-0CMR2EhC6SG6a08ubmRndGuP7bqKE"));
                            Toast.makeText(this,getString(R.string.copied_message),Toast.LENGTH_LONG).show();
                            startActivity(i);
                        })
                .setBackgroundTint(getColor(R.color.colorPrimaryDark))
                                .show();
            }
        }
    }
    private void errorConsists() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(customOnCrashActivity.this, getIntent());

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        RelativeLayout relativeLayout = findViewById(R.id.onCrashRelativeLayout);

        //Are there any devices without clipboard...?
        if (clipboard != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Snackbar.make(relativeLayout, getString(R.string.copied_message_and_consists), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.Send), view -> {
                            ClipData clip = ClipData.newPlainText(getString(R.string.MoreInfo), errorInformation);
                            clipboard.setPrimaryClip(clip);
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://signal.group/#CjQKIOvHZXoDBVldMPF9VqJJAt2JjSRiUptLsto_Rj-0CMR2EhC6SG6a08ubmRndGuP7bqKE"));
                            Toast.makeText(this,getString(R.string.copied_message),Toast.LENGTH_LONG).show();
                            startActivity(i);
                        })
                        .setBackgroundTint(getColor(R.color.colorPrimaryDark))
                        .show();
            }
        }
    }
}

