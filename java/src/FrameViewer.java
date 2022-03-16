/*
 * Final Project
 * 2/11/20
 * 1. The program takes in a variety of user inputs such as text entries
 *    and button presses depending on which part of the program the user is 
 *    interacting with.
 * 2. The program has a range out outputs from display text on a screen to
 *    sending emails and returning answers to text inputs.
 * 3. The purpose of this program is to create a question assistant that will
 *    return different kind of answers based on what question the user inputs.
 *    The program also has an appointment function that allows the user to create 
 *    an appointment with someone selected from a list of options.
 */
import java.io.FileNotFoundException;

import javax.swing.JFrame;
/**
 * James Liem and Shawn Long 
 * CS166 Final Project
 * 3/13/2022
 * This program uses a menu to display a Frame with a GUI system contained within.
 * The program is an Online Messaging System that has basic features such as messaging,
 * sending/receiving friend requests, searching users, and changing an account password.
*/
public class FrameViewer
{
	public static void main(String[] args) throws FileNotFoundException
	{
		GUI frame = new GUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("CS166 Online Messaging System");
		frame.setVisible(true);
	}
}