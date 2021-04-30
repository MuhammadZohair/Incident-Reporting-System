package com.lunatialiens.incidentreportingsystem.views.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.models.Incident;
import com.lunatialiens.incidentreportingsystem.repository.CurrentDatabase;
import com.lunatialiens.incidentreportingsystem.repository.FirebaseDatabaseHelper;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * The type Maps fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    /**
     * The constant MY_PERMISSIONS_REQUEST_LOCATION.
     */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    /**
     * The constant REQUEST_CHECK_SETTINGS.
     */
    public static final int REQUEST_CHECK_SETTINGS = 0x1;

    private GoogleMap mGoogleMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean triggered = true;

    private LatLng pointedLocation;
    private ProgressDialog progressDialog;
    private TextView currentLocationTextView;
    private ImageView goImageView;

    /**
     * Instantiates a new Maps fragment.
     */
    public MapsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        checkLocationPermission();
        displayLocationSettingsRequest(getContext());

        initializeWidgets(rootView);

        return rootView;
    }

    private void initializeWidgets(View view) {
        currentLocationTextView = view.findViewById(R.id.currentLocationLayout);

        goImageView = view.findViewById(R.id.goImageView);
        goImageView.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnCameraIdleListener(onCameraIdleListener);

            setMarkersOnMap(googleMap);

        } else {
            checkLocationPermission();
        }
    }

    private void setMarkersOnMap(GoogleMap googleMap) {
        googleMap.clear();
        ArrayList<Incident> incidentArrayList = FirebaseDatabaseHelper.getIncidentArrayList();

        for (int i = 0; i < incidentArrayList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(AppUtils.parseLocation(incidentArrayList.get(i).getLocation()));
            markerOptions.icon(AppUtils.bitmapDescriptorFromVector(getContext(), R.drawable.ic_marker_pin));
            markerOptions.title(incidentArrayList.get(i).getDesc());
            googleMap.addMarker(markerOptions);
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        // open a progress dialog until the map is not loaded properly

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        // set location values
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        // get the results
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    mapFragment =
                            (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
                    mapFragment.getMapAsync(MapsFragment.this::onMapReady);
                    setOnCameraIdleListener();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        if (status.isSuccess()) {
                            mapFragment =
                                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
                            mapFragment.getMapAsync(MapsFragment.this::onMapReady);
                            setOnCameraIdleListener();
                        }
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    getActivity().finish();
                    break;
            }
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.ic_launcher_foreground)
                        .setTitle(R.string.app_name)
                        .setMessage("App needs to access the maps, please accept to use location functionality")
                        .setPositiveButton("Agree", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .setNegativeButton("Disagree", (dialogInterface, i) -> getActivity().finish())
                        .setCancelable(false)
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void setOnCameraIdleListener() {
        onCameraIdleListener = () -> {
            LatLng latLng = mGoogleMap.getCameraPosition().target;
            pointedLocation = latLng;
            Geocoder geocoder = new Geocoder(getContext());

            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                if (addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    currentLocationTextView.setText(address);
                    goImageView.setClickable(true);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        if (mLastLocation != null) {
            mLastLocation = location;
        }
        if (triggered) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            pointedLocation = latLng;

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                currentLocationTextView.setText(address);

                //load the traffic now
                mGoogleMap.setTrafficEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();

            }
            triggered = false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    mGoogleMap.setMyLocationEnabled(true);
                }

            } else {
                AppUtils.error(getActivity(), "Permission Denied");
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == goImageView) {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
            builder.setTitle("Enter Details");

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("InflateParams") View alertDialogView = inflater.inflate(R.layout.input_desc, null);
            builder.setView(alertDialogView);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(false);

            final EditText descEditText = alertDialogView.findViewById(R.id.et_desc);

            builder.setView(alertDialogView);

            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String desc = descEditText.getText().toString();
                String location = currentLocationTextView.getText().toString().trim();
                if (!location.isEmpty() && !desc.isEmpty()) {
                    Incident incident = new Incident();
                    incident.setUserId(CurrentDatabase.getCurrentPublicUser().getUserId());
                    incident.setLocation(AppUtils.round(pointedLocation.latitude) + "," + AppUtils.round(pointedLocation.longitude));
                    incident.setDesc(desc);
                    incident.setTimestamp(System.currentTimeMillis());
                    AppUtils.success(getContext(), "Incident reported successfully");
                    FirebaseDatabaseHelper.createIncident(incident);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(pointedLocation);
                    markerOptions.icon(AppUtils.bitmapDescriptorFromVector(getContext(), R.drawable.ic_marker_pin));
                    markerOptions.title(incident.getDesc());
                    mGoogleMap.addMarker(markerOptions);
                } else {
                    AppUtils.info(getContext(), "Invalid data");
                    dialog.cancel();
                }

            });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                AppUtils.info(getContext(), "Please write some text");
                dialog.cancel();
            });

            builder.show();
        }
    }
}
