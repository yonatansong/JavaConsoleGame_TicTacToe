package tech.rumlow;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args)
    {
        boolean gameOn=true;
        while (gameOn)
        {
            boolean playFirst = whoSFirst();
            play(playFirst);
            gameOn = askToReplay();
        }
        System.out.println("Okay, then. See ya!");
    }
    public static boolean whoSFirst()
    {
        System.out.println("Welcome to Simple Java Tic Tac Toe!");
        System.out.print("Would you like to play first? (y/n) ");
        Scanner SC = new Scanner(System.in);
        char userInput = SC.next().charAt(0);
        return userInput == 'Y' || userInput == 'y';
    }
    public static void play(boolean playFirst)
    {
        int[][] piecesLocation =
                {
                        {0,0,0},
                        {0,0,0}
                };
        String[] playerName = {"You","Bot"};

        int turnCount=0;
        if(!playFirst) turnCount=1;
        System.out.println
                (
                        playerName[turnCount]+
                        " will play first. Get ready!"
                );

        updateBoard(piecesLocation);
        int placement = 0;
        boolean keepPlaying = true;
        while (keepPlaying)
        {
            int pieceSeq = (turnCount/2)%3;
            int playerNumber = turnCount%2;

            if(playerNumber==0)
                placement = playerPlacement();
            if(playerNumber==1)
                placement = botPlacement(piecesLocation);

            int returnPlacement = pieceChecker
                    (
                            placement,
                            piecesLocation
                    );
            if(returnPlacement!=0)
            {
                updatePiece
                        (
                                playerNumber,
                                pieceSeq,
                                returnPlacement,
                                piecesLocation
                        );
                System.out.println
                        (
                                playerName[playerNumber]+
                                " plays " +
                                returnPlacement + ". "
                        );
                keepPlaying = isGameEnd
                        (
                                piecesLocation,
                                playerNumber,
                                playerName
                        );
                if(turnCount%2==1 || !keepPlaying)
                    updateBoard(piecesLocation);
                turnCount++;
            }
        }
    }
    public static int botPlacement(int[][] piecesLocation)
    {
        boolean keepLooking = true;

        Random rand = new Random();
        int botInput = 0;

        while (keepLooking)
        {
            keepLooking = false;

            botInput =  rand.nextInt(9) + 1;

            for (int[] row : piecesLocation)
            {
                for (int value : row) {
                    if (botInput == value)
                    {
                        keepLooking = true;
                        break;
                    }
                }
                if (keepLooking) break;
            }
        }
        return botInput;
    }
    public static int playerPlacement()
    {
        Scanner SC = new Scanner(System.in);
        while (true)
        {
            System.out.print("Enter your placement (1-9): ");
            int userInput = SC.nextInt();

            if(userInput>=1 && userInput<=9) return userInput;
            System.out.println
                    (
                            "Your input is outside the boundaries!"
                    );
        }
    }
    public static void printGameBoard(char[][] gameBoard)
    {
        for (char[] row : gameBoard)
        {
            for (char c : row) System.out.print(c);
            System.out.println();
        }
    }
    public static char turn(int playerNumber)
    {
        char[] symbol = {'X','O'};
        return symbol[playerNumber];
    }
    public static int pieceChecker
            (
                    int userInput,
                    int[][] piecesLocation
            ){
        for (int[] ints : piecesLocation)
            for
            (
                    int pieces = 0;
                    pieces < piecesLocation[0].length;
                    pieces++
            )
            {
                if (userInput == ints[pieces]) {
                    System.out.println
                            (
                                    "Cell " +
                                    userInput +
                                    " is already filled."
                            );
                    return 0;
                }
            }

        return userInput;
    }
    public static void updateBoard(int[][] piecesLocation){
        char[][] gameBoard =
                {
                        {' ','|',' ','|',' '},
                        {'-','+','-','+','-'},
                        {' ','|',' ','|',' '},
                        {'-','+','-','+','-'},
                        {' ','|',' ','|',' '}
                };
        updateCell(piecesLocation,gameBoard);
        printGameBoard(gameBoard);
    }
    public static void updatePiece
            (
                    int playerNumber,
                    int pieceSeq,
                    int locationInput,
                    int[][] piecesLocation
            )
    {
        piecesLocation[playerNumber][pieceSeq] = locationInput;
    }
    public static void updateCell
            (
                    int[][] piecesLocation,
                    char[][] gameBoard
            )
    {
        for
        (
                int player = 0;
                player<piecesLocation.length;
                player++
        )
        {
            for
            (
                    int pieces =0;
                    pieces<piecesLocation[0].length;
                    pieces++)
            {
                int value = piecesLocation[player][pieces];
                if(value!=0)
                {
                    int[] location = cellLocator(value);
                    int row = location[0];
                    int col = location[1];
                    gameBoard[row][col]=turn(player);
                }
            }
        }
    }
    public static int[] cellLocator(int userInput)
    {
        int row = 2*((userInput-1)/3);
        int mod3 = userInput%3; if (mod3==0) mod3=3;
        int col = 2*(mod3-1);
        return new int[]{row,col};
    }
    public static boolean isDiffConstant(int[] valueArray)
    {
        Arrays.sort(valueArray);
        int diff1 = valueArray[0] - valueArray[1];
        int diff2 = valueArray[1] - valueArray[2];
        return diff1 == diff2;
    }
    public static boolean askToReplay()
    {
        System.out.print("Play again? (y/n) ");
        Scanner SC = new Scanner(System.in);
        char userInput = SC.next().charAt(0);
        return userInput == 'Y' || userInput == 'y';
    }
    public static boolean isGameEnd
            (
                    int[][] piecesLocation,
                    int playerNumber,
                    String[] playerName
            )
    {
        // no 3 input yet
        if(piecesLocation[playerNumber][2]==0) return true;

        // simple isDiffConstant using piecesLocation value
        int[] simpleCheck = new int[3];
        System.arraycopy
                (
                        piecesLocation[playerNumber],
                        0,
                        simpleCheck,
                        0,
                        piecesLocation[playerNumber].length
                );
        if(!isDiffConstant(simpleCheck)) return true;

        // complex isDiffConstant using board location
        int[] rowArray = new int[3];
        int[] colArray = new int[3];
        for
        (
                int cell=0;
                cell<piecesLocation[playerNumber].length;
                cell++
        )
        {
            int[] location = cellLocator
                    (
                            piecesLocation[playerNumber][cell]
                    );
            int row = location[0];
            int col = location[1];
            rowArray[cell]=row;
            colArray[cell]=col;
        }
        if(isDiffConstant(colArray) && isDiffConstant(rowArray))
        {
            System.out.println
                    (
                            "Game Over! " +
                            playerName[playerNumber] +
                            " win."
                    );
            return false;
        }
        return true;
    }
}
