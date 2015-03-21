package vandyhacks.dios.hsphuc.healthystart.Models;

import android.app.*;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.Context;

import org.json.JSONArray;

import java.util.Calendar;

import vandyhacks.dios.hsphuc.healthystart.AlarmReceiver;
import vandyhacks.dios.hsphuc.healthystart.AlarmsActivity;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class Alarm {
    public static final String ID_TAG = "id";
    public static final String TIME_TAG = "time";
    public static final String SET_TAG = "set";
    public static final String MSG_TAG = "msg";

    private int id;
    private Calendar time;
    private boolean isScheduled;
    private String message;

    private AlarmPersistanceCallback persistanceCallback;

    /**
     * Default alarm constructor
     *
     * @param time The time of the alarm
     */
    public Alarm(Calendar time) {
        this.time = time;
        this.isScheduled = false;
        this.message = null;
    }

    /**
     * Sets the alarm's id
     */
    public void setId(int id) {
        this.id = id;
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
     * Sets the alarm's time
     */
    public void setTime(Context context, Calendar time){
        if(isScheduled){
            reschedule(context, time);
        } else {
            this.time = time;
        }
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
    public boolean isScheduled() {
        return this.isScheduled;
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

    /**
     * Should return a JSON array of the relevant fields in the object in order to store on device
     *
     * @return The JSON array
     */
    public JSONArray toJson() {
        JSONArray alarmArray = new JSONArray();

        return alarmArray;
    }

    /**
     * Schedules the alarm with the phone
     */
    public void schedule(Context context) {
        setTimeToNextOccurence();
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager androidAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        androidAlarmManager.set(AlarmManager.RTC, time.getTimeInMillis(), pendingIntent);
        isScheduled = true;
    }

    /**
     * Cancels the past alarm and schedules a new one
     */
    public void reschedule(Context context, Calendar newTime) {
        unschedule(context);
        this.time = newTime;
        schedule(context);
        isScheduled = true;
    }

    /**
     * Cancels the alarm if it is set
     */
    public void unschedule(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager androidAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        androidAlarmManager.cancel(pendingIntent);
        isScheduled = false;
    }

    /**
     * Sets the alarm to the next occurence of the time it is set to
     */
    private void setTimeToNextOccurence(){
        Calendar currentTime = Calendar.getInstance();
        while(currentTime.getTimeInMillis() > time.getTimeInMillis()){
            time.add(Calendar.DATE, 1);
        }
    }
}
