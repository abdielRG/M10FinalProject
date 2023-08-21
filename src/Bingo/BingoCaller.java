package Bingo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents the ball tumbler traditionally used in Bingo to randomly pull a number from 1 through 75.
 * @author Abdiel Ramirez
 * @version 1.0
 */
public class BingoCaller {
	/**
	 * The ball tumbler.
	 */
	ArrayList<Integer> tumbler = new ArrayList<Integer>();
	Random randGen = new Random();
	int i;

	/**
	 * Default constructor filling the ArrayList tumbler with Integer objects containing the numbers
	 * 1 through 75.
	 */
	public BingoCaller()
	{
		for(i = 1; i <= 75; i++)
			tumbler.add(i);
	}

	/**
	 * Removes and returns a random number from the tumbler.
	 * @return An Integer object representing the number pulled from the tumbler.
	 */
	public Integer getNext()
	{
		return tumbler.remove(randGen.nextInt(tumbler.size()));
	}
}
