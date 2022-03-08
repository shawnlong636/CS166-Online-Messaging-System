import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserInfo 
{
	String[] data;
	
	UserInfo()
	{
		data = new String[3];
	}
	
	/**
	 * A method that reads the userID field and checks if there exists 
	 * a user with that userID. If there is, then it imports the user data
	 * @param userIDField the UserId typed into the userIDField text field
	 * @return returns an array that contains the user's info
	 */
	public String[] readInfo(String userIDField)
	{
		File file = new File("Database.txt");
		for(int i = 0; i < data.length; i++)
		{
			data[i] = "";
		}
		try 
		{
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine())
			{	
				if(sc.next().contentEquals(userIDField))
				{
					data[0] = (sc.next()); //First Name
					data[1] = (sc.next()); //Last Name
					data[2] = (sc.next()); //Email
					sc.close();
					break;
				}
				else
				{
					sc.nextLine();
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

		return data;
	}
	
	/**
	 * A method that saves the user's info to the file.
	 * @param dataToSave an array that contains the data to be saved
	 */
	public void saveInfo(String[] dataToSave)
	{
		JPanel panel = new JPanel();
		int counter = 0;
		String append = "";
		for(int i = 0; i < dataToSave.length; i++)
		{
			append += dataToSave[i] + " ";
			if(dataToSave[i] == "")
			{
				counter++;
			}
		}
		if(counter == 0)
		{
	        try 
	        { 
	            BufferedWriter out = new BufferedWriter( 
	            new FileWriter("Database.txt", true)); 
	            out.write(append + "\n"); 
	            JOptionPane.showMessageDialog(panel, "New Info has been added to the database.");
	            out.close(); 
	        } 
	        catch (IOException e) 
	        { 
	            System.out.println("File Not Found"); 
	        } 
		}
		else
		{
            JOptionPane.showMessageDialog(panel, "Please complete the form before submitting.");
		}
	}
}
