package hu.bme.vmarci94.homeworok.kupon.fragments;

import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import hu.bme.vmarci94.homeworok.kupon.KuponsActivity;
import hu.bme.vmarci94.homeworok.kupon.R;
import hu.bme.vmarci94.homeworok.kupon.interfaces.OnDialogListener;

/**
 * Created by vmarci94 on 2017.05.06..
 */

public class NFCReadFragment extends DialogFragment {

    public static final String TAG = NFCReadFragment.class.getSimpleName(); //NFCReadFragment

    private TextView mTvMessage;
    private Button mBtnOk;
    private OnDialogListener mOnDialogListener;

    public static NFCReadFragment newInstance(){
        return new NFCReadFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read,container,false);
        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        mBtnOk = (Button) view.findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnDialogListener = (KuponsActivity)context;
        mOnDialogListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mTvMessage != null && mTvMessage.getText().toString().length() > 0) {
            mOnDialogListener.onDialogDismissed(mTvMessage.getText().toString());
        } else {
            mOnDialogListener.onDialogDismissed(null);
        }

    }

    public void onNfcDetected(Ndef ndef){
        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: "+message);

            //FIXME
            mTvMessage.setText(message); //itt fog kelleni ellenőrizni, hogy a kupon valódi-e

            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }

}
