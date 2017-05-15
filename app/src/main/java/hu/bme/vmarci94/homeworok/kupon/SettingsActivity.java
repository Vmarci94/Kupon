package hu.bme.vmarci94.homeworok.kupon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.List;

import hu.bme.vmarci94.homeworok.kupon.service.ServiceLocation;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EXTRA_NO_HEADERS = ":android:no_headers";
    public static final String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";
    public static final String KEY_START_SERVICE = "start_service";
    public static final String KEY_ALERT = "enable_alert";
    public static final String KEY_SAVE = "enable_save";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 500;

    public boolean alert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(
                this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(
                this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case KEY_START_SERVICE:{
                    Boolean startService = sharedPreferences.getBoolean(KEY_START_SERVICE, false);
                    if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(getApplicationContext(), ServiceLocation.class);
                        if (startService) {
                            startService(i);
                        } else {
                            stopService(i);
                        }
                    }else{
                        Log.e("hiba", "nincs engedély");
                    }

                break;
            }
            case KEY_SAVE:{
                boolean startService = sharedPreferences.getBoolean(KEY_SAVE, false);
                // TODO: Service indítása/leállítása

                break;
            }
        }
    }


    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.fragmentsettings, target);
    }

    public static class FragmentSettingsBasic extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.mainsettings);
        }
    }

}
