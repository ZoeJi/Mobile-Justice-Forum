package edu.umn.cs.APAC.data_structures;

import java.sql.Timestamp;

/**
 * Data structures for forum object.
 * 
 * @author cjonathan
 *
 */
public class Forum {

    private String title;
    private String description;
    private Timestamp lastModified;
    
    public Forum(String title, String description, Timestamp lastModified) {
        this.title = title;
        this.description = description;
        this.lastModified = lastModified;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public Timestamp getLastModified() {
        return lastModified;
    }
}
