import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class TestMenu {

    private Test loadedTest;

    public TestMenu() {
        this.loadedTest = null;
    }

    Scanner scan = new Scanner(System.in);
    
    public void MainMenu() throws IOException {
        String input = "";
        while (!input.equals("10")) {
            System.out.println("1) Create a new Test\n" +
                    "2) Display an existing Test without correct answers\n" +
                    "3) Display an existing Test with correct answers\n" +
                    "4) Load an existing Test\n" +
                    "5) Save the current Test\n" +
                    "6) Take the current Test\n" +
                    "7) Modifying the current Test\n" +
                    "8) Tabulate a Test\n" +
                    "9) Grade a Test\n" +
                    "10) Return to Previous Menu");
            input = scan.nextLine();
            if (input.equals("1")) {
                CreateMenu();
            } else if (input.equals("2")) {
                this.DisplayTestNoAnswers();
            } else if (input.equals("3")) {
                this.DisplayTestWithAnswers();
            } else if (input.equals("4")) {
                try {
                    ArrayList<File> files = FindFiles.findFiles("Definition.ser");
                    if (files.size() != 0) {
                        System.out.println("Please select a file to load:");
                        int k = 1;
                        for (File f : files) {
                            if (TransientWhenSerializing.deserialize(f.getPath()).getClass() == Test.class) {
                                String[] nameList = f.getName().split("\\.");
                                System.out.println(k + ") " + nameList[0]);
                                k++;
                            }
                        }
                        String fileInput = scan.nextLine();
                        this.Load(files.get(Integer.parseInt(fileInput) - 1).getPath());
                    } else {
                        System.out.println("No .ser files found in directory");
                    }

                } catch (Exception e) {
                    System.out.println("Invalid Input");
                }
            } else if (input.equals("5")) {
                this.Save(this.loadedTest);
            } else if (input.equals("6")) {
                this.Take();
            } else if (input.equals("7")) {
                this.ModMenu();
            } else if (input.equals("8")) {
                this.Tabulate();
            } else if (input.equals("9")) {

                    ArrayList<File> files = FindFiles.findFiles("Definition" + ".ser");
                    if (files.size() != 0) {
                        System.out.println("Please select an existing test to grade:");
                        int k = 1;
                        for (File f : files) {
                            if (TransientWhenSerializing.deserialize(f.getPath()).getClass() == Test.class) {
                                String[] nameList = f.getName().split("\\.");
                                System.out.println(k + ") " + nameList[0]);
                                k++;
                            }
                        }
                        String fileInput = scan.nextLine();
                        this.Grade((Test) TransientWhenSerializing.deserialize(files.get(Integer.parseInt(fileInput) - 1).getPath()));
                    } else {
                        System.out.println("No .ser files found in directory");
                    }

            } else if (input.equals("10")) {
            } else {
                System.out.println("Invalid Input. Try Again.");
            }

        }
    }

    private void Grade(Test t) throws IOException {
            ArrayList<File> files = FindFiles.findFiles(t.getName() + "Response");
            if (files.size() != 0) {
                System.out.println("Please select an existing response set:");
                int k = 1;
                for (File f : files) {
                    String[] nameList = f.getName().split("\\.");
                    System.out.println(k + ") " + nameList[0]);
                    k++;
                }
            } else {
                System.out.println("No responses found for this test");
            }
            String fileInput = scan.nextLine();
            ((Test) TransientWhenSerializing.deserialize(files.get(Integer.parseInt(fileInput) - 1).getPath())).Grade();
    }


    private void DisplayTestNoAnswers() {
        if (this.loadedTest == null) {
            System.out.println("You must have a Test loaded in order to display it.");
        } else {
            this.loadedTest.Display();
        }
    }

    private void DisplayTestWithAnswers() {
        if (this.loadedTest == null) {
            System.out.println("You must have a Test loaded in order to display it.");
        } else {
            this.loadedTest.DisplayWithAnswers();
        }
    }

    private void Take() {
        if (this.loadedTest == null) {
            System.out.println("You must have a test loaded in order to take it.");
        } else if (this.loadedTest.getType().equals("Response")) {
            System.out.println("This is a response to a test and therefore cannot be taken");
        } else {
            this.loadedTest.setResponseNum(this.loadedTest.getResponseNum() + 1);
            Test tempTest = new Test(this.loadedTest.getName());
            tempTest.setResponseNum(this.loadedTest.getResponseNum());
            for (Question q : this.loadedTest.getQuestions()) {
                tempTest.addQuestion(q.Clone());
            }
            tempTest.Take();
            tempTest.setCorrectAnswers(this.loadedTest.getCorrectAnswers());
            tempTest.makeResponse();
            this.Save(tempTest);
        }
    }

    private void Tabulate() throws IOException {
        if (this.loadedTest == null) {
            System.out.println("You must have a test loaded in order to display it.");
        } else if (this.loadedTest.getType().equals("Response")) {
            System.out.println("This is a response to a test and therefore cannot be tabulated");
        } else {
            this.loadedTest.Tabulate();
        }
    }

    private void ModMenu() {
        if (this.loadedTest == null) {
            System.out.println("You must have a Test loaded in order to modify it.");
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
                    if (0 < qNum && qNum <= this.loadedTest.getQuestions().size()) {
                        noInput = false;
                    } else {
                        System.out.println("Out of Range");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                }
            }
            System.out.println(this.loadedTest.getQuestions().get(qNum-1).getPrompt());
            noInput = true;
            if (this.loadedTest.getQuestions().get(qNum-1).getClass().equals(MultipleChoice.class)) {
                while (noInput) {
                    System.out.println("Do you wish to modify the prompt? Y/N");
                    String doMod = scan.nextLine();
                    if (doMod.toUpperCase(Locale.ROOT).equals("Y")) {
                        System.out.println(this.loadedTest.getQuestions().get(qNum).getPrompt());
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
                        ((MultipleChoice) this.loadedTest.getQuestions().get(qNum-1)).DisplayChoices();
                        System.out.println("Which choice do you want to modify?");
                        char intI = scan.nextLine().charAt(0);
                        intI -= 64;
                        if (!(0 < intI && intI < ((MultipleChoice) this.loadedTest.getQuestions().get(qNum - 1)).getChoices().size())) {
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
            } else if (this.loadedTest.getQuestions().get(qNum-1).getClass().equals(Matching.class)) {
                noInput = true;
                while (noInput) {
                    System.out.println("Do you wish to modify the prompt? Y/N");
                    String doMod = scan.nextLine();
                    if (doMod.toUpperCase(Locale.ROOT).equals("Y")) {
                        System.out.println(this.loadedTest.getQuestions().get(qNum-1).getPrompt());
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
                        ((Matching) this.loadedTest.getQuestions().get(qNum-1)).DisplayChoices();
                        System.out.println("Which choice do you want to modify? Write as 'Side Row' (ex. L 2 )");
                        try {
                            String[] inputN = scan.nextLine().split(" ");
                            side = inputN[0];
                            cNum = Integer.parseInt(inputN[1]);
                            if (0 < cNum && cNum <= ((Matching) this.loadedTest.getQuestions().get(qNum-1)).getChoices("Right").size()) {
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
            this.loadedTest.Modify(qNum, newP, cNum, newC, side, operation);
        }
    }

    private void Save(Test Test) {
        if (Test == null) {
            System.out.println("You must have a Test loaded in order to save it.");
        } else {
            TransientWhenSerializing.serialize(Test);
        }
    }

    private void Load(String savePath) {
        this.loadedTest = (Test) TransientWhenSerializing.deserialize(savePath);
    }

    private void CreateMenu() {
        String input = "";
        System.out.println("Please enter a name for your Test");
        String name = scan.nextLine();
        this.loadedTest = new Test(name);
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
                TrueFalseMenu(this.loadedTest.getQuestions().size()+1);
            } else if (input.equals("2")) {
                MultipleChoiceMenu(this.loadedTest.getQuestions().size()+1);
            } else if (input.equals("3")) {
                ShortAnswerMenu(this.loadedTest.getQuestions().size()+1);
            } else if (input.equals("4")) {
                EssayMenu(this.loadedTest.getQuestions().size()+1);
            } else if (input.equals("5")) {
                ValidDateMenu(this.loadedTest.getQuestions().size()+1);
            } else if (input.equals("6")) {
                MatchingMenu(this.loadedTest.getQuestions().size()+1);
            } else if (input.equals("7")) {
            } else {
                System.out.println("Invalid Input. Try Again.");
            }
        }
        this.loadedTest.makeDefinition();
        this.Save(this.loadedTest);
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
        this.loadedTest.addQuestion(q);
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
        int t = 0;
        System.out.println("Please enter the correct answer(s) to this question");
        while (t < numA) {
            try {
                String a = scan.nextLine();
                if (q.isValidResponse(a)) {
                    this.loadedTest.addAnswers(new Response(a));
                    t += 1;
                }
            } catch (Exception e) {
                System.out.println("INVALID INPUT");
            }
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
        this.loadedTest.addQuestion(new ValidDate(i, prompt, numA));
        int t = 0;
        System.out.println("Please enter the correct answer(s) to this question");
        while (t < numA) {
            try {
                String a = scan.nextLine();
                ValidDate v = new ValidDate(0, "", 0);
                if (v.isValidResponse(a)) {
                    this.loadedTest.addAnswers(new Response(a));
                    t += 1;
                }
            } catch (Exception e) {
                System.out.println("INVALID INPUT");
            }
        }
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
        this.loadedTest.addQuestion(new Essay(i, prompt, numA));
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
        System.out.println("Please enter the correct answer(s) to this question");
        noInput = true;
        while (noInput) {
            try {
                String a = scan.nextLine();
                this.loadedTest.addAnswers(new Response(a));
                noInput = false;
            } catch (Exception e) {
                System.out.println("INVALID INPUT");
            }
        }
        this.loadedTest.addQuestion(new ShortAnswer(i, prompt, numA));
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
        this.loadedTest.addQuestion(q);
        for (int j = 0; j < numC; j++) {
            System.out.println("Enter choice #" + (j+1) + ".");
            String choice = scan.nextLine();
            q.addChoice(choice);
        }
        int t = 0;
        System.out.println("Please enter the correct answer(s) to this question");
        while (t < numA) {
            try {
                String a = scan.nextLine();
                if (q.isValidAnswer(a)) {
                    this.loadedTest.addAnswers(new Response(a));
                    t += 1;
                } else {
                    System.out.println("INVALID INPUT");
                }
            } catch (Exception e) {
                System.out.println("INVALID INPUT");
            }
        }
    }

    private void TrueFalseMenu(int i) {
        System.out.println("Enter the prompt for your True/False question:");
        String prompt = scan.nextLine();
        this.loadedTest.addQuestion(new TrueFalse(i, prompt));
        System.out.println("Please enter the correct answer to this question");
        boolean noInput = true;
        TrueFalse dQ = new TrueFalse(0, "");
        while (noInput) {
            try {
                String a = scan.nextLine();
                if (dQ.isValidAnswer(a)) {
                    this.loadedTest.addAnswers(new Response(a));
                    noInput = false;
                } else {
                    System.out.println("INVALID INPUT");
                }
            } catch (Exception e) {
                System.out.println("INVALID INPUT");
            }
        }
    }
}
