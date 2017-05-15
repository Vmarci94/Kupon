package hu.bme.vmarci94.homeworok.kupon;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by vmarci94 on 2017.05.13..
 */

public class Utility {

    public ProgressDialog progressDialog;
    public Context context;

    public Utility(Context context) {
        this.context = context;
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            //progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
