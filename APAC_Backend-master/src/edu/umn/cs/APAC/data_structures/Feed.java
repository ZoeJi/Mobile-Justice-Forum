package edu.umn.cs.APAC.data_structures;

import java.sql.Timestamp;

/**
 * Data structure for the Feed object
 * @author cjonathan
 *
 */
public class Feed {

    private int feedId;
    private String content;
    private Timestamp postedDate;

    public Feed(int feedId, String content, Timestamp postedDate) {
        this.feedId = feedId;
        this.content = content;
        this.postedDate = postedDate;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public int getFeedId() {
        return feedId;
    }
}
