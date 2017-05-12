package hu.bme.vmarci94.homeworok.kupon.interfaces;

import android.support.annotation.Nullable;

/**
 * Created by vmarci94 on 2017.05.06..
 */

public interface OnDialogListener {

    void onDialogDisplayed();

    void onDialogDismissed(@Nullable String result);
}
