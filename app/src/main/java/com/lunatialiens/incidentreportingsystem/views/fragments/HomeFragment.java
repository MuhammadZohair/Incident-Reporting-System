package com.lunatialiens.incidentreportingsystem.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.repository.CurrentDatabase;

import org.joda.time.DateTime;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private TextView nameTextView;

    private ImageView leftArrowImageView;
    private TextView currentDayTextView;
    private ImageView rightArrowImageView;

    private FloatingActionButton addFloatingActionButton;

    private DateTime currentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        currentDate = new DateTime();

        nameTextView = v.findViewById(R.id.tv_name);

        leftArrowImageView = v.findViewById(R.id.iv_left_arrow);
        currentDayTextView = v.findViewById(R.id.tv_current_day);
        rightArrowImageView = v.findViewById(R.id.iv_right_arrow);

        if (CurrentDatabase.getCurrentPublicUser() != null) {
            nameTextView.setText(CurrentDatabase.getCurrentPublicUser().getName());
        }

        addFloatingActionButton = v.findViewById(R.id.fab_add);
        addFloatingActionButton.setOnClickListener(this);

        leftArrowImageView.setOnClickListener(this);
        rightArrowImageView.setOnClickListener(this);

        currentDayTextView.setText(currentDate.toString("dd/MM/yyyy"));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        if (v == addFloatingActionButton) {
        } else if (v == leftArrowImageView) {

        } else if (v == rightArrowImageView) {
            currentDate = currentDate.plusDays(1);
            currentDayTextView.setText(currentDate.toString("dd/MM/yyyy"));
        }
    }
}
