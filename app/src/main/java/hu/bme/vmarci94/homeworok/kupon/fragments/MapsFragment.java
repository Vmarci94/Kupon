package hu.bme.vmarci94.homeworok.kupon.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Map;

import hu.bme.vmarci94.homeworok.kupon.KuponsActivity;
import hu.bme.vmarci94.homeworok.kupon.R;
import hu.bme.vmarci94.homeworok.kupon.interfaces.OnDialogListener;


/**
 * Created by vmarci94 on 2017.05.13..
 */

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private View mView;
    private OnDialogListener mOnDialogListener;
    private Map mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        this = (SupportMapFragment) inflater.inflate(R.layout.fragment_maps, container, false);
        ser
        mapFragment.getMapAsync(this);

        return  mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnDialogListener.onDialogDismissed(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnDialogListener = (KuponsActivity)context;
        mOnDialogListener.onDialogDisplayed();
    }
}
