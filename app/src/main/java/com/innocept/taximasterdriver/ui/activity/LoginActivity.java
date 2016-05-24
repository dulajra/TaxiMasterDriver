package com.innocept.taximasterdriver.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.innocept.taximasterdriver.R;
import com.innocept.taximasterdriver.model.foundation.Driver;
import com.innocept.taximasterdriver.presenter.DriverStatePresenter;
import com.innocept.taximasterdriver.presenter.LoginPresenter;

/**
 * Created by Dulaj on 16-Apr-16.
 */

/**
 * User login here
 */
public class LoginActivity extends AppCompatActivity {

    LoginPresenter loginPresenter;;

    private Toolbar toolbar;
    private EditText inputUserName, inputPassword;
    private TextInputLayout inputLayoutUserName, inputLayoutPassword;
    private Button btnSignIn;
    CoordinatorLayout coordinatorLayoutParent;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (loginPresenter == null) {
            loginPresenter = LoginPresenter.getInstance();
        }
        loginPresenter.setView(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        setSupportActionBar(toolbar);

        inputLayoutUserName = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputUserName = (EditText) findViewById(R.id.input_username);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        coordinatorLayoutParent = (CoordinatorLayout)findViewById(R.id.coordinator_parent);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_sign_in);

        inputUserName.addTextChangedListener(new MyTextWatcher(inputUserName));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(inputPassword.getWindowToken(), 0);

//        Call sign in logic here
        signIn();
    }

    private boolean validateName() {
        if (inputUserName.getText().toString().trim().isEmpty()) {
            inputLayoutUserName.setError(getString(R.string.err_msg_username));
            requestFocus(inputUserName);
            return false;
        } else {
            inputLayoutUserName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_username:
                    validateName();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    public void signIn(){
        progressBar.setVisibility(View.VISIBLE);
        loginPresenter.signIn(inputUserName.getText().toString().trim(), inputPassword.getText().toString().trim());
    }

    public void onSignInSuccess(){
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(LoginActivity.this, DriverStateActivity.class));
        this.finish();
    }


    public void onSignInFailed(String error){
        progressBar.setVisibility(View.GONE);
        Snackbar.make(coordinatorLayoutParent, error, Snackbar.LENGTH_LONG).show();
    }
}
