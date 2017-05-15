package hu.bme.vmarci94.homeworok.kupon.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArrayMap;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import hu.bme.vmarci94.homeworok.kupon.KuponsActivity;
import hu.bme.vmarci94.homeworok.kupon.R;
import hu.bme.vmarci94.homeworok.kupon.data.Kupon;
import hu.bme.vmarci94.homeworok.kupon.interfaces.OnDialogListener;


/**
 * Created by vmarci94 on 2017.05.13..
 */

public class MapsFragment extends DialogFragment implements OnMapReadyCallback{

    public static final String TAG = MapsFragment.class.getSimpleName();

    private View mView;
    private OnDialogListener mOnDialogListener;
    private GoogleMap mMap;
    private ArrayMap<String, Kupon> kupons;
    private SupportMapFragment mapFragment;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    public MapsFragment(){
        super();
    }

    public static MapsFragment newInstace(ArrayMap<String, Kupon> kupons){
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.kupons = kupons;
        return mapsFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            mView = inflater.inflate(R.layout.fragment_maps, container, false);

            mapFragment = (SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            return mView;
        } catch (InflateException e){
            Toast.makeText(mView.getContext(), "Hiba :(", Toast.LENGTH_LONG).show();
            return mView;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(int idx = 0; idx < this.kupons.size(); idx++){
            Kupon tmpKupon = kupons.valueAt(idx);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(tmpKupon.getLatitude(), tmpKupon.getLongitude()))
                    .title(tmpKupon.getCompany() + " " +tmpKupon.getSale());
            mMap.addMarker(markerOptions)
                    .showInfoWindow();
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    //Fix me akutális pozíciótól való navigálás
                    String uri = "http://maps.google.com/maps?saddr=" + mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude() + "(" + "Saját pozíció" + ")&daddr=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + " (" + marker.getTitle() + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    try {
                        startActivity(intent);
                    }catch (ActivityNotFoundException e){
                        try {
                            Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(unrestrictedIntent);
                        }catch (ActivityNotFoundException err){
                            Toast.makeText(MapsFragment.this.getContext(), "Please install a maps application", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            });


        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.5011151657, 19.0531965145), 12)); //budapest

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mapFragment != null){
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
        mOnDialogListener.onDialogDismissed(null);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnDialogListener = (KuponsActivity)context;
        mOnDialogListener.onDialogDisplayed();
    }
}
