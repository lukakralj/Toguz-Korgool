/*
 * Main class for the Team Platypus Agile Project
 *
 */
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        System.out.println("Main method works as intended");
        Board core = new Board();
        core.printBoard();
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        while (!line.equals("q")) {
            core.makeMove(Integer.parseInt(line), true);
            core.printBoard();
            line = sc.nextLine();
        }
    }
}
