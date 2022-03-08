import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


/**
This frame has a menu with commands to send an email
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
	private JComboBox options;
	private ActionListener listener;
	private JPanel panel;
	private JRadioButton accountCreation;
	private JRadioButton logIn;
	private JButton logInButton;
	private JSplitPane verticalSplit;
	private String[] userData;
	private JButton submitButton;
	private JPasswordField PasswordField;
	private JLabel Password;
	private JPasswordField ConfirmPasswordField;
	private JLabel ConfirmPassword;
	private JPanel tasks;
	private JMenuBar menuBar;
	private String selection;
	
	/**
	Constructs the main GUI layout
	*/
	public GUI()
	{
		userData = new String[3];
		panel = new JPanel();
		menuBar = new JMenuBar();
		listener = new ChoiceListener();
		panel.setLayout(new BorderLayout());
		tasks = createRadioButtons();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		createGUI();
	}
	
	public void createGUI()
	{
		
		// Construct menu
		panel.removeAll();

		getContentPane().add(panel);

		createTextField();
		createTextArea();
		createPanel();
		createControlPanel();
		
		System.out.println(selection);
		main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
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
		options = new JComboBox();
		
		options.addItem("Failure");
		options.addItem("Logout");
		options.addItem("Change Password");
		options.addItem("Search");
		options.addItem("Connection Request");
		options.addItem("View Friends");
		options.addItem("Messages");
		
		options.addActionListener(listener);
		options.setSelectedIndex(0);
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
		ChangePassword changePassword = new ChangePassword();
		Search search = new Search();
		Connection_Request connectionRequest = new Connection_Request();
		Friends friends = new Friends();
		Messages messages = new Messages();
		
		System.out.println(selection);
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
			changePassword.display();		
			//confirm current password
			//new password
			//confirm new password
		}
		if(selection.equals("Search"));
		{
			search.display();
			//send query into database to find a match
		}
		if(selection.equals("Connection Request"));
		{
			connectionRequest.display();
		}
		if(selection.equals("Friends"));
		{
			friends.display();
		}
		if(selection.equals("Messages"));
		{
			messages.display();
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
			//JPanel tasks = createRadioButtons();
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			menuBar.add(createFileMenu());
			
			listener = new ChoiceListener();
			// Construct menu
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			createControlPanel();
			
			inputPanel = new JPanel();
			inputPanel.setBorder(new EtchedBorder());
			inputPanel.setLayout(new FlowLayout());
			
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
			panel.add(main, BorderLayout.CENTER);
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
			
			listener = new ChoiceListener();
			// Construct menu
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			getContentPane().add(panel);
			
			createTextField();
			createTextArea();
			createPanel();
			createControlPanel();
			
			inputPanel = new JPanel();
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
	        	else
	        	{
		            JOptionPane.showMessageDialog(panel, "Password does not match. Please try again.");
	        	}
	         }
	     };
	     class LogInListener implements ActionListener
			{
		         public void actionPerformed(ActionEvent event) {
		        	 String textFieldValue;
		        	if(panel.equals(0))//if username and password match database entries, then x
		        	{
		        		//some code that pulls data from the database to populate the user page
		    			
		    			/*panel.removeAll();
		    			JMenuBar menuBar = new JMenuBar();
		    			setJMenuBar(menuBar);
		    			menuBar.add(createFileMenu());
		    			
		    			//listener = new ChoiceListener();
		    			// Construct menu
		    			panel = new JPanel();
		    			panel.setLayout(new BorderLayout());
		    			getContentPane().add(panel);
		    			
		    			createTextField();
		    			createControlPanel();
		    			
		    			inputPanel = new JPanel();
		    			inputPanel.setBorder(new EtchedBorder());
		    			inputPanel.setLayout(new FlowLayout());
		    			
		    			
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
		    			panel.updateUI();*/
		        	}
		        	else
		        	{
			            JOptionPane.showMessageDialog(panel, "Information entered does not match any known accounts. Please try again.");
		        	}
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
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(submitListener);
		
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
		ActionListener listener = new ButtonListener();
		accountCreation = new JRadioButton("Account Creation");
		accountCreation.addActionListener(listener);
		
		logIn = new JRadioButton("Log In");
		logIn.addActionListener(listener);
		
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
		JMenuItem exitItem = new JMenuItem("Exit");
		ActionListener listener = new ExitItemListener();
		exitItem.addActionListener(listener);
		menu.add(exitItem);
		return menu;
	}
}