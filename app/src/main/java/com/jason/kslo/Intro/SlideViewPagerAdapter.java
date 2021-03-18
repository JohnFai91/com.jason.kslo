package com.jason.kslo.Intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.jason.kslo.Main.Activity.MainActivity;
import com.jason.kslo.Main.AutoUpdate.AppUtils;
import com.jason.kslo.R;

public class SlideViewPagerAdapter extends PagerAdapter {
    Context ctx;

    public SlideViewPagerAdapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_screen, container, false);

        RelativeLayout Background = view.findViewById(R.id.SlideScreenBackground);

        ImageView logo = view.findViewById(R.id.Photo);
        ImageView pg1 = view.findViewById(R.id.pgNo_1);
        ImageView pg2 = view.findViewById(R.id.pgNo_2);
        ImageView pg3 = view.findViewById(R.id.pgNo_3);
        ImageView pg4 = view.findViewById(R.id.pgNo_4);
        ImageView pg5 = view.findViewById(R.id.pgNo_5);

        TextView title = view.findViewById(R.id.SlideTitle);
        TextView desc = view.findViewById(R.id.SlideDesc);
        TextView newInVersion = view.findViewById(R.id.NewInVersion);

        ImageView next = view.findViewById(R.id.next);
        ImageView previous = view.findViewById(R.id.previous);

        Button getStarted = view.findViewById(R.id.GetStartedBtn);

        getStarted.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);


            SharedPreferences.Editor editor = ctx.getSharedPreferences("MyPref",Context.MODE_PRIVATE).edit();
            editor.putString("slide", "done");
            editor.commit();
            ctx.startActivity(intent);
        });

        next.setOnClickListener(v -> SlideActivity.viewPager.setCurrentItem(position+1));

        previous.setOnClickListener(v -> SlideActivity.viewPager.setCurrentItem(position-1));

        switch (position)
        {
            case 0:
                logo.setImageResource(R.drawable.light_dashboard);
                pg1.setImageResource(R.drawable.selected);
                pg2.setImageResource(R.drawable.unselected);
                pg3.setImageResource(R.drawable.unselected);
                pg4.setImageResource(R.drawable.unselected);
                pg5.setImageResource(R.drawable.unselected);

                title.setText(R.string.Dashboard);
                desc.setText(R.string.ShowHomework);
                desc.setTextColor(Color.parseColor("#000000"));
                newInVersion.setVisibility(View.GONE);

                Background.setBackgroundColor(Color.parseColor("#ffffff"));

                previous.setVisibility(View.GONE);
                getStarted.setVisibility(View.GONE);

                break;
            case 1:
                logo.setImageResource(R.drawable.featured_notice);
                pg1.setImageResource(R.drawable.unselected);
                pg2.setImageResource(R.drawable.selected);
                pg3.setImageResource(R.drawable.unselected);
                pg4.setImageResource(R.drawable.unselected);
                pg5.setImageResource(R.drawable.unselected);

                title.setText(R.string.View_Pdf);
                desc.setText(R.string.ViewPdfDesc);
                desc.setTextColor(Color.parseColor("#000000"));
                newInVersion.setVisibility(View.GONE);

                Background.setBackgroundColor(Color.parseColor("#ffffff"));

                previous.setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.GONE);
                break;
            case 2:
                logo.setImageResource(R.drawable.dark_settings);
                pg1.setImageResource(R.drawable.unselected);
                pg2.setImageResource(R.drawable.unselected);
                pg3.setImageResource(R.drawable.selected);
                pg4.setImageResource(R.drawable.unselected);
                pg5.setImageResource(R.drawable.unselected);

                title.setText(R.string.Customise);
                desc.setText(R.string.CustomiseDesc);
                desc.setTextColor(Color.parseColor("#ffffff"));
                newInVersion.setVisibility(View.GONE);

                Background.setBackgroundColor(Color.parseColor("#000000"));

                previous.setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.GONE);
                break;
            case 3:
                logo.setImageResource(R.drawable.dashboard_both_horizontal);
                logo.setMaxWidth(400);
                pg1.setImageResource(R.drawable.unselected);
                pg2.setImageResource(R.drawable.unselected);
                pg3.setImageResource(R.drawable.unselected);
                pg4.setImageResource(R.drawable.selected);
                pg5.setImageResource(R.drawable.unselected);

                title.setText(R.string.Tips);
                desc.setText(R.string.DashboardTips);
                desc.setTextColor(Color.parseColor("#000000"));
                newInVersion.setVisibility(View.GONE);

                Background.setBackgroundColor(Color.parseColor("#ffffff"));

                previous.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.GONE);
                break;
            case 4:
                logo.setVisibility(View.GONE);

                pg1.setImageResource(R.drawable.unselected);
                pg2.setImageResource(R.drawable.unselected);
                pg3.setImageResource(R.drawable.unselected);
                pg4.setImageResource(R.drawable.unselected);
                pg5.setImageResource(R.drawable.selected);

                String newInVersionTitle = ctx.getString(R.string.NewFeatures);
                title.setText(newInVersionTitle + " (" + AppUtils.getVersionName(ctx.getApplicationContext()) + ")");
                desc.setVisibility(View.GONE);
                newInVersion.setVisibility(View.VISIBLE);
                newInVersion.setText(ctx.getString(R.string.changelogDescLv_1_0_8_beta) +
                        ctx.getString(R.string.changelogDescRv_1_0_8_beta));

                Background.setBackgroundColor(Color.parseColor("#ffffff"));

                previous.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                getStarted.setVisibility(View.VISIBLE);
                break;
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
