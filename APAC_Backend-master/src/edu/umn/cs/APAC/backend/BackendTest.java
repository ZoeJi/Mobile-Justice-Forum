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
    
    private static String serverAddress = "127.0.0.1";
    
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
            Socket socket = new Socket(serverAddress, 1234);
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
            Object feedObject = json.get("forum");
            if(feedObject instanceof JSONArray) {
                JSONArray forumJSONArray = (JSONArray) feedObject;
                List<Forum> forumList = new ArrayList<Forum>();
                for(int i = 0; i < forumJSONArray.length(); i++) {
                    Forum temp = gson.fromJson(forumJSONArray.getString(i), Forum.class);
                    forumList.add(temp);
                }
            } else {
                Forum temp = gson.fromJson(feedObject.toString(), Forum.class);
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
            System.out.println("Request = " + request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Socket socket = new Socket(serverAddress, 1234);
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
            Object feedObject = json.get("answer_feed");
            if(feedObject instanceof JSONArray) {
                JSONArray forumJSONArray = (JSONArray) feedObject;
                List<Feed> forumList = new ArrayList<Feed>();
                for(int i = 0; i < forumJSONArray.length(); i++) {
                    Feed temp = gson.fromJson(forumJSONArray.getString(i), Feed.class);
                    forumList.add(temp);
                }
            } else {
                Feed temp = gson.fromJson(feedObject.toString(), Feed.class);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private static void testCreateForum() {
        JSONObject request = new JSONObject();
        try {
            request.put("type", "create_new_forum");
            request.put("description", "Qianying");
            request.put("title", "Second Forum");
            
            Socket socket = new Socket(serverAddress, 1234);
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
            request.put("title", "First Forum");
            request.put("content", "Angela, I am trully the leader.");
            
            Socket socket = new Socket(serverAddress, 1234);
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
    
    /**
     * Test Create FAQ.
     */
    private static void testCreateFAQ() {
        JSONObject request = new JSONObject();
        try {
            request.put("type", "create_new_faq");
            request.put("description", "Boil it");
            request.put("title", "How to Make Instant Noodle");
            
            Socket socket = new Socket(serverAddress, 1234);
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
    
    /**
     * Test the get FAQ API.
     */
    private static void testGetFAQ() {
        JSONObject request = new JSONObject();
        Gson gson = new Gson();
        try {
            request.put("type", "get_faq_list");
            System.out.println("Request = " + request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Socket socket = new Socket(serverAddress, 1234);
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
            Object feedObject = json.get("forum");
            if(feedObject instanceof JSONArray) {
                JSONArray forumJSONArray = (JSONArray) feedObject;
                List<Forum> forumList = new ArrayList<Forum>();
                for(int i = 0; i < forumJSONArray.length(); i++) {
                    Forum temp = gson.fromJson(forumJSONArray.getString(i), Forum.class);
                    forumList.add(temp);
                }
            } else {
                Forum temp = gson.fromJson(feedObject.toString(), Forum.class);
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
     * Test Create FAQ.
     */
    private static void testUpdateFAQDescription() {
        JSONObject request = new JSONObject();
        try {
            request.put("type", "update_faq_description");
            request.put("description", "Boil it and Serve it");
            request.put("title", "How to Make Instant Noodle");
            
            Socket socket = new Socket(serverAddress, 1234);
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
        testCreateForum();
    }
    
}
