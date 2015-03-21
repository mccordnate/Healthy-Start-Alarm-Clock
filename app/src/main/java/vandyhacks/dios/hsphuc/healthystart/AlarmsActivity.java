package vandyhacks.dios.hsphuc.healthystart;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Calendar;

import vandyhacks.dios.hsphuc.healthystart.R;
import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;
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
        alarmManager.addAlarm(alarmA);
        alarmManager.addAlarm(alarmB);

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
        }

        return super.onOptionsItemSelected(item);
    }
}
