package com.example.chatz;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatz.Models.Users;
import com.example.chatz.databinding.ActivitySignupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    ActivitySignupBinding binding;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    DBhelper dBhelper;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        dBhelper = new DBhelper(this);
        dBhelper.getReadableDatabase();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are Creating Your Account");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String mail = binding.email.getText().toString();
                String code = binding.Password.getText().toString();
                String name = binding.username.getText().toString();
                progressDialog.show();
                if(mail.equals("")||code.equals("")||name.equals(""))
                {
                    progressDialog.dismiss();
                    Toast.makeText(signup.this, "You have not entered the username", Toast.LENGTH_SHORT).show();
                }
                else{

                    auth.createUserWithEmailAndPassword(binding.email.getText().toString(),binding.Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                        if(task.isSuccessful())
                        {

                                Users user = new Users(binding.username.getText().toString(),binding.email.getText().toString(),
                                        binding.Password.getText().toString());
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(user);
                                Toast.makeText(signup.this, "User Created Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(signup.this,MainActivity.class);
                                startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        }
                    });
                }
            }
        });
        binding.alreadyHavAcc.setOnClickListener(view -> {
            Intent intent = new Intent(this,signIn.class);
            startActivity(intent);
            finish();
        });
        binding.googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUP();
            }
        });
    }
        int RC_SIGN_UP = 65;
        private void signUP() {
            Intent signUPIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signUPIntent, RC_SIGN_UP);
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_UP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle: "+ account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(signup.this, "Signing in with Google......", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(signup.this, MainActivity.class);
                            startActivity(intent);
                            Users users = new Users();
                            users.setUserid(user.getUid());
                            users.setEmail(user.getEmail());
                            users.setUsername(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            // ...
                        }
                    }
                });
    }
}