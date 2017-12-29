package edu.umn.cs.APAC.backend;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.umn.cs.APAC.data_structures.Feed;
import edu.umn.cs.APAC.data_structures.Forum;

/**
 * The server that receives request and return response to client.
 * 
 * @author cjonathan
 *
 */
public class Server {

    private static int serverPort = 1234;

    /**
     * Shows how to run the application.
     */
    private static void printUsage() {
        System.out
                .println("To run the program: java -jar <Database Configuration File> <Port #>");
        System.out.println("The database configuration file should contains:");
        System.out.println("Database_address=<db adress>");
        System.out.println("Database_port=<db port>");
        System.out.println("Database_name=<db name>");
        System.out.println("Database_username=<db username>");
        System.out.println("Database_password=<db password>");
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Starting the server.");

        // Check the input.
        if (args.length != 2 || args[0].equals("-h") || args[0].equals("-help")) {
            printUsage();
            System.exit(0);
        }
        // Update the server port.
        serverPort = Integer.parseInt(args[1]);

        // Check the configuration file.
        System.out.println("Reading the configuration file.");
        File configFile = new File(args[0]);
        if (!configFile.exists()) {
            System.out
                    .println("Database Configuration file cannot be found. Please check the path of the file");
            System.exit(0);
        }
        BufferedReader br = new BufferedReader(new FileReader(configFile));
        String databaseAddress = br.readLine().split("=")[1];
        int databasePort = Integer.parseInt(br.readLine().split("=")[1]);
        String databaseName = br.readLine().split("=")[1];
        String databaseUsername = br.readLine().split("=")[1];
        String databasePassword = br.readLine().split("=")[1];
        br.close();

        System.out.println("Connecting to Database.");
        MySQLConnector dbConnector = new MySQLConnector(databaseAddress,
                databasePort, databaseName, databaseUsername, databasePassword);
        
        // Create the socket listener.
        InetAddress ip = InetAddress.getLocalHost();
        ServerSocket listener = new ServerSocket(serverPort);
        System.out.println("Server ip is " + ip);
        System.out.println("Waiting for user connection.");
        // Set an infinite timeout.
        listener.setSoTimeout(0);
        // Start a thread for heartbeat.
        new DBHeartBeatHandler(dbConnector).start();
        try {
        	// Looping while waiting for request.
            while (true) {
                new BackendHandler(listener.accept(), dbConnector).start();
            }
        } finally {
            listener.close();
        }
    }
    
    /**
     * A thread that will handle the backend request.
     * 
     * @author cjonathan
     *
     */
    private static class BackendHandler extends Thread {
        private Socket socket;
        private MySQLConnector dbConnector;

        public BackendHandler(Socket socket, MySQLConnector dbConnector) {
            this.socket = socket;
            this.dbConnector = dbConnector;
        }

        /**
         * Create a new Forum.
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void createNewForumHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Receives create forum request.");

            // Send the request to the MySQLConnector.
            String forumTitle = json.getString("title");
            String description = json.getString("description");
            String result = dbConnector.createNewForum(forumTitle, description);

            // Create the response object and send it back.
            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            response.put("result", result);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }

        /**
         * Handle get forum list request.
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws SQLException
         * @throws JSONException
         * @throws IOException
         */
        private void getForumListHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws SQLException, JSONException,
                IOException {
            System.out.println("Receives get forum list request.");

            // Creating the response object.
            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            // Get the forum from the MySQLConnection.
            List<Forum> forumList = dbConnector.getForumList();
            Gson gson = new Gson();
            for (Forum forum : forumList) {
                // Serialize the object.
                response.accumulate("forum", gson.toJson(forum));
            }
            // Send out the response JSON.
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }

        /**
         * Submit an answer for a forum.
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws IOException
         */
        private void createAnswerHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, IOException {
            System.out.println("Inserting answer for forum "
                    + json.getString("title"));

            // Parse the incoming request.
            String title = json.getString("title");
            String content = json.getString("content");
            JSONObject response = new JSONObject();

            // Send the request to MySQLConnector.
            String result = dbConnector.createFeedForForum(title, content);
            response.put("type", json.get("type"));
            response.put("result", result);
            // Send out the result.
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }

