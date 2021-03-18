package com.jason.kslo.Main.Fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import com.jason.kslo.Main.AutoUpdate.AppUtils;
import com.jason.kslo.Main.AutoUpdate.UpdateChecker;
import com.jason.kslo.Main.Changelog.ChangelogActivity;
import com.jason.kslo.Main.Dialog.InstallUnknownAppsDialog;
import com.jason.kslo.Intro.SlideActivity;
import com.jason.kslo.Main.PdfView.PdfViewFeaturedNotice;
import com.jason.kslo.Main.PdfView.PdfViewHalfDaySchedule;
import com.jason.kslo.Main.PdfView.PdfViewSchoolCal;
import com.jason.kslo.Main.Activity.SettingsActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jason.kslo.*;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.jason.kslo.App.updateLanguage;


public class AboutFragment extends Fragment {
    View view;
    SharedPreferences pref;
    String locale,theme,versionVar;
    TextView version,Locale,ThemeText;
    Button ChangeLogButton,SchoolCal,FeaturedNotice,HalfDaySchedule,Settings,Intro,CheckForUpdate,SourceCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
            updateLanguage(getContext());
            pref = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", MODE_PRIVATE);
            locale = pref.getString("lang","");
            theme = pref.getString("theme","");
            versionVar = AppUtils.getVersionName(getContext());

            Locale = view.findViewById(R.id.Locale);
            ThemeText = view.findViewById(R.id.Theme);
            version = view.findViewById(R.id.Version);
            ChangeLogButton = view.findViewById(R.id.button_ChangeLog);
            SchoolCal = view.findViewById(R.id.SchoolCal);
            FeaturedNotice = view.findViewById(R.id.Featured_Notice);
            HalfDaySchedule = view.findViewById(R.id.Half_Day_Schedule);
            Settings = view.findViewById(R.id.Settings_button);
            Intro = view.findViewById(R.id.Intro_Button);
            SourceCode = view.findViewById(R.id.button_SourceCode);
            CheckForUpdate = view.findViewById(R.id.CheckForUpdate);

            Content content = new Content();
            content.run();
        return view;
        }

    void openInstallUnknownDialog(){
        InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
        installUnknownAppsDialog.setCancelable(false);
            installUnknownAppsDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "InstallUnknownAppsDialog");
    }

    class Content implements Runnable{
        @Override
        public void run() {
            version.setText(versionVar);
            Locale.setText(locale);
            ThemeText.setText(theme);

            ChangeLogButton.setOnClickListener(view1 -> {
                    Intent intent = new Intent(getContext(), ChangelogActivity.class);
                    startActivity(intent);
            });

            SchoolCal.setOnClickListener(view12 -> {
                Intent intent = new Intent(getContext(), PdfViewSchoolCal.class);
                startActivity(intent);

            });

            FeaturedNotice.setOnClickListener(view13 -> {
                Intent intent = new Intent(getContext(), PdfViewFeaturedNotice.class);
                startActivity(intent);
            });

            HalfDaySchedule.setOnClickListener(view14 -> {
                Intent intent = new Intent(getContext(), PdfViewHalfDaySchedule.class);
                startActivity(intent);
            });

            Settings.setOnClickListener(view15 -> {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            });

            Intro.setOnClickListener(view16 -> {
                Intent intent = new Intent(getContext(), SlideActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });

            SourceCode.setOnClickListener(view17 -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JohnFai91/com.jason.kslo"));
                startActivity(browserIntent);
            });

            CheckForUpdate.setOnClickListener(view17 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!Objects.requireNonNull(getContext()).getPackageManager().canRequestPackageInstalls()){
                        openInstallUnknownDialog();
                    }
                    else{
                        UpdateChecker.checkForDialog(getContext());
                    }
                }
            });
        }
    }
}
