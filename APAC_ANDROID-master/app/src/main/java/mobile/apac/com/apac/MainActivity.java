package mobile.apac.com.apac;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {
    public static String API_address = "ec2-54-236-198-128.compute-1.amazonaws.com";
    private final String APP_TAG = "MainActivity";

    Toolbar toolbar;
    ViewPager pager;
    TabsPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Forum", "FAQ","APAC"};
    int NumbOfTabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new TabsPagerAdapter(getSupportFragmentManager(),Titles, NumbOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(0);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }


    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.action_login) {
//
//            // Call Login API intent service.
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    ApiIntentService.startActionLogIn(getApplication(),"qianying","abc");
//                }
//            }).start();
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

