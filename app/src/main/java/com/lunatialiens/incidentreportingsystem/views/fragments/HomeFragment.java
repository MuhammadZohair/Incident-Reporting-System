package com.lunatialiens.incidentreportingsystem.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.controller.IncidentAdapter;
import com.lunatialiens.incidentreportingsystem.repository.CurrentDatabase;
import com.lunatialiens.incidentreportingsystem.repository.FirebaseDatabaseHelper;

/**
 * The type Home fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private IncidentAdapter incidentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        TextView nameTextView = v.findViewById(R.id.tv_name);
        if (CurrentDatabase.getCurrentPublicUser() != null) {
            nameTextView.setText(CurrentDatabase.getCurrentPublicUser().getName());
        }

        recyclerView = v.findViewById(R.id.rv_incidents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        incidentAdapter = new IncidentAdapter(getContext(), FirebaseDatabaseHelper.getIncidentArrayList());
        recyclerView.setAdapter(incidentAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
