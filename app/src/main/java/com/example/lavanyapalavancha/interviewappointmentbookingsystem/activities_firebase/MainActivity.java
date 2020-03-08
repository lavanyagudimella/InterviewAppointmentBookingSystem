package com.example.lavanyapalavancha.interviewappointmentbookingsystem.activities_firebase;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText studentEmail, studentPassword, checkPassword;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        studentEmail = (EditText) findViewById(R.id.emailEditText);
        studentPassword = (EditText) findViewById(R.id.passwordEditText);
        checkPassword = (EditText) findViewById(R.id.checkpasswordEditText);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Student Registration");
        }
        checkConnection();
    }

    private void studentRegistration() {
        checkConnection();
        String email = studentEmail.getText().toString().trim();
        String password = studentPassword.getText().toString().trim();
        String cPassword = checkPassword.getText().toString().trim();

        String emailPattern = Patterns.EMAIL_ADDRESS.pattern();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Please enter email, Fields can't be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Please enter password, Fields can't be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.equals("") && password.equals("")) {
            Toast.makeText(MainActivity.this, "Registration Fields can't be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (!email.matches(emailPattern)) {
            Toast.makeText(MainActivity.this, "Please enter valid email address", Toast.LENGTH_LONG).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(MainActivity.this, "Password must have atleast 6 characters", Toast.LENGTH_LONG).show();
        } else if (password.length() > 20) {
            Toast.makeText(MainActivity.this, "Password is too long", Toast.LENGTH_LONG).show();
        } else if (!password.matches(cPassword)) {
            Toast.makeText(MainActivity.this, "Please match both the passwords", Toast.LENGTH_LONG).show();
            return;
        }

        if (!email.equals("") && !password.equals("") && !cPassword.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(MainActivity.this, "Registration Successful, Please Login", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(MainActivity.this, "Registration Failed" + "\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("TAG", "onComplete: Failed=" + task.getException().getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == registerButton) {
            studentRegistration();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.StudentLogin:
                Intent studentIntent = new Intent(MainActivity.this, StudentLoginActivity.class);
                startActivity(studentIntent);
                break;
            case R.id.AdminLogin:
                Intent adminIntent = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(adminIntent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkConnection(){
        if(!checkNetworkConnection()){
            Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
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
}
