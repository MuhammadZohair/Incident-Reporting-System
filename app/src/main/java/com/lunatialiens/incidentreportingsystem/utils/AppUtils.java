package com.lunatialiens.incidentreportingsystem.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lunatialiens.incidentreportingsystem.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

/**
 * The type App utils.
 */
public class AppUtils {

    /**
     * The constant PUBLIC_USER_TYPE.
     */
    public static final String PUBLIC_USER_TYPE = "PUBLIC_USER";


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
     * Gets date.
     *
     * @param context  the context
     * @param editText the edit text
     */
    public static void getDate(Activity context, EditText editText) {
        Calendar myCalendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            String startDateStr = sdf.format(myCalendar.getTime());
            editText.setText(startDateStr);

        };
        new DatePickerDialog(Objects.requireNonNull(context), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Gets time.
     *
     * @param context  the context
     * @param editText the edit text
     */
    public static void getTime(Context context, EditText editText) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, (timePicker, hourOfDay, minute1) -> {
            boolean isPM = (hourOfDay >= 12);
            String time = " " + String.format("%02d:%02d %s",
                    (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute1, isPM ? "PM" : "AM");
            editText.setText(time);
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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
     * Clear shared prefs.
     *
     * @param context the context
     */
    public static void clearSharedPrefs(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("email", "").apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("password", "").apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("type", "").apply();
    }

    /**
     * Gets todays total cals.
     *
     * @param context the context
     * @return the todays total cals
     */
    public static float getTodaysTotalCals(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat("CALS", 0);
    }


    /**
     * Save todays total cals.
     *
     * @param context the context
     * @param cals    the cals
     */
    public static void saveTodaysTotalCals(Context context, Float cals) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat("CALS", (
                PreferenceManager.getDefaultSharedPreferences(context).getFloat("CALS", 0) + cals
        )).apply();
    }

    /**
     * Round double.
     *
     * @param value  the value
     * @param places the places
     * @return the double
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
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
     * Gets simple date.
     *
     * @param timeStamp the time stamp
     * @return the simple date
     */
    public static String getSimpleDate(long timeStamp) {
        return new SimpleDateFormat("dd/MM/yyyy").format(timeStamp);
    }

    /**
     * Get date string [ ].
     *
     * @param time the time
     * @return the string [ ]
     */
    public static String[] getDate(long time) {
        String[] array = new String[3];
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        array[0] = date.substring(0, 2);
        array[1] = date.substring(3, 5);
        array[2] = date.substring(6);
        return array;
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

    /**
     * Open dialog to get details string.
     *
     * @param activity the activity
     * @return the string
     */
    public static String openDialogToGetDetails(FragmentActivity activity) {
        final String[] text = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Enter Details");

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View alertDialogView = inflater.inflate(R.layout.input_desc, null);
        builder.setView(alertDialogView);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        final EditText codeEditText = alertDialogView.findViewById(R.id.et_desc);

        builder.setView(alertDialogView);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String t = codeEditText.getText().toString();
            text[0] = t;
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            info(activity, "Please write some text");
            dialog.cancel();
        });

        builder.show();
        return text[0];
    }
}
