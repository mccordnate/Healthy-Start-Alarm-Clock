package vandyhacks.dios.hsphuc.healthystart.Models;

import java.util.Calendar;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class Alarm {

    private int id;
    private Calendar time;
    private boolean isSet;
    private String message;

    private AlarmPersistanceCallback persistanceCallback;

    /**
     * Default alarm constructor
     *
     * @param time The time of the alarm
     * @param isSet Whether the alarm is set to go off
     */
    public Alarm(int id, Calendar time, boolean isSet) {
        this.id = id;
        this.time = time;
        this.isSet = isSet;
        this.message = null;
    }

    /**
     * Gets the alarm's id
     *
     * @return The alarm's id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the alarm's time
     *
     * @return The Calendar object of the time
     */
    public Calendar getTime() {
        return this.time;
    }

    /**
     * Gets whether the alarm is set or not
     *
     * @return The boolean for whether it is set or not
     */
    public boolean isSet() {
        return this.isSet;
    }

    /**
     * Set the alarm's message
     *
     * @param message The new message for the alarm
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the alarm's message
     *
     * @return The message as a string
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the alarm callback to the proper manager to be able to save to disk
     */
    public void setPersistanceCallback(AlarmPersistanceCallback callback) {
        this.persistanceCallback = callback;
    }

    /**
     * Calls the alarm manager's method to be able to save to disk
     */
    public void save() {
        persistanceCallback.save();
    }

}
