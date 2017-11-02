package example.com.readit.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import example.com.readit.data.local.SubredditContract.SubredditEntry;

import static android.database.sqlite.SQLiteDatabase.OPEN_READONLY;

/**
 * Created by FamilyPC on 2017-10-25.
 */

@SuppressWarnings("ALL")
class SubredditDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "DATABASE";

    private static String DATABASE_PATH = "/data/data/example.com.readit/databases/";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "subreddits.db";
    private SQLiteDatabase myDataBase;

    private Context mContext;

    public SubredditDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate");

        // Database will be created from file in assets folder
        // Note: db file name in assets folder should match the DATABASE_NAME

        /*
        final String CREATE_TABLE = "CREATE TABLE " + SubredditEntry.TABLE_NAME + " (" +
                SubredditEntry._ID + " INTEGER PRIMARY KEY, " +
                SubredditEntry.COLUMN_SUBREDDIT_DISPLAY_NAME + " TEXT, " +
                SubredditEntry.COLUMN_SUBREDDIT_ID + " TEXT, " +
                SubredditEntry.COLUMN_SUBREDDIT_URL + " TEXT, " +
                SubredditEntry.COLUMN_SUBREDDIT_POSITION + " INTEGER, " +
                "UNIQUE(" + SubredditEntry.COLUMN_SUBREDDIT_ID + "));";

        db.execSQL(CREATE_TABLE);
        */
    }

    public void createDataBase() {
        Log.d(LOG_TAG, "createDataBase");
        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.d(LOG_TAG, "dbExist");
            // do nothing
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {
        Log.d(LOG_TAG, "copyDataBase");
        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public boolean checkDataBase() {
        Log.d(LOG_TAG, "checkDataBase");

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            Log.d(LOG_TAG, "opening " + myPath);
            checkDB = SQLiteDatabase.openDatabase(myPath, null, OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.d(LOG_TAG, "database doesn't exist yet.");
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SubredditEntry.TABLE_NAME);
        onCreate(db);
    }
}
