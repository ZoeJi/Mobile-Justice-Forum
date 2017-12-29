package mobile.apac.com.apac.schema;

/**
 * Created by haklim on 11/16/15.
 *
 */

import com.google.gson.annotations.Expose;

import java.sql.Timestamp;

public class Forum {
    @Expose
    private String title;

    @Expose
    private String description;

    @Expose
    private Timestamp lastModified;

    public Forum(String title, String description, Timestamp lastModified) {
        this.title = title;
        this.description = description;
        this.lastModified = lastModified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }
}
