package vandyhacks.dios.hsphuc.healthystart;

import android.media.MediaPlayer;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import vandyhacks.dios.hsphuc.healthystart.R;

public class GenerateAlarmActivity extends Activity {

    MediaPlayer mp;
    int timesPlayed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_alarm);

        mp = MediaPlayer.create(this, R.raw.heartbeat);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                if (timesPlayed < 3) {
                    mp.start();
                    ++timesPlayed;
                } else {
                    mp.stop();
                    mp.release();
                    Toast.makeText(getApplicationContext(), "You suck and don't do workout", Toast.LENGTH_LONG);
                    finish();
                }
            }
        });
        mp.start();
        ++timesPlayed;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.generate_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void beginMeasureHeartRate(View v) throws InterruptedException {
        mp.pause();
        //do measure heartrate activity here return a bool
        boolean highHeartRate = true;
        if (!highHeartRate)
            mp.start();
        else {
            mp.stop();
            mp.release();
            finish();
        }
    }
}
