package mobile.apac.com.apac;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import mobile.apac.com.apac.adapter.FeedsListAdapter;

/**
 * Created by qianyingji on 11/20/15.
 */
public class FAQAnswerListActivity extends Activity {
    private String API_address = MainActivity.API_address;
    private String APP_TAG = "AnswerListFragment";

    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private String title;
    private String description;
    private Toolbar toolbar;

    public FeedsListAdapter feedsListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_faq_answers);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("FAQ Answer");

        mTextViewTitle = (TextView) findViewById(R.id.questionTitle);
        mTextViewDescription = (TextView) findViewById(R.id.answerDesc);
        title = getIntent().getStringExtra("Question Title");
        description = getIntent().getStringExtra("Question Description");
        mTextViewTitle.setText(title);
        mTextViewDescription.setText(description);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
