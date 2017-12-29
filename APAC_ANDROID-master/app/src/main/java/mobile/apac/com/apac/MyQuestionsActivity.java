package mobile.apac.com.apac;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyQuestionsActivity extends Activity {

    private String[] questions = { "Question1", "Question2", "Question3", "Question4", "Question5"};
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_questions);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("My questions");
        //setSupportActionBar(toolbar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, questions);
        ListView listView = (ListView) findViewById(R.id.my_question_list);
        listView.setAdapter(adapter);
    }

}
