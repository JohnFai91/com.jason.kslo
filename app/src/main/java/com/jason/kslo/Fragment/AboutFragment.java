package com.jason.kslo.Fragment;

import com.jason.kslo.Dialog.ChangelogDialog;
import com.jason.kslo.PdfView.PdfViewFeaturedNotice;
import com.jason.kslo.PdfView.PdfViewHalfDaySchedule;
import com.jason.kslo.PdfView.PdfViewSchoolCal;
import com.jason.kslo.Activity.SettingsActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View.OnClickListener;

import java.util.Locale;

import androidx.core.os.ConfigurationCompat;
import com.jason.kslo.*;


public class AboutFragment extends Fragment {
    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);

        TextView Locale = view.findViewById(R.id.Locale);
        Locale.setText(locale.getDisplayLanguage());

        Button ChangeLogbutton = (Button) view.findViewById(R.id.button_ChangeLog);
        ChangeLogbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();

            }
        });

        Button SchoolCal = (Button)view.findViewById(R.id.SchoolCal);
        SchoolCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PdfViewSchoolCal.class);
                startActivity(intent);

            }
        });

        Button FeaaturedNotice = (Button)view.findViewById(R.id.Featured_Notice);
        FeaaturedNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PdfViewFeaturedNotice.class);
                startActivity(intent);
            }
        });

        Button HalfDaySchedule = (Button)view.findViewById(R.id.Half_Day_Schedule);
        HalfDaySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PdfViewHalfDaySchedule.class);
                startActivity(intent);
            }
        });

        Button Settings = (Button)view.findViewById(R.id.Settings_button);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    void openDialog(){
        ChangelogDialog changelogDialog = new ChangelogDialog();
            changelogDialog.show(getActivity().getSupportFragmentManager(), "ChangelogDialog");
    }

}
