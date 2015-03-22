package vandyhacks.dios.hsphuc.healthystart;

import android.content.Context;
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
import android.widget.Toast;

import java.io.IOException;

import vandyhacks.dios.hsphuc.healthystart.Heartrate.HeartRateMonitor;
import vandyhacks.dios.hsphuc.healthystart.R;

public class GenerateAlarmActivity extends Activity {

    private static PowerManager.WakeLock wakeLock = null;
    static final int GET_HEARTRATE = 1;  // The request code
    MediaPlayer mp = new MediaPlayer();
    int timesMeasured = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_alarm);
        setTitle("Healthy Start");

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
        startActivityForResult(intent, GET_HEARTRATE);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == GET_HEARTRATE) {
            if (resultCode == RESULT_OK) {
                if (!data.getBooleanExtra("highHeartRate", false)) {
                    if (timesMeasured < 3)
                        mp.start();
                    else {
                        mp.stop();
                        mp.release();
                        Toast.makeText(getApplicationContext(), "You suck and don't do workout", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    mp.stop();
                    mp.release();
                    finish();
                }
            }
        }
    }
}
