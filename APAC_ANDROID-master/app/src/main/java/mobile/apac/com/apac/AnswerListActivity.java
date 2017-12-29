package mobile.apac.com.apac;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import mobile.apac.com.apac.adapter.FeedsListAdapter;
import mobile.apac.com.apac.schema.Feed;

/**
 * Created by qianyingji on 11/20/15.
 */
public class AnswerListActivity extends Activity {
    private String API_address = MainActivity.API_address;
    private String APP_TAG = "AnswerListFragment";

    private ArrayList<Feed> answersArrayList;

    private ListView mListView;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private String title;
    private String description;
    private Toolbar toolbar;

    public FeedsListAdapter feedsListAdapter;

//    public ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_answer_list);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Question & Answers");

        mListView = (ListView) findViewById(R.id.answerList);
        mTextViewTitle = (TextView) findViewById(R.id.questionTitle);
        mTextViewDescription = (TextView) findViewById(R.id.questionDesc);
        title = getIntent().getStringExtra("Question Title");
        description = getIntent().getStringExtra("Question Description");
        mTextViewTitle.setText(title);
        mTextViewDescription.setText(description);
        FloatingActionButton answerQuestionBtn = (FloatingActionButton) findViewById(R.id.answer_btn);
        answerQuestionBtn.setOnCheckedChangeListener(mAddAnswerBtnListener);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_answerlist,container,false);
//
//        FloatingActionButton addQuestionBtn = (FloatingActionButton) view.findViewById(R.id.add_question_btn);
//        addQuestionBtn.setOnCheckedChangeListener(mAddQuestionBtnListener);
//
//        mListView = (ListView) view.findViewById(R.id.answerList);
//        mTextViewTitle = (TextView) view.findViewById(R.id.questionTitle);
//        mListView.setOnItemClickListener(mListViewItemClickListener);
//
//        Log.e("AnswerListActivity", "AnswerListActivity Called");
//
//        return view;
//    }

    @Override
    public void onResume() {
        super.onResume();

        //===========================
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("type", "get_answer");
            jsonRequest.put("forum_title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyClientTask myClientTask = new MyClientTask(API_address, 1234, jsonRequest.toString(), this);
        myClientTask.execute();
        //===========================
    }

    /**
     * Listener for forum list item click.
     */
//    AdapterView.OnItemClickListener mListViewItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Forum selectedForumObject = answersArrayList.get(position);
//
//            Toast.makeText(getActivity(),
//                    "Selected Forum title: " + selectedForumObject.getTitle(), Toast.LENGTH_LONG).show();
//
//            //@TODO Start new Feed activity
//        }
//    };

    /**
     * Listener for Floating Button.
     */
    FloatingActionButton.OnCheckedChangeListener mAddAnswerBtnListener = new FloatingActionButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {

            Intent intent = new Intent(getApplicationContext(),CreateAnswerActivity.class);
            intent.putExtra("Question Title Answer", title);
            intent.putExtra("Question Description Answer", description);
            startActivity(intent);
        }
    };

    /**
     * Use background thread to communicate with server and also update Adapter in UI Thread.
     */
    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;

        String apiRequestJson = "";
        String response = "";
        Activity activity = null;

        MyClientTask(String addr, int port, String APIRequestJson, Activity activity) {
            dstAddress = addr;
            dstPort = port;
            apiRequestJson = APIRequestJson;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            // Show progress dialog
//            progressDialog = new ProgressDialog(activity);
//            progressDialog.setMessage("Communicating with server...");
//            //progressDialog.setIndeterminate(true);
//            progressDialog.setCancelable(true);
//            progressDialog.setIndeterminate(true);
//            progressDialog.show();
        }

        /**
         * Don't touch this.
         *
         * @param arg0
         * @return
         */
        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                // Create Socket Connection
                socket = new Socket(dstAddress, dstPort);

                // Send API request
                PrintWriter out = new PrintWriter(new DataOutputStream(
                        socket.getOutputStream()));
                Log.i(APP_TAG, "Sending Request: " + apiRequestJson);
                out.println(apiRequestJson);
                out.flush();

                // Process response from Server
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                /*
                * Note: inputStream.read() will block if no data return
                */
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException";
                Log.e(APP_TAG, response);
            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException";
                Log.e(APP_TAG, response);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //textResponse.setText(response);
            try {
//                progressDialog.dismiss();

                if(!response.equalsIgnoreCase("UnknownHostException") && !response.equalsIgnoreCase("IOException")){

                    JSONObject json = new JSONObject(response);

                    Gson gson = new Gson();

                    Object jsonObject = json.get("answer_feed");

                    // Change this list accordingly
                    ArrayList<Feed> feedList = new ArrayList<>();

                    if(jsonObject instanceof JSONArray){

                        JSONArray jsonArray = (JSONArray) jsonObject;

                        for(int i = 0; i < jsonArray.length(); i++) {
                            Feed temp = gson.fromJson(jsonArray.getString(i), Feed.class);
                            feedList.add(temp);
                        }

                    }else{
                        Feed temp = gson.fromJson(jsonObject.toString(), Feed.class);
                        feedList.add(temp);
                    }

                    // feedList is the data received from the server.
                    answersArrayList = feedList;

                    feedsListAdapter = new FeedsListAdapter(activity, feedList);

                    mListView.setAdapter(feedsListAdapter);

                    Log.i(APP_TAG, "Response Json: " + json.toString());
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e(APP_TAG, e.getMessage());
            }

            super.onPostExecute(result);
        }
    }
}
