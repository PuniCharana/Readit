package example.com.readit.data.remote.models;

import java.util.List;

/**
 * Created by FamilyPC on 10/21/2017.
 */

@SuppressWarnings("ALL")
public class SubredditResponse {
    private String kind;
    private SubredditResponseData data;

    public class SubredditResponseData {
        String whitelist_status;
        String after;
        String before;
        List<SubredditResult> children;

        public class SubredditResult {
            String kind;
            Subreddit data;

            public String getKind() {
                return kind;
            }

            public Subreddit getData() {
                return data;
            }
        }

        public String getWhitelist_status() {
            return whitelist_status;
        }

        public String getAfter() {
            return after;
        }

        public String getBefore() {
            return before;
        }

        public List<SubredditResult> getChildren() {
            return children;
        }
    }

    public String getKind() {
        return kind;
    }

    public SubredditResponseData getData() {
        return data;
    }
}
