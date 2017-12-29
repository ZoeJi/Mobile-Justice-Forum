package mobile.apac.com.apac;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import transport.ApiIntentService;

/**
 * Created by Yu on 11/22/15.
 */
public class CreateAnswerActivity extends Activity {
    private static String AppTag ="CreateAnswerActivity";

    private Button postAnswerbutton;
    private TextView forumTitleText;
    private TextView forumDescText;
    private EditText forumAnswerText;

    private String forumTitle;
    private String answerText;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_answer_layout_activity);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Add Your Answer");

        forumTitleText = (TextView) findViewById(R.id.answerTitle);
        forumDescText = (TextView) findViewById(R.id.answerDesc);
        forumAnswerText = (EditText) findViewById(R.id.answerText);
        String title = getIntent().getStringExtra("Question Title Answer");
        String description = getIntent().getStringExtra("Question Description Answer");
        forumTitleText.setText(title);
        forumDescText.setText(description);
        postAnswerbutton = (Button) findViewById(R.id.answerButton);//add click event of answer button
        postAnswerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumTitle = forumTitleText.getText().toString();
                answerText = forumAnswerText.getText().toString();
                if (forumTitle != null && answerText != null) {
                    ApiIntentService.startActionCreateNewAnswer(getApplication(),
                            forumTitleText.getText().toString(), forumAnswerText.getText().toString());
                    Log.e("title description",forumTitle+" "+answerText);
                    finish();
                }
            }
        });

    }
}

