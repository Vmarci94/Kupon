package hu.bme.vmarci94.homeworok.kupon;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hu.bme.vmarci94.homeworok.kupon.adapter.KuponAdapter;
import hu.bme.vmarci94.homeworok.kupon.data.Kupon;
import hu.bme.vmarci94.homeworok.kupon.fragments.KuponReaderFragment;
import hu.bme.vmarci94.homeworok.kupon.fragments.NFCReadFragment;
import hu.bme.vmarci94.homeworok.kupon.interfaces.OnDialogListener;
import hu.bme.vmarci94.homeworok.kupon.interfaces.OnKuponClickListener;


public class KuponsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnDialogListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    //ezek felelnek a felülettel való összekötésért. Definiálás
    private RecyclerView recyclerViewPosts;
    private KuponAdapter kuponAdapter;

    //A kuon olvasáshoz NFC tag technológiával
    private NfcAdapter mNfcAdapter;
    private NFCReadFragment mNfcReadFragment;
    private boolean isDialogDisplayed = false;

    private KuponReaderFragment mKuponReaderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //[START] Generált kód
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_kupon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///[END] Generált kód

        initListener();
        initRecycleView();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this); //NFC inicializálása
        mNfcReadFragment = NFCReadFragment.newInstance();
        initPostsListener(); //Ő felel a valós idejű frissítésért

        View navigationHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_kupon);
        initUserData(navigationHeaderView);

    }

    private void initUserData(View view) {
        FirebaseUser tmpAuth = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView) view.findViewById(R.id.userNameTextView)).setText(tmpAuth.getDisplayName());
        ((TextView) view.findViewById(R.id.userEmailTextView)).setText(tmpAuth.getEmail());

        Glide.with(getApplicationContext()).load(tmpAuth.getPhotoUrl()).into(((ImageView) view.findViewById(R.id.userImageView)));

    }


    private void initListener() {
        kuponAdapter = new KuponAdapter(getApplicationContext(), new OnKuponClickListener() {
            @Override
            public void onKuponClicked(String imgKupinUrl) {

                mKuponReaderFragment = (KuponReaderFragment) getSupportFragmentManager().findFragmentByTag(KuponReaderFragment.TAG);

                if (mKuponReaderFragment == null) {

                    mKuponReaderFragment = mKuponReaderFragment.newInstance(imgKupinUrl);
                }
                mKuponReaderFragment.show(getSupportFragmentManager(),mKuponReaderFragment.TAG);

            }

            @Override
            public void onKuponLongClick() {

            }

        });
    }

    private void initRecycleView() {
        recyclerViewPosts = (RecyclerView) findViewById(R.id.recyclerViewPosts);
        //Implementál...
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        recyclerViewPosts.setLayoutManager(layoutManager);
        recyclerViewPosts.setAdapter(kuponAdapter);
    }

    private void initPostsListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("kupons");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Kupon newKupon = dataSnapshot.getValue(Kupon.class); //itt jön meg a Firebase-ről

                kuponAdapter.addKupon(dataSnapshot.getKey(), newKupon);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                kuponAdapter.removeKupon(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            logout();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_kupon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Biztosan ki akarsz jelentkezni?")
                .setPositiveButton("igen", logOutDialogOnClickListener)
                .setNegativeButton("nem", logOutDialogOnClickListener);
        builder.create();
        builder.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_camera:{
                showNFCReadFragment();
                break;
            }
            case R.id.nav_manage:{
                Intent intentSettings = new Intent(KuponsActivity.this,
                        SettingsActivity.class);
                intentSettings.putExtra(SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.FragmentSettingsBasic.class.getName());
                intentSettings.putExtra(SettingsActivity.EXTRA_NO_HEADERS, true);
                startActivity(intentSettings);
             break;
            }
            case R.id.nav_logout:{
                logout();
                break;
            }
            case R.id.nav_share:{
                break;
            }
            case R.id.nav_send:{
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showSettingsActivity(){
        Intent intentSettings = new Intent(KuponsActivity.this,
                SettingsActivity.class);
        intentSettings.putExtra(SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.FragmentSettingsBasic.class.getName());
        intentSettings.putExtra(SettingsActivity.EXTRA_NO_HEADERS, true);
        startActivity(intentSettings);
    }

    private void showNFCReadFragment(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        mNfcReadFragment = (NFCReadFragment) getSupportFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

        if (mNfcReadFragment == null) {

            mNfcReadFragment = NFCReadFragment.newInstance();
        }

        mNfcReadFragment.show(getSupportFragmentManager(),NFCReadFragment.TAG);
    }

    //Listenerek
    private DialogInterface.OnClickListener logOutDialogOnClickListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == DialogInterface.BUTTON_POSITIVE){
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                setResult(9002);
                finish();
            }
        }
    };


    @Override
    public void onDialogDisplayed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed(String result) {
        isDialogDisplayed = false;
        if(result != null && kuponAdapter.get(result) != null ) {
            //result is a valid key
            kuponAdapter.update(result);
            //FIXME..
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if(tag != null) {
            if(!isDialogDisplayed) {
                Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            }
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {
                mNfcReadFragment = (NFCReadFragment) getSupportFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                mNfcReadFragment.onNfcDetected(ndef);
            }
        }
    }

    static void printLocationToLog(Double... args){
        String LocTAG = "Location Tag";
        for(Double str: args){
            Log.i(LocTAG, Double.toString(str));
        }
    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance( this ).registerReceiver(
                mMessageReceiver,
                new IntentFilter(ServiceLocation.BR_NEW_LOCATION));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver,
                new IntentFilter(ServiceLocation.PROX_KUPON_ALERT_INTENT));

    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance( this ).unregisterReceiver(
                mMessageReceiver);
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location currentLocation = intent.getParcelableExtra(ServiceLocation.KEY_LOCATION);

            Boolean entering = intent.getBooleanExtra(ServiceLocation.PROX_KUPON_ALERT_INTENT, false);
            if(entering){
                Toast.makeText(KuponsActivity.this ,"Kupon a közelben!", Toast.LENGTH_LONG).show();
            }


            printLocationToLog(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
            );
        }
    };*/

}
