package example.com.readit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by FamilyPC on 11/2/2017.
 */

@SuppressWarnings("ALL")
public class InternetConnectivity {
    private InternetConnectivity() {
        // Not allowed to instantiate
    }

    public static boolean isConnected(Context context) {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
