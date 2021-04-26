package com.lunatialiens.incidentreportingsystem.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.models.PublicUser;
import com.lunatialiens.incidentreportingsystem.repository.FirebaseDatabaseHelper;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;
    private EditText phoneNumberEditText;
    private Button registerButton;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeWidgets();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeWidgets() {

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.usernameEditText);

        phoneNumberEditText = findViewById(R.id.phoneEditText);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        registerButton.setOnTouchListener((view, motionEvent) -> {
            Animation click = AnimationUtils.loadAnimation(SignupActivity.this.getApplicationContext(),
                    R.anim.click);
            registerButton.startAnimation(click);
            return false;
        });

        loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);
        loginTextView.setOnTouchListener((view, motionEvent) -> {
            Animation click = AnimationUtils.loadAnimation(SignupActivity.this.getApplicationContext(),
                    R.anim.click);
            loginTextView.startAnimation(click);
            return false;
        });

    }


    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                    && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(username)) {
                if (FirebaseDatabaseHelper.getPublicUserByEmail(email) == null) {
                    PublicUser user = new PublicUser();
                    user.setEmailAddress(email);
                    user.setPassword(password);
                    user.setPhoneNumber(phoneNumber);
                    user.setName(username);

                    FirebaseDatabaseHelper.createPublicUser(user, null);
                    Toasty.success(getApplicationContext(), "Account Created Successfully", Toasty.LENGTH_SHORT, true).show();
                    emailEditText.setText("");
                    passwordEditText.setText("");
                    phoneNumberEditText.setText("");
                    usernameEditText.setText("");
                    finish();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));

                } else {
                    Toasty.info(getApplicationContext(), "User already exist, please login", Toasty.LENGTH_SHORT, true).show();
                    emailEditText.setText("");
                    passwordEditText.setText("");
                    phoneNumberEditText.setText("");
                    usernameEditText.setText("");
                }
            } else {
                Toasty.error(getApplicationContext(), "Please fill all the fields", Toasty.LENGTH_SHORT, true).show();
            }
        }

        if (v == loginTextView) {
            finish();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
    }
}
