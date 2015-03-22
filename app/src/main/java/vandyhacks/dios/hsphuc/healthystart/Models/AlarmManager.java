package vandyhacks.dios.hsphuc.healthystart.Models;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class AlarmManager implements AlarmPersistanceCallback {

    private static final String JSON_FILENAME = "alarms.json";

    private Context context;
    private ArrayList<Alarm> alarms;
    private int id_counter;

    /**
     * Default constructor for the AlarmManager object
     */
    public AlarmManager(Context context) {
        alarms = new ArrayList<Alarm>();
        this.context = context;
        id_counter = 0;
    }

    /**
     * Adds the alarm to the manager
     * @param alarm The alarm to add
     */
    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        alarm.setId(++id_counter);

        Collections.sort(alarms, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm alarm, Alarm alarm2) {
                SimpleDateFormat compareFormat = new SimpleDateFormat("aKKmm");
                String formattedTime1 = compareFormat.format(alarm.getTime().getTime());
                String formattedTime2 = compareFormat.format(alarm2.getTime().getTime());

                return formattedTime1.compareTo(formattedTime2);
            }
        });
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

    /**
     * Saves the list of alarms to a file
     */
    public void save() {
        JSONArray alarmArray = new JSONArray();

        for (int i = 0; i < alarms.size(); i ++) {
            Alarm alarm = alarms.get(i);
            JSONObject jsonAlarm = alarm.toJson();

            alarmArray.put(jsonAlarm);
        }

        FileWriter jsonFileWriter = null;

        try {
            FileOutputStream fos = context.openFileOutput(JSON_FILENAME, Context.MODE_PRIVATE);
            fos.write(alarmArray.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the list of alarms from a JSON file
     */
    public void load() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("KK:mm a");

        String json = null;
        try {
            FileInputStream is = context.openFileInput(JSON_FILENAME);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject alarmObject = (JSONObject)jsonArray.get(i);
                    Calendar time = Calendar.getInstance();

                    int id = -1;
                    Date date = null;
                    boolean set = false;
                    String msg = null;

                    try {
                        id = alarmObject.getInt(Alarm.ID_TAG);
                        date = simpleDateFormat.parse(alarmObject.getString(Alarm.TIME_TAG));
                        set = alarmObject.getBoolean(Alarm.SET_TAG);
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }

                    if (date != null && id >= 0) {
                        time.setTime(date);
                        Alarm alarm = new Alarm(time);
                        alarm.setId(id);
                        alarm.setScheduled(set);

                        alarms.add(alarm);

                        if (id > id_counter) id_counter = id;
                    }
                }

            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }
}
