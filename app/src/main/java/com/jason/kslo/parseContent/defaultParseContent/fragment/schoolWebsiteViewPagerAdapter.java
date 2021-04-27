package com.jason.kslo.parseContent.defaultParseContent.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.BorrowedBooksFragment;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.IntranetFragment;

public class schoolWebsiteViewPagerAdapter extends FragmentStateAdapter {
    public schoolWebsiteViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LatestNewsFragment();
            case 1:
                return new GalleryFragment();
            default:
                return new LatestNewsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
