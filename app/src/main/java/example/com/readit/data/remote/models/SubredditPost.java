package example.com.readit.data.remote.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FamilyPC on 10/25/2017.
 */

@SuppressWarnings("ALL")
public class SubredditPost implements Parcelable {
    private final String domain;
    private final String subreddit;
    private final String selftext;
    private final String thumbnail;
    private final String id;
    private final String title;
    private final long score;
    private final boolean over_18;
    private final String subreddit_id;
    private final String name;
    private final String permalink;
    private final long created;
    private final String url;
    private final String author;
    private final long ups;
    private final long num_comments;
    private final boolean is_video;

    private SubredditPost(Parcel in) {
        domain = in.readString();
        subreddit = in.readString();
        selftext = in.readString();
        thumbnail = in.readString();
        id = in.readString();
        title = in.readString();
        score = in.readLong();
        over_18 = in.readByte() != 0x00;
        subreddit_id = in.readString();
        name = in.readString();
        permalink = in.readString();
        created = in.readLong();
        url = in.readString();
        author = in.readString();
        ups = in.readLong();
        num_comments = in.readLong();
        is_video = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(domain);
        dest.writeString(subreddit);
        dest.writeString(selftext);
        dest.writeString(thumbnail);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeLong(score);
        dest.writeByte((byte) (over_18 ? 0x01 : 0x00));
        dest.writeString(subreddit_id);
        dest.writeString(name);
        dest.writeString(permalink);
        dest.writeLong(created);
        dest.writeString(url);
        dest.writeString(author);
        dest.writeLong(ups);
        dest.writeLong(num_comments);
        dest.writeByte((byte) (is_video ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SubredditPost> CREATOR = new Parcelable.Creator<SubredditPost>() {
        @Override
        public SubredditPost createFromParcel(Parcel in) {
            return new SubredditPost(in);
        }

        @Override
        public SubredditPost[] newArray(int size) {
            return new SubredditPost[size];
        }
    };

    public String getDomain() {
        return domain;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getSelftext() {
        return selftext;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getScore() {
        return score;
    }

    public boolean isOver_18() {
        return over_18;
    }

    public String getSubreddit_id() {
        return subreddit_id;
    }

    public String getName() {
        return name;
    }

    public String getPermalink() {
        return permalink;
    }

    public long getCreated() {
        return created;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public long getUps() {
        return ups;
    }

    public long getNum_comments() {
        return num_comments;
    }

    public boolean is_video() {
        return is_video;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public static Creator<SubredditPost> getCREATOR() {
        return CREATOR;
    }
}
