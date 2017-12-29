package edu.umn.cs.APAC.data_structures;

import java.sql.Timestamp;

public class Feed {

    private int feedId;
    private String content;
    private String owner;
    private Timestamp postedDate;
    private boolean isAnswer;

    public Feed(int feedId, String content, String owner, Timestamp postedDate, boolean isAnswer) {
        this.feedId = feedId;
        this.content = content;
        this.owner = owner;
        this.postedDate = postedDate;
        this.setAnswer(isAnswer);
    }

    public String getContent() {
        return content;
    }

    public String getOwner() {
        return owner;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public int getFeedId() {
        return feedId;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean isAnswer) {
        this.isAnswer = isAnswer;
    }
}
