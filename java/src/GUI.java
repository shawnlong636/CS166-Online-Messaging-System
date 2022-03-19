import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


/**
This java file creates the main body and content that FrameViewer.java uses. 
Additionally contains the logic and necessary code to link the GUI the database provided in ProfNetwork.java.
*/
public class GUI extends JFrame
{
	// Declaring Variables
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 500;
	
	private JSplitPane main;
	private JTextArea textArea;
	private JLabel UserID;
	private JTextField UserIDField;
	private JLabel First;
	private JTextField FirstField;
	private JLabel Last;
	private JTextField LastField;
	private JLabel Email;
	private JTextField EmailField;
	private JPanel inputPanel;
	private JPanel outputPanel;
	private JComboBox<String> options;
	private JComboBox<String> friends;
	private ActionListener listener;
	private ActionListener friend;
	private JPanel panel;
	private JRadioButton accountCreation;
	private JRadioButton logIn;
	private JButton logInButton;
	private JSplitPane verticalSplit;
	private String[] userData;
	private JButton submitButton;
	private JButton searchSubmitButton;
	private JLabel Password;
	private JPasswordField PasswordField;
	private JLabel ConfirmPassword;
	private JPasswordField ConfirmPasswordField;
	private JLabel CurrentPassword;
	private JPasswordField CurrentPasswordField;
	private JLabel Search;
	private JTextField SearchField;
	private JLabel Connection;
	private JLabel Friends;
	private JButton ConnectionSearchButton;
	private JTextField ConnectionField;
	private JPanel tasks;
	private JMenuBar menuBar;
	private String selection;
	private JButton FriendRequestButton;

	/**
	Initializes all necessary variables needed to create the GUI
	*/
	public GUI()
	{
		userData = new String[3];
		panel = new JPanel();
		main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		menuBar = new JMenuBar();
		panel.setLayout(new BorderLayout());
		options = new JComboBox<String>();
		tasks = createRadioButtons();
		inputPanel = new JPanel();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		createGUI();
		//userHomeGUI();
	}
	
	/**
	Constructs the main GUI layout
	*/	
	public void createGUI()
	{
		
		// Construct menu
		panel.removeAll();
		inputPanel.removeAll();
		
		getContentPane().add(panel);
		
		createTextField();
		createTextArea();
		createPanel();
		//createControlPanel();
		
		verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		verticalSplit.removeAll();
		panel.add(main, BorderLayout.CENTER);
		verticalSplit.setLeftComponent(tasks);
		verticalSplit.setRightComponent(inputPanel);
		main.setLeftComponent(verticalSplit);
		main.setRightComponent(outputPanel);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		panel.revalidate();
		panel.updateUI();
		panel.repaint();
		
	
	}

