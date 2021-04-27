package com.jason.kslo.parseContent.loggedInParseContent.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.App;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.parseContent.loggedInParseContent.activity.LoginActivity;
import com.jason.kslo.parseContent.loggedInParseContent.parseAdapter.ParseAdapterForLoginFragment;
import com.jason.kslo.parseContent.loggedInParseContent.parseItem.LoginParseItem;
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

import static com.jason.kslo.App.updateLanguage;

public class IntranetFragment extends Fragment {
    ParseAdapterForLoginFragment parseAdapterForLoginFragment;
    final ArrayList<LoginParseItem> loginParseItems = new ArrayList<>();

    static Map<String, String> Cookies;
    String title, sender, date, detailUrl, time, booksCountReturn;
    View view;
    ProgressBar progressBar, intranetProgress;
    TextView intranetTitleString, pageDesc;
    SwipeRefreshLayout pullToRefresh;
    int IntranetPage;
    SharedPreferences pref;
    int size, booksSize;
    RecyclerView recyclerView;
    private static String finalUsername, finalPw;

    final String LOGIN_FORM_URL = "https://www.hkmakslo.edu.hk/it-school//php/login_v5.php3?ran=0.20496586848356468";
    final String LOGIN_ACTION_URL = "https://www.hkmakslo.edu.hk/it-school/php/login_do.php3";
    final String IntranetUrl = "https://www.hkmakslo.edu.hk/it-school/php/intra/index.php3?index.php3?folder=inbox&page=";

    String Username, filePresent;
    String Password, originPw;

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_intranet, container, false);

        pref = Objects.requireNonNull(getActivity()).getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String renewed = pref.getString("Renewed","");

        if (renewed.equals("true")){
            Content content = new Content();
            content.execute();
            pref.edit().putString("Renewed","false").apply();
        }


        pullToRefresh = view.findViewById(R.id.loginRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            Content content = new Content();
            content.execute();
            pullToRefresh.setRefreshing(true);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pullToRefresh.setColorSchemeColors(
                    Objects.requireNonNull(getActivity()).getColor(android.R.color.holo_blue_dark),
                    getActivity().getColor(android.R.color.holo_orange_dark),
                    getActivity().getColor(android.R.color.holo_green_dark),
                    getActivity().getColor(android.R.color.holo_red_dark)
            );
        }

        Content content = new Content();
        content.execute();

        return view;
    }

    @SuppressWarnings({"deprecation", "EmptyMethod"})
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void, Void, Void> {
        @SuppressLint("SetTextI18n")
        @SuppressWarnings("deprecation")
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

            parseAdapterForLoginFragment = new ParseAdapterForLoginFragment(loginParseItems, getContext());
            recyclerView.setAdapter(parseAdapterForLoginFragment);

            IntranetPage = 0;
            loginParseItems.clear();

            checkInternet();

            super.onPreExecute();

        }

        @SuppressWarnings("deprecation")
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

                    int closestDueDate = Integer.parseInt(booksCountReturn);


                    if (closestDueDate < prevCount) {
                        prevCount = closestDueDate;
                        booksCountReturn = String.valueOf(closestDueDate);
                    } else if (prevCount == 0) {
                        prevCount = closestDueDate;
                        booksCountReturn = String.valueOf(closestDueDate);
                    }

                }
                booksCountReturn = " (" + booksCountReturn + getString(R.string.DaysToDueDate) + ")";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pageDesc.setText(getString(R.string.BorrowedBooks) + " " +
                    booksSize + " " + getString(R.string.Books) +
                    booksCountReturn);

            parseAdapterForLoginFragment.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            pullToRefresh.setRefreshing(false);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    FloatingActionButton floatingActionButton = view.findViewById(R.id.goToTopIntranet);

                    if (!recyclerView.canScrollVertically(1)) {
                        IntranetPage = IntranetPage + 1;
                        Log.d("IntranetPages", "No. : " + IntranetPage);
                        floatingActionButton.setVisibility(View.VISIBLE);


                        Con con = new Con();
                        con.execute();

                    } else if (recyclerView.canScrollVertically(View.FOCUS_UP)) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            floatingActionButton.setVisibility(View.VISIBLE);
                            floatingActionButton.startAnimation(AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in));
                            floatingActionButton.setOnClickListener(view -> {
                                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                                        .getLayoutManager();
                                assert linearLayoutManager != null;
                                linearLayoutManager.scrollToPositionWithOffset(0, 0);
                            });
                        } else {
                            floatingActionButton.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                            floatingActionButton.setVisibility(View.GONE);
                        }
                    } else if (!recyclerView.canScrollVertically(View.FOCUS_UP)) {
                        floatingActionButton.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                        floatingActionButton.setVisibility(View.GONE);
                    }
                }
            });
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    @SuppressWarnings({"deprecation", "EmptyMethod"})
    @SuppressLint("StaticFieldLeak")
    private class Con extends AsyncTask<Void, Void, Void> {
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intranetProgress.setVisibility(View.VISIBLE);

            pageDesc.setText(getString(R.string.BorrowedBooks) + " " +
                    LoginFragment.getSize() + " " + getString(R.string.Books) +
                    LoginFragment.getCountReturn());
        }

        Boolean btmMsg;

        @Override
        protected Void doInBackground(Void... voids) {

            if (IntranetPage < size) {
                parseIntranet();
                btmMsg = false;
            } else {
                btmMsg = true;
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            parseAdapterForLoginFragment.notifyDataSetChanged();
            intranetProgress.setVisibility(View.GONE);

            if (btmMsg.equals(true)) {
                runOnUiThread run = new runOnUiThread();
                run.run();

            }
            btmMsg = false;
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

    private  void parseIntranet(){
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

            Document document = Jsoup.connect(IntranetUrl + IntranetPage)
                    .cookies(response.cookies())
                    .get();

            Elements table = document.select("TR");

            int i;

            int dateCount = 0, senderCount = 24, detailUrlCount = 15, fileCount = 26;

            String firstSize = table.select("option")
                    .eq(0)
                    .text();

            firstSize = firstSize.replaceAll(".+/","");
            firstSize = firstSize.replace(")","");
            size = (int) Math.ceil(Float.parseFloat(firstSize)/20);

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

                if (filePresent.contains("attachment.gif")) {
                    filePresent = "true";
                }

                detailUrlCount = detailUrlCount + 1;
                dateCount = dateCount + 2;
                senderCount = senderCount + 16;
                fileCount = fileCount + 5;

                date = date + " " + time;

                detailUrl = detailUrl.substring(22,30);

                Log.d("parseIntranet", "File: " + filePresent);

                loginParseItems.add(new LoginParseItem(title,sender,date,detailUrl,filePresent));
                Log.d("Test", IntranetPage + " Title: " + title +
                        " Sender: " + sender + " Time: " + date + ". Size: " + size + " detailUrl: " + detailUrl);
            }

            } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) Objects.requireNonNull(getActivity())
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

    public static String getFinalPw() {
        return finalPw;
    }

    public static String getFinalUsername() {
        return finalUsername;
    }

    public static Map<String, String> getCookies() {
        return Cookies;
    }
}