package example.com.readit.ui.manage;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.local.SubredditContract;
import example.com.readit.data.local.models.LocalSubreddit;
import example.com.readit.ui.settings.SettingsActivity;
import example.com.readit.widget.ReaditWidget;
import example.com.readit.widget.WidgetContract;

public class ManageSubredditsActivity extends AppCompatActivity implements ManageSubredditsContract.ManageSubredditsView {

    private static final String LOG_TAG = ManageSubredditsActivity.class.getSimpleName();
    private ManageSubredditsPresenter subredditsPresenter;
    @BindView(R2.id.local_subreddit_recycleView)
    RecyclerView mRecyclerView;
    private ArrayList<LocalSubreddit> localSubreddits = new ArrayList<>();
    private FirebaseAnalytics mFirebaseAnalytics;
    @BindView(R2.id.no_subscription)
    TextView noSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subreddits);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        subredditsPresenter = new ManageSubredditsPresenter(this, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        subredditsPresenter.loadData(getSupportLoaderManager());
    }

    @Override
    public void onDataLoaded(@NonNull ArrayList<LocalSubreddit> subreddits) {
        noSubscription.setVisibility(View.GONE);
        localSubreddits = subreddits;
        ManageSubredditsAdapter manageSubredditsAdapter;
        manageSubredditsAdapter = new ManageSubredditsAdapter(this, localSubreddits, subredditsPresenter);
        mRecyclerView.setAdapter(manageSubredditsAdapter);
        manageSubredditsAdapter.notifyDataSetChanged();

        if (subreddits.size()<=0) {
            noSubscription.setVisibility(View.VISIBLE);
            Log.d(LOG_TAG, "NO DATA FOUND");
        }
    }

    @Override
    public void onDataLoadError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSettingsOptionClick() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onHelpOptionClick() {
        Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDescendingOptionClick() {
        Toast.makeText(this, "Sorted descending", Toast.LENGTH_SHORT).show();

        Collections.sort(localSubreddits, new Comparator<LocalSubreddit>() {
            @Override
            public int compare(LocalSubreddit o1, LocalSubreddit o2) {
                return o1.getDisplay_name().compareTo(o2.getDisplay_name());
            }
        });
        Collections.reverse(localSubreddits);
        onDataLoaded(localSubreddits);

        updateToDB();
    }

    @Override
    public void onAscendingOptionClick() {

        Collections.sort(localSubreddits, new Comparator<LocalSubreddit>() {
            @Override
            public int compare(LocalSubreddit o1, LocalSubreddit o2) {
                return o1.getDisplay_name().compareTo(o2.getDisplay_name());
            }
        });
        onDataLoaded(localSubreddits);

        updateToDB();
        Toast.makeText(this, "Sorted as ascending", Toast.LENGTH_SHORT).show();
    }

    private void updateToDB(){
        Log.d(LOG_TAG, ""+localSubreddits.size());
        Uri uri = SubredditContract.SubredditEntry.CONTENT_URI;
        int update = 0;
        for (int i=0; i<localSubreddits.size(); i++) {
            Log.d(LOG_TAG, localSubreddits.get(i).getDisplay_name());

            ContentValues contentValues = new ContentValues();
            contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_POSITION, i);

            String where = SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_ID + "= ?";
            String[] whereArgs = new String[]{localSubreddits.get(i).getId()};
            update = getContentResolver().update(uri, contentValues, where, whereArgs);
            Log.d(LOG_TAG, ""+update);
        }

        // if update is successful
        // Add ORDER_HAS_CHANGED to true so that on main screen the tabs can be updated accordingly
        if (update > 0) {
            SharedPreferences sharedPreferences = getSharedPreferences("ORDER_CHANGED", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("ORDER_HAS_CHANGED", true);
            editor.apply();
        }

    }

    @Override
    public void onAddToWidgetClicked() {
        if (localSubreddits.size()>0) {
            Log.d(LOG_TAG, "Adding item to widget");

            Gson gson = new Gson();
            String jsonIngredient = gson.toJson(localSubreddits);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(WidgetContract.SUBREDDIT_LIST, jsonIngredient);
            editor.apply();

            updateAllWidgets();

            showSnackBar("Added to home widget");

            String android_id = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            // Firebase Analytics
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, android_id);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "widget added to home screen");
            bundle.putInt(FirebaseAnalytics.Param.QUANTITY, localSubreddits.size());
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "widget");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            
        } else {
            showSnackBar("Nothing to add to widget!");
        }
    }

    private void updateAllWidgets() {
        Intent widgetUpdater = new Intent(this, ReaditWidget.class);
        widgetUpdater.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, ReaditWidget.class));
        widgetUpdater.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(widgetUpdater);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return subredditsPresenter.onOptionItemSelected(item);
    }
    
    private void showSnackBar(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
