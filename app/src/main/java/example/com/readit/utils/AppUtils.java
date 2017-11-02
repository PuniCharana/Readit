package example.com.readit.utils;

import java.util.Calendar;

/**
 * Created by FamilyPC on 2017-10-25.
 */

@SuppressWarnings("ALL")
public class AppUtils {

    private AppUtils() {
        // No instantiation
    }

    public static String formatSubscriber(double n, int iteration) {
        char[] c = new char[]{'k', 'm', 'b', 't'};
        if (n <= 1000) {
            return "" + (int) n;
        }
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;
        return d < 1000 ? (d > 99.9 || isRound || d > 9.99 ? (int) d * 10 / 10 : d + "") + "" + c[iteration] : formatSubscriber(d, iteration + 1);
    }

    public static int formatYear(long datetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(datetime * 1000);
        return cal.get(Calendar.YEAR);
    }
}
