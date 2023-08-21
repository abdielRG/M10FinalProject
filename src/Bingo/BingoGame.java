package Bingo;

import Bingo.Exceptions.GameMenuException;
import Bingo.Exceptions.GameModeException;
import Bingo.Exceptions.PlayerCountException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
    Abdiel Ramirez
    CS 143
    Final Project Beta Test
    20 AUG 23

    This program allows the user to play a game of Bingo for an unlimited number of players.
    The program uses a main Class with 4 helper methods, 2 programmer-defined Classes,
    3 API Classes, and 3 user-defined Exceptions.

    Documentation has been provided in standard Javadoc format.

    TEST DATA:
    For the final round of testing, I ran the program 13 times, entering different values for
    the user-input variables gameMode, numPlayers and inGameMenuResult.

    Input
    (gameMode, numPlayers, inGameMenuResult)        Result
    ---------                                       ------------
    All game modes w/ autorun:
    1, 10, 2                                        30 rounds, 1 winner, PASS
    2, 20, 2                                        60 rounds, 1 winner, PASS
    3, 30, 2                                        62 rounds, 1 winner, PASS
    4, 40, 2                                        28 rounds, 1 winner, PASS
    1, 50, 2                                        15 rounds, 1 winner, PASS

    Manual run output:
    1, 5, 1                                         5 cards printed, then called number and updated cards printed, PASS

    Ending the game early:
    1, 5, 3                                         Game ended, 0 rounds, PASS
    1, 5, 1 then 3                                  Game ended, 1 round, PASS

    Game mode errors:
    5, none, none                                   Error message, PASS
    0, none, none                                   Error message, PASS

    Player number error:
    1, 0, none                                      Error message, PASS

    In game menu errors:
    1, 5, 0                                         Error message, PASS
    1, 5, 4                                         Error message, PASS
 */

/**
 * Program runs a game of Bingo for any number of players, using a user-determined ruleset
 * when determining the win condition.
 * @author Abdiel Ramirez
 * @version 2023.08.19
 */
