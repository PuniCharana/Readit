package example.com.readit.data.remote.models;

import java.util.List;

/**
 * Created by FamilyPC on 10/30/2017.
 */

@SuppressWarnings("ALL")
public class PostResponse {
    private String kind;
    private PostResponseData data;


    public class PostResponseData {
        String whitelist_status;
        String after;
        String before;
        List<PostResponseResult> children;

        public class PostResponseResult {
            String kind;
            PostComment data;

            public String getKind() {
                return kind;
            }

            public PostComment getData() {
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

        public List<PostResponseResult> getChildren() {
            return children;
        }
    }

    public String getKind() {
        return kind;
    }

    public PostResponseData getData() {
        return data;
    }
}
