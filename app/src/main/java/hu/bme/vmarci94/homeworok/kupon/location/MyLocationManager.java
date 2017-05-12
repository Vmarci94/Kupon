package hu.bme.vmarci94.homeworok.kupon.location;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Created by vmarci94 on 2017.05.08..
 */

public class MyLocationManager {
    private Context context;
    private LocationListener listener;
    private LocationManager locMan = null;

    public MyLocationManager(Context aContext, LocationListener listener) {
        context = aContext;
        this.listener = listener;
        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationMonitoring() {
        locMan.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                100, 100, listener);
        // EMULÁTORON A NETWORK PROVIDER NEM ÉRHETŐ EL!!!
        locMan.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0, 0, listener);


    }

    public void stopLocationMonitoring() {
        if (locMan != null) {
            locMan.removeUpdates(listener);
        }
    }
}
