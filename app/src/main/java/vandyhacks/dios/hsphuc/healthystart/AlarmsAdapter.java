package vandyhacks.dios.hsphuc.healthystart;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
                    alarmListCallback.editAlarmClock(context, null);
                }
            });
        } else {
            Alarm alarm = alarmManager.get(position);

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String time = timeFormat.format(alarm.getTime().getTime());

            view = inflater.inflate(R.layout.alarm_list_item, parent, false);

            TextView timeTextView = (TextView)view.findViewById(R.id.time_text);
            TextView intensityTextView = (TextView)view.findViewById(R.id.intensity_text);
            timeTextView.setText(time);
            intensityTextView.setText("Target: " + HealthyStartApplication.user.findTargetHeartrate(alarm.getIntensity()) + " bpm");

            Switch isSetSwitch = (Switch)view.findViewById(R.id.set_switch);
            isSetSwitch.setChecked(alarm.isScheduled());

            ImageView deleteAlarmImage = (ImageView)view.findViewById(R.id.delete_image);


            final Alarm tempAlarm = alarm;
            View.OnClickListener editAlarmListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmListCallback.editAlarmClock(context, tempAlarm);
                }
            };

            timeTextView.setOnClickListener(editAlarmListener);
            intensityTextView.setOnClickListener(editAlarmListener);

            isSetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tempAlarm.schedule(context);
                    } else {
                        tempAlarm.unschedule(context);
                    }
                }
            });

            deleteAlarmImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmListCallback.showDeleteConfirmation(context, tempAlarm);
                }
            });
        }

        return view;
    }
}