        /**
         * Handle get answer for a forum.
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void getFeedListHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {

            // Parse the request.
            String forumTitle = json.getString("forum_title");
            System.out.println("Receives get feed for forum " + forumTitle);

            JSONObject response = new JSONObject();
            List<Feed> feedList = dbConnector.getFeedForForum(forumTitle);
            if(feedList.size() != 0) {
                Gson gson = new Gson();
            	for (Feed feed : feedList) {
                    // Serialize the object.
                    response.accumulate("answer_feed", gson.toJson(feed));
                }
            }
            // Send the response back.
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }

        /**
         * Handle get FAQ list request.
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws SQLException
         * @throws JSONException
         * @throws IOException
         */
        private void getFAQHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws SQLException, JSONException,
                IOException {
            System.out.println("Receives get FAQ list request.");

            // Creating the response object.
            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            // Get the forum from the MySQLConnection.
            List<Forum> forumList = dbConnector.getFAQList();
            Gson gson = new Gson();
            for (Forum forum : forumList) {
                // Serialize the object.
                response.accumulate("forum", gson.toJson(forum));
            }
            // Send out the response JSON.
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }
        
        /*
         * 
         * BELOW ARE FOR ADMIN PURPOSE
         * 
         */
        
        /**
         * Create a new FAQ. Only by Admin
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void createNewFAQHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Receives create FAQ request.");

            // Send the request to the MySQLConnector.
            String faqTitle = json.getString("title");
            String description = json.getString("description");
            String result = dbConnector.createNewFAQ(faqTitle, description);

            // Create the response object and send it back.
            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            response.put("result", result);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }
        
        /**
         * Update the FAQ Description
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void updateFAQDescriptionHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Receives update FAQ description request.");

            // Send the request to the MySQLConnector.
            String faqTitle = json.getString("title");
            String newDescription = json.getString("description");
            String result = dbConnector.updateFAQDescription(faqTitle, newDescription);

            // Create the response object and send it back.
            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            response.put("result", result);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }
        
        /**
         * Remove a forum.
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void removeForumHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Receives remove forum request.");

            // Send the request to the MySQLConnector.
            String title = json.getString("title");
            String result = dbConnector.removeForum(title);

            // Create the response object and send it back.
            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            response.put("result", result);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }
        
        /**
         * Remove a forum.
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void removeFAQHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Receives remove forum request.");

            // Send the request to the MySQLConnector.
            String title = json.getString("title");
            String result = dbConnector.removeFAQ(title);

            // Create the response object and send it back.
            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            response.put("result", result);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }
        
        /**
         * Run the thread.
         */
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                JSONObject json = new JSONObject(in.readLine());
                String requestType = json.getString("type");
                // Choose the correct type of request handled.
                switch (requestType) {
                case "get_forum_list":
                    getForumListHandler(json, socket, dbConnector);
                    break;
                case "get_answer":
                    getFeedListHandler(json, socket, dbConnector);
                    break;
                case "create_new_forum":
                    createNewForumHandler(json, socket, dbConnector);
                    break;
                case "create_answer":
                    createAnswerHandler(json, socket, dbConnector);
                    break;
                case "get_faq_list":
                    getFAQHandler(json, socket, dbConnector);
                    break;
                case "create_new_faq":
                    createNewFAQHandler(json, socket, dbConnector);
                    break;
                case "update_faq_description":
                    updateFAQDescriptionHandler(json, socket, dbConnector);
                    break;
                case "remove_forum":
                    removeForumHandler(json, socket, dbConnector);
                    break;
                case "remove_faq":
                    removeFAQHandler(json, socket, dbConnector);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Keep heart beat to the database to maintain connectivity.
     * @author cjonathan
     *
     */
    private static class DBHeartBeatHandler extends Thread {
        private MySQLConnector dbConnector;
        private long sleepTime = 1800000;
        
        public DBHeartBeatHandler(MySQLConnector dbConnector) {
            this.dbConnector = dbConnector;
        }
        
        public void run() {
            while(true) {
                List<Forum> forumList = null;
                try {
                    forumList = dbConnector.getFAQList();
                    forumList.clear();
                    Thread.sleep(sleepTime);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
