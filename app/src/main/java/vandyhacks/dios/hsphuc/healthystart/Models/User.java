package vandyhacks.dios.hsphuc.healthystart.Models;

import android.content.SharedPreferences;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class User {

    public static final String PREFS_NAME = "UserHeartRatePrefs";

    private static final String USER_AGE = "UserAge";
    private static final String USER_INTENSITY = "UserIntensity";
    private static final String USER_TARGET_HR = "UserTargetHeartrate";

    private SharedPreferences sharedPreferences;

    private int age;
    private double intensity;
    private int targetHeartRate;
    private boolean loaded;

    /**
     * Null constructor to be used when just loading data from the device
     */
    public User() {
        this.loaded = false;
    }

    /**
     * Default user constructor (standard intensity for a user's heartrate is 65%)
     *
     * @param age The user's age
     */
    public User(int age) {
        this.age = age;
        this.intensity = 0.65;

        this.findTargetHeartrate();
        if (sharedPreferences != null) {
            save(sharedPreferences);
        }
    }

    public int getAge() {
        return this.age;
    }

    /**
     * Calculates the user's target heartrate based on the intensity
     */
    private void findTargetHeartrate() {
        this.targetHeartRate = (int)(this.intensity * (220 - this.age));
    }

    /**
     * Gets the user's target heart rate
     *
     * @return The target heart rate as an int
     */
    public int getTargetHeartRate() {
        return this.targetHeartRate;
    }

    /**
     * Sets the user's target heart rate intensity (% of max heart rate)
     *
     * @param intensity The intensity percentage as a double
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
        this.findTargetHeartrate();

        if (sharedPreferences != null) {
            save(sharedPreferences);
        }
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean getLoaded() {
        return this.loaded;
    }

    /**
     * Saves the user's data to shared preferences
     *
     * @param preferences The SharedPreferences for the calling activity
     */
    public void save(SharedPreferences preferences) {
        SharedPreferences.Editor prefsEditor = preferences.edit();

        prefsEditor.putInt(USER_AGE, this.age);
        prefsEditor.putFloat(USER_INTENSITY, (float)this.intensity);
        prefsEditor.putInt(USER_TARGET_HR, this.targetHeartRate);

        prefsEditor.commit();
    }

    /**
     * Loads the user's data from the SharedPreferences
     * (defaults to 25 y/o, 65% intensity, and a target HR of 127 bpm if no data found)
     *
     * @param preferences The SharedPreferences for the calling activity
     */
    public void load(SharedPreferences preferences) {
        this.sharedPreferences = preferences;

        this.age = preferences.getInt(USER_AGE, 25);
        this.intensity = (double)preferences.getFloat(USER_INTENSITY, 0.65f);
        this.targetHeartRate = preferences.getInt(USER_TARGET_HR, 0);
    }
}
