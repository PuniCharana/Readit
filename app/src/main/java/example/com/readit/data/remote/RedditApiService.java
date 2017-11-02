package example.com.readit.data.remote;

import example.com.readit.data.remote.models.SubredditPostResponse;
import example.com.readit.data.remote.models.SubredditResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by FamilyPC on 10/19/2017.
 */

@SuppressWarnings("ALL")
public interface RedditApiService {

    @GET("{path}/{filter}/.json")
    Call<SubredditResponse> getSubreddits(
            @Path("path") String path,
            @Path("filter") String filter,
            @Query("count") int count,
            @Query("after") String after
    );

    @GET("r/{subreddit}/{filter}/.json")
    Call<SubredditPostResponse> getSubredditPosts(
            @Path("subreddit") String subreddit,
            @Path("filter") String filter,
            @Query("count") int count,
            @Query("after") String after
    );

    @GET("{permalink}.json")
    Call<String> getSubredditPostDetail(
            @Path(value = "permalink", encoded = true) String permalink
    );
}