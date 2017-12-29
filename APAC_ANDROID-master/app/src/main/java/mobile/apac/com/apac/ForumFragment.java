package mobile.apac.com.apac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

import mobile.apac.com.apac.adapter.MainForumAdapter;
import mobile.apac.com.apac.schema.Forum;

public class ForumFragment extends Fragment {

    private String API_address = MainActivity.API_address;
    private String APP_TAG = "ForumFragment";

    private ArrayList<Forum> forumArrayList;

    private ListView mListView;

    public MainForumAdapter mainForumAdapter;

    public ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum,container,false);

        FloatingActionButton addQuestionBtn = (FloatingActionButton) view.findViewById(R.id.add_question_btn);
        addQuestionBtn.setOnCheckedChangeListener(mAddQuestionBtnListener);

        mListView = (ListView) view.findViewById(R.id.forumList);
        mListView.setOnItemClickListener(mListViewItemClickListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Call get_forum_list endpoint
        // Set up package data to be transmitted to server.
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("type", "get_forum_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Execute async task for "get_forum_list" API
        MyClientTask myClientTask = new MyClientTask(API_address, 1234, jsonRequest.toString());
        myClientTask.execute();

        // Start Thread Progress Monitor
        startSeverCommThreadProgress();
    }

    /**
     * Listener for forum list item click.
     */
    AdapterView.OnItemClickListener mListViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Forum selectedForumObject = forumArrayList.get(position);

            // Start new Feed activity
            Intent myFeeds = new Intent(getActivity(), AnswerListActivity.class);
            myFeeds.putExtra("Question Title", selectedForumObject.getTitle());
            myFeeds.putExtra("Question Description", selectedForumObject.getDescription());
            getActivity().startActivity(myFeeds);
        }
    };

    /**
     * Listener for Floating Button.
     */
    FloatingActionButton.OnCheckedChangeListener mAddQuestionBtnListener = new FloatingActionButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {

            Intent intent = new Intent(getActivity(),CreateForumActivity.class);
            startActivity(intent);
        }
    };

    /**
     * Listener for Refresh Button.
     */
    FloatingActionButton.OnCheckedChangeListener mRefreshBtnListener = new FloatingActionButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {

            // Call get_forum_list endpoint
            // Set up package data to be transmitted to server.
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("type", "get_forum_list");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Execute async task for "get_forum_list" API
            MyClientTask myClientTask = new MyClientTask(API_address, 1234, jsonRequest.toString());
            myClientTask.execute();

            // Start Thread Progress Monitor
            startSeverCommThreadProgress();
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

        MyClientTask(String addr, int port, String APIRequestJson) {
            dstAddress = addr;
            dstPort = port;
            apiRequestJson = APIRequestJson;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Show progress dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Communicating with server...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
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
                final String message = e.getMessage();
                Log.e(APP_TAG, response);

            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException";
                Log.e(APP_TAG, response);
                final String message = e.getMessage();

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

                if(!response.equalsIgnoreCase("UnknownHostException") && !response.equalsIgnoreCase("IOException")){

                    progressDialog.dismiss();

                    JSONObject json = new JSONObject(response);

                    Gson gson = new Gson();

                    Object jsonObject = json.get("forum");

                    // Change this list accordingly
                    ArrayList<Forum> forumList = new ArrayList<>();

                    if(jsonObject instanceof JSONArray){

                        JSONArray jsonArray = (JSONArray) jsonObject;

                        for(int i = 0; i < jsonArray.length(); i++) {
                            Forum temp = gson.fromJson(jsonArray.getString(i), Forum.class);
                            forumList.add(temp);
                        }

                    }else{
                        Forum temp = gson.fromJson(jsonObject.toString(), Forum.class);
                        forumList.add(temp);
                    }

                    // forumlist is the data received from the server.
                    forumArrayList = forumList;

                    mainForumAdapter = new MainForumAdapter(getActivity(), forumList);
                    mListView.setAdapter(mainForumAdapter);

                    Log.i(APP_TAG, "Response Json: " + json.toString());
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e(APP_TAG, e.getMessage());
            }

            super.onPostExecute(result);
        }
    }

    /**
     * Monitor server communication progress
     */
    private void startSeverCommThreadProgress(){

        final int timeout = 15;

        // Monitor the progress dialog to make sure it's not taking too long.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    int count = 0;
                    if (progressDialog != null) {
                        while (count < timeout && progressDialog.isShowing()) {

                            count++;

                            if (count == 7) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.setMessage("Server Communication is taking longer than normal...");
                                    }
                                });
                            }

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                if (count >= 13) {

                                    progressDialog.cancel();

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.setMessage("Server Communication is taking longer than normal...");
                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Current process is terminated. Please make sure that you have internet connection."
                                                    , Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
                                    });

                                }
                            }

                        }
                    }
                }
            }
        }).start();
    }

}