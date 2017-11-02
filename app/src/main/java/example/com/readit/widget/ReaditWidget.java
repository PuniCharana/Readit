package example.com.readit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import example.com.readit.R;
import example.com.readit.ui.subredditView.SubredditViewActivity;

/**
 * Implementation of App Widget functionality.
 */
public class ReaditWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Log.d("WIDGET", "updateAppWidget");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.readit_widget);

        // Set adapter
        Intent widgetIntent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, widgetIntent);

        // set widget title
        views.setTextViewText(R.id.widget_title, context.getString(R.string.widget_title));

        // Create pending intent so that when an item is clicked the intent will fired
        Intent intent = new Intent(context, SubredditViewActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("WIDGET", "onUpdate");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

