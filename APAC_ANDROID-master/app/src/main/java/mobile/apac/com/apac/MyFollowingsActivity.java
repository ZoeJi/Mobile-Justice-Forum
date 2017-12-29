package mobile.apac.com.apac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyFollowingsActivity extends AppCompatActivity {
    private String[] followings = { "Question1", "Question2", "Question3", "Question4", "Question5"};
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_followings);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("My followings");

        setSupportActionBar(toolbar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, followings);
        ListView listView = (ListView) findViewById(R.id.my_followings_list);
        listView.setAdapter(adapter);
    }


}
