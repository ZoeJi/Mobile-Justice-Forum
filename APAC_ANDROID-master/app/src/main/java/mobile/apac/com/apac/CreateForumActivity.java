package mobile.apac.com.apac;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import transport.ApiIntentService;

/**
 * Created by haklim on 11/16/15.
 */
public class CreateForumActivity extends Activity {

    private static String AppTag ="CreateForumActivity";

    private Button mPostbutton;
    private EditText forumTitleEditText;
    private EditText forumDescEditText;

    private String forumTitle;
    private String userName;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_forum_layout_activity);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Post Your Question");

        forumTitleEditText = (EditText) findViewById(R.id.forumTitle);
        forumDescEditText = (EditText) findViewById(R.id.forumDesc);

        /*
         * call a service for create new forum
         */
        mPostbutton = (Button) findViewById(R.id.postButton);
        mPostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumTitle = forumTitleEditText.getText().toString();
                if(forumTitle != null) {
                    ApiIntentService.startActionCreateNewForum(getApplication(),
                            forumTitleEditText.getText().toString(), forumDescEditText.getText().toString());

                    finish();
                }
            }
        });

    }
}
