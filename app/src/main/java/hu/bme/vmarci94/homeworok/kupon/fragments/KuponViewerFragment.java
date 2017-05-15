package hu.bme.vmarci94.homeworok.kupon.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import hu.bme.vmarci94.homeworok.kupon.R;

/**
 * Created by vmarci94 on 2017.05.06..
 */

public class KuponViewerFragment extends DialogFragment {

    public static final String TAG = NFCReadFragment.class.getSimpleName();
    private static final String imgId = "ID";


    //OnDialogListener mOnDialogListener;
    View mView;
    ImageView img;


    public static KuponViewerFragment newInstance(ImageView img){
        KuponViewerFragment myKuponViewerFragment =  new KuponViewerFragment();
        myKuponViewerFragment.img = img;
        /*
        KuponViewerFragment myKuponViewerFragment =  new KuponViewerFragment();
        Bundle args = new Bundle();
        args.putInt(imgId, id);
        myKuponViewerFragment.setArguments(args);
        return myKuponViewerFragment;
        */
        return myKuponViewerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_kupon_viewer,container,false);
        ImageView tmp = (ImageView) mView.findViewById(R.id.img);
        //tmp.setImageResource(R.drawable.error);
        tmp = img;
        //img = (ImageView) mView.findViewById(R.id.img);
        //img = (ImageView) getActivity().findViewById(getArguments().getInt(imgId));
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
