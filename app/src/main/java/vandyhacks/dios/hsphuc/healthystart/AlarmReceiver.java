package vandyhacks.dios.hsphuc.healthystart;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;

/**
 * Created by nate on 3/21/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent){
        int alarmId = intent.getIntExtra(Alarm.REQUEST_CODE, 0);

        Intent newIntent = new Intent(context, GenerateAlarmActivity.class);
        newIntent.putExtra(Alarm.REQUEST_CODE, alarmId);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(newIntent);
    }
}