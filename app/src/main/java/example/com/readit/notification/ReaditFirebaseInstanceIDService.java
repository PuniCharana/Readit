package example.com.readit.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by FamilyPC on 11/1/2017.
 */

@SuppressWarnings("ALL")
public class ReaditFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String LOG_TAG = ReaditFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d(LOG_TAG, "onTokenRefresh");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(LOG_TAG, "Refreshed token: " + refreshedToken);

        // Send token to db
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d(LOG_TAG, "sending token to server...");
        // TODO associate FCM InstanceID token with the user's account
    }
}
