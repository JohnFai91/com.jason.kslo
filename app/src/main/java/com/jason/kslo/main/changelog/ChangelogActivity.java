package com.jason.kslo.main.changelog;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.BuildConfig;
import com.jason.kslo.R;

import java.util.ArrayList;

public class ChangelogActivity extends AppCompatActivity {

    ParseAdapterForChangelog parseAdapterForChangelog;
    final ArrayList<ChangelogParseItem> parseItems = new ArrayList<>();
    View stateSeparator;
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.changelog);

        RecyclerView recyclerView = findViewById(R.id.changelog_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutVerticalManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutVerticalManager);

        parseAdapterForChangelog = new ParseAdapterForChangelog(parseItems, getApplicationContext());
        recyclerView.setAdapter(parseAdapterForChangelog);

        Runnable parseChangelog = new parseChangelog();
        parseChangelog.run();

        String updated = getIntent().getStringExtra("updated");
        if (updated != null && updated.equals("true")){
            Toast.makeText(this,getString(R.string.UpdatedSuccessfully),Toast.LENGTH_SHORT)
                    .show();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class parseChangelog implements Runnable{

        @Override
        public void run() {
            int versionCode = BuildConfig.VERSION_CODE;
            String version, descL, descR, separatorVisibility;

            for (int i = versionCode + 1; i > 0 ; i--) {
                if (i == 1){
                    version = "1.0.1 alpha\n" +
                            "(7-2-2021)\n" +
                            "(Initial release and start of alpha state)";

                    descL = getString(R.string.changelogDescLv_1_0_1_alpha);
                    descR = getString(R.string.changelogDescRv_1_0_1_alpha);
                    separatorVisibility = "Gone";
                }
                else if (i == 2){
                    version = "1.0.2 alpha\n" +
                            "(7-2-2021)\n" +
                            "(Code update)";

                    descL = getString(R.string.changelogDescLv_1_0_2_alpha);
                    descR = getString(R.string.changelogDescRv_1_0_2_alpha);
                    separatorVisibility = "Gone";
                }
                else if (i == 3) {
                    version = "1.0.3 alpha\n" +
                            "(11-2-2021)\n" +
                            "(UI and\n" +
                            " code update)";

                    descL = getString(R.string.changelogDescLv_1_0_3_alpha);
                    descR = getString(R.string.changelogDescRv_1_0_3_alpha);
                    separatorVisibility = "Gone";
                }
                else if (i == 4){
                    version = "1.0.4 alpha\n" +
                            "(19-2-2021)\n" +
                            "(UI update)";

                    descL = getString(R.string.changelogDescLv_1_0_4_alpha);
                    descR = getString(R.string.changelogDescRv_1_0_4_alpha);
                    separatorVisibility = "Gone";
                }
                else if (i == 5){

                    separatorVisibility = "Visible";
                    parseItems.add(new ChangelogParseItem("","",
                            getString(R.string.changelogDescAlpha),separatorVisibility));
                    separatorVisibility = "Gone";

                    version = "1.0.5-alpha\n" +
                            "(26-2-2021)\n" +
                            "(UI update)\n" +
                            "(End of alpha state)";

                    descL = getString(R.string.changelogDescLv_1_0_5_alpha);
                    descR = getString(R.string.changelogDescRv_1_0_5_alpha);
                }
                else if (i == 6){
                    version = "1.0.6-beta\n" +
                            "(Test state: 27-2-2021, Full state: 28-2-2021)\n" +
                            "(UI and code update)\n" +
                            "(Initial of beta state)";

                    descL = getString(R.string.changelogDescLv_1_0_6_beta);
                    descR = getString(R.string.changelogDescRv_1_0_6_beta);
                    separatorVisibility = "Gone";
                }
                else if (i == 7){
                    version = "1.0.7-beta\n" +
                            "(7-3-2021)\n" +
                            "(UI update)";

                    descL = getString(R.string.changelogDescLv_1_0_7_beta);
                    descR = getString(R.string.changelogDescRv_1_0_7_beta);
                    separatorVisibility = "Gone";
                }
                else if (i == 8){
                    version = "1.0.8-beta\n" +
                            "(18-3-2021)\n" +
                            "(Code + " +
                            "\nUI update)";

                    descL = getString(R.string.changelogDescLv_1_0_8_beta);
                    descR = getString(R.string.changelogDescRv_1_0_8_beta);
                    separatorVisibility = "Gone";
                }
                else if (i == 9){

                    separatorVisibility = "Gone";
                    version = "1.0.9-beta\n" +
                            "(Code + " +
                            "\nUI update)";
                    descL = getString(R.string.changelogDescLv_1_0_9_beta);
                    descR = getString(R.string.changelogDescRv_1_0_9_beta);

                }
                else if (i == 10) {

                    separatorVisibility = "Visible";
                    parseItems.add(new ChangelogParseItem("","",
                            getString(R.string.changelogDescStable),separatorVisibility));

                    separatorVisibility = "Visible";
                    parseItems.add(new ChangelogParseItem("","",
                            getString(R.string.changelogDescBeta),separatorVisibility));

                    separatorVisibility = "Gone";
                    version = "1.1.0-beta\n" +
                            "(Code + " +
                            "\nUI update)";
                    descL = getString(R.string.changelogDescLv_1_1_0_beta);
                    descR = getString(R.string.changelogDescRv_1_1_0_beta);
                }
                else if (i == versionCode + 1){

                    separatorVisibility = "Visible";

                    version = getString(R.string.Future);

                    descL = getString(R.string.changelogDescLFuture);
                    descR = getString(R.string.changelogDescRFuture);
                }
                else {
                    version = BuildConfig.VERSION_NAME;

                    descL = getString(R.string.changelogDescL);
                    descR = getString(R.string.changelogDescR);
                    separatorVisibility = "Gone";
                }
                parseItems.add(new ChangelogParseItem(descL,descR,version,separatorVisibility));
            }
        }
    }
}