package Bingo;

import java.util.Random;

/**
 * A class representing a 5x5 Bingo card, a stamper, and a win-checking tool.
 * @author Abdiel Ramirez
 * @version 1.0
 */
public class BingoCard {
    /**
     * The 5x5 Bingo card.
     */
    int[][] field = new int[5][5];
    int row;
    int col;

    /**
     * Default constructor fills the first column with pseudorandom numbers between 1 and 15, the second
     * column between 16 and 30, the third column between 31 and 45, the fourth column between 46 and 60, and the
     * fifth column between 61 and 75. The numbers for each column are determined by the private fillFiveRandom()
     * method. The center square is replaced with 0, which represents the "free space".
     */
    public BingoCard()
    {
        int range = 1;
        int[] arrayOfFive = {0, 0, 0, 0, 0};

        for(col = 0; col < 5; col++, range += 15)
        {
           fillFiveRandom(arrayOfFive);

            for(row = 0; row < 5; row++)
            {
                field[row][col] = arrayOfFive[row] + range;
            }
        }

        //Create the FREE space
        field[2][2] = 0;
    }

    /**
     * A private helper method used by the constructor.
     * @param array A reference to the 5 element array used by the class's constructor to fill a
     *              Bingo card's columns.
     */
    private void fillFiveRandom(int[] array)
    {
        Random randGen = new Random();
        int numToAssign = 0;
        boolean duplicateFound = true;

        for(int x = 0; x < 5; x++)
        {
            while(duplicateFound)
            {
                duplicateFound = false;
                numToAssign = randGen.nextInt(15);
                //System.out.print("Random number gen output: " + numToAssign);
                for (int y = 0; y < x + 1; y++)
                {
                    if(array[y] == numToAssign)
                        duplicateFound = true;
                }
            }
            array[x] = numToAssign;
            duplicateFound = true;
        }

        /*
        System.out.print("fillFiveRandom() called and output: ");
        for(int z = 0; z < 5; z++)
        {
            System.out.print(array[z] + ", ");
        }
        System.out.println();
        */
    }

    /**
     * Replaces the element that matches the passed argument with a 0, if one exists.<br>
     * The 0 represents a number that has been stamped on a Bingo card.
     * @param calledNum The number that needs to replaced with a 0.
     */
    public void stamp(int calledNum)
    {
        for(col = 0; col < 5; col++)
        {
            for(row = 0; row < 5; row++)
            {
                if(field[row][col] == calledNum)
                    field[row][col] = 0;
            }
        }
    }

    /**
     * Checks the Bingo card for a possible 5-in-a-row win condition.
     * @return True if a win condition is found, or false.
     */
    public boolean checkForStandardWin()
    {
        boolean winnerFound = false;
        int row;
        int col;
        int zerosInRow;

        //Check for straight lines
        for(row = 0; row < 5 && !winnerFound; row++)
        {
            zerosInRow = 0;
            for(col = 0; col < 5 && !winnerFound; col++)
            {
                //Count how many zeros are in each row
                if(field[row][col] == 0)
                    zerosInRow++;
            }
            //A winner is found if five zeros are found in a row
            if(zerosInRow == 5)
                winnerFound = true;
        }

        //Check for top-to-bottom diagonal
        for(row = 0, col = 0, zerosInRow = 0; row < 5 && !winnerFound; row++, col++)
        {
            if(field[row][col] == 0)
                zerosInRow++;

            if(zerosInRow == 5)
                winnerFound = true;
        }

        //Check for bottom-to-top diagonal
        for(row = 4, col = 0, zerosInRow = 0; col < 5 && !winnerFound; row--, col++)
        {
            if(field[row][col] == 0)
                zerosInRow++;

            if(zerosInRow == 5)
                winnerFound = true;
        }

        return winnerFound;
    }

    /**
     * Checks the Bingo card for a possible pattern-type win condition.
     * @param winCondition A 5x5 array that is to be used for comparison.
     * @return True if a win condition is found, or false.
     */
    public boolean checkForWinPattern(int[][] winCondition)
    {
        boolean winnerFound = true;

        for(col = 0; col < 5 && winnerFound; col ++)
        {
            for(row = 0; row < 5 && winnerFound; row++)
            {
                if(winCondition[row][col] == 0 && field[row][col] != 0)
                    winnerFound = false;
            }
        }

        return winnerFound;
    }

    /**
     * Prints the elements of the Bingo card formatted to look like a real card.
     */
    public void printCard()
    {
        System.out.println("*********************");
        System.out.println("* B * I * N * G * O *");
        System.out.println("*********************");
        for(row = 0; row < 5; row++)
        {
            System.out.print("|");
            for(col = 0; col < 5; col++)
            {
                System.out.printf(" %-2d|", field[row][col]);
            }
            System.out.println();
            System.out.println("*********************");
        }
    }
}