public class BingoGame {
    /**
     * Runs a game of Bingo by:
     * <ol>
     * <li>Getting the number of players</li>
     * <li>Getting the desired game mode</li>
     * <li>If a pattern-type game is selected, creating an array that represents
     * the winning pattern</li>
     * <li>Preparing and printing Bingo cards using BingoCard's constructor and
     * printCard() method</li>
     * <li>Calling a new number every round using BingoCaller's getNext() method</li>
     * <li>Stamping the Bingo cards using BingoCard's stamp() method</li>
     * <li>Looking for possible winners using BingoCard's checkForStandardWin() or
     * checkForWinPattern() methods</li>
     * <li>Printing the winning Bingo cards and round count once a winner is found</li>
     * </ol>
     * @param args command-line arguments.
     * @see BingoCard#printCard()
     * @see BingoCaller#getNext()
     * @see BingoCard#stamp(int)
     * @see BingoCard#checkForStandardWin()
     * @see BingoCard#checkForWinPattern(int[][])
     */
    public static void main(String[] args)
    {
        Scanner scnr = new Scanner(System.in);
        BingoCaller caller = new BingoCaller();

        //COURSE OBJECTIVE: Data abstraction
        ArrayList<BingoCard> bingoCards = new ArrayList<BingoCard>();
        ArrayList<BingoCard> winningCards = new ArrayList<BingoCard>();

        int[][] patternChosen = new int[5][5];  //Holds the winning pattern if a pattern-type game mode is selected
        int gameMode = 1;
        int numPlayers = 0;
        int numToStamp;
        int idx;
        int inGameMenuResult;
        int rounds = 0;
        boolean modeSelected = false;
        boolean playersEntered = false;
        boolean winnerFound = false;

        //Introduce the program
        System.out.print("*****************************\n" +
                "Welcome to BINGO v2023.08.19!\n" +
                "*****************************\n\n" +
                "RULES:\n" +
                "-------\n" +
                "To play the game, you must first input the number of desired \n" +
                "players and the version of the game you would like to play.\n\n" +
                "Once the game has started, all cards will be displayed. Then,\n" +
                "you must start each round manually, or select AutoPlay to speed\n" +
                "the game through until a winner is found\n\n" +
                "Each round, the number drawn, and the cards, will be displayed.\n" +
                "A stamp on a card is represented by the number 0. At the end of\n" +
                "every round, a winner is automatically searched for. When a winner\n" +
                "is found, the winning card(s) will be displayed and the game will\n" +
                "be over.\n\n" +
                "Let the games begin!\n\n");

        //Get desired game mode
        while(!modeSelected)
        {
            try
            {
                System.out.print("Game modes:\n" +
                        "--------\n" +
                        "1. Standard: Five in a row (straight or diagonal)\n" +
                        "2. X letter: Fill in 9-square X shape\n" +
                        "3. Blackout: Fill in entire card\n" +
                        "4. Four corner: Fill each corner square\n" +
                        "Enter 1-4: ");
                gameMode = getMode(scnr);
                modeSelected = true;
            }
            catch(InputMismatchException e)
            {
                System.out.print("Invalid input. Please try again.\n\n");
                scnr.nextLine();
            }
            catch(GameModeException e)
            {
                System.out.println(e.getMessage());
                System.out.println();
                scnr.nextLine();
            }
        }

        //Prepare the winning pattern if a pattern-type game mode is selected
        if(gameMode > 1)
            fillPattern(gameMode, patternChosen);

        //Get number of players
        while(!playersEntered)
        {
            try
            {
                System.out.print("Enter the number of players: ");
                numPlayers = getPlayers(scnr);
                playersEntered = true;
            }
            catch(InputMismatchException e)
            {
                System.out.println("Invalid input. Please try again. ");
                scnr.nextLine();
            }
            catch(PlayerCountException e)
            {
                System.out.println(e.getMessage());
                System.out.println();
                scnr.nextLine();
            }
        }

        //Prepare bingo cards
        for(idx = 0; idx < numPlayers; idx++)
        {
            bingoCards.add(new BingoCard());
        }

        //Print out newly generated bingo cards and ask user to start game
        //COURSE OBJECTIVE: Dynamic memory allocation
        System.out.print("\nHere are your newly generated cards:\n\n");
        for(BingoCard curr : bingoCards)
        {
            System.out.print("Player " + (bingoCards.indexOf(curr) + 1) + ":\n");
            curr.printCard();
            System.out.println();
        }

        inGameMenuResult = runInGameMenu(scnr);

        if(inGameMenuResult == 1)
        {
            System.out.print("******************\n" +
                    "Round 1... FIGHT!\n" +
                    "******************\n\n");
        }

        //Start the game loop
        while(inGameMenuResult < 3 && !winnerFound)
        {
            //Tick up the round count
            rounds++;

            //Get the next number from caller
            numToStamp = caller.getNext();

            //[Not in autorun] Print the next number
            if(inGameMenuResult == 1)
            {
                System.out.print("The next number is... ");

                if (numToStamp >= 1 && numToStamp <= 15)
                    System.out.print("B");
                else if (numToStamp >= 16 && numToStamp <= 30)
                    System.out.print("I");
                else if (numToStamp >= 31 && numToStamp <= 45)
                    System.out.print("N");
                else if (numToStamp >= 46 && numToStamp <= 60)
                    System.out.print("G");
                else
                    System.out.print("O");

                System.out.print(numToStamp + "!\n");
            }

            //Stamp the cards and print them
            if(inGameMenuResult == 1)
                System.out.print("Stamping cards...\n\n");
            for(BingoCard curr : bingoCards)
            {
                curr.stamp(numToStamp);
                if(inGameMenuResult == 1)
                {
                    System.out.print("Player " + (bingoCards.indexOf(curr) + 1) + ":\n");
                    curr.printCard();
                    System.out.println();
                }
            }

            //Check for a winner in the standard mode (if selected)
            if(gameMode == 1)
            {
                for(BingoCard curr : bingoCards)
                {
                    if(curr.checkForStandardWin())
                    {
                        winnerFound = true;
                        winningCards.add(curr);
                    }
                }
            }
            //Check for a winner in a pattern-type game mode (if selected)
            else
            {
                for(BingoCard curr : bingoCards)
                {
                    if (curr.checkForWinPattern(patternChosen))
                    {
                        winnerFound = true;
                        winningCards.add(curr);
                    }
                }
            }

            //Run the in-game menu as long as a winner hasn't been found yet
            if(!winnerFound && inGameMenuResult == 1)
                inGameMenuResult = runInGameMenu(scnr);
        }

        //Print out a message once the game is over and a winner is found
        //Print out the winning cards
        if(winnerFound)
        {
            System.out.print("*    *    * * * * * * * * *    *     *\n" +
                    "      * * *  W I N N E R  * * *\n" +
                    "*  * * * *   W I N N E R   * * * *  *\n" +
                    " * *  * *   C H I C K E N   * * *  *\n" +
                    "     * * *   D I N N E R   * * *\n" +
                    "  *      * * * * * * * * * *       *\n\n");
            System.out.print("The winning cards are:\n\n");
            for (BingoCard curr : winningCards)
            {
                System.out.print("Player " + (bingoCards.indexOf(curr) + 1) + ":\n");
                curr.printCard();
                System.out.println();
            }
        }

        //COURSE OBJECTIVE: Complexity analysis
        //Finally, print out the number of rounds elapsed during the bingo game
        System.out.println("Thanks for playing!\n" +
                "Rounds elapsed: " + rounds + "\n");
    }

    /**
     * Gets the desired game mode from the user, and conducts input validation.
     * @param sc Scanner object that reads an integer from the command-line.
     * @return An integer representing the user-selected Bingo game mode.
     * @throws InputMismatchException When anything other than and integer (int) is entered.
     * @throws GameModeException When an integer not representing one of the displayed options is entered.
     */
    public static int getMode(Scanner sc) throws InputMismatchException, GameModeException
    {
        int mode = sc.nextInt();

        if(mode < 1 || mode > 4)
            throw new GameModeException();

        return mode;
    }

