package com.innocept.taximasterdriver.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.innocept.taximasterdriver.ApplicationPreferences;
import com.innocept.taximasterdriver.R;
import com.innocept.taximasterdriver.model.Communicator;
import com.innocept.taximasterdriver.model.foundation.Driver;

/**
 * Created by Dulaj on 24-Mar-16.
 */
public class MyPreferenceFragment extends PreferenceFragment {

    private TextInputLayout textInputLayoutOldPassword;
    private TextInputLayout textInputLayoutNewPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;

    Preference preferenceFullName;
    Preference preferencePhone;
    Preference preferencePassword;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        preferenceFullName = (Preference) findPreference("fullName");
        preferencePhone = (Preference) findPreference("phone");
        preferencePassword = (Preference) findPreference("password");

        Driver driver = ApplicationPreferences.getDriver();

        preferenceFullName.setSummary(driver.getFirstName() + " " + driver.getLastName());
        preferencePhone.setSummary(driver.getPhone());

        preferencePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                changePassword();
                return false;
            }
        });


    }

    public void changePassword() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View alertDialogView = inflater.inflate(R.layout.inflater_alert_dialog_change_password, null);

        textInputLayoutOldPassword = (TextInputLayout) alertDialogView.findViewById(R.id.input_layout_old_password);
        textInputLayoutNewPassword = (TextInputLayout) alertDialogView.findViewById(R.id.input_layout_new_password);
        textInputLayoutConfirmPassword = (TextInputLayout) alertDialogView.findViewById(R.id.input_layout_confirm_password);
        editTextOldPassword = (EditText) alertDialogView.findViewById(R.id.edit_old_password);
        editTextNewPassword = (EditText) alertDialogView.findViewById(R.id.edit_new_password);
        editTextConfirmPassword = (EditText) alertDialogView.findViewById(R.id.edit_confirm_password);

        final AlertDialog changePasswordAlertDialog = new AlertDialog.Builder(getActivity())
                .setView(alertDialogView)
                .setCancelable(false)
                .setTitle("Change Password")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Change", null)
                .create();
        changePasswordAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String oldPassword = editTextOldPassword.getText().toString();
                        final String newPassword = editTextNewPassword.getText().toString();
                        String confirmPassword = editTextConfirmPassword.getText().toString();

                        if (oldPassword.trim().length() < 6) {
                            textInputLayoutOldPassword.setError("Min length is 6");
                        } else if (newPassword.trim().length() < 6) {
                            textInputLayoutOldPassword.setErrorEnabled(false);
                            textInputLayoutNewPassword.setError("Min length is 6");
                        } else if (!confirmPassword.equals(newPassword)) {
                            textInputLayoutOldPassword.setErrorEnabled(false);
                            textInputLayoutNewPassword.setErrorEnabled(false);
                            textInputLayoutConfirmPassword.setError("Passwords don't match");
                        } else {
                            new AsyncTask<Void, Void, Void>() {

                                ProgressDialog progressDialog;
                                int result;

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog = new ProgressDialog(getActivity());
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                }

                                @Override
                                protected Void doInBackground(Void... params) {
                                    result = new Communicator().changePassword(ApplicationPreferences.getDriver().getUsername(), oldPassword, newPassword);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    progressDialog.dismiss();
                                    if (result == 0) {
                                        Toast.makeText(getActivity(), "Password changed", Toast.LENGTH_SHORT).show();
                                        changePasswordAlertDialog.dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), "Old password is incorrect", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }.execute();
                        }
                    }
                });
            }
        });
        changePasswordAlertDialog.show();
    }
}