package example.com.readit.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by FamilyPC on 11/1/2017.
 */

@SuppressWarnings("ALL")
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("WIDGET", "onGetViewFactory");
        return new WidgetFactory(this, intent);
    }
}
