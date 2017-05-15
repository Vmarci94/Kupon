package hu.bme.vmarci94.homeworok.kupon;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by vmarci94 on 2017.05.13..
 */

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
