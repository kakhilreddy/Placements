package com.cse.jntu.placements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cse.jntu.placements.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import viethoa.com.snackbar.BottomSnackBarMessage;
import viethoa.com.snackbar.SnackBar;

public class SignUp extends AppCompatActivity  implements View.OnClickListener{

    EditText email,password,confirmPassword,uname;
    Button register,login;
    private int mStatusCode=0;
    private FirebaseAuth mAuth;
    private ProgressDialog progressBar;
    void startProgressBar()
    {
        progressBar.show();
    }
    void stopProgressBar()
    {
        progressBar.cancel();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email= (EditText) findViewById(R.id.email);
uname= (EditText) findViewById(R.id.uname);
        password= (EditText) findViewById(R.id.password);
        confirmPassword= (EditText) findViewById(R.id.confirmpassword);
        register= (Button) findViewById(R.id.btnRegister);
        login= (Button) findViewById(R.id.btnLinkToLoginScreen);
        register.setOnClickListener(this);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Signing Up ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        login.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
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
                            Log.d("google","in unsc");
                            if( task.getResult().isEmpty())
                            {
                                FirebaseFirestore.getInstance().collection("users").add(user);
                                Log.d("google","in unsc");
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
    private void register(String email,String password)
    {
        startProgressBar();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "createUserWithEmail:success");
                          final  FirebaseUser user = mAuth.getCurrentUser();


                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                BottomSnackBarMessage bottomSnackBarMessage = new BottomSnackBarMessage(SignUp.this);
                                                bottomSnackBarMessage.showSuccessMessage("Verification email sent");
                                                Log.d("email", "Email sent.");
                                                User user1=new User(user.getUid(),uname.getText().toString(),user.getEmail(),false);
                                                addToDb(user1);
                                                //updateUI(user);
                                                saveDetails(user1);
                                                stopProgressBar();
                                                finish();
                                            }
                                        }
                                    });


                          //  Toast.makeText(SignUp.this,"Welcome",Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            BottomSnackBarMessage bottomSnackBarMessage = new BottomSnackBarMessage(SignUp.this);
                            bottomSnackBarMessage.showSuccessMessage("Sign Up failed!");

                          //  updateUI(null);
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });

    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.btnRegister:
                //validate();
                final String EMAIL=email.getText().toString();

                final String pass=password.getText().toString();
                String conpass=confirmPassword.getText().toString();
                if(!EMAIL.trim().isEmpty()   && !pass.trim().isEmpty() && !conpass.trim().isEmpty() &&!EMAIL.equals("")  && !pass.equals("") && !conpass.equals("")) {
                    if (pass.equals(conpass)) {
register(EMAIL,pass);
                    } else {
                        BottomSnackBarMessage bottomSnackBarMessage = new BottomSnackBarMessage(this);
                        bottomSnackBarMessage.showSuccessMessage("Passwords dont match!");

                    }
                }else
                {
                    BottomSnackBarMessage bottomSnackBarMessage = new BottomSnackBarMessage(this);
                    bottomSnackBarMessage.showSuccessMessage("Enter Details");


                }

                break;
            case R.id.btnLinkToLoginScreen:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }


}
