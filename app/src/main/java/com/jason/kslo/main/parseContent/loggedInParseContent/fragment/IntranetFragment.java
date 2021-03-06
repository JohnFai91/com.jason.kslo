package com.jason.kslo.main.parseContent.loggedInParseContent.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.activity.LoginActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter.ParseAdapterForIntranet;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseItem.LoginParseItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IntranetFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    static ParseAdapterForIntranet parseAdapterForIntranet;
    final static ArrayList<LoginParseItem> loginParseItems = new ArrayList<>();

    static Map<String, String> Cookies;

    View view;
    ProgressBar progressBar, intranetProgress;
    TextView intranetTitleString, pageDesc;
    SwipeRefreshLayout pullToRefresh;
    RecyclerView recyclerView;
    Spinner IntranetFragmentTitle;
    FloatingActionButton goToTop, search;
    EditText searchEditText;

    SharedPreferences pref;
    static ArrayAdapter<? extends String> adapter;
    static int inboxSize, booksSize, binSize, IntranetPage, previousPage;
    final int[] size = {5};

    final static String LOGIN_FORM_URL = "https://www.hkmakslo.edu.hk/it-school//php/login_v5.php3?ran=";
    final static String LOGIN_ACTION_URL = "https://www.hkmakslo.edu.hk/it-school/php/login_do.php3";
    final static String IntranetUrl = "https://www.hkmakslo.edu.hk/it-school/php/intra/index.php3?folder=";
    static String firstInboxSize, firstBinSize,folder = "inbox", finalUsername, finalPw;
    static String Username, filePresent, Password, originPw, title, sender, date, detailUrl, time, booksCountReturn;

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_intranet, container, false);

        pref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String renewed = pref.getString("Renewed","");

        pullToRefresh = view.findViewById(R.id.loginRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            Content content = new Content();
            content.execute();
        });

        pullToRefresh.setColorSchemeColors(
                requireActivity().getResources().getColor(android.R.color.holo_blue_dark),
                requireActivity().getResources().getColor(android.R.color.holo_orange_dark),
                requireActivity().getResources().getColor(android.R.color.holo_green_dark),
                requireActivity().getResources().getColor(android.R.color.holo_red_dark)
        );

        Content content = new Content();
        content.execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void, Void, Void> {
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {

            pageDesc = view.findViewById(R.id.IntranetPageText);

            TextView showBtmMsg = view.findViewById(R.id.reachedBottomIntranet);
            showBtmMsg.setVisibility(View.GONE);

            pullToRefresh = view.findViewById(R.id.loginRefresh);
            pullToRefresh.setRefreshing(true);

            RelativeLayout loginBackground = view.findViewById(R.id.LoginBackground);

            Username = pref.getString("Username","");
            Password = pref.getString("Password","");

            originPw = Password;

            finalUsername = Username;
            finalUsername = Username.replaceAll("s","");
            finalPw = originPw;

            if (!TextUtils.isEmpty(Username)) {
                loginBackground.setVisibility(View.GONE);
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                Objects.requireNonNull(md).update(Password.getBytes());

                byte[] byteData = md.digest();

                StringBuilder sb = new StringBuilder();
                for (byte byteDatum : byteData)
                    sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));

                Password = sb.toString();
                Log.d("Md5 Converter", "Converted: " + Password);
            } else {
                loginBackground.setVisibility(View.VISIBLE);
                Button login = view.findViewById(R.id.login_button);
                login.setOnClickListener(view -> {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                });
            }

            progressBar = view.findViewById(R.id.loginProgress);
            progressBar.setVisibility(View.VISIBLE);

            intranetProgress = view.findViewById(R.id.IntranetProgressBar);
            intranetProgress.setVisibility(View.GONE);

            recyclerView = view.findViewById(R.id.IntranetRecycler);

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutHorizontalManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutHorizontalManager);

            parseAdapterForIntranet = new ParseAdapterForIntranet(loginParseItems, IntranetFragment.this.getContext());
            recyclerView.setAdapter(parseAdapterForIntranet);

            IntranetPage = 0;
            loginParseItems.clear();

            checkInternet();

            IntranetFragmentTitle = view.findViewById(R.id.IntranetFragmentTitle);

            IntranetFragmentTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    switch (position) {
                        case 1:
                            folder = "inbox";
                            IntranetPage = 0;
                            loginParseItems.clear();
                            Con con = new Con();
                            con.execute();
                            break;
                        case 2:
                            folder = "trash";
                            IntranetPage = 0;
                            loginParseItems.clear();
                            con = new Con();
                            con.execute();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseIntranet();
            try {
                Connection.Response loginForm = Jsoup.connect("https://lm.hkmakslo.edu.hk/Handlers/UserLogin.ashx?&sno=" +
                        finalUsername + "&pass=" + originPw)
                        .method(Connection.Method.GET)
                        .execute();
                Log.d("Intranet", "Username: " + finalUsername + " Pw: " + originPw);

                Document doc = Jsoup.connect("https://lm.hkmakslo.edu.hk/PrivatePages/_book_table.aspx?")
                        .cookies(loginForm.cookies())
                        .get();

                Elements bk = doc.select("a");

                booksSize = bk.size();

                int prevCount = 0;
                for (int i = 0; i < booksSize; i++) {
                    booksCountReturn = doc.select("span.due")
                            .eq(i)
                            .text();
                    booksCountReturn = booksCountReturn.replace(" 天後到期","");

                    Log.d("ParseLM", "Size: " + booksSize + booksCountReturn);

                }
                if (booksCountReturn != null) {
                    if (booksSize != 0) {
                        booksCountReturn = " (" + booksCountReturn + requireActivity().getString(R.string.DaysToDueDate) + ")";
                    }
                } else {
                    booksCountReturn = "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pageDesc.setText(getString(R.string.BorrowedBooks) + " " +
                    booksSize + " " + getString(R.string.Books) +
                    booksCountReturn);

            parseAdapterForIntranet.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            pullToRefresh.setRefreshing(false);

            searchEditText = view.findViewById(R.id.IntranetSearchEditText);
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    editTextReload(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            TextInputLayout editTextBackground = view.findViewById(R.id.IntranetSearchBackground);
            search = view.findViewById(R.id.searchInternet);
            search.setOnClickListener(view -> {
                if (editTextBackground.getVisibility() == View.VISIBLE) {
                    searchEditText.setText("");
                    editTextBackground.setVisibility(View.GONE);
                } else {
                    editTextBackground.setVisibility(View.VISIBLE);
                }
            });
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    goToTop = view.findViewById(R.id.goToTopIntranet);

                    goToTop.setOnClickListener(view -> {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                                .getLayoutManager();
                        assert linearLayoutManager != null;
                        recyclerView.setNestedScrollingEnabled(false);
                        linearLayoutManager.scrollToPositionWithOffset(0, 0);
                        recyclerView.setNestedScrollingEnabled(true);
                    });

                    if (!recyclerView.canScrollVertically(1)) {
                        Log.d("IntranetPages", "No. : " + IntranetPage);
                        goToTop.setVisibility(View.VISIBLE);

                        if (IntranetFragmentTitle.getSelectedItem().equals(0)) {
                            folder = "inbox";
                        } else if (IntranetFragmentTitle.getSelectedItem().equals(1)) {
                            folder = "trash";
                        }

                        Con con = new Con();
                        con.execute();

                    }
                }
            });

            String[] folders = {
                    requireActivity().getString(R.string.Intranet) + ": " +
                            requireActivity().getString(R.string.IntranetMail) + " (" + firstInboxSize + ")",
                    requireActivity().getString(R.string.IntranetMail) + " (" + firstInboxSize + ")",
                    requireActivity().getString(R.string.RecyclingBin) + " (" + firstBinSize + ")"
            };
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, folders);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            IntranetFragmentTitle.setAdapter(adapter);

            filter(searchEditText.getText().toString());
        }
    }

    private void editTextReload(CharSequence editable) {
        if (editable != "") {
            for (int i = 0; i < size[0]; i++) {
                progressBar.setVisibility(View.VISIBLE);
                Con con = new Con();
                con.execute();
                filter(editable.toString());
                progressBar.setVisibility(View.GONE);
                Snackbar.make(MainActivity.getView(), R.string.FinishedSearching, Snackbar.LENGTH_LONG)
                        .setAction(R.string.Yes, view -> {
                            if (!(size[0] + 3 > inboxSize)) {
                                size[0] = size[0] + 3;
                                editTextReload(editable);
                            } else {
                                Snackbar snackbar = Snackbar.make(view, R.string.SearchComplete, BaseTransientBottomBar.LENGTH_LONG);
                                snackbar.setAction("OK", view1 -> snackbar.dismiss());
                            }
                        }).show();
            }
        }
    }

    private void filter(String text) {
        ArrayList<LoginParseItem> filteredList = new ArrayList<>();

        for (LoginParseItem item : loginParseItems) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase()) || item.getSender().contains(text)) {
                filteredList.add(item);
            }
        }

        parseAdapterForIntranet.filterList(filteredList);
    }

    @SuppressLint("StaticFieldLeak")
    private class Con extends AsyncTask<Void, Void, Void> {
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String selected = requireActivity().getString(R.string.IntranetMail);
            if (folder.equals("inbox")) {
                selected = requireActivity().getString(R.string.IntranetMail) +  " (" + firstInboxSize + ")";
            } else if (folder.equals("trash")) {
                selected = requireActivity().getString(R.string.RecyclingBin) +  " (" + firstBinSize + ")";
            }

            String[] folders = {
                    requireActivity().getString(R.string.Intranet) + ": " + selected,
                    requireActivity().getString(R.string.IntranetMail) + " (" + firstInboxSize + ")",
                    requireActivity().getString(R.string.RecyclingBin) + " (" + firstBinSize + ")"
            };
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, folders);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            IntranetFragmentTitle.setAdapter(adapter);

            intranetProgress.setVisibility(View.VISIBLE);

            pageDesc.setText(getString(R.string.BorrowedBooks) + " " +
                    LoginFragment.getSize() + " " + getString(R.string.Books) +
                    LoginFragment.getCountReturn());
        }

        Boolean btmMsg;

        @Override
        protected Void doInBackground(Void... voids) {

            int size = 1;
            if (folder.equals("inbox")) {
                size = inboxSize;
            } else if (folder.equals("trash")) {
                size = binSize;
            }
            if (IntranetPage < size) {
                parseIntranet();
                btmMsg = false;
            } else {
                btmMsg = true;
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            parseAdapterForIntranet.notifyDataSetChanged();
            intranetProgress.setVisibility(View.GONE);

            if (btmMsg.equals(true)) {
                runOnUiThread run = new runOnUiThread();
                run.run();
            } else {
                if (IntranetPage > previousPage) {
                    filter(searchEditText.getText().toString());
                    previousPage = IntranetPage;
                }
            }
            btmMsg = false;
            IntranetPage = IntranetPage + 1;
        }
    }

    private class runOnUiThread implements Runnable{

        @Override
        public void run() {

            TextView showBtmMsg = view.findViewById(R.id.reachedBottomIntranet);
            showBtmMsg.startAnimation(AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in));
            showBtmMsg.setVisibility(View.VISIBLE);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if(recyclerView.canScrollVertically(1)) {
                        showBtmMsg.startAnimation(AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out));
                        showBtmMsg.setVisibility(View.GONE);
                        showBtmMsg.clearAnimation();
                    }
                }
            });
        }
    }

    private static void parseIntranet(){
        //parse Intranet
        try {

            Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                    .method(Connection.Method.GET)
                    .execute();

            Map<String, String> formData = new HashMap<>();
            formData.put("userloginid",Username);
            formData.put("password",Password);
            formData.put("FakePassword", "登入密碼 / Password");
            formData.put("language","zh-HK");

            Connection.Response response = Jsoup.connect(LOGIN_ACTION_URL)
                    .data(formData)
                    .cookies(loginForm.cookies())
                    .execute();
            Cookies = response.cookies();

            Document document = Jsoup.connect(IntranetUrl + folder + "&page=" + IntranetPage)
                    .cookies(response.cookies())
                    .get();
            Document inboxDocument = Jsoup.connect(IntranetUrl + "inbox" + "&page=" + IntranetPage)
                    .cookies(response.cookies())
                    .get();
            Document binDocument = Jsoup.connect(IntranetUrl + "trash" + "&page=" + IntranetPage)
                    .cookies(response.cookies())
                    .get();

            Elements table = document.select("TR");
            Elements inboxTable = inboxDocument.select("TR");
            Elements binTable = binDocument.select("TR");

            int i;
            int dateCount = 0, senderCount = 24, detailUrlCount = 15, fileCount = 26, readCount = 27, sizeCount = 30;

            firstInboxSize = inboxTable.select("option")
                    .eq(0)
                    .text();
            firstBinSize = binTable.select("option")
                    .eq(3)
                    .text();

            firstInboxSize = firstInboxSize.replaceAll(".+/","");
            firstInboxSize = firstInboxSize.replace(")","");
            inboxSize = (int) Math.ceil(Float.parseFloat(firstInboxSize)/20);

            firstBinSize = firstBinSize.replaceAll(".+/","");
            firstBinSize = firstBinSize.replace(")","");
            firstBinSize = firstBinSize.replace("回收筒(","");
            binSize = (int) Math.ceil(Float.parseFloat(firstBinSize)/20);

            for (i = 0; i < 20; i++) {


                title = table.select("A")
                        .eq(i + 9)
                        .text();

                sender = document
                        .select("font")
                        .eq(senderCount)
                        .text();

                date = document
                        .select("nobr")
                        .eq(dateCount)
                        .text();

                time = document
                        .select("nobr")
                        .eq(dateCount + 1)
                        .text();

                detailUrl = document.select("A")
                        .eq(detailUrlCount)
                        .attr("HREF");

                filePresent = document
                        .select("tbody")
                        .select("td")
                        .select("img")
                        .eq(fileCount)
                        .attr("src");

                String size = document
                        .select("font")
                        .eq(sizeCount)
                        .text();

                if (filePresent.contains("attachment.gif")) {
                    filePresent = "true";
                }

                String readMail = document
                        .select("tbody")
                        .select("td")
                        .select("img")
                        .eq(readCount)
                        .attr("src");
                if (readMail.contains("/it-school//images/themes/itschool/icon_read.gif")) {
                    readMail = "true";
                }

                detailUrlCount = detailUrlCount + 1;
                dateCount = dateCount + 2;
                senderCount = senderCount + 16;
                sizeCount = sizeCount + 16;
                fileCount = fileCount + 5;
                readCount = readCount + 5;

                date = date + " " + time;

                detailUrl = detailUrl.substring(22,30);

                loginParseItems.add(new LoginParseItem(title,sender,date,detailUrl,filePresent, readMail, size));
                Log.d("Intranet", IntranetPage + " Title: " + title +
                        " Sender: " + sender + " Time: " + date + ". Size: " + inboxSize + " detailUrl: " + detailUrl + " read: "
                        + readMail + " MailSize: " +  size);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) requireActivity()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // Network is present and connected
        return networkInfo != null && networkInfo.isConnected();
    }

    public void checkInternet() {

        if (!isNetworkAvailable()) {
            Snackbar.make(MainActivity.getView(),
                    getString(R.string.CheckInternet), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(MainActivity.getPrimary())
                    .setAction(getString(R.string.Go), view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .show();
        }
    }
    public static class ReloadIntranet extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginParseItems.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseIntranet();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            parseAdapterForIntranet.notifyDataSetChanged();
        }
    }

    public static String getFinalPw() {
        return finalPw;
    }

    public static String getFinalUsername() {
        return finalUsername;
    }

    public static Map<String, String> getCookies() {
        return Cookies;
    }

    public static String getFolder() {
        return folder;
    }
}