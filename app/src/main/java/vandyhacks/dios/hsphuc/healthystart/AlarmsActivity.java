package vandyhacks.dios.hsphuc.healthystart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;
import vandyhacks.dios.hsphuc.healthystart.Models.AlarmManager;
import vandyhacks.dios.hsphuc.healthystart.Models.User;


public class AlarmsActivity extends ActionBarActivity implements AlarmListCallback {

    private ListView alarmsListView;
    private AlarmsAdapter alarmsAdapter;

    private AlarmManager alarmManager;
    private Alarm tempAlarm;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_alarms);

        alarmsListView = (ListView)findViewById(R.id.alarm_list);

        alarmManager = new AlarmManager(this);
        alarmManager.load();

        alarmsAdapter = new AlarmsAdapter(this, R.id.time_text, alarmManager, this);
        alarmsListView.setAdapter(alarmsAdapter);

        User user = ((HealthyStartApplication)getApplication()).user;

        if (user.getAge() == -1) {
            // Display tutorial
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);

            // Get user's age
            showAgeDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        alarmManager.save();
        //getSharedPreferences(User.PREFS_NAME, MODE_PRIVATE).edit().clear().commit();
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
        if (id == R.id.action_change_age) {
            showAgeDialog();
            return true;
        } else if(id == R.id.action_add_alarm) {
            editAlarmClock(this, null);
            return true;
        } else if (id == R.id.action_tutorial) {
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
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
        np.setMaxValue(99);
        np.setMinValue(1);
        int curAge = ((HealthyStartApplication) getApplication()).user.getAge();
        np.setValue( (curAge == -1) ? 20 : curAge);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = np.getValue();

                // Set the user's age and save it to the device
                ((HealthyStartApplication) getApplication()).user = new User(age);
                SharedPreferences sharedPreferences = getSharedPreferences(User.PREFS_NAME, MODE_PRIVATE);
                ((HealthyStartApplication) getApplication()).user.save(sharedPreferences);
                d.dismiss();
            }
        });
        d.show();

        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.titleBarPurple));

        int textViewId = d.getContext().getResources().getIdentifier("android:id/title", null, null);
        TextView tv = (TextView) d.findViewById(textViewId);
        tv.setTextColor(getResources().getColor(R.color.titleBarPurple));
    }

    /**
     * Opens up the dialog for the Time Picker to pick what time the alarm will be set
     * Creates a new alarm based off of this time
     */
    public void editAlarmClock(final Context context, final Alarm alarm) {
        tempAlarm = alarm;
        if (tempAlarm == null) {
            tempAlarm = new Alarm(Calendar.getInstance());
        }

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(false);
        timePicker.setCurrentHour(tempAlarm.getTime().get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(tempAlarm.getTime().get(Calendar.MINUTE));
        timePicker.setLayoutParams(layoutParams);
        linearLayout.addView(timePicker);

        final TextView intensityBarTitle = new TextView(this);
        intensityBarTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        intensityBarTitle.setTextSize(20f);
        intensityBarTitle.setTextColor(getResources().getColor(R.color.titleBarPurple));
        intensityBarTitle.setText("Intensity: " + tempAlarm.getIntensity() + "%");

        final SeekBar intensityBar = new SeekBar(this);
        intensityBar.setMax(50);
        intensityBar.setProgress(tempAlarm.getIntensity() - 35);
        intensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intensityBarTitle.setText("Intensity: " + (progress + 35) + "%");
                tempAlarm.setIntensity(progress + 35);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        linearLayout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        intensityBar.setLayoutParams(linearLayoutParams);
        linearLayout.addView(intensityBarTitle);
        linearLayout.addView(intensityBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogStyle)
                .setTitle("Edit Alarm")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                        time.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                        time.set(Calendar.SECOND, 0);

                        if (alarm == null) {
                            tempAlarm.setTime(context, time);
                            alarmManager.addAlarm(tempAlarm);
                            tempAlarm.schedule(context);
                        } else {
                            tempAlarm.setTime(context, time);
                        }
                        Log.i("MANAGER SIZE", "size: " + alarmManager.size());
                        refreshList();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null).setView(linearLayout);

        Dialog d = builder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.titleBarPurple));

        int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) d.findViewById(textViewId);
        tv.setTextColor(getResources().getColor(R.color.titleBarPurple));
    }

    /**
     * Opens a confirmation dialog to delete an alarm
     */
    public void showDeleteConfirmation(final Context context, final Alarm alarm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Delete Alarm")
                .setMessage("Are you sure you want to delete this alarm?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alarm.isScheduled()) alarm.unschedule(context);

                        alarmManager.removeAlarm(alarm);
                        alarmManager.save();

                        refreshList();
                    }

                })
                .setNegativeButton("No", null);

        Dialog d = builder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.titleBarPurple));

        int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) d.findViewById(textViewId);
        tv.setTextColor(getResources().getColor(R.color.titleBarPurple));
    }
}
