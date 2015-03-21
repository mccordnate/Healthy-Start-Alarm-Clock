package vandyhacks.dios.hsphuc.healthystart;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by nate on 3/21/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent){
        Intent newIntent = new Intent(context, GenerateAlarmActivity.class);
        context.startActivity(newIntent);
    }
}
