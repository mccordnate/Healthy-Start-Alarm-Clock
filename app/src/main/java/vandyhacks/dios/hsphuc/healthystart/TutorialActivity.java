package vandyhacks.dios.hsphuc.healthystart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialActivity extends Activity {

    private static final int[] resourceArray = {R.drawable.alarm,
                            R.drawable.intensity,
                            R.drawable.workout,
                            R.drawable.heartrate};

    private static final String[] stringArray = {"Set an alarm",
                            "Pick an intensity",
                            "Get heart rate up",
                            "Pass heart rate test"};
    private int index = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        context = this;
        final ImageView backgroundImage = (ImageView) findViewById(R.id.background_image);
        final TextView tutorialText = (TextView) findViewById(R.id.tutorial_text);
        final View buttony = findViewById(R.id.buttony);

        backgroundImage.setImageResource(resourceArray[0]);
        tutorialText.setText(stringArray[0]);

        buttony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index ++;

                if (index >= resourceArray.length) {
                    finish();
                } else {
                    backgroundImage.setImageResource(resourceArray[index]);
                    tutorialText.setText(stringArray[index]);
                }
            }
        });
    }

}
