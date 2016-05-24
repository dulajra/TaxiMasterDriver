package com.innocept.taximasterdriver.presenter;

import android.os.AsyncTask;
import android.support.annotation.IntegerRes;

import com.innocept.taximasterdriver.ApplicationPreferences;
import com.innocept.taximasterdriver.model.Communicator;
import com.innocept.taximasterdriver.model.foundation.Driver;
import com.innocept.taximasterdriver.ui.activity.DriverStateActivity;
import com.innocept.taximasterdriver.ui.activity.LoginActivity;

/**
 * Created by Dulaj on 16-Apr-16.
 */
public class LoginPresenter {

    private static LoginPresenter instance = null;
    private LoginActivity view;

    private LoginPresenter() {
    }

    public static LoginPresenter getInstance(){
        if(instance==null){
            instance = new LoginPresenter();
        }
        return instance;
    }

    public void setView(LoginActivity view) {
        this.view = view;
    }

    public void signIn(final String username, final String password){
        new AsyncTask<Void, Void, Integer>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                Communicator communicator = new Communicator();
                int resultCode = communicator.login(username, password);
                return resultCode;
            }

            @Override
            protected void onPostExecute(Integer resultCode) {
                super.onPostExecute(resultCode);
                switch (resultCode){
                    case 0:
                        view.onSignInSuccess();
                        break;
                    case 1:
                        view.onSignInFailed("Username or password is incorrect");
                        break;
                    case 2:
                        view.onSignInFailed("Username not exists");
                        break;
                    default:
                        view.onSignInFailed("Something went wrong. Try again!");
                }
            }
        }.execute();
    }
}
