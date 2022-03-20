/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.HashMap;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class ProfNetwork {

   // reference to physical database connection.
   private Connection _connection = null;

   /**
    * Creates a new instance of ProfNetwork
    * Constructor for the ProfNetwork class. Takes in database credentials as arguments. 
    * By default, it connects to Postgres via *localhost*.
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public ProfNetwork (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end ProfNetwork

   /**
    * Method to execute a SQL statement. 
    * Update SQL instructions includes `CREATE`, `UPDATE`, `INSERT`, `DELETE`,  and`DROP`.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   private void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   private int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   private List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
          List<String> record = new ArrayList<String>();
         for (int i=1; i<=numCol; ++i)
            record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   private int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       if(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   private int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * Creates a new user with provided login, password and email
    * An empty block and contact list would be generated and associated with a user
    * @param the userID that will be used to login into the account
    * @param the password that will be used to login into the account
    * @param the email that will be associated with the account
    * @return returns true if the the user account has been successfully created and false otherwise
    */
   public boolean CreateUser(String login, String password, String email){
      try{

         //Creating empty contact\block lists for a user
         String query = String.format("INSERT INTO USR (userId, password, email) VALUES ('%s','%s','%s')", login, password, email);

         this.executeUpdate(query);
         System.out.println("User successfully created!");

         return true;

      }catch(Exception e){
         System.err.println (e.getMessage ());
         return false;
      }
   }//end

   /**
    * Check log in credentials for an existing user
    * @param the userID for the existing user account
    * @param the password for the existing user account
    * @return true if login succeeded else false if 
    * the user/password doesn't exist in the database
    */
   public boolean LogIn(String login, String password){
      try{
         String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", login, password);
         return (this.executeQuery(query) > 0);
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return false;
      }
   }//end

// Rest of the functions definition go in here

   /**
    * Query the list of friends for the given user
    * @param the user currently logged into the database
    * @return the list, or null if there are no friends
    */
public List<String> FriendList(String user){
   try{
      String query = String.format("SELECT userA FROM FRIENDS WHERE userB = '%s'", user)
                   + String.format("UNION SELECT userB FROM FRIENDS WHERE userA = '%s';", user);

      List<List<String> > queryResponse = executeQueryAndReturnResult(query);
      return queryResponse.stream().flatMap(Collection::stream).collect(Collectors.toList());

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return null;
   }
}

 	/**
    * This is is a simple function which checks if the user exists in the database, 
    * and if so, updates the user with the specified password. 
    * @param The current user logged into the database
    * @param the password that the user wants to update their account with.
    * @return true if the password change succeeds, otherwise false.
    */
public boolean ChangePassword(String user, String password){
   try{

      String query = String.format("UPDATE USR SET password = '%s' WHERE userid = '%s';", password, user);
      this.executeUpdate(query);

      return true;

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

	/** 
    * This method sends a message from the user to the recipient. 
    * @param the current user logged into the database
    * @param the user who the message will be sent to
    * @param the message contained within a string
    * @return true if message is sent, otherwise false.
    */
public boolean NewMessage(String user, String recipient, String message){
   try{
      // TODO: IMPLEMENT ME

      return true;

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

	/** 
    * This method sends a friend request from the user to the requestedUser by 
    * adding a record to the database if it doesn't already exist.
    * @param the user who will be sending the request
    * @param the requested user who will be recieving the request
    * @return true if the request is sent, otherwise false.
    */
public boolean SendRequest(String user, String requestedUser){
   try{

      // CHECK IF REQUEST (USER -> RECIPIENT) ALREADY EXISTS
      if (this.checkRequest(user, requestedUser)) {
         // DO NOTHING SINCE REQUEST ALREADY SUBMITTED
         return true;
      }

      // CHECK IF REQUEST (RECIPIENT -> USER) EXISTS
      if (this.checkRequest(requestedUser, user)) {
         // IF SO: DELETE REQUEST, INSERT FRIEND
         String deleteRequest = String.format("DELETE FROM REQUESTS WHERE userId = '%s' AND connectionId = '%s';", requestedUser, user);
         String insertFriend = String.format("INSERT INTO FRIENDS (userA, userB) VALUES ('%s', '%s');", user, requestedUser);
         this.executeUpdate(deleteRequest);
         this.executeUpdate(insertFriend);
      } else {
         // IF NOT: ADD REQUEST
         String insertRequest = String.format("INSERT INTO REQUESTS (userId, connectionId) VALUES ('%s', '%s');", user, requestedUser);
         this.executeUpdate(insertRequest);
      }

      return true;

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

 	/** 
 	* This method checks if the two users can connect. The constraint
    * is that if the user has less than 5 friends, they can add anyone
    * but if they have 5 or more, they can only add someone with a
    * connection level of at most 3. This is done by running BFS
    * and getting the min distance between the two users. If a 
    * path of 3 or less exists between the two, the request can
    * be submitted.
    * @param a user in the database
    * @param another user in the database
    * @return true if the users can connect with one another, false otherwise
    */
public boolean canConnect(String user1, String user2) {
   List<String> user1Friends = this.FriendList(user1);
   if (user1Friends.size() < 5) {
      return true;
   }

   HashSet<String> visited = new HashSet<String>();
   HashMap<String, Integer> distances = new HashMap<String, Integer>();
   LinkedList<String> queue = new LinkedList<String>();
   queue.add(user1);
   distances.put(user1,0);

   String curUser = "";
   List<String> friends = null;
   int curDistance;
   while (!queue.isEmpty() && !distances.containsKey(user2)) {
      curUser = queue.remove();
      curDistance = distances.get(curUser);
      if (!visited.contains(curUser) && curDistance < 3) {
         visited.add(curUser);
         friends = this.FriendList(curUser);
         for (String friend : friends) {
            if (!visited.contains(friend)) {
               queue.add(friend);
               distances.put(friend, curDistance + 1);
            }
         }
      }
   }
   Integer value = distances.get(user2);
   System.out.println(String.format("Distance from %s to %s is %d", user1, user2, value));

   return !(value == null);
}

/** 
 * This method checks if a user exists in the database.
 * @param the user being checked in the database
 * @return true if the user can be found, otherwise false.
 */
public boolean userExists(String user) {
   try{
      String query = String.format("SELECT * FROM USR WHERE userId = '%s'", user);
      return (this.executeQuery(query) > 0);
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

/** 
 * This method checks if two users are friends.
 * @param a user in the database
 * @param another user in the database
 * @return true if the two users are friends, otherwise false.
 */
public boolean checkFriends(String user1, String user2) {
   try {
      String query = String.format("SELECT * FROM FRIENDS WHERE (userA = '%s' AND userB = '%s')", user1, user2)
                   + String.format("OR  (userB = '%s' AND userA = '%s');", user1, user2);

      return (this.executeQuery(query) > 0);

   } catch (Exception e) {
      System.err.println(e.getMessage ());
      return false;
   }
}

/** 
 * This method checks if a friend request has been sent from user1 to user2
 * @param a user in the database
 * @param another user in the database
 * @return true if there has been a friend request sent between the users, otherwise false.
 */
public boolean checkRequest(String user1, String user2) {
   try {
      String query = String.format("SELECT * FROM REQUESTS WHERE userId = '%s' AND connectionId = '%s';", user1, user2);
      return (this.executeQuery(query) > 0);
   } catch (Exception e) {
      System.err.println(e.getMessage ());
      return false;
   }
}

/** 
 * This method gets the friend requests that have been sent to the user.
 * @param the user who is receiving the requests
 * @return returns the list of requests (if any) that the user has received
 */
public List<String> getRequested(String username) {
   try{
      String requested = String.format("SELECT userId FROM REQUESTS WHERE connectionId = '%s';", username);

      List<List<String> > requestedResponse = executeQueryAndReturnResult(requested);
      return requestedResponse.stream().flatMap(Collection::stream).collect(Collectors.toList());

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return null;
   }
}

/** 
 * This method gets the friend request that the user has sent out
 * @param the user who is sending the requests
 * @return returns the list of requests (if any) that the user has sent
 */
public List<String> getRequesting(String username) {
   try{
      String requested = String.format("SELECT connectionId FROM REQUESTS WHERE userId = '%s';", username);

      List<List<String> > requestedResponse = executeQueryAndReturnResult(requested);
      return requestedResponse.stream().flatMap(Collection::stream).collect(Collectors.toList());

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return null;
   }
}

/** 
 * This method allows a user to accept a friend request
 * @param the user who is receiving the friend request
 * @param the user who sent the friend request
 * @return returns true if the friend request has been accepted, false otherwise
 */
public boolean acceptRequest(String username, String otherUser) {
   try {
      System.out.println("ACCEPTING REQUEST");
      this.SendRequest(username, otherUser);
      return true;

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

/** 
 * This method allows a user to deny a friend request
 * @param the user who is receiving the friend request
 * @param the user who sent the friend request
 * @return returns true if the friend request has been denied, false otherwise
 */
public boolean denyRequest(String username, String otherUser) {
   try {
      if (this.checkRequest(otherUser, username)) {
         String deleteReq = String.format("DELETE FROM REQUESTS WHERE userId = '%s' AND connectionId = '%s';", otherUser, username);
         this.executeUpdate(deleteReq);
         return true;
      }
      return false;
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

/** 
 * This method allows a user withdraw a sent friend request
 * @param the user who is sending the friend request
 * @param the user who is receiving the friend request
 * @return returns true if the friend request has been withdrawn, false otherwise
 */
public boolean withdrawRequest(String username, String otherUser) {
   try {
      if (this.checkRequest(username, otherUser)) {
         String deleteReq = String.format("DELETE FROM REQUESTS WHERE userId = '%s' AND connectionId = '%s';", username, otherUser);
         this.executeUpdate(deleteReq);
         return true;
      }
      return false;
      
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

/** 
 * This method allows a user to get the profile of another user
 * @param the user whose profile is being viewed
 * @return returns the profile of the user being viewed
 */
public List<String> getProfile(String username) {
   try{
      String query = String.format("SELECT * FROM USR WHERE userId = '%s'", username);

      List<List<String> > queryResponse = executeQueryAndReturnResult(query);
      return queryResponse.stream().flatMap(Collection::stream).collect(Collectors.toList());
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return null;
   }
}

/** 
 * This method allows a user to update their profile information
 * @param the user whose profile is being updated
 * @param the attribute of the profile that will be modified
 * @param the value of the attribute of the profile being modified
 * @return returns true if the profile has been updated, false otherwise
 */
public boolean updateProfile(String username, String attribute, String value) {
   try {
      if (this.userExists(username)) {
         String update = String.format("UPDATE USR SET %s = '%s' WHERE userId = '%s';", attribute, value, username);
         this.executeUpdate(update);
         return true;
      }
      return false;
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

/** 
 * This method allows a user to search other people in the database
 * @param the search term that is used to scan through the database
 * @return returns the users in the database that have met the search criteria
 */
public List<String> searchPeople(String search_term) {
   try {
      String query = "SELECT userId FROM USR WHERE userId LIKE '%" + search_term + "%'"
                   + " OR name LIKE '%" + search_term + "%' LIMIT 10;";
      List<List<String> > queryResponse = executeQueryAndReturnResult(query);
      return queryResponse.stream().flatMap(Collection::stream).collect(Collectors.toList());

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return null;
   }
}

/** 
 * This method gets the message data between two users
 * @param one user in the database
 * @param another user in the database
 * @param limits the number of messages to pull from the database
 * @return returns the messages between the two specified users
 */
public List<String> getThread(String user1, String user2, int limit) {
   try {

      String query = String.format("SELECT senderId, contents, sendTime, status FROM (")
                       + String.format("    SELECT * FROM MESSAGE")
                       + String.format("    WHERE (senderId = '%s' AND receiverId = '%s') ", user1, user2)
                       + String.format("    OR  (receiverId = '%s' AND senderId = '%s') ", user1, user2)
                       + String.format("    ORDER BY msgId DESC LIMIT %d", limit)
                       + String.format(") AS M ")
                       + String.format("ORDER BY msgId ASC");

      List<List<String> > query_response = executeQueryAndReturnResult(query);
      List<String> return_query = query_response.stream().flatMap(Collection::stream).collect(Collectors.toList());
      
      
      String update = String.format("UPDATE MESSAGE SET status = 'Read' WHERE senderId = '%s' AND receiverId = '%s' AND status = 'Delivered';", user2, user1);
      this.executeUpdate(update);

      return return_query;
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return null;
   } 
}

/** 
 * This method obtains the amount of unread messages that the user has
 * @param the user currently logged into the database
 * @return returns an integer with the number of unread messages
 */
public int getUnreadCount(String username) {
   try {
      String query = String.format("SELECT msgId FROM MESSAGE WHERE receiverId = '%s' AND status = 'Delivered';", username);
      return executeQuery(query);
   }

   catch (Exception e) {
      System.err.println (e.getMessage ());
      return 0;
   }
}

/** 
 * This method gets the data of unread messages that the user has
 * @param the user currently logged into the database
 * @return returns the unread messages that the user has in the database
 */
public List<String> getUnreadMessages(String username) {
   try {
      String query = String.format("SELECT senderId, contents, sendTime FROM MESSAGE ")
                   + String.format("WHERE receiverId = '%s' AND status = 'Delivered' ", username)
                   + String.format("ORDER BY msgId ASC;");

      List<List<String> > query_response = executeQueryAndReturnResult(query);
      List<String> return_query = query_response.stream().flatMap(Collection::stream).collect(Collectors.toList());

      String update = String.format("UPDATE MESSAGE SET status = 'Read' WHERE receiverId = '%s' AND status = 'Delivered';", username);

      this.executeUpdate(update);

      return return_query;

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return null;
   } 
}

/** 
 * This method sends a message to another user in the database
 * @param the sender of the message
 * @param the receiver of the message
 * @param the message that is being sent
 * @return returns true if the message was successfully sent, false otherwise
 */
public boolean sendMessage(String sender, String receiver, String message) {
   try {
      if (this.userExists(sender) && this.userExists(receiver)) {
         String insert = String.format("INSERT INTO MESSAGE (senderId, receiverId, contents, sendTime, status) ")
                       + String.format("VALUES ('%s', '%s', '%s', CURRENT_TIMESTAMP, 'Delivered');", sender, receiver, message);
         
         this.executeUpdate(insert);
         return true;
      }
      return false;
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

/** 
 * This method deletes the last message sent between two users
 * @param the sender of the message
 * @param the receiver of the message
 * @return returns true if the message was successfully deleted, false otherwise
 */
public boolean deleteLastMessage(String sender, String receiver) {
   try {
      if (this.userExists(sender) && this.userExists(receiver)) {
         String insert = String.format("DELETE FROM MESSAGE WHERE senderId = '%s' AND receiverId = '%s' AND ", sender, receiver)
                       + String.format("msgId IN (SELECT msgId FROM MESSAGE WHERE senderId = '%s' AND receiverId = '%s' ", sender, receiver)
                       + String.format("ORDER BY msgId DESC LIMIT 1);");
         System.out.println ("Message Deleted!");
         this.executeUpdate(insert);
         return true;
      }
      return false;
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

}//end ProfNetwork
