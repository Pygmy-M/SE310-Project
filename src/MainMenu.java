import java.util.Scanner;

public class MainMenu {

    Scanner scan = new Scanner(System.in);

    public int getInput() {
        boolean noInput = true;
        int choice = 0;
            while (noInput) {
                try {
                    System.out.println("1) Survey\n" +
                            "2) Test\n" +
                            "3) Quit");
                    choice = Integer.parseInt(scan.nextLine());
                    if (choice < 1 || choice > 3) {
                        System.out.println("INVALID INPUT");
                    } else {
                        noInput = false;
                    }
                } catch (Exception e) {
                    System.out.println("INVALID INPUT");
                }
            }
        return choice;

    }
}
