import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class CommandLineDriver {

   private static Scanner in = new Scanner(System.in);
    
    /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            ProfNetwork.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      ProfNetwork esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the ProfNetwork object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new ProfNetwork (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String currentUser = "";
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: currentUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (currentUser != "") {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. Goto Friend List");
                System.out.println("2. Change Password");
                System.out.println("3. Write a new message");
                System.out.println("4. Send Friend Request");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: FriendList(esql, currentUser); break;
                   case 2: ChangePassword(esql, currentUser); break;
                   case 3: NewMessage(esql, currentUser); break;
                   case 4: SendRequest(esql, currentUser); break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try

      in.close();
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {

      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.next());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   public static void CreateUser(ProfNetwork esql) {

    System.out.print("\tEnter user login: ");
    String login = in.next();
    System.out.print("\tEnter user password: ");
    String password = in.next();
    System.out.print("\tEnter user email: ");
    String email = in.next();

      esql.CreateUser(login, password, email);
    
    }

   public static String LogIn(ProfNetwork esql) {
      System.out.print("\tEnter user login: ");
      String login = in.next();
      System.out.print("\tEnter user password: ");
      String password = in.next();

      boolean succeeded = esql.LogIn(login, password);

      if (succeeded) {
         System.out.println("Login succeeded!");
         return login;
      } else {
         System.out.println("Login failed.");
         return "";
      }
   }

   public static void FriendList(ProfNetwork esql, String username) {
      List<String> friends = esql.FriendList(username);
      if (friends.size() == 0) {
         System.out.println("\nYou haven't added any friends yet. Try sending them a request!\n");
      } else {
         System.out.println("\nFriends List:");

         for (String friend : friends) {
            System.out.println("\t - " + friend);
         }
         System.out.println();
      }
      
   }

   public static void ChangePassword(ProfNetwork esql, String username) {
      System.out.print("Please enter current password: ");
      String curr_password = in.next();
      while (!esql.LogIn(username, curr_password)) {
         System.out.print("Login Failed. Enter new Password or type 'cancel' to cancel: ");
         curr_password = in.next();
         if (curr_password.equals("cancel")) {
            return;
         }
      }

      System.out.print("Please enter new password: ");
      String passwordAttempt1 = in.next();
      System.out.print("Confirm password: ");
      String passwordAttempt2 = in.next();

      while (!passwordAttempt1.equals(passwordAttempt2)) {
         System.out.println("Passwords do not match. Please try again. To cancel, type 'cancel'.");
         System.out.print("Please enter new password: ");
         passwordAttempt1 = in.next();

         if (passwordAttempt1.equals("cancel")) {
            return;
         }

         System.out.print("Confirm password: ");
         passwordAttempt2 = in.next();

         if (passwordAttempt2.equals("cancel")) {
            return;
         }
      }
      esql.ChangePassword(username, passwordAttempt1);
      System.out.println("Password Updated");
   }
    public static void NewMessage(ProfNetwork esql, String username) {
        // TODO: IMPLEMENT ME
        System.out.println("SENDING FAKE MESSAGE");
    }

   public static void SendRequest(ProfNetwork esql, String username) {
      System.out.print("Please enter your friend's username: ");
      String recipient = in.next();

      // CONFIRM THAT USER IS VALID OTHER USER
      boolean validUser = true;

      if (recipient.equals(username) || !esql.userExists(recipient)) {
         validUser = false;
      }

      while (!validUser) {
         validUser = true;

         if (recipient.equals(username)) {
         System.out.println("You must enter a different user than yourself");
         System.out.print("Please try again or type 'cancel' to cancel: ");
         recipient = in.next();
         validUser = false;
         }

         if (!esql.userExists(recipient)) {
            System.out.println("The specified user doesn't exist.");
            System.out.print("Please try again or type 'cancel' to cancel: ");
            recipient = in.next();
            validUser = false;
         }
      }

      System.out.println("Valid User");

      // CHECK IF ALREADY A FRIEND
      if (esql.checkFriends(username, recipient)) {
         System.out.println(String.format("You are already friends with %s!\n",recipient));
         return;
      }

      // CHECK CONNECTIONS <= 3
      if (!esql.canConnect(username, recipient)) {
         System.out.println(String.format("You don't have enough connections with %s to add them!\n", recipient));
         return;
      }

      esql.SendRequest(username, recipient);

   }
}

