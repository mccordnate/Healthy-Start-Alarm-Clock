package vandyhacks.dios.hsphuc.healthystart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;

import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;
import vandyhacks.dios.hsphuc.healthystart.Models.AlarmManager;
import vandyhacks.dios.hsphuc.healthystart.Models.User;


public class AlarmsActivity extends ActionBarActivity implements AlarmListCallback {

    private ListView alarmsListView;
    private AlarmsAdapter alarmsAdapter;

    private AlarmManager alarmManager;

    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_alarms);

        alarmsListView = (ListView)findViewById(R.id.alarm_list);

        alarmManager = new AlarmManager();

        alarmsAdapter = new AlarmsAdapter(this, R.id.time_text, alarmManager, this);
        alarmsListView.setAdapter(alarmsAdapter);

        User user = ((HealthyStartApplication)getApplication()).user;

        if (user.getTargetHeartRate() == 0) {
            showAgeDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getSharedPreferences(User.PREFS_NAME, MODE_PRIVATE).edit().clear().commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_add_alarm){
            showTimePicker(this, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Refreshes the alarms list
     */
    public void refreshList() {
        alarmsAdapter.notifyDataSetChanged();
    }

    /**
     * Opens a dialog for selecting a user's age if they have not entered it before.
     */
    private void showAgeDialog() {
        final Dialog d = new Dialog(this);
        d.setTitle("Select your age");
        d.setContentView(R.layout.age_dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(5);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                age = np.getValue();

                // Set the user's age and save it to the device
                ((HealthyStartApplication)getApplication()).user = new User(age);
                SharedPreferences sharedPreferences = getSharedPreferences(User.PREFS_NAME, MODE_PRIVATE);
                ((HealthyStartApplication) getApplication()).user.save(sharedPreferences);
                d.dismiss();
            }
        });
        d.show();
    }

    /**
     * Opens up the dialog for the Time Picker to pick what time the alarm will be set
     * Creates a new alarm based off of this time
     */
    public void showTimePicker(final Context context, final Alarm alarm) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        final TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(false);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        new AlertDialog.Builder(this)
                .setTitle("Test")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                        time.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                        if (alarm == null) {
                            Alarm alarm = new Alarm(time);
                            alarmManager.addAlarm(alarm);
                            alarm.schedule(context);
                        } else {
                            alarm.setTime(context, time);
                        }
                        Log.i("MANAGER SIZE", "size: " + alarmManager.size());
                        refreshList();
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.d("Picker", "Cancelled!");
                            }
                        }).setView(timePicker).show();
        return;
    }

    /**
     * Opens a confirmation dialog to delete an alarm
     */
    public void showDeleteConfirmation(final Context context, final Alarm alarm) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Alarm")
                .setMessage("Are you sure you want to delete this alarm?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alarm.isScheduled()) alarm.unschedule(context);

                        alarmManager.removeAlarm(alarm);
                        refreshList();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
