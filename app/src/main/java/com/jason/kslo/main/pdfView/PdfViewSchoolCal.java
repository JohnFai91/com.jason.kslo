package com.jason.kslo.main.pdfView;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.pm.ShortcutManagerCompat;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.jason.kslo.BuildConfig;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.SettingsActivity;

import java.io.*;

public class PdfViewSchoolCal extends AppCompatActivity {
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);
        setTitle(R.string.School_Cal);

        PDFView pdfView = findViewById(R.id.ViewPdf);

        pdfView.fromAsset("SchoolCal.pdf")
                .enableSwipe(true)
                .enableAnnotationRendering(false)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(2)
                .fitEachPage(true)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_files_with_shortcuts_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        InputStream inputStream;
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_action_bar_settings) {
            this.startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.menu_action_bar_add_to_home_screen) {
            addShortcut(getString(R.string.Calendar), R.mipmap.ic_launcher_calendar);
            return true;
        }
        if (item.getItemId() == R.id.menu_action_bar_share) {
            AssetManager am = getAssets();
            try {
                inputStream = am.open("SchoolCal.pdf");
            byte[] buffer = new byte[inputStream.available()];

            File targetFile = new File(getCacheDir() + "/" + "SchoolCal.pdf");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

            Uri path;
            path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                    new File(getCacheDir() + "/" + "SchoolCal.pdf"));
            Intent intent = ShareCompat.IntentBuilder.from(this)
                    .setType("application/pdf")
                    .setStream(path)
                    .setChooserTitle("Choose bar")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addShortcut(String shortcutName, int icon){
        ShortcutManager shortcutManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager = getSystemService(ShortcutManager.class);
        }

        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {

                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, shortcutName)
                        .setIcon(Icon.createWithResource(this, icon))
                        .setShortLabel(shortcutName)
                        .setIntent(new Intent(this, PdfViewSchoolCal.class).setAction(Intent.ACTION_MAIN))
                        .build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    shortcutManager.requestPinShortcut(pinShortcutInfo, null);
                }
            }
        }
    }
}