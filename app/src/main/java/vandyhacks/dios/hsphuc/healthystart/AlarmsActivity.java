package vandyhacks.dios.hsphuc.healthystart;

import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import vandyhacks.dios.hsphuc.healthystart.R;
import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;
import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;

import java.util.Calendar;
import java.util.Date;

import vandyhacks.dios.hsphuc.healthystart.Models.AlarmManager;


public class AlarmsActivity extends ActionBarActivity {

    private ListView alarmsListView;
    private AlarmsAdapter alarmsAdapter;

    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_alarms);

        alarmsListView = (ListView)findViewById(R.id.alarm_list);

        alarmManager = new AlarmManager();

        Calendar cal = Calendar.getInstance();
        Alarm alarmA = new Alarm(cal, false);
        Alarm alarmB = new Alarm(cal, false);
        //alarmManager.addAlarm(alarmA);
        //alarmManager.addAlarm(alarmB);

        alarmsAdapter = new AlarmsAdapter(this, R.id.time_text, alarmManager);
        alarmsListView.setAdapter(alarmsAdapter);
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
            openTimePicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
