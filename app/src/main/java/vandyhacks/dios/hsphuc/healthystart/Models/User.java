package vandyhacks.dios.hsphuc.healthystart.Models;

import android.content.SharedPreferences;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class User {

    public static final String PREFS_NAME = "UserHeartRatePrefs";

    private static final String USER_AGE = "UserAge";

    private SharedPreferences sharedPreferences;

    private int age;
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
    public int findTargetHeartrate(int intensity) {
        return (int)( ((double)intensity / 100) * (220 - this.age));
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

        this.age = preferences.getInt(USER_AGE, -1);
    }
}