	/**
	Constructs the main user homepage that the user will see after logging in
	This method displays text relevant to the user's homepage and provides selections to access
	other parts of the messaging system.
	*/		
	public void userHomeGUI()
	{
		panel.removeAll();
		getContentPane().add(panel);
		listener = new ChoiceListener();
		createTextArea();
		createPanel();
		createControlPanel();
		textArea.setText("Welcome [name] to the User Homepage");
		panel.add(outputPanel);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
	}
	/**
	 * A listener that implements the ActionListener class and 
	 * overrides the actionPerformed method to set the options
	 * based on a user input.
	 */
	class ChoiceListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event) 
		{
			setOptions();	
		}
	}


	/**
	 * A listener that implements the ActionListener class and 
	 * overrides the actionPerformed method to set the options
	 * based on a user input.
	 */
	class FriendListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event) 
		{
			//friend selection action
		}
	}
	/**
	 * A listener that implements the ActionListener class and 
	 * overrides the actionPerformed method to exit the program
	 * based on a user input.
	 */
	class ExitItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.exit(0);
		}
	}
	
	/**
	 * A listener that implements the ActionListener class and 
	 * overrides the actionPerformed method to return to the landing page
	 * based on a user input.
	 */
	class HomeListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			int choice = JOptionPane.showConfirmDialog(null, "Returning to the home screen. This will log you out if you are logged in. Confirm?");
			if(choice == 0)
			{
				createGUI();
				return;
			}
		}
	}	
	/**
	 * A listener that implements the ActionListener class and 
	 * overrides the actionPerformed method to set the index
	 * based on a user input.
	 */
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			setIndex();
		}
	}

	/**
	 * A listener that implements the ActionListener class and 
	 * overrides the actionPerformed method to create a panel based on
	 * a user's action.
	 */
	class AccountListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			createPanel();
		}
	}
	/**
	 * A method that creates and sets a location for a control panel 
	 * containing a combo box for the GUI
	 */
	public void createControlPanel()
	{
		JPanel optionsPanel = createComboBox();
		JPanel controlPanel = new JPanel();
		
		controlPanel.setLayout(new GridLayout(1,1));
		controlPanel.add(optionsPanel);
		
		add(controlPanel, BorderLayout.EAST);
	}
	
	/**
	 * A method that creates a comboBox that adds choices for 
	 * different options for the GUI
	 * @return returns a comboBox containing user options
	 */
	private JPanel createComboBox()
	{

		options.removeAllItems();
		options.removeActionListener(listener);

		options.addItem("");
		options.addItem("Back");
		options.addItem("Logout");
		options.addItem("Change Password");
		options.addItem("Search");
		options.addItem("Connection Request");
		options.addItem("Friends");
		options.addItem("Messages");

		options.addActionListener(listener);

		JPanel panel = new JPanel();
		panel.add(options);
		return panel;
	}
	
	/**
	 * A method that sets the options based on user selection
	 */
	public void setOptions()
	{
		selection = (String)options.getSelectedItem();

		createControlPanel();
		if(selection.equals("Back"))
		{
			userHomeGUI();
		}
		if(selection.equals("Logout"))
		{
			int choice = JOptionPane.showConfirmDialog(null, "Would you like to logout?");
			if(choice == 0)
			{
				createGUI();
				return;
			}
		}
		if(selection.equals("Change Password"))
		{		
			//confirm current password
			//new password
			//confirm new password
			panel.removeAll();
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			menuBar.add(createFileMenu());
			
			// Construct menu
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			
			getContentPane().add(panel);

			inputPanel.removeAll();
			inputPanel.setLayout(new GridLayout(0,2));
			
			inputPanel.add(CurrentPassword);
			inputPanel.add(CurrentPasswordField);

			inputPanel.add(Password);
			inputPanel.add(PasswordField);

			inputPanel.add(ConfirmPassword);
			inputPanel.add(ConfirmPasswordField);

			
			inputPanel.add(submitButton);
			panel.add(inputPanel,BorderLayout.NORTH);
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
			
			panel.revalidate();
			panel.updateUI();
			panel.repaint();
		}
		if(selection.equals("Search"))
		{
			//send query into database to find a match
			//search person
			panel.removeAll();
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			menuBar.add(createFileMenu());
			
			// Construct menu
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			
			getContentPane().add(panel);
			
			inputPanel.removeAll();
			inputPanel.setLayout(new FlowLayout());
			
			inputPanel.add(Search);
			inputPanel.add(SearchField);
			
			inputPanel.add(searchSubmitButton);
			panel.add(inputPanel,BorderLayout.NORTH);
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
			
			panel.revalidate();
			panel.updateUI();
			panel.repaint();
		}
		if(selection.equals("Connection Request"))
		{
			panel.removeAll();
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			menuBar.add(createFileMenu());
			
			// Construct menu
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			
			getContentPane().add(panel);

			inputPanel.removeAll();
			inputPanel.setLayout(new FlowLayout());
			
			inputPanel.add(Connection);
			inputPanel.add(ConnectionField);
			
			textArea.setText("Show database connections and connection requests here");
			outputPanel.add(textArea);
			inputPanel.add(ConnectionSearchButton);
			panel.add(inputPanel,BorderLayout.NORTH);
			panel.add(outputPanel, BorderLayout.SOUTH);
			
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
			
			panel.revalidate();
			panel.updateUI();
			panel.repaint();
		}
		if(selection.equals("Friends"))
		{
			/*some more code here depending on the linking where we can call connection request or send message
			also need to be able to click on profile 
			from userUI menu -> friends -> friend profile -> friend list -> friend profile -> friend list ...
			connection request only have to be shown on profile*/
			panel.removeAll();
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			menuBar.add(createFileMenu());
			
			// Construct menu
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			
			getContentPane().add(panel);
			
			inputPanel.removeAll();
			inputPanel.setLayout(new FlowLayout());
			
			textArea.setText("Show friends here");
			outputPanel.add(textArea);
			inputPanel.add(FriendRequestButton);
			panel.add(inputPanel,BorderLayout.NORTH);
			panel.add(outputPanel, BorderLayout.SOUTH);
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
			
			panel.revalidate();
			panel.updateUI();
			panel.repaint();
		}
		if(selection.equals("Messages"))
		{
			panel.removeAll();
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			menuBar.add(createFileMenu());
			
			friends = new JComboBox<String>();
			friends.removeAllItems();
			friends.removeActionListener(listener);

			friends.addItem("");
			friends.addItem("Adam");
			friends.addItem("Bill");

			friends.addActionListener(friend);
			
			// Construct menu
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			//createControlPanel();
			
			getContentPane().add(panel);
			//listener = new ChoiceListener();
			
			//inputPanel = new JPanel();
			inputPanel.removeAll();
			inputPanel.setLayout(new FlowLayout());
			
			inputPanel.add(Friends);
			
			textArea.setText("messages here");
			outputPanel.add(textArea);
			inputPanel.add(friends);
			panel.add(inputPanel,BorderLayout.NORTH);
			panel.add(outputPanel, BorderLayout.SOUTH);
			
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
			
			panel.revalidate();
			panel.updateUI();
			panel.repaint();

		}
		outputPanel.repaint();
	}
	
	/**
	 * A method that sets the index for the radio buttons where 
	 * the user can select between Account Creation or Log In
	 * Also contains the logic for what each button does.
	 */
	public void setIndex()
	{
		if(accountCreation.isSelected())
		{
			userData = new String[3];
			panel.removeAll();

			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			menuBar.add(createFileMenu());
			
			// Construct menu
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			//createControlPanel();
			
			//inputPanel = new JPanel();
			inputPanel.removeAll();
			inputPanel.setBorder(new EtchedBorder());
			inputPanel.setLayout(new GridLayout(0,2));
			
			inputPanel.add(UserID);
			inputPanel.add(UserIDField);
			
			inputPanel.add(First);
			inputPanel.add(FirstField);
			
			inputPanel.add(Last);
			inputPanel.add(LastField);
			
			inputPanel.add(Email);
			inputPanel.add(EmailField);
			
			inputPanel.add(Password);
			inputPanel.add(PasswordField);
			
			inputPanel.add(ConfirmPassword);
			inputPanel.add(ConfirmPasswordField);
			
			inputPanel.add(submitButton);
			textArea.setText("Please enter your user information in the fields to the left.");
			textArea.setEditable(false);
			createTextArea();
			main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			panel.add(main,BorderLayout.NORTH);
			main.setLeftComponent(inputPanel);
			main.setRightComponent(outputPanel);
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
			panel.updateUI();
			panel.revalidate();
			panel.repaint();

		}
		else if(logIn.isSelected())
		{
			userData = new String[3];
			panel.removeAll();
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			menuBar.add(createFileMenu());
			
			// Construct menu
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			
			inputPanel.removeAll();
			inputPanel.setBorder(new EtchedBorder());
			inputPanel.setLayout(new FlowLayout());
			
			inputPanel.add(UserID);
			inputPanel.add(UserIDField);
			
			inputPanel.add(Password);
			inputPanel.add(PasswordField);
			
			inputPanel.add(logInButton);
			textArea.setText("Please enter your user information in the fields to the left.");
			textArea.setEditable(false);
			createTextArea();
			main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			panel.add(main, BorderLayout.CENTER);
			main.setLeftComponent(inputPanel);
			main.setRightComponent(outputPanel);
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
			
			panel.revalidate();
			panel.repaint();
			panel.updateUI();
		}
	}
	
	/**
	 * A method that creates a panel to hold the user information
	 */
	private void createPanel()
	{
		
		outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());

		outputPanel.add(textArea);
	}
	
	/**
	 * A method that creates a text field for user information
	 */
	private void createTextField()
	{
		String[] dataToSave = new String[4];
		
		class UserListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				String textFieldValue = UserIDField.getText();
				UserInfo existing = new UserInfo();
				
				for(int i = 0; i < userData.length; i++)
				{
					userData[i] = existing.readInfo(textFieldValue)[i];
				}
				
				if(userData[0] != "")
				{
					JPanel panel = new JPanel();
		            JOptionPane.showMessageDialog(panel, "Existing User Data has been found. \n Importing now...");
				}
				
				dataToSave[0] = textFieldValue;
				
				FirstField.setText(userData[0]);
				LastField.setText(userData[1]);
				EmailField.setText(userData[2]);
			}
		}
		
		class SubmitListener implements ActionListener
		{
	         public void actionPerformed(ActionEvent event) {
	        	 String textFieldValue;
	        	 //System.out.print(PasswordField.getText() + "|"+ ConfirmPasswordField.getText());
	        	if(PasswordField.getText().equals(ConfirmPasswordField.getText()))
	        	{
					textFieldValue = FirstField.getText();
					dataToSave[1] = textFieldValue;
					
					textFieldValue = LastField.getText();
					dataToSave[2] = textFieldValue;
					
					textFieldValue = EmailField.getText();
					dataToSave[3] = textFieldValue;
					UserInfo add = new UserInfo();
					add.saveInfo(dataToSave);
		            //JOptionPane.showMessageDialog(panel, "Submitted!");
	        	}
	        	/*else if(CurrentPasswordField.getText().equals(database password))
	        	{
	        		
	        	}*/
	        	else
	        	{
		            JOptionPane.showMessageDialog(panel, "Password does not match. Please try again.");
	        	}
	         }
	     };
	     
	     class SearchSubmitListener implements ActionListener
		{
		         public void actionPerformed(ActionEvent event) {
		        	/*if(search matches database)
		        	{

		        	}
		        	else
		        	{
			            JOptionPane.showMessageDialog(panel, "User could not be found.");
		        	}*/
		        	JOptionPane.showMessageDialog(panel, "User could not be found.");
		         }
		};
	     class ConnectionSearchListener implements ActionListener
		{
		         public void actionPerformed(ActionEvent event) {
		        	/*if(new user then 5 new connections without rule)
		        	{

		        	}
		        	else
		        	{
			           //apply lvl 3 rule here
		        	}*/
		        	JOptionPane.showMessageDialog(panel, "Button Test.");
		         }
		};
		          
	     class LogInListener implements ActionListener
			{
		         public void actionPerformed(ActionEvent event) {
		        	 String textFieldValue;
		        	if(panel.equals(0))//if username and password match database entries, then x
		        	{
		        		//some code that pulls data from the database to populate the user page
		    			userHomeGUI();
		    			//but also personalized with user info
		        	}
		        	else
		        	{
			            JOptionPane.showMessageDialog(panel, "Information entered does not match any known accounts. Please try again.");
		        	}
		         }
		     };
		     
	 	/**
	 	 * A listener that implements the ActionListener class and 
	 	 * overrides the actionPerformed method to give the user confirmation
	 	 * that a friend request was sent.
	 	 */
	     class FriendRequestListener implements ActionListener
			{
		         public void actionPerformed(ActionEvent event) {
		        	 //some code for friend request if not already friend otherwise dialog "already friends"
			            JOptionPane.showMessageDialog(panel, "Friend Request sent!");
		        	}
		    };
		
		/**
		 * A listener that implements the ActionListener class and 
		 * stores the data found in the First Name text field
		 */
		class FirstListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				//String textFieldValue = FirstField.getText();
				//dataToSave[1] = textFieldValue;
			}
		}
		
		/**
		 * A listener that implements the ActionListener class and 
		 * stores the data found in the Last Name text field
		 */
		class LastListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				//String textFieldValue = LastField.getText();
				//dataToSave[2] = textFieldValue;
			}
		}
		
		/**
		 * A listener that implements the ActionListener class and 
		 * stores the data found in the Email text field
		 */
		class EmailListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				//String textFieldValue = EmailField.getText();
				//dataToSave[3] = textFieldValue;
				//UserInfo add = new UserInfo();
				//add.saveInfo(dataToSave);
			}
		}
		final int WIDTH = 20;
		//Setting listener variables
		ActionListener userListener = new UserListener();
		ActionListener firstListener = new FirstListener();
		ActionListener lastListener = new LastListener();
		ActionListener emailListener = new EmailListener();
		ActionListener submitListener = new SubmitListener();
		ActionListener searchSubmitListener = new SearchSubmitListener();
		ActionListener connectionSearchListener = new ConnectionSearchListener();
		ActionListener friendRequestListener = new FriendRequestListener();
		ActionListener logInListener = new LogInListener();
		
		UserID = new JLabel("User ID: ");
		UserIDField = new JTextField(WIDTH);
		//UserIDField.addActionListener(userListener);
		
		First = new JLabel("First Name: ");
		FirstField = new JTextField(WIDTH);
		//FirstField.addActionListener(firstListener);
		
		Last = new JLabel("Last Name: ");
		LastField = new JTextField(WIDTH);
		//LastField.addActionListener(lastListener);
		
		Email = new JLabel("Email Address: ");
		EmailField = new JTextField(WIDTH);
		//EmailField.addActionListener(emailListener);
		
		Password = new JLabel("Password: ");
		PasswordField = new JPasswordField(WIDTH);
		
		ConfirmPassword = new JLabel("Confirm Password: ");
		ConfirmPasswordField = new JPasswordField(WIDTH);
		
		CurrentPassword = new JLabel("Current Password: ");
		CurrentPasswordField = new JPasswordField(WIDTH);
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(submitListener);
		
		Search = new JLabel("Search: ");
		SearchField = new JTextField(WIDTH);
		
		searchSubmitButton = new JButton("Submit");
		searchSubmitButton.addActionListener(searchSubmitListener);
		
		Connection = new JLabel("Connection Search");
		ConnectionField = new JTextField(WIDTH);
		
		ConnectionSearchButton = new JButton("Submit");
		ConnectionSearchButton.addActionListener(connectionSearchListener);
		
		Friends = new JLabel("Friends");
		FriendRequestButton = new JButton("Friend Request");
		FriendRequestButton.addActionListener(friendRequestListener);
		
		logInButton = new JButton("Log In");
		logInButton.addActionListener(logInListener);
		
	}
	
	/**
	 * A method that creates the Account Creation and Log In
	 * radio buttons.
	 * @return returns the panel holding the radio buttons
	 */
	public JPanel createRadioButtons()
	{
		ActionListener buttonListener = new ButtonListener();
		accountCreation = new JRadioButton("Account Creation");
		accountCreation.addActionListener(buttonListener);
		
		logIn = new JRadioButton("Log In");
		logIn.addActionListener(buttonListener);
		
		ButtonGroup group = new ButtonGroup();
		group.add(accountCreation);
		group.add(logIn);
		
		JPanel panel = new JPanel();
		panel.add(accountCreation);
		panel.add(logIn);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Index"));
		
		return panel;
	}
	
	/**
	 * A method that creates a large text area to contain the program's output.
	 */
	private void createTextArea()
	{
		final int ROWS = 25;
		final int COLUMNS = 30;
		textArea = new JTextArea(ROWS, COLUMNS);
		textArea.setText("Welcome to the CS166 Online Messaging System. \n FG204 2nd Edition Ver. 2.31 \n\n "
				+ "To login to an existing account, please enter your username and password in the appropriate fields. \n"
				+ "To create a new account, please select the 'Account Creation' button");
		textArea.setEditable(false);
	}
	
	/**
	 * A method that sets the text for the main Text Area output.
	 * @param text the text to be used.
	 */
	private void setTextArea(String text)
	{
		textArea.setText(text);
	}
	
	/**
	Creates the File menu.
	@return the menu
	*/
	public JMenu createFileMenu()
	{
		JMenu menu = new JMenu("File");
		JMenuItem back = new JMenuItem("Home");
		ActionListener homeListener = new HomeListener();
		back.addActionListener(homeListener);
		menu.add(back);
		JMenuItem exitItem = new JMenuItem("Exit");
		ActionListener exitItemListener = new ExitItemListener();
		exitItem.addActionListener(exitItemListener);
		menu.add(exitItem);
		return menu;
	}
}