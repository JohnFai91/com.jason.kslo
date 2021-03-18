package com.jason.kslo.ParseContent.LoggedInParseContent.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.Main.Dialog.ChangeDisplayAmountDialog;
import com.jason.kslo.ParseContent.LoggedInParseContent.Activity.LoginActivity;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseAdapter.ParseAdapterForBooks;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseItem.LoginParseItem;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseAdapter.ParseAdapterForLoginFragment;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseItem.SecondLoginParseItem;
import com.jason.kslo.R;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.jason.kslo.App.updateLanguage;

public class LoginFragment extends Fragment {

    ParseAdapterForBooks parseAdapterForBooks;
    ArrayList<SecondLoginParseItem> secondLoginParseItems = new ArrayList<>();
    ParseAdapterForLoginFragment parseAdapterForLoginFragment;
    ArrayList<LoginParseItem> loginParseItems = new ArrayList<>();

    Map<String, String> Cookies;
    String title;
    String sender;
    String date;
    String detailUrl;
    String time;
    View view;
    ProgressBar progressBar;
    TextView intranetTitleString, BkTitle;
    String numberCount;
    Button reLogin, changeAmount;
    int IntranetSize, BookSize;

    final String LOGIN_FORM_URL = "https://www.hkmakslo.edu.hk/it-school//php/login_v5.php3?ran=0.20496586848356468";
    final String LOGIN_ACTION_URL = "https://www.hkmakslo.edu.hk/it-school/php/login_do.php3";
    final String IntranetUrl = "https://www.hkmakslo.edu.hk/it-school/php/intra/index.php3?index.php3?folder=inbox&page=0&page_msg=";

    final String LmUrl = "https://lm.hkmakslo.edu.hk/PrivatePages/_book_table.aspx?";
    String Username;
    String Password, originPw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        updateLanguage(requireContext());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        intranetTitleString = view.findViewById(R.id.IntranetTitleString);
        intranetTitleString.setText(getString(R.string.Intranet));

        BkTitle = view.findViewById(R.id.TitleBorrowedBooks);
        BkTitle.setText(getString(R.string.BorrowedBooks));

        Content content = new Content();
        content.execute();

