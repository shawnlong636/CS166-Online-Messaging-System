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
                System.out.println("2. View Profile");
                System.out.println("3. View Messages");
                System.out.println("4. Send Friend Request");
                System.out.println("5. View Requests");
                System.out.println("6. Explore Friends' Profiles");
                System.out.println("7. Search for a Friend");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: FriendList(esql, currentUser); break;
                   case 2: currentUser = ViewProfile(esql, currentUser); break;
                   case 3: viewMessages(esql, currentUser); break;
                   case 4: SendRequest(esql, currentUser); break;
                   case 5: viewRequests(esql, currentUser); break;
                   case 6: friendsProfiles(esql, currentUser); break;
                   case 7: searchProfiles(esql, currentUser); break;
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
         if (recipient.equals("cancel")) {
            return;
         }

         if (!esql.userExists(recipient)) {
            System.out.println("The specified user doesn't exist.");
            System.out.print("Please try again or type 'cancel' to cancel: ");
            recipient = in.next();
            validUser = false;
         }
         if (recipient.equals("cancel")) {
            return;
         }
      }

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
   public static void viewRequests(ProfNetwork esql, String username) {
      List<String> requestedMe = esql.getRequested(username);
      List<String> imRequesting = esql.getRequesting(username);

      // PRINT LISTS OF PPL REQUESTIN CUR USER / PPL WHO REQUESTED CUR USER
      if (requestedMe.size() == 0 && imRequesting.size() == 0) {
         System.out.println("You have no requests at this time.\n");
      } else if (requestedMe.size() == 0) {
         // Has Requesting only
         System.out.println("You sent the following pending requests: ");
         for (String user : imRequesting) {
            System.out.println("\t - " + user);
         }
      } else if (imRequesting.size() == 0) {
         // Has Requested only
         System.out.println("You have been sent the following requests: ");
         for (String user : requestedMe) {
            System.out.println("\t - " + user);
         }
      } else {
         // Has Requsts and Requesting
         System.out.println("You have been sent the following pending requests: ");
         for (String user : requestedMe) {
            System.out.println("\t - " + user);
         }
         System.out.println("You sent the following requests: ");
         for (String user : imRequesting) {
            System.out.println("\t - " + user);
         }
      }
      System.out.println();

      // OPTIONS FOR ACCEPTING / DENYING PEOPLE WHO REQUESTED CUR USER
      if (requestedMe.size() > 0) {
         System.out.print("Would you like to Accept/Deny any requests? (y/n): ");
         String response = in.next();
         
         while (!response.equals("y") && !response.equals("n")) {
            System.out.print("You entered an invalid response. Try again: ");
            response = in.next();
         }

         boolean ContinueRequests = (response.equals("y"));

         String name;
         String decision;

         while (ContinueRequests) {
            System.out.print("Enter a name: ");
            name = in.next();

            while (!esql.userExists(name)) {
               System.out.println("The specified user does not exist. Please try again.");
               System.out.print("Enter a name or 'cancel': ");
               name = in.next();
               if (name.equals("cancel")) {
                  return;
               }
            }

            System.out.print("Accept or Deny? (a/d): ");
            decision = in.next();
            
            if (decision.equals("a")) {
               esql.acceptRequest(username, name);
            } else if (decision.equals("d")) {
               esql.denyRequest(username, name);
            } else if (decision.equals("cancel")) {
               return;
            } else {
               System.out.println("Invalid Input. Please try again or type 'cancel'.");
            }

            requestedMe = esql.getRequested(username);
            if (requestedMe.size() > 0) {
               System.out.println("Continue? (y/n): ");
               response = in.next();

               while (!response.equals("y") && !response.equals("n")) {
                  System.out.print("You entered an invalid response. Try again: ");
                  response = in.next();
               }
               ContinueRequests = (response.equals("y"));
            } else {
               ContinueRequests = false;
            }
         }
      }
      // OPTIONS FOR WITHDRAWING REQUESTS SENT BY CUR USER
      if (imRequesting.size() > 0) {
         System.out.print("Would you like to Withdraw any requests? (y/n): ");
         String response = in.next();
         
         while (!response.equals("y") && !response.equals("n")) {
            System.out.print("You entered an invalid response. Try again: ");
            response = in.next();
         }

         boolean ContinueRequests = (response.equals("y"));

         String name;
         String decision;

         while (ContinueRequests) {
            System.out.print("Enter a name: ");
            name = in.next();

            while (!esql.userExists(name)) {
               System.out.println("The specified user does not exist. Please try again.");
               System.out.print("Enter a name or 'cancel': ");
               name = in.next();
               if (name.equals("cancel")) {
                  return;
               }
            }
            esql.withdrawRequest(username, name);

            imRequesting = esql.getRequesting(username);

            if (imRequesting.size() > 0) {
               System.out.println("Continue? (y/n): ");
               response = in.next();

               while (!response.equals("y") && !response.equals("n")) {
                  System.out.print("You entered an invalid response. Try again: ");
                  response = in.next();
               }
               ContinueRequests = (response.equals("y"));
            } else {
               ContinueRequests = false;
            }
         }
      }
      System.out.println();
   }

   public static String ViewProfile(ProfNetwork esql, String username) {
      // VIEW PROFILE
      System.out.println("PROFILE: " + username);
      System.out.println("==============================");
      List<String> attributes = esql.getProfile(username);

      System.out.println("Password: **********");
      System.out.println("Email: " + attributes.get(2));
      System.out.println("Name: " + attributes.get(3));
      System.out.println("Birth Date: " + attributes.get(4));
      System.out.println();

      // UPDATE PROFILE
      System.out.print("Update profile? (y/n): ");
      boolean continueUpdating = true;
      String response = in.next();

      while (!response.equals("y") && !response.equals("n")) {
            System.out.print("You entered an invalid response. Try again: ");
            response = in.next();
         }
      
      continueUpdating = (response.equals("y"));
      int option;
      String value = "";
      while (continueUpdating) {
         System.out.println("1. Password");
         System.out.println("2. Email");
         System.out.println("3. Name");
         System.out.println("4. Birth Date");
         System.out.println("5. User Name");
         System.out.println("9. Cancel");
         System.out.print("Select an option: ");
         option = readChoice();
         if (option != 9 && option != 1) {
            System.out.print("Enter new value (without spaces): ");
            value = in.next();
         }

         switch (option){
            case 1: ChangePassword(esql, username); break;
            case 2: esql.updateProfile(username, "email", value); break;
            case 3: esql.updateProfile(username, "name", value); break;
            case 4: esql.updateProfile(username, "dateOfBirth", value); break;
            case 5: esql.updateProfile(username, "userId", value); break;
            case 9: return username;
            default : System.out.println("Unrecognized choice!"); break;
         }
         if (option == 5) {
            username = value;
         }

         System.out.print("Continue updating? (y/n): ");
         response = in.next();

         while (!response.equals("y") && !response.equals("n")) {
                  System.out.print("You entered an invalid response. Try again: ");
                  response = in.next();
         }
         continueUpdating = response.equals("y");

      }
      return username;
   }

   public static void friendsProfiles(ProfNetwork esql, String username) {
      FriendList(esql, username);
      System.out.print("Enter your friend's name to view their profile: ");
      String friend = in.next();

      while (!esql.userExists(friend) || !esql.checkFriends(username, friend)) {
         if (!esql.userExists(friend)) {
            System.out.println("The specified user doesn't exist.");
            System.out.print("Please try again or type 'cancel' to cancel: ");
            friend = in.next();
            if (friend.equals("cancel")) {
               return;
            }
         } else {
            System.out.println(String.format("You haven't added %s as a friend yet.", friend));
            System.out.print("Please try again or type 'cancel' to cancel: ");
            friend = in.next();
            if (friend.equals("cancel")) {
               return;
            }
         }  
      }

      System.out.println("\nPROFILE: " + friend);
      System.out.println("==============================");
      List<String> attributes = esql.getProfile(friend);

      System.out.println("Email: " + attributes.get(2));
      System.out.println("Name: " + attributes.get(3));
      System.out.println("Birth Date: " + attributes.get(4));
      System.out.println();
   }
   public static void searchProfiles(ProfNetwork esql, String username) {
      System.out.print("Enter a username: ");
      String response = in.next();
      List<String> users = esql.searchPeople(response);
      
      if (users.size() == 0) {
         System.out.println("No results Found\n");
      } else {
         System.out.println("Top 10 Results: ");
         for (String user: users) {
            System.out.println("\t" + user);
         }
         System.out.println();
      }
      
   }
   public static void viewMessages(ProfNetwork esql, String username) {
      System.out.println("Messages: ");
      System.out.println("==============================");

      int numMessages = esql.getUnreadCount(username);
      String response = "";

      if (numMessages > 0) {
         System.out.println("You have " + numMessages + " unread messages!");
         System.out.print("Would you like to view them? (y/n): ");
         response = in.next();
         
         while (!response.equals("y") && !response.equals("n")) {
            System.out.print("You entered an invalid response. Try again: ");
            response = in.next();
         }

         if (response.equals("y")) {
            List<String> unreadMessages = esql.getUnreadMessages(username);
            String senderId;
            String contents;
            String sendTime;
            System.out.println();
            for (int i = 0; i < unreadMessages.size() / 3; i += 1) {
               senderId = unreadMessages.get(3*i);
               contents = unreadMessages.get(3 * i + 1);
               sendTime = unreadMessages.get(3 * i + 2);
               System.out.println(String.format("(%s) %s : %s", sendTime, senderId, contents));
            }
         }
      }

     

      System.out.println();
      boolean messageLoop = true;

      while (messageLoop) {

         System.out.println("Please select a friend to message");
         System.out.println("If you would like to see your friend list first, enter '/friends'");
         System.out.print("Name: ");

         String friendName = in.next();

         if (friendName.equals("/friends")) {
            FriendList(esql, username);
            
            System.out.print("\nPlease enter a user: ");
            friendName = in.next();
         }

    

         boolean validUser = true;

         if (!esql.userExists(username) || !esql.checkFriends(username, friendName)) {
            validUser = false;
         }

         while (!validUser) {
            validUser = true;

            if (!esql.userExists(friendName)) {
               System.out.println("The specified user doesn't exist.");
               System.out.print("Please try again or type 'cancel' to cancel: ");
               friendName = in.next();
               validUser = false;
            }

            if (friendName.equals("cancel")) {
               return;
            }

            if (!esql.checkFriends(username, friendName)) {
            System.out.println("You are not yet friends with " + friendName + ".");
            System.out.print("Please try again or type 'cancel' to cancel: ");
            friendName = in.next();
            validUser = false;
            }
            if (friendName.equals("cancel")) {
               return;
            }
         }

         // Display Thread
         messageLoop = displayThread(esql, username, friendName);

         System.out.println();
      }
   }

   public static boolean displayThread(ProfNetwork esql, String username, String friendName) {

      int MessageLimit = 5;
      
      // Temp variables
      String senderId;
      String contents;
      String sendTime;
      String status;

      while (true) {
         List<String> thread = esql.getThread(username, friendName, MessageLimit);
         boolean printedUnreadMessage = false;
         System.out.println();
         if (thread.size() == 0) {
            System.out.println("This thread is empty. Why don't you say hello?");
         } else {
            System.out.println("==============================");
            for (int i = 0; i < thread.size() / 4; i += 1) {
               senderId = thread.get(4*i);
               contents = thread.get(4 * i + 1);
               sendTime = thread.get(4 * i + 2);
               status = thread.get(4 * i + 3);

               if (!printedUnreadMessage && !status.equals("Read") && senderId == friendName) {
                  System.out.println("===== NEW MESSAGES BELOW =====");
                  printedUnreadMessage = true;
               }

               System.out.println(String.format("(%s) %s : %s", sendTime, senderId, contents));
            }
            System.out.println("==============================");
         }
         System.out.println("\nOPTIONS: | 1. New Message | 2. Delete Last Message |");
         System.out.println("         | 3. Refresh     | 4. Return    | 5. Exit |");
         System.out.println();
         int option = readChoice();

         switch (option) {
               case 1: receiveMessage(esql, username, friendName); break;
               case 2: System.out.println("DELETING DUMMY MSG"); break;
               case 3: break;
               case 4: return true; // continue viewing messages
               case 5: return false; // leave messages altogether
               default : System.out.println("Unrecognized choice!"); break;
         }
      }
   }
   public static void receiveMessage(ProfNetwork esql, String sender, String receiver) {
      System.out.println("Please Type your Message below.");
      
      String message = in.nextLine();
      message = in.nextLine();
      message = message.replace("'", "''");
      
      if (esql.sendMessage(sender, receiver, message)) {
         System.out.println("Message Sent!");
      } else {
         System.out.println("Unable to send message!");
      }
   }
}