    /**
     * When a pattern-type game is selected, fills the array that is passed as an argument to
     * BingoCard's checkForWinPattern() method.
     * @param mode An integer representing the game mode which determines the fill pattern.
     * @param patternArray A reference to the 2D array getting filled.
     * @see BingoCard#checkForWinPattern(int[][])
     */
    public static void fillPattern(int mode, int[][] patternArray)
    {
        int row;
        int col;

        //Prepare the array by filling it with 1s, which represent irrelevant squares once the method finishes
        for(row = 0; row < 5; row++)
        {
            for(col = 0; col < 5; col++)
            {
                patternArray[row][col] = 1;
            }
        }

        //Place 0s in the squares depending on the game mode, which represent relevant squares
        switch(mode)
        {
            //X letter
            case 2:
                for(row = 0, col = 0; row < 5; row++, col++)
                    patternArray[row][col] = 0;
                for(row = 4, col = 0; col < 5; row--, col++)
                    patternArray[row][col] = 0;
                break;
            //Blackout
            case 3:
                for(row = 0; row < 5; row ++)
                {
                    for(col = 0; col < 5; col++)
                    {
                        patternArray[row][col] = 0;
                    }
                }
                break;
            //Four corner
            case 4:
                patternArray[0][0] = 0;
                patternArray[4][0] = 0;
                patternArray[0][4] = 0;
                patternArray[4][4] = 0;
                patternArray[2][2] = 0;
                break;
        }
    }

    /**
     * Gets the amount of players to prepare Bingo cards for, and conducts input validation.
     * @param sc Scanner object that reads an integer from the command-line.
     * @return An integer representing the amount of players.
     * @throws InputMismatchException When anything other than an integer (int) is entered.
     * @throws PlayerCountException When a negative integer is entered.
     */
    public static int getPlayers(Scanner sc) throws InputMismatchException, PlayerCountException
    {
        int players = sc.nextInt();

        if(players < 1)
            throw new PlayerCountException();

        return players;
    }

    /**
     * Prints the in-game menu used to either continue the game, autorun the game, or end the game
     * early. Also conducts input validation.
     * @param sc Scanner object that reads an integer from the command-line.
     * @return An integer representing the choice made by the menu's user.
     */
    public static int runInGameMenu(Scanner sc)
    {
        int option = 0;
        boolean optionSelected = false;

        while(!optionSelected)
        {
            try
            {
                System.out.print("Choose an option:\n" +
                        "------------------\n" +
                        "1. Continue to the next round\n" +
                        "2. Autorun the game (only display winners)\n" +
                        "3. End the game early\n" +
                        "Enter: ");
                option = sc.nextInt();

                if(option < 1 || option > 3)
                    throw new GameMenuException();

                optionSelected = true;
            }
            catch(InputMismatchException e)
            {
                System.out.print("Invalid input. Please try again.\n\n");
                sc.nextLine();
            }
            catch(GameMenuException e)
            {
                System.out.println(e.getMessage());
                System.out.println();
                sc.nextLine();
            }
        }
        return option;
    }
}
/*  REFLECTION:

    I really enjoyed this project! It was just the right amount of challenging to take me a long time, but not too
    challenging that I would've needed more time than was allotted. The most challenging aspect was designing the
    algorithms that test for a win condition in the BingoCard class; a lot of my time was spent reviewing
    Zybooks and the Java documentation, but that is not difficult, just time confusing, whereas designing an algorithm
    requires conceptual design, trial and error. Because it was challenging, I had the most fun trying to put
    together the win condition algorithm.

    For an extra challenge, I took on the option of including the ability to play different game modes. I stuck to the
    simplest of the options available, but I'm very proud of how easy it was and how I came up with the idea to
    create an array that is filled with the winning pattern, which is then passed to a BingoCard object for
    comparison.

    For an extra EXTRA challenge, I tried documenting and creating a Javadoc. Zybooks explained what the Javadoc
    comments looks like, but not how to create the HTML files. I also thought it was really cool how you can include
    HTML tags to format how the documentation will read in the web browser. Lucky for me, IntelliJ IDEA makes it
    really beginner-friendly to create the Javadoc files.

    Beta test feedback:
        - While making a Bingo game was challenging, I'm not quite sure if it suits the intentions of the final
        project, which was to demonstrate that I've learned the course objectives. I couldn't find a use case for
        recursion, stacks, queues or binary lists. For the fourth objective, I could've used further guidance because
        I wasn't sure where to include algorithm analysis other than the amount of rounds it takes to find a winner.
        There's something to be said about how long a bingo game can be based on the type of game, but that's more
        of an analysis on bingo and not the search algorithms used in the program.

        - You provided starter code, which I somehow completely missed, but I honestly don't think it was required.
        I enjoyed the challenge of starting from scratch. Zybooks holds your hand all throughout, and it felt great
        to be let loose and apply a lot of what I've learned, as well as experiment with the things that weren't
        covered in great detail, such as Javadoc and Packages.

    Advice to future students:
        - When planning out the program, make sure to keep the course objectives in mind so as to incorporate
        their aspects into the program. Failing to do so may create undesired rework.
 */
