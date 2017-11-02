package example.com.readit.data.remote.models;

import java.util.List;

/**
 * Created by FamilyPC on 10/25/2017.
 */

@SuppressWarnings("ALL")
public class SubredditPostResponse {
    private String kind;
    private SubredditResponseData data;

    public class SubredditResponseData {
        String whitelist_status;
        String after;
        String before;
        List<SubredditPostResponse.SubredditResponseData.SubredditResult> children;

        public class SubredditResult {
            String kind;
            SubredditPost data;

            public String getKind() {
                return kind;
            }

            public SubredditPost getData() {
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

        public List<SubredditPostResponse.SubredditResponseData.SubredditResult> getChildren() {
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
