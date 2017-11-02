package example.com.readit.data.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by FamilyPC on 10/19/2017.
 */

@SuppressWarnings("ALL")
public class ApiClient {
    private static final String BASE_URL = "https://www.reddit.com/";

    private static Retrofit retrofit = null;

    private static final OkHttpClient httpClient = new OkHttpClient();

    private static final GsonConverterFactory gsonFactory = GsonConverterFactory.create();
    private static final ScalarsConverterFactory scalarFactory = ScalarsConverterFactory.create();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(scalarFactory)
                    .addConverterFactory(gsonFactory)
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }
}
