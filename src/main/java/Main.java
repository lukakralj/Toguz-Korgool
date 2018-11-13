import java.util.Scanner;

/**
 * Main class for the Team Platypus Agile Project
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Main method works as intended");
        Board core = new Board();
        core.printBoard();
        Player startPlayer = core.getWhitePlayer();
        Player waitingPlayer = core.getBlackPlayer();
        System.out.println("--white turn:");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        while (!line.equals("q")) {
            try {
                if (!line.trim().equals("")) {
//                    core.makeMove(Integer.parseInt(line), core.getWhitePlayer(), core.getBlackPlayer());
                    int hole = Integer.parseInt(line);
                    if (hole > 8 || hole < 0) {
                        throw new Exception();
                    }
                    core.makeMove(hole, startPlayer, waitingPlayer);

                    core.printBoard();
                    //switching turns
                    Player temp = startPlayer;
                    startPlayer = waitingPlayer;
                    waitingPlayer = temp;
                    if (startPlayer.equals(core.getWhitePlayer())) {
                        System.out.println("--white turn:");
                    }
                    else {
                        System.out.println("--black turn:");
                    }
                }
                else {
                    throw new Exception();
                }
            }
            catch (Exception e) {
                System.out.println("Enter: 0-8 or 'q' to exit.");
            }
            finally {
                line = sc.nextLine();
            }
        }
    }
}

