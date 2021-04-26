package com.lunatialiens.incidentreportingsystem.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.repository.CurrentDatabase;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton addFloatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        TextView nameTextView = v.findViewById(R.id.tv_name);
        if (CurrentDatabase.getCurrentPublicUser() != null) {
            nameTextView.setText(CurrentDatabase.getCurrentPublicUser().getName());
        }

        addFloatingActionButton = v.findViewById(R.id.fab_add);
        addFloatingActionButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        if (v == addFloatingActionButton) {
        }
    }
}
