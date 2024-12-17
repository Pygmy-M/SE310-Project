import java.io.IOException;

public class Driver {

    public static void main(String[] args) throws IOException {
        MainMenu menu = new MainMenu();
        int choice = 0;
        while (choice != 3) {
            choice = menu.getInput();
            if (choice == 1) {
                SurveyMenu surveyMenu = new SurveyMenu();
                surveyMenu.MainMenu();
            } else if (choice == 2) {
                TestMenu testMenu = new TestMenu();
                testMenu.MainMenu();
            } else {
            }
        }
    }
}
