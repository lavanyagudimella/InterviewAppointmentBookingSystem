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
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.student_fragments.StudentFragmentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by lavanyapalavancha on 10/19/17.
 */

public class StudentLoginActivity extends AppCompatActivity {

    private EditText studentloginEmail, studentloginPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);

        firebaseAuth = FirebaseAuth.getInstance();
        studentloginEmail = (EditText) findViewById(R.id.studentloginEmailEditText);
        studentloginPassword = (EditText) findViewById(R.id.studentPasswordEditText);

        progressBar = (ProgressBar) findViewById(R.id.studentloginProgressBar);
        progressBar.setVisibility(View.GONE);

        loginButton = (Button) findViewById(R.id.studentloginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentLogin();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Student Login");
        }
        checkConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_studentlogin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.StudentRegistration:
                Intent studentIntent = new Intent(StudentLoginActivity.this, MainActivity.class);
                startActivity(studentIntent);
                break;
            case R.id.AdminLogin:
                Intent adminIntent = new Intent(StudentLoginActivity.this, AdminLoginActivity.class);
                startActivity(adminIntent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void studentLogin() {
        checkConnection();
        String studentEmail = studentloginEmail.getText().toString().trim();
        String studentPassword = studentloginPassword.getText().toString().trim();

        TextUtils(studentEmail,studentPassword);
        EmailValidation(studentEmail);
        checkPassword(studentPassword);

        if(!studentEmail.equals("") && !studentPassword.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(studentEmail,studentPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithEmailAndPassword:onComplete:" + task.isSuccessful());
                        Toast.makeText(StudentLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        finish();
                        startActivity(new Intent(getApplicationContext(), StudentFragmentActivity.class));
                    } else {
                        Toast.makeText(StudentLoginActivity.this, "Login Failed" + "\n" +task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("TAG", "onComplete: Failed=" + task.getException().getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    //data validations
    private void TextUtils(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(StudentLoginActivity.this, "Please enter email, Fields can't be blank", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)) {
            Toast.makeText(StudentLoginActivity.this, "Please enter password, Fields can't be blank", Toast.LENGTH_SHORT).show();
        }
    }

    private void EmailValidation(String email) {
        String emailPattern = Patterns.EMAIL_ADDRESS.pattern();
        if(email.equals("")) {
            Toast.makeText(StudentLoginActivity.this, "Login Failed, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(!email.matches(emailPattern)) {
            Toast.makeText(StudentLoginActivity.this, "Login Failed, Please enter valid email address", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void checkPassword(String password) {
        if(password.equals("")) {
            Toast.makeText(StudentLoginActivity.this, "Login Failed, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(password.length()<6) {
            Toast.makeText(StudentLoginActivity.this, "Password must have atleast 6 characters", Toast.LENGTH_LONG).show();
            return;
        } else if(password.length()>20) {
            Toast.makeText(StudentLoginActivity.this, "Password is too long", Toast.LENGTH_LONG).show();
            return;
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
            Toast.makeText(StudentLoginActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
}

