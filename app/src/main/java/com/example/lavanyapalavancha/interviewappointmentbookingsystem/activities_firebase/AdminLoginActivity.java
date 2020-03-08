package com.example.lavanyapalavancha.interviewappointmentbookingsystem.activities_firebase;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lavanyapalavancha.interviewappointmentbookingsystem.R;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.admin_fragments.AdminFragmentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by lavanyapalavancha on 10/19/17.
 */

public class AdminLoginActivity extends AppCompatActivity {

    private EditText adminloginEmail, adminloginPassword;
    private Button adminloginButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        firebaseAuth = FirebaseAuth.getInstance();

        adminloginEmail = (EditText) findViewById(R.id.adminloginEmailEditText);
        adminloginPassword = (EditText) findViewById(R.id.adminloginPasswordEditText);

        progressBar = (ProgressBar) findViewById(R.id.adminloginprogressBar);
        progressBar.setVisibility(View.GONE);

        adminloginButton = (Button) findViewById(R.id.adminloginButton);
        adminloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminlogin();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Admin Login");
        }
        checkConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_adminlogin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.StudentLogin:
                Intent studentIntent = new Intent(AdminLoginActivity.this, StudentLoginActivity.class);
                startActivity(studentIntent);
                break;
            case R.id.StudentRegistration:
                Intent studentRegistration = new Intent(AdminLoginActivity.this, MainActivity.class);
                startActivity(studentRegistration);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void adminlogin() {
        checkConnection();
        String email = "bhagyasree.gudimella@gmail.com";
        String adminEmail = adminloginEmail.getText().toString().trim();
        String adminPassword = adminloginPassword.getText().toString().trim();

        TextUtils(adminEmail,adminPassword);
        EmailValidation(adminEmail);
        checkPassword(adminPassword);

        if(!adminEmail.equals("") && !adminPassword.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            if(adminEmail.equals(email)) {
                firebaseAuth.signInWithEmailAndPassword(adminEmail,adminPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmailAndPassword:onComplete:" + task.isSuccessful());
                            Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            finish();
                            startActivity(new Intent(getApplicationContext(), AdminFragmentActivity.class));
                        } else {
                            Toast.makeText(AdminLoginActivity.this, "Login Failed" + "\n" +task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("TAG", "onComplete: Failed=" + task.getException().getMessage());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                Toast.makeText(AdminLoginActivity.this, "Sorry! You don't have the Admin access", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    //data validations
    private void TextUtils(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(AdminLoginActivity.this, "Please enter email, Fields can't be blank", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)) {
            Toast.makeText(AdminLoginActivity.this, "Please enter password, Fields can't be blank", Toast.LENGTH_SHORT).show();
        }
    }

    private void EmailValidation(String email) {
        String emailPattern = Patterns.EMAIL_ADDRESS.pattern();
        if(email.equals("")) {
            Toast.makeText(AdminLoginActivity.this, "Login Failed, Fields can't be blank", Toast.LENGTH_LONG).show();
        } else if(!email.matches(emailPattern)) {
            Toast.makeText(AdminLoginActivity.this, "Login Failed, Please enter valid email address", Toast.LENGTH_LONG).show();
        }
    }

    private void checkPassword(String password) {
        if(password.equals("")) {
            Toast.makeText(AdminLoginActivity.this, "Login Failed, Fields can't be blank", Toast.LENGTH_LONG).show();
        } else if(password.length()<6) {
            Toast.makeText(AdminLoginActivity.this, "Password must have atleast 6 characters", Toast.LENGTH_LONG).show();
        } else if(password.length()>20) {
            Toast.makeText(AdminLoginActivity.this, "Password is too long", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if(activeNetwork.isConnectedOrConnecting()) {
                //Toast.makeText(MainActivity.this, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                if(activeNetwork.isConnectedOrConnecting()) {
                    return true;
                }
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if(activeNetwork.isConnectedOrConnecting()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkConnection(){
        if(!checkNetworkConnection()){
            Toast.makeText(AdminLoginActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
