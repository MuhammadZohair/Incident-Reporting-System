package com.lunatialiens.incidentreportingsystem.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.models.Incident;
import com.lunatialiens.incidentreportingsystem.repository.CurrentDatabase;
import com.lunatialiens.incidentreportingsystem.repository.FirebaseDatabaseHelper;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;
import com.theartofdev.edmodo.cropper.CropImage;

/**
 * The type Add marker activity.
 */
public class AddMarkerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView incidentImageView;
    private EditText descEditText;
    private Button saveButton;

    private String location;
    private LatLng pointedLocation;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        location = getIntent().getStringExtra("LOCATION");
        pointedLocation = new LatLng(getIntent().getDoubleExtra("LAT", 0), getIntent().getDoubleExtra("LNG", 0));

        incidentImageView = findViewById(R.id.iv_incident);
        incidentImageView.setOnClickListener(this);

        descEditText = findViewById(R.id.et_desc);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == incidentImageView) {
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .start();
        } else if (view == saveButton) {
            String desc = descEditText.getText().toString();
            if (!location.isEmpty() && !desc.isEmpty() && imageUri != null) {
                Incident incident = new Incident();
                incident.setUserId(CurrentDatabase.getCurrentPublicUser().getUserId());
                incident.setLocation(AppUtils.round(pointedLocation.latitude) + "," + AppUtils.round(pointedLocation.longitude));
                incident.setDesc(desc);
                incident.setTimestamp(System.currentTimeMillis());
                AppUtils.success(getApplicationContext(), "Incident reported successfully");
                FirebaseDatabaseHelper.createIncident(incident);
                AppUtils.uploadIncidentsImage(incident.getIncidentId(), imageUri);
                finish();

            } else {
                AppUtils.info(getApplicationContext(), "Invalid data");
            }
        }
    }

    /**
     * This method is called when the camera / gallery returns the image
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                incidentImageView.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}