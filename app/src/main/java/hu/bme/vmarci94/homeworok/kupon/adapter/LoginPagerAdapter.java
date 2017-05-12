package hu.bme.vmarci94.homeworok.kupon.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hu.bme.vmarci94.homeworok.kupon.fragments.LoginFragment;
import hu.bme.vmarci94.homeworok.kupon.fragments.MyKuponFragment;

/**
 * Created by vmarci94 on 2017.05.05..
 */

public class LoginPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 2;

    public LoginPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new LoginFragment();
            case 1: return new MyKuponFragment();
            default: return new LoginFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
