package com.lunatialiens.incidentreportingsystem.views.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.models.PublicUser;
import com.lunatialiens.incidentreportingsystem.repository.CurrentDatabase;
import com.lunatialiens.incidentreportingsystem.repository.FirebaseDatabaseHelper;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;
import com.lunatialiens.incidentreportingsystem.utils.Constants;

import java.util.Objects;


/**
 * The type Login activity.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    /**
     * The Double back to exit pressed once.
     */
    boolean doubleBackToExitPressedOnce = false;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView forgotPasswordTextView;
    private Button loginButton;
    private TextView registerTextView;
    private String resetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeWidgets();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeWidgets() {

        emailEditText = findViewById(R.id.emailEditText);

        passwordEditText = findViewById(R.id.passwordEditText);

        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(this);
        forgotPasswordTextView.setOnTouchListener((view, motionEvent) -> {
            Animation click = AnimationUtils.loadAnimation(LoginActivity.this.getApplicationContext(),
                    R.anim.click);
            forgotPasswordTextView.startAnimation(click);
            return false;
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        loginButton.setOnTouchListener((view, motionEvent) -> {
            Animation click = AnimationUtils.loadAnimation(LoginActivity.this.getApplicationContext(),
                    R.anim.click);
            loginButton.startAnimation(click);
            return false;
        });

        registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(this);
        registerTextView.setOnTouchListener((view, motionEvent) -> {
            Animation click = AnimationUtils.loadAnimation(LoginActivity.this.getApplicationContext(),
                    R.anim.click);
            registerTextView.startAnimation(click);
            return false;
        });

    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AppUtils.success(getApplicationContext(), "Login Successful!");
                        AppUtils.saveDataInSharedPrefs(getApplicationContext(), Constants.EMAIL, email);
                        AppUtils.saveDataInSharedPrefs(getApplicationContext(), Constants.PASSWORD, password);

                        CurrentDatabase.setCurrentPublicUser(FirebaseDatabaseHelper.getPublicUserByEmail(email));
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        AppUtils.error(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
            } else
                AppUtils.error(getApplicationContext(), "Please fill both the fields to login");
        }

        if (v == forgotPasswordTextView) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Reset Password");

            LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("InflateParams") View alertDialogView = inflater.inflate(R.layout.input_email, null);
            builder.setView(alertDialogView);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(false);

            final EditText inputEditText = alertDialogView.findViewById(R.id.inputEditText);

            builder.setView(alertDialogView);

            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String dialogEmailAddress = inputEditText.getText().toString();
                dialog.cancel();
                if (AppUtils.validateEmail(dialogEmailAddress)) {
                    if (FirebaseDatabaseHelper.getPublicUserByEmail(dialogEmailAddress) != null) {
                        AppUtils.success(LoginActivity.this, "Password reset email sent to: " + dialogEmailAddress);
                        AppUtils.info(getApplicationContext(), " Email Sent");
                        confirmCode(dialogEmailAddress);

                    } else {
                        AppUtils.error(getApplicationContext(), "No user found");
                    }
                } else
                    AppUtils.warning(LoginActivity.this, "Incorrect email");

            });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

            builder.show();

        }

        if (v == registerTextView) {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        }
    }

    private void confirmCode(final String emailAdd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Enter Code");

        LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View alertDialogView = inflater.inflate(R.layout.input_desc, null);
        builder.setView(alertDialogView);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        final EditText codeEditText = alertDialogView.findViewById(R.id.inputEditText);

        builder.setView(alertDialogView);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String code = codeEditText.getText().toString();
            if (code.equals(resetCode)) {

                PublicUser publicUser = FirebaseDatabaseHelper.getPublicUserByEmail(emailAdd);
                if (publicUser != null) {
                    publicUser.setPassword(resetCode);

                    FirebaseDatabaseHelper.updatePublicUser(publicUser);
                    AppUtils.success(getApplicationContext(), "Reset Code is now your current password");
                } else {
                    AppUtils.error(getApplicationContext(), "Error occurred! Please try again");
                }
                dialog.cancel();
            } else {
                AppUtils.error(getApplicationContext(), "Invalid code");
            }

        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            AppUtils.info(getApplicationContext(), "Reset Failed");
            dialog.cancel();
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        AppUtils.warning(this, "Click BACK twice to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}

