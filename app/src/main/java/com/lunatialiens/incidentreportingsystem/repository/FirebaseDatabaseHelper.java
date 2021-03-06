package com.lunatialiens.incidentreportingsystem.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lunatialiens.incidentreportingsystem.models.Incident;
import com.lunatialiens.incidentreportingsystem.models.PublicUser;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The type Firebase database helper.
 */
public class FirebaseDatabaseHelper {

    private static final String TAG = "FirebaseDatabaseHelper";

    private static FirebaseAuth firebaseAuth; // this is the firebase authentication object by which we check if user can login
    private static DatabaseReference publicUserEndPoint; // this is the path of the database folder for the users
    private static DatabaseReference incidentEndPoint; // this is the path of the database folder for the incidents

    private static ArrayList<PublicUser> publicUserArrayList; // the whole array list of users
    private static ArrayList<Incident> incidentArrayList; // the whole array list containing all the incidents

    /**
     * Instantiates a new Firebase database helper.
     */
    public FirebaseDatabaseHelper() {

        firebaseAuth = FirebaseAuth.getInstance(); // instantiate the object
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // firebase initailization
        publicUserEndPoint = firebaseDatabase.getReference("public_user_table"); // linking the end point to the folder
        incidentEndPoint = firebaseDatabase.getReference("incidents_table"); // linking the end point to the folder

        publicUserArrayList = getAllPublicUsersFromFirebase(); // fetching all the users from the database
        incidentArrayList = getAllIncidentsFromFirebase(); // fetching all the incidents from the database
    }

    /**
     * @return the list of all users fetched from the database
     */
    private static ArrayList<PublicUser> getAllPublicUsersFromFirebase() {
        final ArrayList<PublicUser> publicUsers = new ArrayList<>();
        readData(publicUserEndPoint, dataSnapshot -> {
            for (DataSnapshot s : dataSnapshot.getChildren()) {
                publicUsers.add(s.getValue(PublicUser.class));
            }
        });
        return publicUsers;
    }

    /**
     * @return the list of all the incidents from the database
     */
    private static ArrayList<Incident> getAllIncidentsFromFirebase() {
        final ArrayList<Incident> incidents = new ArrayList<>();
        readData(incidentEndPoint, dataSnapshot -> {
            for (DataSnapshot s : dataSnapshot.getChildren()) {
                incidents.add(s.getValue(Incident.class));
            }
        });
        return incidents;
    }

    /**
     * Gets incident array list.
     *
     * @return the incident array list
     */
    public static ArrayList<Incident> getIncidentArrayList() {
        return incidentArrayList;
    }

    /**
     * Create public user.
     *
     * @param publicUser the public user
     * @param uri        the uri
     */
    /* *********************************** PUBLIC USER CRUD ********************************** */
    public static void createPublicUser(final PublicUser publicUser, Uri uri) {
        firebaseAuth.createUserWithEmailAndPassword(publicUser.getEmailAddress(), publicUser.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "Auth Created");
                        publicUser.setUserId(firebaseAuth.getCurrentUser().getUid());
                        publicUserEndPoint.child(publicUser.getUserId()).
                                setValue(publicUser).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                if (uri != null) {
                                    AppUtils.uploadImage(publicUser.getUserId(), uri);
                                    Log.e(TAG, "Database entry created");
                                    publicUserArrayList.add(publicUser);
                                }
                            } else {
                                Log.e(TAG, task1.getException().toString());
                                Objects.requireNonNull(firebaseAuth.getCurrentUser()).delete();
                            }
                        });
                    } else Log.e(TAG, task.getException().toString());
                });
    }

    /**
     * Gets public user by email.
     *
     * @param email the email
     * @return the public user by email
     */
    public static PublicUser getPublicUserByEmail(String email) {
        for (int i = 0; i < publicUserArrayList.size(); i++) {
            if (publicUserArrayList.get(i).getEmailAddress().equals(email))
                return publicUserArrayList.get(i);
        }
        return null;
    }

    /**
     * Gets public user by email and password.
     *
     * @param email    the email
     * @param password the password
     * @return the public user by email and password
     */
    public static PublicUser getPublicUserByEmailAndPassword(String email, String password) {
        for (int i = 0; i < publicUserArrayList.size(); i++) {
            if (publicUserArrayList.get(i).getEmailAddress().equals(email)
                    && publicUserArrayList.get(i).getPassword().equals(password)) {
                return publicUserArrayList.get(i);
            }
        }
        return null;
    }

    /**
     * Gets public user by id.
     *
     * @param id the id
     * @return the public user by id
     */
    public static PublicUser getPublicUserByID(String id) {
        for (int i = 0; i < publicUserArrayList.size(); i++) {
            if (publicUserArrayList.get(i).getUserId().equals(id))
                return publicUserArrayList.get(i);
        }
        return null;
    }

    /**
     * Update public user.
     *
     * @param publicUser the public user
     */
    public static void updatePublicUser(final PublicUser publicUser) {
        publicUserEndPoint.child(publicUser.getUserId()).
                setValue(publicUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, "Database entry created");
                updateLocalUsersList(publicUser);
            } else {
                Log.e(TAG, task.getException().toString());
            }
        });
    }

    /**
     * Create incident.
     *
     * @param incident the incident
     */
    public static void createIncident(Incident incident) {
        incidentEndPoint.child(incident.getDesc()).
                setValue(incident).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                Log.e(TAG, "Database entry created");
                incidentArrayList.add(incident);
            } else {
                Log.e(TAG, task1.getException().toString());
            }
        });
    }

    /**
     * Gets incident by id.
     *
     * @param id the id
     * @return the incident by id
     */
    public static Incident getIncidentById(String id) {
        for (int i = 0; i < incidentArrayList.size(); i++) {
            if (incidentArrayList.get(i).getIncidentId().equalsIgnoreCase(id)) {
                return incidentArrayList.get(i);
            }
        }
        return null;
    }


    ///////////////////////////////////////// HELPER METHODS ///////////////////////////////////////////////////////

    private static void readData(DatabaseReference databaseReference,
                                 final OnGetDataListener listener) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private static void updateLocalUsersList(PublicUser user) {
        for (int i = 0; i < publicUserArrayList.size(); i++) {
            if (publicUserArrayList.get(i).equals(user)) {
                publicUserArrayList.get(i).setName(user.getName());
                publicUserArrayList.get(i).setPassword(user.getPassword());
                publicUserArrayList.get(i).setName(user.getName());
                break;
            }
        }
    }

    /**
     * Change password.
     *
     * @param password the password
     */
    public static void changePassword(String password) {
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(password);
        FirebaseAuth.getInstance().signOut();
    }


    /**
     * The interface On get data listener.
     */
    public interface OnGetDataListener {
        /**
         * On success.
         *
         * @param dataSnapshot the data snapshot
         */
        void onSuccess(DataSnapshot dataSnapshot);
    }

}
