package com.lunatialiens.incidentreportingsystem.views.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.models.PublicUser;
import com.lunatialiens.incidentreportingsystem.repository.CurrentDatabase;
import com.lunatialiens.incidentreportingsystem.repository.FirebaseDatabaseHelper;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;
import com.lunatialiens.incidentreportingsystem.utils.Constants;
import com.lunatialiens.incidentreportingsystem.views.activities.LoginActivity;


/**
 * The type Settings fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText passwordEditText;
    private Button updateButton;

    private PublicUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        user = CurrentDatabase.getCurrentPublicUser();
        initializeWidgets(v);
        return v;
    }

    private void initializeWidgets(View v) {
        passwordEditText = v.findViewById(R.id.et_pasword);

        updateButton = v.findViewById(R.id.btn_update);
        updateButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == updateButton) {
            String password = passwordEditText.getText().toString();


            if (!TextUtils.isEmpty(password)) {
                user.setPassword(password);
                SharedPreferences preferences2 = getActivity().getSharedPreferences(Constants.CREDENTIALS, 0);
                preferences2.edit().remove(Constants.EMAIL).apply();
                preferences2.edit().remove(Constants.PASSWORD).apply();

                CurrentDatabase.setCurrentPublicUser(null);
                FirebaseDatabaseHelper.changePassword(password);
                getActivity().finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
                AppUtils.success(getContext(), "Password updated");

            } else AppUtils.warning(getContext(), "Password cannot be empty");

        }
    }
}
