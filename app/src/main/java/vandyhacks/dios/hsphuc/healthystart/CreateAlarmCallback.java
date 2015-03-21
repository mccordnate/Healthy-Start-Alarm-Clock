package vandyhacks.dios.hsphuc.healthystart;

import android.content.Context;

import java.util.Calendar;

import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public interface CreateAlarmCallback {

    public void openTimePicker(final Context context, final Alarm alarm);

}
