package transport;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import mobile.apac.com.apac.MainActivity;
import util.DisplayToast;

/**
 * Created by haklim on 11/15/15.
 */
public class ApiIntentService extends IntentService {

    private static final String APP_TAG = "ApiItentService";
    private static final String EXTRA_JSON = "mobile.apac.com.extra.JSON";

    private static final String ACTION_LOGIN = "mobile.apac.com.LOGIN";
    private static final String ACTION_GET_FORUM_LIST = "mobile.apac.com.GETFORUMLIST";
    private static final String ACTION_CREATE_NEW_FORUM = "mobile.apac.com.CREATENEWFORUM";
    private static final String ACTION_CREATE_NEW_ANSWSER ="mobile.apac.com.CREATEANSWER";
    private static final String ACTION_GET_ANSWER ="mobile.apac.com.GETANSWER";

    private static Context mContext;

    private String API_address = MainActivity.API_address;

    private Handler mHandler;

    /**
     * Constructor
     */
    public ApiIntentService(){
        super("ApiIntentService");

        mHandler = new Handler();
    }

    /**
     * Start action Login API call.
     *
     * @param context
     * @param username
     * @param password
     */
    public static void startActionLogIn(Context context, String username, String password){

        mContext = context;
        //@TODO current username and password is hardcoded => need to be removed.

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("type", "login");
            jsonRequest.put("username", username);
            jsonRequest.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, ApiIntentService.class);
        intent.setAction(ACTION_LOGIN);
        intent.putExtra(EXTRA_JSON, jsonRequest.toString());
        context.startService(intent);

        Log.i(APP_TAG, "IntentService for Login API is in progress");
    }

    /**
     * Start action create_new_forum api
     */
    public static void startActionCreateNewForum(Context context, String forumTitle, String content){

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("type", "create_new_forum");
            jsonRequest.put("title", forumTitle);
            jsonRequest.put("description", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, ApiIntentService.class);
        intent.setAction(ACTION_CREATE_NEW_FORUM);
        intent.putExtra(EXTRA_JSON, jsonRequest.toString());
        context.startService(intent);

        Log.i(APP_TAG, "IntentService for Login API is in progress");
    }

    public static void startActionCreateNewAnswer(Context context, String forumTitle, String content){

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("type", "create_answer");
            jsonRequest.put("title", forumTitle);
            jsonRequest.put("content", content);
            Log.e("json+============:", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, ApiIntentService.class);
        intent.setAction(ACTION_CREATE_NEW_ANSWSER);
        intent.putExtra(EXTRA_JSON, jsonRequest.toString());
        context.startService(intent);

        Log.i(APP_TAG, "IntentService for Login API is in progress");
    }

    /**
     * Start action get_forum_list API call.
     *
     * @param context
     * @param username
     * @param password
     */
    public static void startActionGetForumList(Context context, String username, String password){

        mContext = context;
        //@TODO current username and password is hardcoded => need to be removed.

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("type", "get_forum_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, ApiIntentService.class);
        intent.setAction(ACTION_GET_FORUM_LIST);
        intent.putExtra(EXTRA_JSON, jsonRequest.toString());
        context.startService(intent);

        Log.i(APP_TAG, "IntentService for get_forum_list API is in progress");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if(action.equals(ACTION_LOGIN)){
                final String json = intent.getStringExtra(EXTRA_JSON);
                handleActionLogin(json);
            }

            if(action.equals(ACTION_GET_FORUM_LIST)){
                final String json = intent.getStringExtra(EXTRA_JSON);
                handleActionGetForumList(json);
            }

            if(action.equals(ACTION_CREATE_NEW_FORUM)){
                final String json = intent.getStringExtra(EXTRA_JSON);
                handleActionCreateForum(json);
            }
            if(action.equals(ACTION_CREATE_NEW_ANSWSER)){
                final String json = intent.getStringExtra(EXTRA_JSON);
                handleActionCreateAnswer(json);
            }
        }
    }

    /**
     * Handle Action Login
     *This will Create a socket with the server and send Login request API
     *
     * @param jsonRequest
     */
    private void handleActionLogin(String jsonRequest){

        try {
            Socket socket = new Socket(API_address, 1234);

            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));

            Log.i(APP_TAG, "Sending Request: " + jsonRequest );

            out.println(jsonRequest);
            out.flush();

            Log.i(APP_TAG, "Request sent. Waiting for response");

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            socket.close();

            Log.i(APP_TAG, "Response Json: " + json.toString());

        } catch (UnknownHostException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        } catch (IOException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        } catch (JSONException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * Handle action GetForumList
     */
    private void handleActionGetForumList(String jsonRequest){
        try {
            Socket socket = new Socket(API_address, 1234);

            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));

            Log.i(APP_TAG, "Sending Request" + jsonRequest );

            out.println(jsonRequest);
            out.flush();

            Log.i(APP_TAG, "Request sent. Waiting for response");

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            socket.close();

            Log.i(APP_TAG, "Response Json: " + json.toString());
            //@TODO do s.th with the response

        } catch (UnknownHostException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        } catch (IOException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        } catch (JSONException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * Handle action GetForumList
     */
    private void handleActionCreateForum(String jsonRequest){
        try {
            Socket socket = new Socket(API_address, 1234);

            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));

            Log.i(APP_TAG, "Sending Request" + jsonRequest );

            out.println(jsonRequest);
            out.flush();

            Log.i(APP_TAG, "Request sent. Waiting for response");

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            socket.close();

            Log.i(APP_TAG, "Response Json: " + json.toString());
            //@TODO do s.th with the response

        } catch (UnknownHostException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        } catch (IOException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        } catch (JSONException e) {
            mHandler.post(new DisplayToast(this, e.getMessage()));
            e.printStackTrace();
        }
    }

/*handle create answer*/
private void handleActionCreateAnswer(String jsonRequest){
    try {
        Socket socket = new Socket(API_address, 1234);

        PrintWriter out = new PrintWriter(new DataOutputStream(
                socket.getOutputStream()));

        Log.i(APP_TAG, "Sending Request" + jsonRequest );

        out.println(jsonRequest);
        out.flush();

        Log.i(APP_TAG, "Request sent. Waiting for response");

        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        JSONObject json = new JSONObject(in.readLine());
        socket.close();

        Log.i(APP_TAG, "Response Json: " + json.toString());
        //@TODO do s.th with the response

    } catch (UnknownHostException e) {
        mHandler.post(new DisplayToast(this, e.getMessage()));
        e.printStackTrace();
    } catch (IOException e) {
        mHandler.post(new DisplayToast(this, e.getMessage()));
        e.printStackTrace();
    } catch (JSONException e) {
        mHandler.post(new DisplayToast(this, e.getMessage()));
        e.printStackTrace();
    }
}

}
