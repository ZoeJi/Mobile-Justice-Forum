package edu.umn.cs.APAC.backend;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.umn.cs.APAC.data_structures.Feed;
import edu.umn.cs.APAC.data_structures.Forum;

public class BackendTest {
	
	private static void testLogin() {
        JSONObject request = new JSONObject();
        try {
            request.put("type", "login");
            request.put("username", "qianying");
            request.put("password", "abc");
            System.out.println("Request = " + request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Socket socket = new Socket("127.0.0.1", 1234);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            System.out.println("Sending Request");
            out.println(request);
            out.flush();
            System.out.println("Request sent. Waiting for response");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            socket.close();
            System.out.println(json.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test the get forum / questions API.
     */
    private static void testGetForum() {
        JSONObject request = new JSONObject();
        Gson gson = new Gson();
        try {
            request.put("type", "get_forum_list");
            System.out.println("Request = " + request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Socket socket = new Socket("127.0.0.1", 1234);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            System.out.println("Sending Request");
            out.println(request);
            out.flush();
            System.out.println("Request sent. Waiting for response");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            socket.close();
            System.out.println(json.toString());
            JSONArray forumJSONArray = json.getJSONArray("forum");
            List<Forum> forumList = new ArrayList<Forum>();
            for(int i = 0; i < forumJSONArray.length(); i++) {
                Forum temp = gson.fromJson(forumJSONArray.getString(i), Forum.class);
                forumList.add(temp);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test get answer for a forum API.
     */
    private static void testGetAnswer() {
        JSONObject request = new JSONObject();
        Gson gson = new Gson();
        try {
            request.put("type", "get_answer");
            request.put("forum_title", "First Forum");
            request.put("answer_id", 0);
            System.out.println("Request = " + request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Socket socket = new Socket("127.0.0.1", 1234);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            System.out.println("Sending Request");
            out.println(request);
            out.flush();
            System.out.println("Request sent. Waiting for response");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            socket.close();
            System.out.println(json.toString());
            JSONArray forumJSONArray = json.getJSONArray("answer_feed");
            List<Feed> forumList = new ArrayList<Feed>();
            for(int i = 0; i < forumJSONArray.length(); i++) {
                Feed temp = gson.fromJson(forumJSONArray.getString(i), Feed.class);
                forumList.add(temp);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private static void testCreateAccount() {
        JSONObject request = new JSONObject();
        Gson gson = new Gson();
        try {
            request.put("type", "create_account");
            request.put("username", "angela");
            request.put("password", "abc");
            request.put("role", "NONE");
            request.put("firstname", "Angela");
            request.put("lastname", "Yu");
            request.put("email", "asdada@gmail.com");
            // Get the calendar.
            Calendar calendar = Calendar.getInstance();
            calendar.set(2012, Calendar.DECEMBER, 25);
            Timestamp birthdate = new Timestamp(calendar.getTimeInMillis());
            request.put("birthdate", gson.toJson(birthdate));
            
            Socket socket = new Socket("127.0.0.1", 1234);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            System.out.println("Sending Request");
            System.out.println(request.toString());
            out.println(request);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            socket.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void testCreateForumTest() {
        JSONObject request = new JSONObject();
        try {
            request.put("type", "create_new_forum");
            request.put("username", "angela");
            request.put("title", "Tenth Forum");
            
            Socket socket = new Socket("127.0.0.1", 1234);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            System.out.println("Sending Request");
            System.out.println(request.toString());
            out.println(request);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            socket.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
    
    private static void testSubmitAnswer() {

        JSONObject request = new JSONObject();
        try {
            request.put("type", "create_answer");
            request.put("username", "qianying");
            request.put("title", "First Forum");
            request.put("content", "I am trully the leader.");
            
            Socket socket = new Socket("127.0.0.1", 1234);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            System.out.println("Sending Request");
            System.out.println(request.toString());
            out.println(request);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            socket.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void testChooseAnswer() {

        JSONObject request = new JSONObject();
        try {
            request.put("type", "choose_answer");
            request.put("title", "I AM THE LEADER");
            request.put("feed_id", 6);
            
            Socket socket = new Socket("127.0.0.1", 1234);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            System.out.println("Sending Request");
            System.out.println(request.toString());
            out.println(request);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            socket.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        
    }
    
    public static void main(String[] args) {
        testLogin();
    }
    
}
