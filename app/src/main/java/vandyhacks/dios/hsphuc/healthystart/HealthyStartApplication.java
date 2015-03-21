package vandyhacks.dios.hsphuc.healthystart;

import android.app.Application;

import vandyhacks.dios.hsphuc.healthystart.Models.User;

/**
 * Created by paulrachwalski on 3/21/15.
 */
public class HealthyStartApplication extends Application {

    public static User user;

    @Override
    public void onCreate() {
        super.onCreate();

        user = new User();
        user.load(this.getSharedPreferences(User.PREFS_NAME, MODE_PRIVATE));
        if (user.getTargetHeartRate() != 0) {
            user.setLoaded(true);
        }
    }

}
