package example.com.readit.data.remote.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FamilyPC on 10/30/2017.
 */
@SuppressWarnings("ALL")
public class PostComment implements Parcelable {
    private final String id;
    private final String author;
    private final long ups;
    private final long downs;
    private final String body;
    private final long created;
    private final long depth;
    private final long score;

    private PostComment(Parcel in) {
        id = in.readString();
        author = in.readString();
        ups = in.readLong();
        downs = in.readLong();
        body = in.readString();
        created = in.readLong();
        depth = in.readLong();
        score = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeLong(ups);
        dest.writeLong(downs);
        dest.writeString(body);
        dest.writeLong(created);
        dest.writeLong(depth);
        dest.writeLong(score);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PostComment> CREATOR = new Parcelable.Creator<PostComment>() {
        @Override
        public PostComment createFromParcel(Parcel in) {
            return new PostComment(in);
        }

        @Override
        public PostComment[] newArray(int size) {
            return new PostComment[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public long getUps() {
        return ups;
    }

    public long getDowns() {
        return downs;
    }

    public String getBody() {
        return body;
    }

    public long getCreated() {
        return created;
    }

    public long getDepth() {
        return depth;
    }

    public long getScore() {
        return score;
    }

    public static Parcelable.Creator<PostComment> getCREATOR() {
        return CREATOR;
    }
}