package example.com.readit.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import example.com.readit.R;
import example.com.readit.data.local.models.LocalSubreddit;
import example.com.readit.ui.subredditView.SubredditViewActivity;

/**
 * Provides data for the widget
 */

class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private ArrayList<LocalSubreddit> localSubreddits = new ArrayList<>();

    public WidgetFactory(Context mContext, Intent intent) {
        Log.d("WIDGET", "WidgetFactory");
        this.mContext = mContext;
    }

    private void initData() {
        Log.d("WIDGET", "initData");
        if (localSubreddits != null) {
            localSubreddits.clear();
        }

        // retrieve sharedPref
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();

        if (pref.contains(WidgetContract.SUBREDDIT_LIST)) {
            String jsonIngredients = pref.getString(WidgetContract.SUBREDDIT_LIST, null);
            TypeToken<ArrayList<LocalSubreddit>> token = new TypeToken<ArrayList<LocalSubreddit>>() {
            };
            localSubreddits = gson.fromJson(jsonIngredients, token.getType());
        }
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return localSubreddits.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);

        LocalSubreddit localSubreddit = localSubreddits.get(position);
        String displayName = "/r/" + localSubreddit.getDisplay_name();

        remoteView.setTextViewText(android.R.id.text1, displayName);
        remoteView.setTextColor(android.R.id.text1, ContextCompat.getColor(mContext, R.color.colorBlack));

        // Create pending intent so that when an item is clicked the intent will fired
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(SubredditViewActivity.SUBREDDIT_TO_LOAD, localSubreddit.getDisplay_name());
        remoteView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
