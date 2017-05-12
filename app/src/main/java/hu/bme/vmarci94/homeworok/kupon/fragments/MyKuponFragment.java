package hu.bme.vmarci94.homeworok.kupon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.vmarci94.homeworok.kupon.R;

/**
 * Created by vmarci94 on 2017.05.05..
 */

public class MyKuponFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_kupon, null);
        return rootView;
    }

}
