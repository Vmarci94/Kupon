package hu.bme.vmarci94.homeworok.kupon.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import hu.bme.vmarci94.homeworok.kupon.KuponsActivity;
import hu.bme.vmarci94.homeworok.kupon.MainActivity;
import hu.bme.vmarci94.homeworok.kupon.R;
import hu.bme.vmarci94.homeworok.kupon.Utility;

/**
 * Created by vmarci94 on 2017.05.05..
 */

public class LoginFragment extends Fragment {

    //FIXME
    EditText etEmail;
    EditText etPassword;

    private View rootView;

    private static final int RC_SIGN_IN_GOOGLE = 9001;
    private static final int RC_SIGN_OUT = 9002;;

    public static GoogleApiClient mGoogleApiClient;
    protected FirebaseAuth mAuth;
    private Uri GoogleAccountPhotoUri; //vagy valami jobb

    public Utility utility;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, null);
        utility = new Utility(rootView.getContext());
        initView();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                /*.requestIdToken(getString(R.string.web_client_id))*/
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(rootView.getContext())
                .enableAutoManage((MainActivity)getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //hiba..
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        return rootView;
    }

    private void initView(){
        SignInButton btnGoogleLogin = (SignInButton) rootView.findViewById(R.id.btnGoogleLogin);
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });

        Button btnFirebaseLogin = (Button) rootView.findViewById(R.id.btnFirebaseLogin);
        btnFirebaseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFirebase();
            }
        });

        Button btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        etEmail = (EditText) rootView.findViewById(R.id.etEmail);

        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

            Enter(currentUser);

    }
    // [END on_start_check_user]

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) { //FIXME
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                GoogleAccountPhotoUri = account.getPhotoUrl();
                Log.i("Gpics:", GoogleAccountPhotoUri.toString());
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                //UpdateUi...
                //FIXME
            }
        }
        if(resultCode == RC_SIGN_OUT){
            logoutFromGoogle();
        }


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Google Sing in", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        utility.showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) rootView.getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google Sing in", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            try {

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(GoogleAccountPhotoUri)
                                        .build();

                                user.updateProfile(profileUpdates);

                                Enter(user);
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(rootView.getContext(), "ismeretlen hiba", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google Sing in", "signInWithCredential:failure", task.getException());
                            Toast.makeText(rootView.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        utility.hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void logoutFromGoogle() {
        utility.showProgressDialog();
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        if(mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            //updateUI...
                            Toast.makeText(rootView.getContext(), "A kijelentkezés sikeres!", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        utility.hideProgressDialog();
    }

    private void Enter(FirebaseUser firebaseUser){
        if(firebaseUser != null){
            Intent postActivityIntent = new Intent(rootView.getContext(), KuponsActivity.class);
            startActivityForResult(postActivityIntent, RC_SIGN_OUT);

        } else{
            Log.e("firebase user == ", "null :(");
            //FIXME
            //hibakezelés
        }
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

    private void loginWithFirebase() {
        if (!isFormValid()) {
            return;
        }
        utility.showProgressDialog();

        mAuth.signInWithEmailAndPassword(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        ).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                utility.hideProgressDialog();

                if (task.isSuccessful()) {
                    Enter(mAuth.getCurrentUser());

                } else {
                    //FIXME
                }
            }
        });
    }

    private void loginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void register(){

        if (!isFormValid()) {
            return;
        }

        utility.showProgressDialog();

        mAuth.createUserWithEmailAndPassword(
                etEmail.getText().toString(), etPassword.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                utility.hideProgressDialog();

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    firebaseUser.updateProfile(
                            new UserProfileChangeRequest.Builder().
                                    setDisplayName(
                                            userNameFromEmail(
                                                    firebaseUser.getEmail())).build()
                    );

                            Toast.makeText(rootView.getContext(), "REG OK", Toast.LENGTH_SHORT).show();

                } else {
                            Toast.makeText(rootView.getContext(), "Failed: "+
                                            task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                utility.hideProgressDialog();
                    Toast.makeText(getActivity(),
                            "error: "+e.getMessage(),
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        mAuth.signOut();
        super.onDestroy();
    }
}
