public class CommandLineDriver {
    
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
            boolean authorisedUser = false;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser) {
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
                   case 1: FriendList(esql); break;
                   case 2: ChangePassword(esql); break;
                   case 3: NewMessage(esql); break;
                   case 4: SendRequest(esql); break;
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
            input = Integer.parseInt(in.readLine());
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
    String login = in.readLine();
    System.out.print("\tEnter user password: ");
    String password = in.readLine();
    System.out.print("\tEnter user email: ");
    String email = in.readLine();

    return esql.createUser(login, password, email);
    
    }

    public static boolean LogIn(ProfNetwork esql) {
        System.out.print("\tEnter user login: ");
        String login = in.readLine();
        System.out.print("\tEnter user password: ");
        String password = in.readLine();

      //   return this.esql.LogIn(login, password)
      System.out.println("DUMMY LOGIN SUCCEEDED");
      return true;
    }

    public static void FriendList(ProfNetwork esql) {
        // TODO: IMPLEMENT ME
        for (int i = 0; i < 3; i++) {
           System.out.printf("Fake Friend %d%n", i);
        }
    }

    public static void ChangePassword(ProfNetwork esql) {
        // TODO: IMPLEMENT ME
        System.out.println("DUMMY PASSWORD CHANGED");
    }

    public static void NewMessage(ProfNetwork esql) {
        // TODO: IMPLEMENT ME
        System.out.println("SENDING FAKE MESSAGE");
    }

    public static void SendRequest(ProfNetwork esql) {
        // TODO: IMPLEMENT ME
        System.out.println("SENDING FAKE FRIEND REQUEST");
    }
}

