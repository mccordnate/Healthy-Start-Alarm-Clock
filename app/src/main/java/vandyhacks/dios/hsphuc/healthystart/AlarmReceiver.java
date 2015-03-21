package vandyhacks.dios.hsphuc.healthystart;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

/**
 * Created by nate on 3/21/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent){
        Intent newIntent = new Intent(context, GenerateAlarmActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}