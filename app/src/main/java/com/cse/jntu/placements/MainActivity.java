package com.cse.jntu.placements;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.jntu.placements.models.User;
import com.google.android.gms.auth.api.*;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.forusers.heinsinputdialogs.HeinsInputDialog;
import br.com.forusers.heinsinputdialogs.interfaces.OnInputDoubleListener;
import br.com.forusers.heinsinputdialogs.interfaces.OnInputStringListener;
import viethoa.com.snackbar.BottomSnackBarMessage;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

setTheme(R.style.MyTheme);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Signing In ...");
progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
    // [END config_signin]

    mGoogleApiClient =new GoogleApiClient.Builder(this)
            .

    enableAutoManage(this /* FragmentActivity */,this /* OnConnectionFailedListener */)
                .

    addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .

    build();

    // [START initialize_auth]
    mAuth =FirebaseAuth.getInstance();
    // [END initialize_auth]


}

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]

    void startProgressBar()
    {
progressBar.show();
    }

    void stopProgressBar()
    {
progressBar.cancel();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
       startProgressBar();
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

                            User user1=new User(user.getUid(),user.getDisplayName(),user.getEmail(),true);
                         // FirebaseFirestore.getInstance().collection("users").add(user1);
                            Constants.user=user1;
                            addToDb(user1);
                            saveDetails(user1);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                 stopProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signInGmail() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        startProgressBar();
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
      stopProgressBar();
        if (user != null) {
         Toast.makeText(this,"Welcome "+ user.getEmail(),Toast.LENGTH_LONG).show();
         //   mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
Log.d(TAG,user.getEmail());

            startActivity(new Intent(this,DashBoard.class));
            finish();

        } else {

            Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

        int id=view.getId();

        switch (id)
        {
            case R.id.g:
                signInGmail();
                break;
            case R.id.signImg:
                signInWithEmail();
                break;
            case R.id.signUp:

startActivity(new Intent(this,SignUp.class));
                break;
            case R.id.forgotpass:
                resetPassword();
                break;
        }



    }

    private void resetPassword() {

        HeinsInputDialog dialog = new HeinsInputDialog(this);
        dialog.setPositiveButton(new OnInputStringListener() {
                                     @Override
                                     public boolean onInputString(AlertDialog alertDialog, String s) {

                                         mAuth.sendPasswordResetEmail(s)
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         if (task.isSuccessful()) {
                                                             BottomSnackBarMessage bottomSnackBarMessage = new BottomSnackBarMessage(MainActivity.this);
                                                             bottomSnackBarMessage.showSuccessMessage("Email Sent Successfully");
                                                         }
                                                     }
                                                 });
                                         return false;
                                     }
                                 }
        );
        dialog.setTitle("Reset Password ");
        dialog.setHint("Enter Email");

        dialog.show();


    }

    private void addToDb( final User user)
    {

        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("uid", user.getUID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG,"in unsc");
                        if( task.getResult().isEmpty())
                        {
                            FirebaseFirestore.getInstance().collection("users").add(user);
                            Log.d(TAG,"in unsc");
                        }}
                    }
                });
    }
    private void  saveDetails(User user)
    {

        SharedPreferences preferences=getSharedPreferences("profile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("name",user.getUname());
        editor.putString("email",user.getEmail());
        editor.putString("uid",user.getUID());
        editor.commit();
    }
    private void signInWithEmail() {
startProgressBar();
        EditText em= (EditText) findViewById(R.id.email);
        EditText ps= (EditText) findViewById(R.id.password);
        String email=em.getText().toString();
        String password=ps.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseFirestore.getInstance().collection("users").whereEqualTo("email",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                  String name= task.getResult().getDocuments().get(0).getString("uname");
                                    Log.d(TAG, "signInWithEmail:"+name);
                                    User user1=new User(user.getUid(),task.getResult().getDocuments().get(0).getString("uname"),user.getEmail(),user.isEmailVerified());
                                    SharedPreferences preferences=getSharedPreferences("profile",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferences.edit();
                                    editor.putString("name",user1.getUname());
                                    editor.putString("email",user1.getEmail());
                                    editor.putString("uid",user1.getUID());
                                    editor.commit();
                                }
                            });


                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                        stopProgressBar();
                    }
                });
    }
}
