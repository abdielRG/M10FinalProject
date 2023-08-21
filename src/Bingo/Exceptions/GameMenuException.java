package Bingo.Exceptions;

public class GameMenuException extends Exception{
	public GameMenuException()
	{
		super("Invalid option entered. Please enter a valid option from the menu.");
	}
}
