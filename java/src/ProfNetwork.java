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
    *
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
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
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

   /*
    * Creates a new user with provided login, passowrd and email
    * An empty block and contact list would be generated and associated with a user
    **/
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

   /*
    * Check log in credentials for an existing user
    * @return true if login succeeded else false if 
    * the user/password doesn't exist in the database
    **/
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

 /*
    * Query the list of friends for the given user
    * @return the list, or null if there are no friends
    **/
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

 /*
    * This is is a simple function which performans an update
    * on the user table. If the update fails, it returns false.
    **/
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

/*
    * 
    * TODO: WRITE DESCRIPTION
    **/
public boolean NewMessage(String user, String recipient, String message){
   try{
      // TODO: IMPLEMENT ME

      return true;

   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}

/*
    * 
    * TODO: WRITE DESCRIPTION
    **/
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

 /*
    * This method checks if the two users can connect. The constraint
    * is that if the user has less than 5 friends, they can add anyone
    * but if they have 5 or more, they can only add someone with a
    * connection level of at most 3. This is done by running BFS
    * and getting the min distance between the two users. If a 
    * path of 3 or less exists between the two, the request can
    * be submitted.
    * 
    **/
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

public boolean userExists(String user) {
   try{
      String query = String.format("SELECT * FROM USR WHERE userId = '%s'", user);
      return (this.executeQuery(query) > 0);
   } catch (Exception e) {
      System.err.println (e.getMessage ());
      return false;
   }
}
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

public boolean checkRequest(String user1, String user2) {
   try {
      String query = String.format("SELECT * FROM REQUESTS WHERE userId = '%s' AND connectionId = '%s';", user1, user2);
      return (this.executeQuery(query) > 0);
   } catch (Exception e) {
      System.err.println(e.getMessage ());
      return false;
   }
}

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

}//end ProfNetwork
