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
import com.cagriyorguner.geotask.Models.User;
import com.cagriyorguner.geotask.R;

public class SignUpActivity extends AppCompatActivity {

    EditText password, username;
    ProgressBar progressBar;
    TextView loginButton;
    Button signupButton;
    ImageButton visibilityButton;

    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = RoomDB.getInstance(this);

        password = findViewById(R.id.signup_password);
        username = findViewById(R.id.signup_username);
        progressBar = findViewById(R.id.signup_progress_bar);
        loginButton = findViewById(R.id.signup_login_button);
        signupButton = findViewById(R.id.signup_button);
        visibilityButton = findViewById(R.id.signup_visibility_button);

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

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
                hideKeyboard(SignUpActivity.this);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }

    private void register() {
        String username_var = username.getText().toString().trim();
        String password_var = password.getText().toString().trim();

        if (username_var.isEmpty()) {
            username.setError("Please fill in this field.");
            username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password_var)) {
            password.setError("Please fill in this field.");
            password.requestFocus();
            return;
        }

        if (password_var.length() < 8) {
            password.setError("Minimum character number is 8.");
            password.requestFocus();
            return;
        }

        if(database.mainDao().getUserWithThisUsername(username_var) != null){
            username.setError("This username is taken.");
            username.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        signupButton.setText("");

        //creating user object
        User user = new User();
        user.setPassword(password_var);
        user.setUsername(username_var);
        user.setAuthenticated(true);

        //saving to database
        database.mainDao().insertUser(user);
        password.getText().clear();
        username.getText().clear();
        finish();
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
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