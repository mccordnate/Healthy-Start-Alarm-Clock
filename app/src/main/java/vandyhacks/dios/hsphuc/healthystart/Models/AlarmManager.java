package vandyhacks.dios.hsphuc.healthystart.Models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class AlarmManager implements AlarmPersistanceCallback {

    private static final String JSON_FILENAME = "alarms.json";

    private ArrayList<Alarm> alarms;
    private int id_counter;

    /**
     * Default constructor for the AlarmManager object
     */
    public AlarmManager() {
        alarms = new ArrayList<Alarm>();
        id_counter = 0;
    }

    /**
     * Adds the alarm to the manager
     * @param alarm The alarm to add
     */
    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        alarm.setId(++id_counter);
    }

    /**
     * Removes the alarm from the list
     * @param alarm The alarm to remove
     */
    public void removeAlarm(Alarm alarm) {
        alarms.remove(alarm);
    }

    public void removeAlarmById(int id) {
        Alarm temp = null;

        for (int i = 0; i < alarms.size(); i ++) {
            temp = alarms.get(i);
            if (temp.getId() == id) {
                break;
            }
        }

        if (temp != null) {
            alarms.remove(temp);
        } else {
            Log.e("AlarmManager Null Error", "Tried to remove a nonexistant alarm");
        }
    }

    public int size() {
        return alarms.size();
    }

    public Alarm get(int index) {
        return alarms.get(index);
    }

    public void save() {

    }

    public void load() {

    }
}
