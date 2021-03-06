package com.jason.kslo.main.parseContent.loggedInParseContent.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class loginViewPagerAdapter extends FragmentStateAdapter {
    public loginViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new BorrowedBooksFragment();
        }
        return new IntranetFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