        return view;
    }

    private class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            RelativeLayout loginBackground = view.findViewById(R.id.LoginBackground);
            reLogin = view.findViewById(R.id.reLoginButton);
            changeAmount = view.findViewById(R.id.ChangeAmount);

            SharedPreferences pref = Objects.requireNonNull(getActivity()).getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            numberCount = pref.getString("intranetAmount","");

            if (numberCount.isEmpty()){
                numberCount = "20";
            }

            Username = pref.getString("Username","");
            Password = pref.getString("Password","");

            originPw = Password;

            if (!TextUtils.isEmpty(Username)) {
                    loginBackground.setVisibility(View.GONE);

                    changeAmount.setVisibility(View.VISIBLE);
                    changeAmount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openChangeAmountDialog();
                        }
                    });

                    reLogin.setVisibility(View.VISIBLE);
                    reLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    md.update(Password.getBytes());

                    byte byteData[] = md.digest();

                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < byteData.length; i++)
                        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

                    Password = sb.toString();
                    Log.d("Md5 Converter", "Converted: " + Password);
            } else {
                changeAmount.setVisibility(View.GONE);
                reLogin.setVisibility(View.GONE);
                loginBackground.setVisibility(View.VISIBLE);
                Button login = view.findViewById(R.id.login_button);
                login.setOnClickListener(view -> {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                });
            }

            progressBar = view.findViewById(R.id.loginProgress);
            progressBar.setVisibility(View.VISIBLE);

            RecyclerView recyclerView = view.findViewById(R.id.IntranetRecycler);
            RecyclerView borrowedBooksRecyclerView = view.findViewById(R.id.BorrowedBooksRecyclerView);

            borrowedBooksRecyclerView.setHasFixedSize(true);

            LinearLayoutManager layoutVerticalManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            borrowedBooksRecyclerView.setLayoutManager(layoutVerticalManager);

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutHorizontalManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutHorizontalManager);


             parseAdapterForBooks = new ParseAdapterForBooks(secondLoginParseItems, getContext());
             borrowedBooksRecyclerView.setAdapter(parseAdapterForBooks);

            parseAdapterForLoginFragment = new ParseAdapterForLoginFragment(loginParseItems, getContext());
            recyclerView.setAdapter(parseAdapterForLoginFragment);

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

                parseIntranet();
                parseLm();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            parseAdapterForLoginFragment.notifyDataSetChanged();
            parseAdapterForBooks.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            String finalNumberCount = getString(R.string.Intranet) + " (" + IntranetSize + " / " + numberCount + ")";
            intranetTitleString.setText(finalNumberCount);

            String finalBookCount = getString(R.string.BorrowedBooks) + " (" + BookSize + ")";
            BkTitle.setText(finalBookCount);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    private  void parseIntranet(){
        loginParseItems.clear();
        //parse Intranet
        try {

            Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                    .method(Connection.Method.GET)
                    .execute();

            Map<String, String> formData = new HashMap<String, String>();
            formData.put("userloginid",Username);
            formData.put("password",Password);
            formData.put("FakePassword", "登入密碼 / Password");
            formData.put("language","zh");

            Connection.Response response = Jsoup.connect(LOGIN_ACTION_URL)
                    .data(formData)
                    .cookies(loginForm.cookies())
                    .execute();
            Cookies = response.cookies();

            Document document = Jsoup.connect(IntranetUrl + numberCount)
                    .cookies(response.cookies())
                    .get();

            Elements table = document.select("TR");

            IntranetSize = table.select("nobr").size() / 2;
            int i;
            int dateCount = 0, senderCount = 24, detailUrlCount = 15;

            for (i = 0; i < IntranetSize; i++) {

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

                detailUrlCount = detailUrlCount + 1;
                dateCount = dateCount + 2;
                senderCount = senderCount + 16;

                date = date + " " + time;

                detailUrl = detailUrl.substring(22,30);

                Log.d("Test", "Size: " + IntranetSize + " Title: " + title +
                        " Sender: " + sender + " Time: " + date + " detailUrl: " + detailUrl);
                loginParseItems.add(new LoginParseItem(title,sender,date,detailUrl,Cookies));
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseLm(){
        secondLoginParseItems.clear();
        try{
            String finalUsername = Username.replaceAll("s","");
            Connection.Response loginForm = Jsoup.connect("https://lm.hkmakslo.edu.hk/Handlers/UserLogin.ashx?&sno=" +
                    finalUsername + "&pass=" + originPw)
                    .method(Connection.Method.GET)
                    .execute();

            Document doc = Jsoup.connect(LmUrl)
                    .cookies(loginForm.cookies())
                    .get();

            Log.d("Parse Lm", "Cookies: " + loginForm.cookies());

            Elements bk = doc.select("a");

            BookSize = bk.size();
            Log.d("ParseLM", "Size: " + BookSize);

            for (int i = 0; i < BookSize; i++) {

                String bkTitle = bk
                        .eq(i)
                        .text();

                String bkImg = doc
                        .select("img.book-cover-tbumb")
                        .eq(i)
                        .attr("src");

                bkImg = bkImg.replace("..","");
                bkImg = "https://lm.hkmakslo.edu.hk" + bkImg;

                String bkBorrowedDate = doc
                        .select("td.borrowed-date")
                        .eq(i)
                        .text();

                String bkReturnDate = doc
                        .select("td.return-date")
                        .eq(i)
                        .text();

                String countReturn = doc.select("span.due")
                                        .eq(i)
                                        .text();
                countReturn = countReturn.replace("天後到期",getString(R.string.DaysToDueDate));
                bkReturnDate = bkReturnDate + " (" +countReturn + ")";

                secondLoginParseItems.add(new SecondLoginParseItem(bkTitle,bkImg,bkBorrowedDate,bkReturnDate));
                Log.d("Parse Lm", "Bk: " + " title: " + bkTitle + " imgUrl: " + bkImg +
                        " borrowed date: " + bkBorrowedDate + " return date: " + bkReturnDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openChangeAmountDialog(){
        ChangeDisplayAmountDialog changeDisplayAmountDialog = new ChangeDisplayAmountDialog();
        changeDisplayAmountDialog.setCancelable(true);
        changeDisplayAmountDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "ChangeDisplayAmountDialog");

    }
}