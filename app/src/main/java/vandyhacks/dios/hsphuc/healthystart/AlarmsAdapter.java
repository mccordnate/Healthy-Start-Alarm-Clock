package vandyhacks.dios.hsphuc.healthystart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;
import vandyhacks.dios.hsphuc.healthystart.Models.AlarmManager;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class AlarmsAdapter extends ArrayAdapter<Alarm> {

    private Context context;
    private AlarmListCallback alarmListCallback;

    private AlarmManager alarmManager;

    public AlarmsAdapter(Context context, int textViewResourceId, AlarmManager alarmManager,
                         AlarmListCallback callback) {
        super(context, textViewResourceId, new ArrayList<Alarm>());

        this.context = context;
        this.alarmListCallback = callback;
        this.alarmManager = alarmManager;
    }

    @Override
    public int getCount() {
        return (alarmManager.size() == 0) ? 1 : alarmManager.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (alarmManager.size() == 0) {
            view = inflater.inflate(R.layout.alarm_list_new_item, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmListCallback.showTimePicker(context, null);
                }
            });
        } else {
            Alarm alarm = alarmManager.get(position);

            SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm a");
            String time = timeFormat.format(alarm.getTime().getTime());

            view = inflater.inflate(R.layout.alarm_list_item, parent, false);

            TextView timeTextView = (TextView)view.findViewById(R.id.time_text);
            timeTextView.setText(time);

            Switch isSetSwitch = (Switch)view.findViewById(R.id.set_switch);
            isSetSwitch.setChecked(alarm.isScheduled());

            final Alarm tempAlarm = alarm;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmListCallback.showTimePicker(context, tempAlarm);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    alarmListCallback.showDeleteConfirmation(context, tempAlarm);
                    return true;
                }
            });
        }

        return view;
    }
}
