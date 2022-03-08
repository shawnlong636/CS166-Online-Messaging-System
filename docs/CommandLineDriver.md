# CommandLineDriver

This class serves as the main driver for the command line interface for this app.



#### Protected Properties

| Type    | Name | Description                                                  |
| ------- | ---- | ------------------------------------------------------------ |
| Scanner | in   | Static Scanner object which is used in helper methods for the class to receive input from the user. |



#### Public Methods

| Return Type | Name             | Parameters                                    | Description                                                  |
| ----------- | ---------------- | --------------------------------------------- | ------------------------------------------------------------ |
| void        | main()           | args:**String[]**                             | This is the main static execution method which takes the following command line arguments: `<dbname> <port> <user>`. A ProfNetwork object *esql* is created in main which is the primary interface connecting to the database. This object is passed to other methods to perform certain tasks. |
| void        | Greeting()       |                                               | This is a static method which prints out the Header for the App |
| int         | readChoice()     |                                               | This is a static method which uses the input scanner to receive input for navigating in the main switch statement. |
| void        | CreateUser()     | esql:**ProfNetwork**                          | This is a static method which takes in the ProfNetwork object from main, gets user details from the client, and then uses the ProfNetwork object to update the database with the new user record. |
| String      | LogIn()          | esql:**ProfNetwork**                          | This is a static method which takes in the ProfNetwork object from main, and prompts the user to enter a user and pass to authenticate and access their account. Returns the username if succeeded, otherwise it returns an empty string. |
| void        | FriendList()     | esql:**ProfNetwork**<br />username:**String** | This is a static method which takes in the ProfNetwork object from main as well as the username of the current user. This method prints the list of friends for the given user, using the ProfNetwork object. |
| void        | ChangePassword() | esql:**ProfNetwork**<br />username:**String** | This is a static method which takes in the ProfNetwork object from main as well as the username of the current user. This method prompts user for their current password, and asks twice for a new password before updating the database using the ProfNetwork object. |
| void        | NewMessage()     | esql:**ProfNetwork**<br />username:**String** | This is a static method which takes in the ProfNetwork object from main as well as the username of the current user. This method which prompts for a user and a message and then sends it by adding it to the database. |
| void        | SendRequest()    | esql:**ProfNetwork**<br />username:**String** | This is a static method which takes in the ProfNetwork object from main as well as the username of the current user. This method prompts for a user to send a friend request to and then sends it if the person exists. |

