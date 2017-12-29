package edu.umn.cs.APAC.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.umn.cs.APAC.data_structures.Feed;
import edu.umn.cs.APAC.data_structures.Forum;

/**
 * A class that handles the connection to the MySQL Database.
 * 
 * @author cjonathan
 *
 */
public class MySQLConnector {

    // Default DB Connection Credentials.
    private static String dbUserName = "5115_15_fall_09";
    private static String dbPassword = "6XahjTV3mbCPhHKm";
    private static String dbName = "5115_15_fall_09";

    // Default DB Address.
    private static String dbAddress = "//52.27.175.76";
    private static int dbPort = 2222;

    private static Connection connect = null;

    private static PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    /**
     * Constructor.
     * 
     * @param databaseAddress
     * @param databasePort
     * @param databaseName
     * @param databaeUsername
     * @param databasePassword
     */
    public MySQLConnector(String databaseAddress, int databasePort,
            String databaseName, String databaeUsername, String databasePassword) {
        try {
            MySQLConnector.dbAddress = databaseAddress;
            MySQLConnector.dbPort = databasePort;
            MySQLConnector.dbName = databaseName;
            MySQLConnector.dbUserName = databaeUsername;
            MySQLConnector.dbPassword = databasePassword;

            // Load the MySQL Driver.
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Connect to the Database.
            String address = "jdbc:mysql:" + dbAddress + ":" + dbPort
                    + "?user=" + dbUserName + "&password=" + dbPassword + "&autoReconnect=true";
            connect = DriverManager.getConnection(address);
            System.out.println("Connecting to " + address);
        } catch (Exception e) {
            System.err.println("Failed to load the Database Driver.");
            e.printStackTrace();
        }
    }

    /**
     * Constructor when using default setting.
     */
    public MySQLConnector() {

        try {
            // Load the MySQL Driver.
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Connect to the Database.
            String address = "jdbc:mysql:" + dbAddress + ":" + dbPort
                    + "?user=" + dbUserName + "&password=" + dbPassword;
            connect = DriverManager.getConnection(address);
            System.out.println("Connecting to " + address);
        } catch (Exception e) {
            System.err.println("Failed to load the Database Driver.");
            e.printStackTrace();
        }

    }

