import java.util.Scanner;

/*
 * Main class for the Team Platypus Agile Project
 *
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Main method works as intended");
        Board core = new Board();
        core.printBoard();
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        while (!line.equals("q")) {
            try {
                if (!line.trim().equals("")) {
                    core.makeMove(Integer.parseInt(line), true);
                    core.printBoard();
                }
                else {
                    System.out.println("Enter: 0-8 or 'q' to exit.");
                }
            }
            catch (Exception e) {
                System.out.println("Enter: 0-8");
            }
            finally {
                line = sc.nextLine();
            }
        }
    }
}
