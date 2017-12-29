package mobile.apac.com.apac.schema;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by haklim on 11/16/15.
 */
public class ForumList {

    @Expose
    private ArrayList<Forum> forum = new ArrayList<>();

    public ArrayList<Forum> getForum() {
        return forum;
    }

    public void setForum(ArrayList<Forum> forum) {
        this.forum = forum;
    }
}
