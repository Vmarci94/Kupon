package hu.bme.vmarci94.homeworok.kupon.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import hu.bme.vmarci94.homeworok.kupon.R;

/**
 * Created by vmarci94 on 2017.05.06..
 */

public class KuponViewerFragment extends DialogFragment {

    public static final String TAG = NFCReadFragment.class.getSimpleName();
    private static final String imgUrl = "url";


    //OnDialogListener mOnDialogListener;
    View mView;
    ImageView img;


    public static KuponViewerFragment newInstance(String kuponImgUrl){
        KuponViewerFragment myKuponViewerFragment =  new KuponViewerFragment();
        Bundle args = new Bundle();
        args.putString(imgUrl, kuponImgUrl);
        myKuponViewerFragment.setArguments(args);
        return myKuponViewerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_kupon_viewer,container,false);
        img = (ImageView) mView.findViewById(R.id.img);

        String str = getArguments().getString(imgUrl);
        if(str == null){
            Log.e("BIG PROBLEM", "str is null");
        }else {
            Glide.with(mView.getContext()).load(str).into(img);
        }

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mOnDialogListener = (KuponsActivity)context;
       // mOnDialogListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mOnDialogListener.onDialogDismissed();
    }


    public void setImageDrawable(Drawable drawable) {
        img.setImageDrawable(drawable);
    }
}
