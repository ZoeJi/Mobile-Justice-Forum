package edu.umn.cs.APAC.admin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The APAC Adminsitrator Interface.
 * 
 * @author cjonathan
 *
 */
public class AAI {

    // Server Address and Port
    private static String serverAddress = "ec2-54-236-198-128.compute-1.amazonaws.com";
    private static int serverPort = 1234;

    private static void printUsage() {
        System.out.println("Please provide one of the following commands:");
        System.out.println("- addFAQ <FAQ_Title> <FAQ_Description>");
        System.out.println("- updateFAQ <FAQ_Title> <New_FAQ_Description>");
        System.out.println("- rmForum <Forum_Title>");
        System.out.println("- rmFAQ <FAQ_Title>");
    }

    public static void main(String[] args) throws UnknownHostException,
            IOException, JSONException {
        // If no arguments provide, show the usage and exit.

        if (args.length == 0) {
            printUsage();
            System.exit(0);
        }
        String command = args[0];
        Socket socket = new Socket(serverAddress, serverPort);
        PrintWriter out = new PrintWriter(new DataOutputStream(
                socket.getOutputStream()));
        JSONObject request = new JSONObject();
        // Handle add FAQ request.
        if (command.equals("addFAQ")) {
            if (args.length < 3) {
                printUsage();
                System.exit(0);
            }
            request.put("type", "create_new_faq");
            request.put("description", args[2]);
            request.put("title", args[1]);

            System.out.println("Sending Request");
            System.out.println(request.toString());
            out.println(request);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            socket.close();
        } else
        // Handle update FAQ request.
        if (command.equals("updateFAQ")) {
            if (args.length < 3) {
                printUsage();
                System.exit(0);
            }
            request.put("type", "update_faq_description");
            request.put("description", args[2]);
            request.put("title", args[1]);

            System.out.println("Sending Request");
            System.out.println(request.toString());
            out.println(request);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            socket.close();
        } else
        // Handle remove Forum request.
        if (command.equals("rmForum")) {
            if (args.length < 2) {
                printUsage();
                System.exit(0);
            }
            request.put("type", "remove_forum");
            request.put("title", args[1]);

            System.out.println("Sending Request");
            System.out.println(request.toString());
            out.println(request);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            socket.close();
        } else
        // Handle remove Forum request.
        if (command.equals("rmFAQ")) {
            if (args.length < 2) {
                printUsage();
                System.exit(0);
            }
            request.put("type", "remove_faq");
            request.put("title", args[1]);

            System.out.println("Sending Request");
            System.out.println(request.toString());
            out.println(request);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            JSONObject json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            socket.close();
        }
    }
}
