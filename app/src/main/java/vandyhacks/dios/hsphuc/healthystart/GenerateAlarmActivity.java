package vandyhacks.dios.hsphuc.healthystart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.app.Activity;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import vandyhacks.dios.hsphuc.healthystart.Heartrate.HeartRateMonitor;
import vandyhacks.dios.hsphuc.healthystart.Models.Alarm;
import vandyhacks.dios.hsphuc.healthystart.R;

public class GenerateAlarmActivity extends Activity {

    private static PowerManager.WakeLock wakeLock = null;
    static final int GET_HEARTRATE = 1;  // The request code
    MediaPlayer mp = new MediaPlayer();
    int timesMeasured = 0;
    int alarmId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_alarm);
        setTitle("Healthy Start");

        alarmId = getIntent().getIntExtra(Alarm.REQUEST_CODE, 0);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

        Window wind;
        wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        mp.setAudioStreamType(AudioManager.STREAM_ALARM);
        try {
            mp.setDataSource(this, RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM));
            mp.prepare();
            mp.setLooping(true);
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (HealthyStartApplication.user.isFirstAlarm()) {
            HealthyStartApplication.user.setFirstAlarm(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Instructions")
                    .setMessage("To measure heart rate, place your fingertip on the camera")
                    .setPositiveButton("Okay", null);

            Dialog d = builder.show();
            int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = d.findViewById(dividerId);
            divider.setBackgroundColor(getResources().getColor(R.color.titleBarPurple));

            int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
            TextView tv = (TextView) d.findViewById(textViewId);
            tv.setTextColor(getResources().getColor(R.color.titleBarPurple));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();
    }

    public void beginMeasureHeartRate(View v) {
        ++timesMeasured;
        mp.pause();
        Intent intent = new Intent(this, HeartRateMonitor.class);
        intent.putExtra(Alarm.REQUEST_CODE, alarmId);
        startActivityForResult(intent, GET_HEARTRATE);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == GET_HEARTRATE) {
            if (resultCode == RESULT_OK) {
                if (!data.getBooleanExtra("highHeartRate", false)) {
                    if (timesMeasured < 3) {
                        Toast.makeText(getApplicationContext(), "Get your heart rate up!", Toast.LENGTH_LONG).show();
                        mp.start();
                    } else {
                        mp.stop();
                        mp.release();
                        Toast.makeText(getApplicationContext(), "You'll get it next time!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    mp.stop();
                    mp.release();
                    Toast.makeText(getApplicationContext(), "Good workout!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}
