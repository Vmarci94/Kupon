package hu.bme.vmarci94.homeworok.kupon.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hu.bme.vmarci94.homeworok.kupon.KuponsActivity;
import hu.bme.vmarci94.homeworok.kupon.R;
import hu.bme.vmarci94.homeworok.kupon.interfaces.OnDialogListener;


/**
 * Created by vmarci94 on 2017.05.13..
 */

public class MapsFragment extends DialogFragment implements OnMapReadyCallback {

    public static final String TAG = MapsFragment.class.getSimpleName();

    private View mView;
    private OnDialogListener mOnDialogListener;
    private GoogleMap mMap;
    private ArrayList<Double[]> posLatLong;

    public MapsFragment(){
        super();
    }

    public static MapsFragment newInstace(ArrayList<Double[]> posLatLongParam){
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.posLatLong = posLatLongParam;
        return mapsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return  mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        for(Double[] idx : this.posLatLong){
            mMap.addMarker(new MarkerOptions().position(new LatLng(idx[0], idx[1])).title("haha"));
        }

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
