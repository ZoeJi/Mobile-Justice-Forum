package mobile.apac.com.apac.schema;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haklim on 11/16/15.
 */
public class FeedList {

    @Expose
    private List<Feed> answer_feed = new ArrayList<>();

    public List<Feed> getAnswer_feed() {
        return answer_feed;
    }

    public void setAnswer_feed(List<Feed> answer_feed) {
        this.answer_feed = answer_feed;
    }
}
