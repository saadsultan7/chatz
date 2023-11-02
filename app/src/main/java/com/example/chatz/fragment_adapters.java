package com.example.chatz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatz.Fragments.calls_fragments;
import com.example.chatz.Fragments.chats_fragment;
import com.example.chatz.Fragments.status_fragment;

public class fragment_adapters extends FragmentPagerAdapter {
    public fragment_adapters(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            return new chats_fragment();
        }
        else if (position == 1) {
            return new status_fragment();
        }
        else
        {
            return new calls_fragments();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
        {
            return "chats";
        }
        else if (position == 1) {
            return "status";
        }
        else
        {
            return "calls";
        }
    }
}
