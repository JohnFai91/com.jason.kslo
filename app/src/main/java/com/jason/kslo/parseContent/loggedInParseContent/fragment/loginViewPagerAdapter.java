package com.jason.kslo.parseContent.loggedInParseContent.fragment;

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
        switch (position) {
            case 0:
                return new IntranetFragment();
            case 1:
                return new BorrowedBooksFragment();
            default:
                return new IntranetFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
