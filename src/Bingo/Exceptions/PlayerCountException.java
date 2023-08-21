package Bingo.Exceptions;

public class PlayerCountException extends Exception{
	public PlayerCountException()
	{
		super("You must have at least 1 player to play the game. Please try again.");
	}
}
