package com.lunatialiens.incidentreportingsystem.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

/**
 * The type App utils.
 */
public class AppUtils {


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z]).{6,}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validate email boolean.
     *
     * @param emailStr the email str
     * @return the boolean
     */
    public static boolean validateEmail(CharSequence emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    /**
     * Validate password boolean.
     *
     * @param passwordStr the password str
     * @return the boolean
     */
    public static boolean validatePassword(CharSequence passwordStr) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(passwordStr);
        return matcher.find();
    }

    /**
     * Generate uuid string.
     *
     * @return the string
     */
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Load image.
     *
     * @param context   the context
     * @param userId    the user id
     * @param imageView the image view
     */
    public static void loadImage(Context context, String userId, ImageView imageView) {
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference()
                .child("user_profile_pictures/" + userId + ".png");

        mStorageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .load(uri.toString())
                    .into(imageView);
        });
    }

    /**
     * Upload image.
     *
     * @param id       the id
     * @param imageUri the image uri
     */
    public static void uploadImage(String id, Uri imageUri) {
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference()
                .child("user_profile_pictures/" + id + ".png");

        mStorageRef.putFile(imageUri);
    }


    /**
     * Formulate date string.
     *
     * @param date the date
     * @return the string
     */
    public static String formulateDate(String date) {
        StringBuilder dateBuilder = new StringBuilder();

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6));
        Calendar calendar = new GregorianCalendar(year, (month), (day - 1));

        dateBuilder.append(day + " ");

        switch (month) {
            case 1:
                dateBuilder.append("JANUARY ");
                break;
            case 2:
                dateBuilder.append("FEBRUARY ");
                break;
            case 3:
                dateBuilder.append("MARCH ");
                break;
            case 4:
                dateBuilder.append("APRIL ");
                break;
            case 5:
                dateBuilder.append("MAY ");
                break;
            case 6:
                dateBuilder.append("JUNE ");
                break;
            case 7:
                dateBuilder.append("JULY ");
                break;
            case 8:
                dateBuilder.append("AUGUST ");
                break;
            case 9:
                dateBuilder.append("SEPTEMBER ");
                break;
            case 10:
                dateBuilder.append("OCTOBER ");
                break;
            case 11:
                dateBuilder.append("NOVEMBER ");
                break;
            case 12:
                dateBuilder.append("DECEMBER ");
                break;
        }

        dateBuilder.append(year + ", ");

        int daySwitch = calendar.get(Calendar.DAY_OF_WEEK);

        switch (daySwitch) {
            case Calendar.MONDAY:
                dateBuilder.append("MONDAY");
                break;
            case Calendar.TUESDAY:
                dateBuilder.append("TUESDAY");
                break;
            case Calendar.WEDNESDAY:
                dateBuilder.append("WEDNESDAY");
                break;
            case Calendar.THURSDAY:
                dateBuilder.append("THURSDAY");
                break;
            case Calendar.FRIDAY:
                dateBuilder.append("FRIDAY");
                break;
            case Calendar.SATURDAY:
                dateBuilder.append("SATURDAY");
                break;
            case Calendar.SUNDAY:
                dateBuilder.append("SUNDAY");
                break;

        }
        return dateBuilder.toString();
    }

    /**
     * Success.
     *
     * @param context the context
     * @param message the message
     */
    public static void success(Context context, String message) {
        Toasty.success(context, message, Toasty.LENGTH_SHORT, true).show();
    }

    /**
     * Error.
     *
     * @param context the context
     * @param message the message
     */
    public static void error(Context context, String message) {
        Toasty.error(context, message, Toasty.LENGTH_SHORT, true).show();
    }

    /**
     * Info.
     *
     * @param context the context
     * @param message the message
     */
    public static void info(Context context, String message) {
        Toasty.info(context, message, Toasty.LENGTH_SHORT, true).show();
    }

    /**
     * Warning.
     *
     * @param context the context
     * @param message the message
     */
    public static void warning(Context context, String message) {
        Toasty.warning(context, message, Toasty.LENGTH_SHORT, true).show();
    }

    /**
     * Save data in shared prefs.
     *
     * @param context the context
     * @param key     the key
     * @param value   the value
     */
    public static void saveDataInSharedPrefs(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    /**
     * Gets data from shared prefs.
     *
     * @param context the context
     * @param key     the key
     * @return the data from shared prefs
     */
    public static String getDataFromSharedPrefs(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

    /**
     * Round double.
     *
     * @param value the value
     * @return the double
     */
    public static double round(double value) {

        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Gets simple time.
     *
     * @param timeStamp the time stamp
     * @return the simple time
     */
    public static String getSimpleTime(long timeStamp) {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(timeStamp);
    }

    /**
     * Bitmap descriptor from vector bitmap descriptor.
     *
     * @param context                  the context
     * @param vectorDrawableResourceId the vector drawable resource id
     * @return the bitmap descriptor
     */
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Parse location lat lng.
     *
     * @param location the location
     * @return the lat lng
     */
    public static LatLng parseLocation(String location) {
        List<String> latlng = Arrays.asList(location.split(","));
        return new LatLng(Double.parseDouble(latlng.get(0)), Double.parseDouble(latlng.get(1)));

    }
}
