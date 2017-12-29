package mobile.apac.com.apac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyAnswersActivity extends AppCompatActivity {
    private String[] answers = { "Answer1", "Answer2", "Answer3", "Answer4", "Answer5"};
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_answers);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("My answers");

        setSupportActionBar(toolbar);

        //textView = (TextView) findViewById(R.id.my_answer_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, answers);
        ListView listView = (ListView) findViewById(R.id.my_answer_list);
        listView.setAdapter(adapter);
    }

}
