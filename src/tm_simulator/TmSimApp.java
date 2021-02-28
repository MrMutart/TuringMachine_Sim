package tm_simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Prompts the user to select a text file containing a Turing Machine model, 
 * parses the file and constructs the TM with file data, then prompts the user
 * to enter an input string to test whether accepted or rejected by the TM.
 * 
 * @author MrMutart
 */
public class TmSimApp {
    
    private static String startState = null;
    private static String acceptState = null;
    private static String rejectState = null;
    private static ArrayList<String> states = new ArrayList<>();
    private static Tape tape;
    private static ArrayList<Character> inputAlphabet = new ArrayList<>();
    private static ArrayList<Transition> transitions = new ArrayList<>();

    /**
     * Handles user interaction, calls method within class to open and parse
     * a user selected file, uses parsed data to construct an instance of the
     * TuringMachine class, and calls on a method within that class to
     * test a user input string against the Turing Machine.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        boolean validSelection = false;
        
        // limit to text files only
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        
        JOptionPane.showMessageDialog(null, "Please select a text file "
              + "containing the Turing Machine\n\n", "Select a TM model",
              JOptionPane.INFORMATION_MESSAGE);
        
        // loop until file is selected or user indicates desire to exit program
        while (!validSelection) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                validSelection = true;
                File inFile = fileChooser.getSelectedFile();
                System.out.println("Opening " + inFile.getAbsolutePath());
                parseFile(inFile); // open and parse the file
            }
            else {
                // user did not select file, prompt to try again or exit
                int choice = JOptionPane.showConfirmDialog(null, "No File "
                      + "Selected - Try Again?\n\n", "ERROR - Try Again?",
                      JOptionPane.YES_NO_OPTION);
                
                // only check if No indicating desire to exit
                if (choice == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        }
        
        /**
         * Display TM input alphabet and prompt user for input string until user
         * decides to exit
         * 
         * Validates the string and then constructs a Turing Machine, checks
         * user string against Turing Machine, and prompts for a new string
         */
        while (true) {
            if (transitions.size() > 0) {
                int response = JOptionPane.showConfirmDialog(null, "Input "
                      + "alphabet for TM contains the characters:\n" 
                      + inputAlphabet.toString() + "\n\nWould you like to input"
                      + " a string for testing?\n\n", "TM Alphabet - Continue?", 
                      JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    boolean validInput = false;
                    while (!validInput) {
                        String inputString = JOptionPane.showInputDialog("Please"
                              + " enter a string using only alphabet characters "
                              + inputAlphabet.toString() + "\n\n");
                        if (inputString != null && inputString.trim().length() > 0) {
                            boolean allValidChars = true;

                            // check that all characters are in alphabet
                            for (int i = 0; i < inputString.length(); i++) {
                                if (!inputAlphabet.contains(inputString.charAt(i))) {
                                    JOptionPane.showMessageDialog(null,
                                          "One or more characters entered are "
                                                + "not in the TM alphabet."
                                                + "\n\nPlease try again.\n\n",
                                          "ERROR",JOptionPane.ERROR_MESSAGE);
                                    allValidChars = false;
                                    break; //exit loop so JOptionPane is only displayed once
                                }
                            }
                            // invalid chars, skip TM creation and prompt again
                            if (!allValidChars) continue;

                            validInput = true;
                            
                            // create Tape instance and then initialize
                            tape = new Tape();
                            tape.initialize(inputString);

                            // create TuringMachine and run with input string
                            TuringMachine tm = new TuringMachine(startState, 
                                  acceptState, rejectState, tape, transitions);

                            if (tm.accept()) {
                                // string accepted, prompt user success
                                JOptionPane.showMessageDialog(null, "Turing "
                                      + "Machine *ACCEPTED* user string '" 
                                      + inputString + "'\n\n", "Accepted",
                                      JOptionPane.PLAIN_MESSAGE);
                            }
                            else {
                                // string not accepted, prompt user failure
                                JOptionPane.showMessageDialog(null, "Turing "
                                      + "Machine did *NOT ACCEPT* user string '"
                                      + inputString + "'\n\n", "NOT Accepted",
                                      JOptionPane.PLAIN_MESSAGE);
                            }
                        }
                        else if (inputString == null) {
                            // user hit cancel button on input display
                            int exit = JOptionPane.showConfirmDialog(null, 
                                  "Do you want to exit?\n\n", "Exit",
                                  JOptionPane.YES_NO_OPTION);
                            if (exit == JOptionPane.YES_OPTION) {
                                System.exit(0);
                            }
                        }
                        else {
                            // user hit okay with empty string
                            JOptionPane.showMessageDialog(null, "No string "
                                  + "entered. Please try again.\n\n", "ERROR",
                                  JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                else {
                    System.exit(0);
                }
        
            }
        }
    }
    
    
    /**
     * Opens and parses the input file containing the Turing Machine model,
     * separates file data into specific TM parameters
     * 
     * @param file the user selected input file
     */
    public static void parseFile(File file) {
        ArrayList<String> fileLines = new ArrayList<>();
        String line;
        
        // attempt to open the file and scan the lines
        try {
            Scanner fileReader = new Scanner(file);
            
            while (fileReader.hasNextLine()) {
                line = fileReader.nextLine();
                fileLines.add(line);
            }

            fileReader.close(); // close the input file
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        
        /**
         * Begin to parse file data
         * 
         * Check that at least 5 (i.e. > 4) lines were read from file and saved,
         * 5 lines being the minimum to include starting state, accepted states,
         * rejected state, alphabet, and at least one transition
         */
        if (fileLines.size() > 4) {
            // per file constraints, lines 0-3 are startState, acceptStates,
            // rejectState, and inputAlphabet
            startState = fileLines.get(0).trim();
            acceptState = fileLines.get(1).trim();
            rejectState = fileLines.get(2).trim();
            String[] alphaValues = fileLines.get(3).trim().split(",");
            for (String s : alphaValues) {
                inputAlphabet.add(s.trim().charAt(0));
            }
            
            String[] pieces;
            for (int i = 4; i < fileLines.size(); i++) {
                line = fileLines.get(i).trim();
                String fromState = line.substring(0, line.indexOf("(")).trim();
                String toState = line.substring(line.indexOf(")") + 1, line.length()).trim();
                String symbols = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                pieces = symbols.split(",");
                
                char read, write;
                if (pieces[0].equals(" ")) {
                    read = ' ';
                }
                else {
                    read = pieces[0].charAt(0);
                }
                
                if (pieces[1].equals(" ")) {
                    write = ' ';
                }
                else {
                    write = pieces[1].charAt(0); 
                }               
                char direction = pieces[2].charAt(0);
                      
                // add fromState to states if not already existing
                if (!states.contains(fromState)) {
                    states.add(fromState);
                }
                
                // add toState to states if not already existing
                if (!states.contains(toState)) {
                    states.add(toState);
                }
                
                // create new transition object and add to transitions list
                Transition transition = new Transition(fromState, read,
                      write, direction, toState);
                transitions.add(transition);
            }
            
            /*********************************************************
             * * *
             * * *        ***FOR DEBUGGING PURPOSES***
             * * *
             *********************************************************/
            System.out.println("Starting state: " + startState + "\n");
            System.out.println("Accept State: " + acceptState + "\n");
            System.out.println("Reject State: " + rejectState + "\n");
            System.out.println("All States: " + states.toString() + "\n");
            for (int j = 0; j < transitions.size(); j++) {
                System.out.println("Transition " + (j+1) + ": " 
                      + transitions.get(j).toString());
            }
        }
    }
}
