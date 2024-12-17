import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class SurveyMenu {

    private Survey loadedSurvey;

    public SurveyMenu() {
        this.loadedSurvey = null;
    }

    Scanner scan = new Scanner(System.in);
    
    public void MainMenu() throws IOException {
        String input = "";
        while (!input.equals("8")) {
            System.out.println("1) Create a new Survey\n" +
                    "2) Display an existing Survey\n" +
                    "3) Load an existing Survey\n" +
                    "4) Save the current Survey\n" +
                    "5) Take the current Survey\n" +
                    "6) Modifying the current Survey\n" +
                    "7) Tabulate a Survey\n" +
                    "8) Return to Previous Menu");
            input = scan.nextLine();
            if (input.equals("1")) {
                CreateMenu();
            } else if (input.equals("2")) {
                this.DisplaySurvey();
            } else if (input.equals("3")) {
                try {
                    ArrayList<File> files = FindFiles.findFiles("Definition.ser");
                    if (files.size() != 0) {
                        System.out.println("Please select a file to load:");
                        int k = 1;
                        for (File f : files) {
                            if (TransientWhenSerializing.deserialize(f.getPath()).getClass() != Test.class) {
                                String[] nameList = f.getName().split("\\.");
                                System.out.println(k + ") " + nameList[0]);
                                k++;
                            }
                        }
                    } else {
                        System.out.println("No .ser files found in directory");
                    }
                    String fileInput = scan.nextLine();
                    this.Load(files.get(Integer.parseInt(fileInput) - 1).getPath());
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                }
            } else if (input.equals("4")) {
                this.Save(this.loadedSurvey);
            } else if (input.equals("5")) {
                this.Take();
            } else if (input.equals("6")) {
                this.ModMenu();
            } else if (input.equals("7")) {
                this.Tabulate();
            } else if (input.equals("8")) {
            } else {
                System.out.println("Invalid Input. Try Again.");
            }

        }
    }

    private void Tabulate() throws IOException {
        if (this.loadedSurvey == null) {
            System.out.println("You must have a survey loaded in order to display it.");
        } else if (this.loadedSurvey.getType().equals("Response")) {
            System.out.println("This is a response to a survey and therefore cannot be tabulated");
        } else {
            this.loadedSurvey.Tabulate();
        }
    }

    private void DisplaySurvey() {
        if (this.loadedSurvey == null) {
            System.out.println("You must have a survey loaded in order to display it.");
        } else {
            this.loadedSurvey.Display();
        }
    }

    private void Take() {
        if (this.loadedSurvey == null) {
            System.out.println("You must have a survey loaded in order to take it.");
        } else if (this.loadedSurvey.getType().equals("Response")) {
            System.out.println("This is a response to a survey and therefore cannot be taken");
        } else {
            this.loadedSurvey.setResponseNum(this.loadedSurvey.getResponseNum() + 1);
            Survey tempSurvey = new Survey(this.loadedSurvey.getName());
            tempSurvey.setResponseNum(this.loadedSurvey.getResponseNum());
            for (Question q : this.loadedSurvey.getQuestions()) {
                tempSurvey.addQuestion(q.Clone());
            }
            tempSurvey.Take();
            tempSurvey.makeResponse();
            this.Save(tempSurvey);
        }
    }

    private void ModMenu() {
        if (this.loadedSurvey == null) {
            System.out.println("You must have a survey loaded in order to modify it.");
        } else {
            boolean noInput = true;
            boolean noInput1 = true;
            int qNum = -1;
            String newP = "";
            int cNum = -1;
            String side = "";
            String newC = "";
            String operation = "prompt";
            while (noInput) {
                System.out.println("What question do you wish to modify?");
                try {
                    qNum = Integer.parseInt(scan.nextLine());
                    if (0 < qNum && qNum <= this.loadedSurvey.getQuestions().size()) {
                        noInput = false;
                    } else {
                        System.out.println("Out of Range");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                }
            }
            System.out.println(this.loadedSurvey.getQuestions().get(qNum-1).getPrompt());
            noInput = true;
            if (this.loadedSurvey.getQuestions().get(qNum-1).getClass().equals(MultipleChoice.class)) {
                while (noInput) {
                    System.out.println("Do you wish to modify the prompt? Y/N");
                    String doMod = scan.nextLine();
                    if (doMod.toUpperCase(Locale.ROOT).equals("Y")) {
                        System.out.println(this.loadedSurvey.getQuestions().get(qNum-1).getPrompt());
                        System.out.println("Enter new prompt:");
                        newP = scan.nextLine();
                        noInput = false;
                    } else if (doMod.toUpperCase(Locale.ROOT).equals("N")) {
                        noInput = false;
                    } else {
                        System.out.println("Invalid Input");
                    }
                }
                noInput = true;
                while (noInput) {
                    System.out.println("Do you wish to modify choices? Y/N");
                    String doMod = scan.nextLine();
                    if (doMod.toUpperCase(Locale.ROOT).equals("Y")) {
                        operation = "choice";
                        ((MultipleChoice) this.loadedSurvey.getQuestions().get(qNum-1)).DisplayChoices();
                        System.out.println("Which choice do you want to modify?");
                        char inputChar = scan.nextLine().charAt(0);
                        char intI = inputChar;
                        intI -= 65;
                        if (!(0 <= intI && intI < ((MultipleChoice) this.loadedSurvey.getQuestions().get(qNum - 1)).getChoices().size())) {
                            System.out.println("Invalid Input");
                        } else {
                            cNum = intI;
                            noInput = false;
                            while (noInput1) {
                                newC = scan.nextLine();
                                if (newC.equals("")) {
                                    System.out.println("Please input valid choice prompt");
                                } else {
                                    noInput1 = false;
                                }
                            }
                        }
                    } else if (doMod.toUpperCase(Locale.ROOT).equals("N")) {
                        noInput = false;
                    } else {
                        System.out.println("Invalid Input");
                    }
                }
            } else if (this.loadedSurvey.getQuestions().get(qNum-1).getClass().equals(Matching.class)) {
                noInput = true;
                while (noInput) {
                    System.out.println("Do you wish to modify the prompt? Y/N");
                    String doMod = scan.nextLine();
                    if (doMod.toUpperCase(Locale.ROOT).equals("Y")) {
                        System.out.println(this.loadedSurvey.getQuestions().get(qNum-1).getPrompt());
                        System.out.println("Enter new prompt:");
                        newP = scan.nextLine();
                        noInput = false;
                    } else if (doMod.toUpperCase(Locale.ROOT).equals("N")) {
                        noInput = false;
                    } else {
                        System.out.println("Invalid Input");
                    }
                }
                noInput = true;
                while (noInput) {
                    System.out.println("Do you wish to modify choices? Y/N");
                    String doMod = scan.nextLine();
                    if (doMod.toUpperCase(Locale.ROOT).equals("Y")) {
                        operation = "choice";
                        ((Matching) this.loadedSurvey.getQuestions().get(qNum-1)).DisplayChoices();
                        System.out.println("Which choice do you want to modify? Write as 'Side Row' (ex. L 2 )");
                        try {
                            String[] inputN = scan.nextLine().split(" ");
                            side = inputN[0];
                            cNum = Integer.parseInt(inputN[1]);
                            if (0 < cNum && cNum <= ((Matching) this.loadedSurvey.getQuestions().get(qNum-1)).getChoices("Right").size()) {
                                if (side.equals("L") || side.equals("R")) {
                                    noInput = false;
                                    while (noInput1) {
                                        newC = scan.nextLine();
                                        if (newC.equals("")) {
                                            System.out.println("Please input valid choice prompt");
                                        } else {
                                            noInput1 = false;
                                        }
                                    }
                                }
                            } else {
                                System.out.println("Invalid Input");
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR: Invalid Input");
                        }
                    } else if (doMod.toUpperCase(Locale.ROOT).equals("N")) {
                        noInput = false;
                    } else {
                        System.out.println("Invalid Input");
                    }
                }
            } else {
                System.out.println("Enter new prompt:");
                newP = scan.nextLine();
            }
            this.loadedSurvey.Modify(qNum, newP, cNum, newC, side, operation);
        }
    }

    private void Save(Survey survey) {
        if (survey == null) {
            System.out.println("You must have a survey loaded in order to save it.");
        } else {
            TransientWhenSerializing.serialize(survey);
        }
    }

    private void Load(String savePath) {
        this.loadedSurvey = TransientWhenSerializing.deserialize(savePath);
    }

    private void CreateMenu() {
        String input = "";
        System.out.println("Please enter a name for your survey");
        String name = scan.nextLine();
        this.loadedSurvey = new Survey(name);
        while (!input.equals("7")) {
            System.out.println("1) Add a new T/F question\n" +
                    "2) Add a new multiple-choice question\n" +
                    "3) Add a new short answer question\n" +
                    "4) Add a new essay question\n" +
                    "5) Add a new date question\n" +
                    "6) Add a new matching question\n" +
                    "7) Return to previous menu");
            input = scan.nextLine();
            if (input.equals("1")) {
                TrueFalseMenu(this.loadedSurvey.getQuestions().size()+1);
            } else if (input.equals("2")) {
                MultipleChoiceMenu(this.loadedSurvey.getQuestions().size()+1);
            } else if (input.equals("3")) {
                ShortAnswerMenu(this.loadedSurvey.getQuestions().size()+1);
            } else if (input.equals("4")) {
                EssayMenu(this.loadedSurvey.getQuestions().size()+1);
            } else if (input.equals("5")) {
                ValidDateMenu(this.loadedSurvey.getQuestions().size()+1);
            } else if (input.equals("6")) {
                MatchingMenu(this.loadedSurvey.getQuestions().size()+1);
            } else if (input.equals("7")) {
            } else {
                System.out.println("Invalid Input. Try Again.");
            }
        }
        this.loadedSurvey.makeDefinition();
        this.Save(this.loadedSurvey);
    }

    private void MatchingMenu(int i) {
        System.out.println("Enter the prompt for your matching question:");
        String prompt = scan.nextLine();
        int numC = 0;
        boolean noInput = true;
        while (noInput) {
            try {
                System.out.println("Enter the number of rows for your matching question.");
                numC = Integer.parseInt(scan.nextLine());
                noInput = false;
            } catch (Exception e) {
                System.out.println("ERROR: INVALID NUMBER");
            }
        }
        int numA = 0;
        noInput = true;
        while (noInput) {
            try {
                System.out.println("Enter the number of answers that should be given");
                numA = Integer.parseInt(scan.nextLine());
                noInput = false;
            } catch (Exception e) {
                System.out.println("ERROR: INVALID NUMBER");
            }
        }
        Matching q = new Matching(i, prompt, numA);
        this.loadedSurvey.addQuestion(q);
        System.out.println("Left Side Choices:");
        for (int j = 0; j < numC; j++) {
            System.out.println("Enter choice #" + (j+1) + ".");
            String choice = scan.nextLine();
            q.addChoice(choice, "Left");
        }
        System.out.println("Right Side Choices:");
        for (int j = 0; j < numC; j++) {
            System.out.println("Enter choice #" + (j+1) + ".");
            String choice = scan.nextLine();
            q.addChoice(choice, "Right");
        }
    }

    private void ValidDateMenu(int i) {
        System.out.println("Enter the prompt for your date question:");
        String prompt = scan.nextLine();
        int numA = 0;
        boolean noInput = true;
        while (noInput) {
            try {
                System.out.println("Enter the number of answers that should be given");
                numA = Integer.parseInt(scan.nextLine());
                noInput = false;
            } catch (Exception e) {
                System.out.println("ERROR: INVALID NUMBER");
            }
        }
        this.loadedSurvey.addQuestion(new ValidDate(i, prompt, numA));
    }

    private void EssayMenu(int i) {
        System.out.println("Enter the prompt for your essay question:");
        String prompt = scan.nextLine();
        int numA = 0;
        boolean noInput = true;
        while (noInput) {
            try {
                System.out.println("Enter the number of answers that should be given");
                numA = Integer.parseInt(scan.nextLine());
                noInput = false;
            } catch (Exception e) {
                System.out.println("ERROR: INVALID NUMBER");
            }
        }
        this.loadedSurvey.addQuestion(new Essay(i, prompt, numA));
    }

    private void ShortAnswerMenu(int i) {
        System.out.println("Enter the prompt for your short answer question:");
        String prompt = scan.nextLine();
        int numA = 0;
        boolean noInput = true;
        while (noInput) {
            try {
                System.out.println("Enter the number of answers that should be given");
                numA = Integer.parseInt(scan.nextLine());
                noInput = false;
            } catch (Exception e) {
                System.out.println("ERROR: INVALID NUMBER");
            }
        }
        this.loadedSurvey.addQuestion(new ShortAnswer(i, prompt, numA));
    }

    private void MultipleChoiceMenu(int i) {
        System.out.println("Enter the prompt for your multiple-choice question:");
        String prompt = scan.nextLine();
        int numC = 0;
        boolean noInput = true;
        while (noInput) {
            try {
                System.out.println("Enter the number of choices for your multiple-choice question.");
                numC = Integer.parseInt(scan.nextLine());
                noInput = false;
            } catch (Exception e) {
                System.out.println("ERROR: INVALID NUMBER");
            }
        }
        int numA = 0;
        noInput = true;
        while (noInput) {
            try {
                System.out.println("Enter the number of answers that should be given");
                numA = Integer.parseInt(scan.nextLine());
                if (0 < numA && numA <= numC) {
                    noInput = false;
                } else {
                    System.out.println("ERROR: INVALID NUMBER");
                }
            } catch (Exception e) {
                System.out.println("ERROR: INVALID NUMBER");
            }
        }
        MultipleChoice q = new MultipleChoice(i, prompt, numA);
        this.loadedSurvey.addQuestion(q);
        for (int j = 0; j < numC; j++) {
            System.out.println("Enter choice #" + (j+1) + ".");
            String choice = scan.nextLine();
            q.addChoice(choice);
        }
    }

    private void TrueFalseMenu(int i) {
        System.out.println("Enter the prompt for your True/False question:");
        String prompt = scan.nextLine();
        this.loadedSurvey.addQuestion(new TrueFalse(i, prompt));
    }
}
