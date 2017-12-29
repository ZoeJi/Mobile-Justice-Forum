package edu.umn.cs.APAC.data_structures;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data structures for forum object.
 * 
 * @author cjonathan
 *
 */
public class Forum {

    private String title;
    private String creator;
    private Timestamp lastModified;
    private int answerFeedId; 
    
    public Forum(String title, String creator, Timestamp lastModified, int answerFeedId) {
        this.title = title;
        this.creator = creator;
        this.lastModified = lastModified;
        this.setAnswerFeedId(answerFeedId);
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }


    public Timestamp getLastModified() {
        return lastModified;
    }

    public int getAnswerFeedId() {
        return answerFeedId;
    }

    public void setAnswerFeedId(int answerFeedId) {
        this.answerFeedId = answerFeedId;
    }
}
