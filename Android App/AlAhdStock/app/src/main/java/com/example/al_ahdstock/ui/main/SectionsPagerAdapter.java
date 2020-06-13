package com.example.al_ahdstock.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.al_ahdstock.CategoryFragment;
import com.example.al_ahdstock.DeleteFragment;
import com.example.al_ahdstock.ItemFragment;
import com.example.al_ahdstock.R;
import com.example.al_ahdstock.UpdateFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"Add Item", "Add Category","Update","Delete"};
//    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
      //  mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        //Fragment fragment = null;
        switch (position) {
            case 0 :
                return ItemFragment.newInstance();
           //     break;
            case 1 :
                return CategoryFragment.newInstance();
           //     break;
            case 2 :
                return UpdateFragment.newInstance();
           //     break;
            case 3 :
                return DeleteFragment.newInstance();
           //     break;
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }
}