package vandyhacks.dios.hsphuc.healthystart;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class AlarmsAdapter extends ArrayAdapter<Alarm> {

    public AlarmsAdapter(Context context, int textViewResourceId,
                         List<Alarm> objects) {
        super(context, textViewResourceId, objects);
    }
}
