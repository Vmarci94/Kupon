package hu.bme.vmarci94.homeworok.kupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN_GOOGLE = 9001;
    private static final int RC_SIGN_OUT = 9002;

    public GoogleApiClient mGoogleApiClient;
    public FirebaseAuth mAuth;

    private EditText etEmail;
    private EditText etPassword;

    private SignInButton btnGoogleSignIn;
    private Button btnFirebaseSignIn;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    private void init() {
        //Views
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        //Button Listeners
        findViewById(R.id.btnGoogleSignIn).setOnClickListener(this);
        findViewById(R.id.btnFirebaseSignIn).setOnClickListener(this);
        findViewById(R.id.btnRegister).setOnClickListener(this);

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            if(mGoogleApiClient.isConnected()){
                signOut();
            }else {
                mAuth.signOut();
                Log.i(TAG, "firebase kijelentkeztetve");
            }
        }else{
            if(mGoogleApiClient.isConnected()){
                revokeAccess();
            }
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //Enter(currentUser);
    }
    // [END on_start_check_user]

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnGoogleSignIn: {
                signInGoogle();
                break;
            }
            case R.id.btnFirebaseSignIn: {
                signInFirebase();
                break;
            }
            case R.id.btnRegister: {
                register();
                break;
            }
        }
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.i(TAG, "Google fiók kijelentkeztetve!");
                    }
                });
    }


    // [START email format valid]
    private boolean isFormValid() {
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Required");
            return false;
        }

        return true;
    }

    private String userNameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    // [END email format valid]

    private void signInFirebase() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                hideProgressDialog();

                if (task.isSuccessful()) {
                    Enter(mAuth.getCurrentUser());

                } else {
                    //FIXME bajvan
                }
            }
        });
    }

    private void register() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(
                etEmail.getText().toString(), etPassword.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    firebaseUser.updateProfile(
                            new UserProfileChangeRequest.Builder().
                                    setDisplayName(
                                            userNameFromEmail(
                                                    firebaseUser.getEmail())).build()
                    );

                    Toast.makeText(SignInActivity.this, "REG OK",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignInActivity.this, "Failed: "+
                                    task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(SignInActivity.this,
                        "error: "+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                Enter(null);
                // [END_EXCLUDE]
            }
        }
        if(resultCode == RC_SIGN_OUT){
            signOut();
        }
    }

    private void signOut() {
        //FirebaseSignOut
        mAuth.signOut();

        //GoogleSignOut
        if(mGoogleApiClient.isConnected()){
            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Toast.makeText(SignInActivity.this, "Sikeres kiejelentkezés (Google)", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Enter(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Enter(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]


    void Enter(@Nullable FirebaseUser firebaseUser){
        if(firebaseUser != null){
            Intent kuponActivityIntent = new Intent(SignInActivity.this, KuponsActivity.class);
            startActivityForResult(kuponActivityIntent, RC_SIGN_OUT);
        }else {
            Log.e(TAG, "firebaseUser is null");
            //FIXME
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
