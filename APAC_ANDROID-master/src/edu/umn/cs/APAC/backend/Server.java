package edu.umn.cs.APAC.backend;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        ServerSocket listener = new ServerSocket(serverPort);
        System.out.println("Waiting for user connection.");
        try {
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
         * Create an account handler. Return either success or UserExist
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void createAccountHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Receives create account request");
            Gson gson = new Gson();
            String username = json.getString("username");
            String password = json.getString("password");
            String role = json.getString("role");
            String firstName = json.getString("firstname");
            String lastName = json.getString("lastname");
            String email = json.getString("email");
            Timestamp birthdate = gson.fromJson(json.getString("birthdate"),
                    Timestamp.class);

            String status = dbConnector.insertToUserTable(username, password,
                    firstName, lastName, email, role, birthdate);
            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            response.put("result", status);
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }

        /**
         * Handle the login request.
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void loginHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Receives login request");

            String username = json.getString("username");
            String password = json.getString("password");
            String role = dbConnector.checkLogin(username, password);

            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            // If there is no user available, return message: NoUser
            if (role.equals("NoUser")) {
                response.put("result", role);
                // If wrong password, return message: WrongPassword
            } else if (role.equals("WrongPassword")) {
                response.put("result", role);
                // If regular user
            } else if (role.equals("NONE")) {
                response.put("user_type", "regular");
                // If APAC user
            } else if (role.equals("APAC")) {
                response.put("user_type", "APAC");
            }
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
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
        private void createNewForum(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Receives create forum request.");

            String username = json.getString("username");
            String forumTitle = json.getString("title");
            String result = dbConnector.createNewForum(forumTitle, username);

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
        private void forumListHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws SQLException, JSONException,
                IOException {
            System.out.println("Receives get forum list request.");

            JSONObject response = new JSONObject();
            response.put("type", json.get("type"));
            List<Forum> forumList = dbConnector.getForumList();
            Gson gson = new Gson();
            for (Forum forum : forumList) {
                // Serialize the object.
                response.accumulate("forum", gson.toJson(forum));
            }
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
        private void createAnswerForForum(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, IOException {
            System.out.println("Inserting answer for forum "
                    + json.getString("title"));

            String user = json.getString("username");
            String title = json.getString("title");
            String content = json.getString("content");
            JSONObject response = new JSONObject();

            String result = dbConnector
                    .createFeedForForum(title, content, user);
            response.put("type", json.get("type"));
            response.put("result", result);
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
        private void forumAnswerHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            String forumTitle = json.getString("forum_title");
            int answerID = json.getInt("answer_id");
            System.out.println("Receives get feed for forum " + forumTitle);

            JSONObject response = new JSONObject();
            List<Feed> feedList = dbConnector.getFeedForForum(forumTitle,
                    answerID);
            Gson gson = new Gson();
            for (Feed feed : feedList) {
                // Serialize the object.
                response.accumulate("answer_feed", gson.toJson(feed));
            }
            PrintWriter out = new PrintWriter(new DataOutputStream(
                    socket.getOutputStream()));
            out.println(response);
            out.flush();
            socket.close();
        }

        /**
         * Handle choosing answer request.
         * 
         * @param json
         * @param socket
         * @param dbConnector
         * @throws JSONException
         * @throws SQLException
         * @throws IOException
         */
        private void chooseAnswerHandler(JSONObject json, Socket socket,
                MySQLConnector dbConnector) throws JSONException, SQLException,
                IOException {
            System.out.println("Choosing an answer for forum "
                    + json.getString("title"));

            String forumTitle = json.getString("title");
            int answerId = json.getInt("feed_id");
            JSONObject response = new JSONObject();

            String result = dbConnector.chooseAnswerForForum(forumTitle,
                    answerId);
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
                case "login":
                    loginHandler(json, socket, dbConnector);
                    break;
                case "get_forum_list":
                    forumListHandler(json, socket, dbConnector);
                    break;
                case "get_answer":
                    forumAnswerHandler(json, socket, dbConnector);
                    break;
                case "create_account":
                    createAccountHandler(json, socket, dbConnector);
                    break;
                case "create_new_forum":
                    createNewForum(json, socket, dbConnector);
                    break;
                case "create_answer":
                    createAnswerForForum(json, socket, dbConnector);
                    break;
                case "choose_answer":
                    chooseAnswerHandler(json, socket, dbConnector);
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
}
