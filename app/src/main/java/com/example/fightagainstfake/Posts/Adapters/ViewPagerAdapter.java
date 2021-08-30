package com.example.fightagainstfake.Posts.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fightagainstfake.Posts.Fragments.AdvertisementPostsFragment;
import com.example.fightagainstfake.Posts.Fragments.NormalPostsFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new NormalPostsFragment();
        else if (position == 1)
            return new AdvertisementPostsFragment();
        else
            return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void add(Fragment fr, String str) {
        fragments.add(fr);
        strings.add(str);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }
}
