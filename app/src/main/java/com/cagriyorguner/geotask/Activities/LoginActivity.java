package com.cagriyorguner.geotask.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cagriyorguner.geotask.Database.RoomDB;
import com.cagriyorguner.geotask.R;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    ProgressBar progressBar;
    TextView signupButton;
    Button loginButton;
    ImageButton visibilityButton;

    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = RoomDB.getInstance(this);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        signupButton = findViewById(R.id.login_sign_up_button);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.login_progress_bar);
        visibilityButton = findViewById(R.id.login_visibility_button);

        final int[] visibilityState = {1, 0};

        //for password visibility on/off
        visibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibilityState[0] == 1){
                    v.setBackgroundResource(R.drawable.ic_visibility_on);
                    password.setTransformationMethod(null);
                    password.setSelection(password.getText().length());
                    visibilityState[0] = 0;
                    visibilityState[1] = 1;
                }
                else if(visibilityState[1] == 1){
                    v.setBackgroundResource(R.drawable.ic_visibility_off);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    password.setSelection(password.getText().length());
                    visibilityState[0] = 1;
                    visibilityState[1] = 0;
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalLogIn();
                hideKeyboard(LoginActivity.this);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if there is an authenticated user in database, go to MainActivity.
        if(database.mainDao().getAuthenticatedUser(true) != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void NormalLogIn(){
        final String username_var = username.getText().toString();
        final String password_var = password.getText().toString();

        if (TextUtils.isEmpty(username_var)) {
            return;
        }

        if (TextUtils.isEmpty(password_var)) {
            return;
        }

        if (password_var.length() < 8) {
            password.setError("Şifre en az 8 haneli olmalı");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setText("");

        //Get username/password matched user's id
        int userId = database.mainDao().getToBeLoggedInUserId(username_var, password_var);

        if(userId != 0){
            //Authenticate user
            database.mainDao().authenticateUser(true, userId);
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginButton.setText(R.string.login_button_text);
        }

    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}