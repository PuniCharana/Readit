package example.com.readit.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FamilyPC on 2017-10-25.
 */
@SuppressWarnings("ALL")
public class LocalSubreddit implements Parcelable {

    private final String display_name;
    private final String id;
    private final String url;
    private final int position;

    public LocalSubreddit(String display_name, String id, String url, int position) {
        this.display_name = display_name;
        this.id = id;
        this.url = url;
        this.position = position;
    }

    private LocalSubreddit(Parcel in) {
        display_name = in.readString();
        id = in.readString();
        url = in.readString();
        position = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(display_name);
        dest.writeString(id);
        dest.writeString(url);
        dest.writeInt(position);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LocalSubreddit> CREATOR = new Parcelable.Creator<LocalSubreddit>() {
        @Override
        public LocalSubreddit createFromParcel(Parcel in) {
            return new LocalSubreddit(in);
        }

        @Override
        public LocalSubreddit[] newArray(int size) {
            return new LocalSubreddit[size];
        }
    };

    public String getDisplay_name() {
        return display_name;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getPosition() {
        return position;
    }

    public static Creator<LocalSubreddit> getCREATOR() {
        return CREATOR;
    }
}