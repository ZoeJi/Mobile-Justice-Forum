package edu.umn.cs.APAC.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
					+ "?user=" + dbUserName + "&password=" + dbPassword;
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
	 * Create a user and insert it to Database.
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param isAdmin
	 * @param birthDate
	 * @throws SQLException
	 */
	public String insertToUserTable(String id, String password,
			String firstName, String lastName, String email, String isAdmin,
			Timestamp birthDate) throws SQLException {
		// Check if exist.
		preparedStatement = connect.prepareStatement("SELECT * FROM " + dbName
				+ ".Users WHERE id='" + id + "'");
		resultSet = preparedStatement.executeQuery();
		if (!resultSet.next()) {
			// Create the prepared statement.
			preparedStatement = connect
					.prepareStatement("INSERT INTO "
							+ dbName
							+ ".Users (id, password, first_name, last_name, email, status, birthdate) VALUES (?, ?, ?, ?, ?, ?, ?)");

			preparedStatement.setString(1, id);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, firstName);
			preparedStatement.setString(4, lastName);
			preparedStatement.setString(5, email);
			preparedStatement.setString(6, isAdmin);
			preparedStatement.setTimestamp(7, birthDate);
			preparedStatement.execute();
			System.out.println(id + " is inserted.");
			return "Success";
		} else {
			return "UserExist";
		}
	}

	/**
	 * Called when login.
	 * 
	 * @param username
	 * @param password
	 * @return - APAC if the user is an APAC - None if the user is a regular
	 *         user - NoUser if the user is not registered - WrongPassword if
	 *         the password is wrong
	 * @throws SQLException
	 */
	public String checkLogin(String username, String password)
			throws SQLException {
		preparedStatement = connect
				.prepareStatement("SELECT password, status FROM " + dbName
						+ ".Users U WHERE U.id=?");

		preparedStatement.setString(1, username);

		resultSet = preparedStatement.executeQuery();
		String role = "";
		if (!resultSet.next()) {
			return "NoUser";
		} else {
			String returnedPassword = resultSet.getString("password");
			role = resultSet.getString("status");
			// If the password is wrong.
			if (!returnedPassword.equals(password)) {
				return "WrongPassword";
			}
		}
		return role;
	}

	/**
	 * Create a new forum.
	 * 
	 * @param title
	 * @param creator
	 * @throws SQLException
	 */
	public String createNewForum(String title, String creator) {
		// Create the prepared statement.
		try {
			preparedStatement = connect
					.prepareStatement("INSERT INTO "
							+ dbName
							+ ".Forum (title, creator, answer_feed, created_time, last_modified) VALUES (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, title);
			preparedStatement.setString(2, creator);
			preparedStatement.setInt(3, 0);

			java.util.Date date = new java.util.Date();
			java.sql.Timestamp timeStamp = new java.sql.Timestamp(
					date.getTime());
			preparedStatement.setTimestamp(4, timeStamp);
			preparedStatement.setTimestamp(5, timeStamp);
			preparedStatement.execute();
			System.out.println(title + " forum is created.");
			followForum(creator, title);
			return "Success";
		} catch (SQLException e) {
			e.printStackTrace();
			return "Failed";
		}
	}

	/**
	 * Called when a user follows a forum.
	 * 
	 * @param userId
	 * @param title
	 * @throws SQLException
	 */
	public static void followForum(String userId, String title)
			throws SQLException {
		preparedStatement = connect.prepareStatement("INSERT INTO " + dbName
				+ ".Follows (user_id, forum_title) VALUES (?, ?)");
		preparedStatement.setString(1, userId);
		preparedStatement.setString(2, title);
		preparedStatement.execute();
	}

	/**
	 * Get the list of forums to be shown. Return null if none available.
	 * 
	 * @throws SQLException
	 */
	public List<Forum> getForumList() throws SQLException {
		List<Forum> result = new ArrayList<Forum>();
		preparedStatement = connect
				.prepareStatement("SELECT title, creator, last_modified, answer_feed FROM "
						+ dbName + ".Forum ORDER BY last_modified DESC");
		resultSet = preparedStatement.executeQuery();
		// Return null if none is available.
		if (!resultSet.next()) {
			return null;
		}
		resultSet.beforeFirst();
		while (resultSet.next()) {
			Forum newForum = new Forum(resultSet.getString("title"),
					resultSet.getString("creator"),
					resultSet.getTimestamp("last_modified"),
					resultSet.getInt("answer_feed"));
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
	public String createFeedForForum(String forumTitle, String content,
			String creator) {
		try {
			preparedStatement = connect
					.prepareStatement("INSERT INTO "
							+ dbName
							+ ".Feed (forum_title, content, owner, posted_date) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, forumTitle);
			preparedStatement.setString(2, content);
			preparedStatement.setString(3, creator);
			java.util.Date date = new java.util.Date();
			java.sql.Timestamp timeStamp = new java.sql.Timestamp(
					date.getTime());
			preparedStatement.setTimestamp(4, timeStamp);
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
	public List<Feed> getFeedForForum(String forumTitle, int answerFeedId)
			throws SQLException {
		List<Feed> result = new ArrayList<Feed>();
		preparedStatement = connect
				.prepareStatement("SELECT id, content, owner, posted_date FROM "
						+ dbName
						+ ".Feed WHERE forum_title = '"
						+ forumTitle
						+ "' ORDER BY posted_date DESC");
		resultSet = preparedStatement.executeQuery();
		// Return null if none is available.
		if (!resultSet.next()) {
			return null;
		}
		resultSet.beforeFirst();
		while (resultSet.next()) {
			Feed newFeed = new Feed(resultSet.getInt("id"),
					resultSet.getString("content"),
					resultSet.getString("owner"),
					resultSet.getTimestamp("posted_date"), false);
			if (answerFeedId == resultSet.getInt("id")) {
				newFeed.setAnswer(true);
				result.add(0, newFeed);
			} else {
				result.add(newFeed);
			}
		}
		return result;
	}

	/**
	 * Set an answer for the forum.
	 * 
	 * @param forumTitle
	 * @param feedId
	 * @throws SQLException
	 */
	public String chooseAnswerForForum(String forumTitle, int feedId)
			throws SQLException {
		try {
			preparedStatement = connect.prepareStatement("UPDATE " + dbName
					+ ".Forum SET answer_feed = " + feedId + " WHERE title = '"
					+ forumTitle + "'");
			preparedStatement.execute();
			return "Success";
		} catch (SQLException e) {
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
