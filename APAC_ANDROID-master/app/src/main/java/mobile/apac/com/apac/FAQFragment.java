package mobile.apac.com.apac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class FAQFragment extends Fragment {

    private String API_address = MainActivity.API_address;
    private String APP_TAG = "FAQFragment";

    private ArrayList<Forum> forumArrayList;

    private ListView mListView;

    public MainForumAdapter mainForumAdapter;

    public ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq,container,false);

        mListView = (ListView) view.findViewById(R.id.forumList);
        mListView.setOnItemClickListener(mListViewItemClickListener);

        Log.e("ForumFragment", "ForumFragment Called");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Call get_faq_list endpoints
        // Set up package data to be transmitted to server.
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("type", "get_faq_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Execute async task for "get_forum_list" API
        MyClientTask myClientTask = new MyClientTask(API_address, 1234, jsonRequest.toString());
        myClientTask.execute();
    }

    /**
     * Listener for forum list item click.
     */
    AdapterView.OnItemClickListener mListViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Forum selectedForumObject = forumArrayList.get(position);

//            Toast.makeText(getActivity(),
//                    "Selected FAQ title: " + selectedForumObject.getTitle(), Toast.LENGTH_LONG).show();

            //@TODO Start new Feed activity
            Intent myFeeds = new Intent(getActivity(), FAQAnswerListActivity.class);
            myFeeds.putExtra("Question Title", selectedForumObject.getTitle());
            myFeeds.putExtra("Question Description", selectedForumObject.getDescription());
            getActivity().startActivity(myFeeds);
        }
    };

    /**
     * Listener for button refresh.
     */
    FloatingActionButton.OnCheckedChangeListener mRefreshBtnListener = new FloatingActionButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {

            // Call get_faq_list endpoints

            // Set up package data to be transmitted to server.
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("type", "get_faq_list");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Execute async task for "get_forum_list" API
            MyClientTask myClientTask = new MyClientTask(API_address, 1234, jsonRequest.toString());
            myClientTask.execute();
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
            //progressDialog.setIndeterminate(true);
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
                progressDialog.dismiss();

                if(!response.equalsIgnoreCase("UnknownHostException") && !response.equalsIgnoreCase("IOException")){

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

}