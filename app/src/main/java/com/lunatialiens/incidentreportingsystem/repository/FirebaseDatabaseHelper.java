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
import com.lunatialiens.incidentreportingsystem.models.PublicUser;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;

import java.util.ArrayList;
import java.util.Objects;

public class FirebaseDatabaseHelper {

    private static final String TAG = "FirebaseDatabaseHelper";

    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference publicUserEndPoint;

    private static ArrayList<PublicUser> publicUserArrayList;

    public FirebaseDatabaseHelper() {

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        publicUserEndPoint = firebaseDatabase.getReference("public_user_table");

        publicUserArrayList = getAllPublicUsersFromFirebase();
    }

    private static ArrayList<PublicUser> getAllPublicUsersFromFirebase() {
        final ArrayList<PublicUser> publicUsers = new ArrayList<>();
        readData(publicUserEndPoint, dataSnapshot -> {
            for (DataSnapshot s : dataSnapshot.getChildren()) {
                publicUsers.add(s.getValue(PublicUser.class));
            }
        });
        return publicUsers;
    }

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

    public static PublicUser getPublicUserByEmail(String email) {
        for (int i = 0; i < publicUserArrayList.size(); i++) {
            if (publicUserArrayList.get(i).getEmailAddress().equals(email))
                return publicUserArrayList.get(i);
        }
        return null;
    }

    public static PublicUser getPublicUserByEmailAndPassword(String email, String password) {
        for (int i = 0; i < publicUserArrayList.size(); i++) {
            if (publicUserArrayList.get(i).getEmailAddress().equals(email)
                    && publicUserArrayList.get(i).getPassword().equals(password)) {
                return publicUserArrayList.get(i);
            }
        }
        return null;
    }

    public static PublicUser getPublicUserByID(String id) {
        for (int i = 0; i < publicUserArrayList.size(); i++) {
            if (publicUserArrayList.get(i).getUserId().equals(id))
                return publicUserArrayList.get(i);
        }
        return null;
    }

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

    public static boolean deletePublicUser(final PublicUser publicUser) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(publicUser.getEmailAddress(),
                publicUser.getPassword()).addOnSuccessListener(authResult -> {
            auth.getCurrentUser().delete();
            publicUserEndPoint.child(publicUser.getUserId())
                    .removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.e(TAG, "Child deleted Successfully");
                    publicUserArrayList.remove(publicUser);
                } else {
                    Log.e(TAG, "Error Deleting child");
                }
            });
        });
        return true;
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
                publicUserArrayList.get(i).setPhoneNumber(user.getPhoneNumber());
                publicUserArrayList.get(i).setName(user.getName());
                break;
            }
        }
    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
    }

}
