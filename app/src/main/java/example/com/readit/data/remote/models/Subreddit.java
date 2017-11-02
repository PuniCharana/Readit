package example.com.readit.data.remote.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FamilyPC on 10/19/2017.
 */

@SuppressWarnings("ALL")
public class Subreddit implements Parcelable {

    private final String banner_img;
    private final String id;
    private final String display_name;
    private final String header_img;
    private final String description;
    private final long subscribers;
    private final long created;
    private final String url;
    private final String public_description;

    private Subreddit(Parcel in) {
        banner_img = in.readString();
        id = in.readString();
        display_name = in.readString();
        header_img = in.readString();
        description = in.readString();
        subscribers = in.readLong();
        created = in.readLong();
        url = in.readString();
        public_description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(banner_img);
        dest.writeString(id);
        dest.writeString(display_name);
        dest.writeString(header_img);
        dest.writeString(description);
        dest.writeLong(subscribers);
        dest.writeLong(created);
        dest.writeString(url);
        dest.writeString(public_description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Subreddit> CREATOR = new Parcelable.Creator<Subreddit>() {
        @Override
        public Subreddit createFromParcel(Parcel in) {
            return new Subreddit(in);
        }

        @Override
        public Subreddit[] newArray(int size) {
            return new Subreddit[size];
        }
    };

    public String getBanner_img() {
        return banner_img;
    }

    public String getId() {
        return id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getHeader_img() {
        return header_img;
    }

    public String getDescription() {
        return description;
    }

    public long getSubscribers() {
        return subscribers;
    }

    public long getCreated() {
        return created;
    }

    public String getUrl() {
        return url;
    }

    public String getPublic_description() {
        return public_description;
    }

    public static Creator<Subreddit> getCREATOR() {
        return CREATOR;
    }
}