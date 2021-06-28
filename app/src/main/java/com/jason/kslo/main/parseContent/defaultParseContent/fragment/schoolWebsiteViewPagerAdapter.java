package com.jason.kslo.main.parseContent.defaultParseContent.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class schoolWebsiteViewPagerAdapter extends FragmentStateAdapter {
    public schoolWebsiteViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new GalleryFragment();
        }
        return new LatestNewsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
