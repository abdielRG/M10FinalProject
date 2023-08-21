package Bingo.Exceptions;

public class GameModeException extends Exception{
	public GameModeException()
	{
		super("Invalid selection. Please enter a number for a valid game mode.");
	}
}
