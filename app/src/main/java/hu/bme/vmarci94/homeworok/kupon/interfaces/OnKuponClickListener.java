package hu.bme.vmarci94.homeworok.kupon.interfaces;

import android.widget.ImageView;

import hu.bme.vmarci94.homeworok.kupon.data.Kupon;

/**
 * Created by vmarci94 on 2017.05.07..
 */

public interface OnKuponClickListener {
    void onKuponClicked(ImageView img);

    void onKuponLongClick(String key, Kupon kupon);
}
