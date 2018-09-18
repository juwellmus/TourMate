package com.example.musa.tourmate.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.musa.tourmate.Current_Fragment;
import com.example.musa.tourmate.Forcast_Fragment;

/**
 * Created by Musa on 4/29/2018.
 */

public class PageAdapter extends FragmentStatePagerAdapter {

    int mNoOfTab;
    String city;
    boolean convert;

    public PageAdapter(FragmentManager fm, int mNoOfTab, String city,boolean convert) {
        super(fm);
        this.mNoOfTab = mNoOfTab;
        this.city = city;
        this.convert = convert;

    }


    @Override
    public Fragment getItem(int position) {


        switch(position)
        {
            case 0:
               /*Current_Fragment sb = new Current_Fragment();
                return sb;*/
                return Current_Fragment.newInstance(city,convert);

            case 1:
               /*Forcast_Fragment bb = new Forcast_Fragment();
                return bb;*/
                return Forcast_Fragment.newInstance(city,convert);
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return mNoOfTab;
    }
}