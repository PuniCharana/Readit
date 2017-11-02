package example.com.readit.data.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by FamilyPC on 2017-10-25.
 */

@SuppressWarnings("ALL")
public class SubredditContentProvider extends ContentProvider {

    private static final int SUBREDDITS = 100;
    private static final int SUBREDDIT_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(SubredditContract.AUTHORITY, SubredditContract.CONTENT_PATH, SUBREDDITS);
        uriMatcher.addURI(SubredditContract.AUTHORITY, SubredditContract.CONTENT_PATH + "/#", SUBREDDIT_WITH_ID);

        return uriMatcher;
    }

    private SubredditDbHelper subredditDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        subredditDbHelper = new SubredditDbHelper(context);
        subredditDbHelper.createDataBase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = subredditDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a loading case
        switch (match) {
            // Query for the tasks directory
            case SUBREDDITS:
                retCursor = db.query(SubredditContract.SubredditEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = subredditDbHelper.getWritableDatabase();
        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case SUBREDDITS:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insertWithOnConflict(SubredditContract.SubredditEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                returnUri = ContentUris.withAppendedId(SubredditContract.SubredditEntry.CONTENT_URI, id);
                break;
            // Set the value for the returnedUri and write the loading case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = subredditDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case SUBREDDITS:
                // Get the task ID from the URI path
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(SubredditContract.SubredditEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = subredditDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int update; // starts as 0

        switch (match) {
            case SUBREDDITS:
                Log.d("DB", "deleting...");
                update = db.update(SubredditContract.SubredditEntry.TABLE_NAME, values, selection, selectionArgs);
                Log.d("DB", update+" deleted");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (update != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return update;


    }
}
