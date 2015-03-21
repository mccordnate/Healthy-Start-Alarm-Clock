package vandyhacks.dios.hsphuc.healthystart.Models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class AlarmManager implements AlarmPersistanceCallback {

    private static final String JSON_FILENAME = "alarms.json";

    private ArrayList<Alarm> alarms;

    public AlarmManager() {
        alarms = new ArrayList<Alarm>();
    }

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }

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

    public void save() {

    }
}
