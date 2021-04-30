package com.jason.kslo.main.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.jason.kslo.BuildConfig;
import com.jason.kslo.R;
import com.jason.kslo.autoUpdate.AppUtils;
import com.jason.kslo.autoUpdate.UpdateChecker;
import com.jason.kslo.intro.SlideActivity;
import com.jason.kslo.main.activity.SettingsActivity;
import com.jason.kslo.main.changelog.ChangelogActivity;
import com.jason.kslo.main.dialog.InstallUnknownAppsDialog;
import com.jason.kslo.main.pdfView.download.PdfViewFeaturedNotice;
import com.jason.kslo.main.pdfView.download.PdfViewSchedule;
import com.jason.kslo.main.pdfView.PdfViewSchoolCal;

import static android.content.Context.MODE_PRIVATE;
import static com.jason.kslo.App.updateLanguage;


public class AboutFragment extends Fragment {
    View view;
    SharedPreferences pref;
    String locale,theme,versionVar;
    TextView version,Locale,ThemeText,desc;
    Button ChangeLogButton,SchoolCal,FeaturedNotice,HalfDaySchedule,Settings,Intro,CheckForUpdate,SourceCode,CrashApp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
            updateLanguage(requireContext());
            pref = requireContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            locale = pref.getString("lang","en");
            theme = pref.getString("theme","Follow System");
            versionVar = BuildConfig.APPLICATION_ID + " " + AppUtils.getVersionName(getContext());

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
            CrashApp = view.findViewById(R.id.CrashApp);
            desc = view.findViewById(R.id.description);

            Content content = new Content();
            content.run();
        return view;
        }

    void openInstallUnknownDialog(){
        InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
        installUnknownAppsDialog.setCancelable(false);
            installUnknownAppsDialog.show(requireActivity().getSupportFragmentManager(), "InstallUnknownAppsDialog");
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
                Intent intent = new Intent(getContext(), PdfViewSchedule.class);
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

            CheckForUpdate.setOnClickListener(view18 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!requireContext().getPackageManager().canRequestPackageInstalls()){
                        openInstallUnknownDialog();
                    }
                    else{
                        UpdateChecker.checkForDialog(getContext());
                    }
                }
            });

            CrashApp.setOnClickListener(view19 -> {
                    throw new RuntimeException(getString(R.string.YouPressedTheCrashAppButton));
            });
        }
    }
}
