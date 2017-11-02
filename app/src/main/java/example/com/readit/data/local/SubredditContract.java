package example.com.readit.data.local;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by FamilyPC on 2017-10-25.
 */

@SuppressWarnings("ALL")
public class SubredditContract {

    public static final String AUTHORITY = "example.com.readit";
    private static final Uri CONTENT_BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final String CONTENT_PATH = "subreddits";

    public static final class SubredditEntry implements BaseColumns {
        public static final Uri CONTENT_URI = CONTENT_BASE_URI.buildUpon().appendPath(CONTENT_PATH).build();
        public static final String TABLE_NAME = "subreddits";

        public static final String COLUMN_SUBREDDIT_DISPLAY_NAME = "COLUMN_SUBREDDIT_DISPLAY_NAME";
        public static final String COLUMN_SUBREDDIT_ID = "COLUMN_SUBREDDIT_ID";
        public static final String COLUMN_SUBREDDIT_URL = "COLUMN_SUBREDDIT_URL";
        public static final String COLUMN_SUBREDDIT_POSITION = "COLUMN_SUBREDDIT_POSITION";
    }
}