    /**
     * Create a new forum.
     * 
     * @param title
     * @param creator
     * @throws SQLException
     */
    public String createNewForum(String title, String description) {
        // Create the prepared statement.
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO "
                            + dbName
                            + ".Forum (title, description, created_time, last_modified) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);

            // Get the current timestamp.
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp timeStamp = new java.sql.Timestamp(
                    date.getTime());
            preparedStatement.setTimestamp(3, timeStamp);
            preparedStatement.setTimestamp(4, timeStamp);
            preparedStatement.execute();
            System.out.println(title + " forum is created.");

            // Return success if no exception found.
            return "Success";
        } catch (SQLException e) {
            // Return failed if exception found.
            e.printStackTrace();
            return "Failed";
        }
    }

    /**
     * Get the list oforumf forums to be shown. Return null if none available.
     * 
     * @throws SQLException
     */
    public List<Forum> getForumList() throws SQLException {
        List<Forum> result = new ArrayList<Forum>();
        // Get the forum list from the Database.
        preparedStatement = connect
                .prepareStatement("SELECT title, description, last_modified FROM "
                        + dbName + ".Forum ORDER BY last_modified DESC");
        resultSet = preparedStatement.executeQuery();
        // Return null if none is available.
        if (!resultSet.next()) {
            return null;
        }
        resultSet.beforeFirst();
        // For each of the forum, create an forum object, put it into the list
        // and return the list.
        while (resultSet.next()) {
            Forum newForum = new Forum(resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getTimestamp("last_modified"));
            result.add(newForum);
        }
        return result;
    }

    /**
     * Insert a feed into a forum.
     * 
     * @param forumTitle
     * @throws SQLException
     */
    public String createFeedForForum(String forumTitle, String content) {
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO "
                            + dbName
                            + ".Feed (forum_title, content, posted_date) VALUES (?, ?, ?)");
            preparedStatement.setString(1, forumTitle);
            preparedStatement.setString(2, content);
            // Get the current timestamp.
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp timeStamp = new java.sql.Timestamp(
                    date.getTime());
            preparedStatement.setTimestamp(3, timeStamp);
            preparedStatement.execute();

            // Update the last modified of the forum.
            preparedStatement = connect.prepareStatement("UPDATE " + dbName
                    + ".Forum SET last_modified = ?  WHERE title = '"
                    + forumTitle + "'");
            preparedStatement.setTimestamp(1, timeStamp);
            preparedStatement.execute();
            return "Success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    /**
     * Get the resulting conversation within a forum. The answerFeedId is 0 if
     * there is no answer yet. The first feed from the list is the answer if it
     * is available.
     * 
     * @param forumTitle
     * @return
     * @throws SQLException
     */
    public List<Feed> getFeedForForum(String forumTitle) throws SQLException {
        List<Feed> result = new ArrayList<Feed>();
        preparedStatement = connect
                .prepareStatement("SELECT id, content, posted_date FROM "
                        + dbName + ".Feed WHERE forum_title = '" + forumTitle
                        + "' ORDER BY posted_date DESC");
        resultSet = preparedStatement.executeQuery();
        // Return null if none is available.
        if (!resultSet.next()) {
            return result;
        }
        resultSet.beforeFirst();
        // For each feed, insert to the list and return it.
        while (resultSet.next()) {
            Feed newFeed = new Feed(resultSet.getInt("id"),
                    resultSet.getString("content"),
                    resultSet.getTimestamp("posted_date"));
            result.add(newFeed);
        }
        return result;
    }

    /**
     * Get the list of FAQ to be shown. Return null if none available.
     * 
     * @throws SQLException
     */
    public List<Forum> getFAQList() throws SQLException {
        List<Forum> result = new ArrayList<Forum>();
        // Get the forum list from the Database.
        preparedStatement = connect
                .prepareStatement("SELECT title, description, last_modified FROM "
                        + dbName + ".FAQ ORDER BY last_modified DESC");
        resultSet = preparedStatement.executeQuery();
        // Return null if none is available.
        if (!resultSet.next()) {
            return null;
        }
        resultSet.beforeFirst();
        // For each of the forum, create an forum object, put it into the list
        // and return the list.
        while (resultSet.next()) {
            Forum newForum = new Forum(resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getTimestamp("last_modified"));
            result.add(newForum);
        }
        return result;
    }
    
    /*
     * 
     * BELOW ARE FUNCTIONS FOR ADMIN USER.
     * 
     */
    
    /**
     * Create a new FAQ.
     * 
     * @param title
     * @param creator
     * @throws SQLException
     */
    public String createNewFAQ(String title, String description) {
        // Create the prepared statement.
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO "
                            + dbName
                            + ".FAQ (title, description, created_time, last_modified) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);

            // Get the current timestamp.
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp timeStamp = new java.sql.Timestamp(
                    date.getTime());
            preparedStatement.setTimestamp(3, timeStamp);
            preparedStatement.setTimestamp(4, timeStamp);
            preparedStatement.execute();
            System.out.println(title + " FAQ is created.");

            // Return success if no exception found.
            return "Success";
        } catch (SQLException e) {
            // Return failed if exception found.
            e.printStackTrace();
            return "Failed";
        }
    }
    
    /**
     * Update the FAQ Description. 
     * @param title
     * @param newDescription
     * @return
     */
    public String updateFAQDescription(String title, String newDescription) {
        // Create the prepared statement.
        try {
            preparedStatement = connect
                    .prepareStatement("UPDATE "
                            + dbName
                            + ".FAQ SET description = ? , last_modified = ? WHERE title = ?");
            preparedStatement.setString(3, title);
            preparedStatement.setString(1, newDescription);

            // Get the current timestamp.
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp timeStamp = new java.sql.Timestamp(
                    date.getTime());
            preparedStatement.setTimestamp(2, timeStamp);
            preparedStatement.execute();
            System.out.println(title + " FAQ is updated.");

            // Return success if no exception found.
            return "Success";
        } catch (SQLException e) {
            // Return failed if exception found.
            e.printStackTrace();
            return "Failed";
        }
    }
    
    /**
     * Update the FAQ Description. 
     * @param title
     * @param newDescription
     * @return
     */
    public String removeForum(String title) {
        // Create the prepared statement.
        try {
            preparedStatement = connect
                    .prepareStatement("DELETE FROM "
                            + dbName
                            + ".Forum WHERE title = ?");
            preparedStatement.setString(1, title);
            preparedStatement.execute();
            System.out.println(title + " is deleted.");

            // Return success if no exception found.
            return "Success";
        } catch (SQLException e) {
            // Return failed if exception found.
            e.printStackTrace();
            return "Failed";
        }
    }
    
    /**
     * Update the FAQ Description. 
     * @param title
     * @param newDescription
     * @return
     */
    public String removeFAQ(String title) {
        // Create the prepared statement.
        try {
            preparedStatement = connect
                    .prepareStatement("DELETE FROM "
                            + dbName
                            + ".FAQ WHERE title = ?");
            preparedStatement.setString(1, title);
            preparedStatement.execute();
            System.out.println(title + " is deleted.");

            // Return success if no exception found.
            return "Success";
        } catch (SQLException e) {
            // Return failed if exception found.
            e.printStackTrace();
            return "Failed";
        }
    }
    
    /**
     * Close the DB Connection.
     */
    public void closeDB() {
        try {
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
