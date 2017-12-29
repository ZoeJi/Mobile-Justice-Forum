package mobile.apac.com.apac.schema;

import com.google.gson.annotations.Expose;

import java.sql.Timestamp;

/**
 * Created by haklim on 11/16/15.
 */
public class Feed {

    @Expose
    private int feedId;

    @Expose
    private String content;

    @Expose
    private Timestamp postedDate;

    public Feed(int feedId, String content, Timestamp postedDate) {
        this.feedId = feedId;
        this.content = content;
        this.postedDate = postedDate;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Timestamp postedDate) {
        this.postedDate = postedDate;
    }
}
