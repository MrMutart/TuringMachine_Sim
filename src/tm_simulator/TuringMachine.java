package tm_simulator;

import java.util.*;

/**
 * Constructs a TM model based on file data parsed in the main method and passed
 * in via the constructor. Runs a user string through the TM and returns true
 * if accepted by the TM, and false otherwise
 * 
 * @author MrMutart
 */
public class TuringMachine {
    private final String startState;
    private final String acceptState;
    private final String rejectState;
    private final Tape tape;
    private final ArrayList<Transition> transitions;

    
    /**
     * Parameterized Constructor
     * 
     * @param startState the starting state
     * @param acceptState the accepting state
     * @param rejectState the rejected state
     * @param tape the tape containing the input stream
     * @param transitions the defined transitions per input file
     */
    public TuringMachine(String startState, String acceptState, 
          String rejectState, Tape tape, ArrayList<Transition> transitions) {
        this.startState = startState;
        this.acceptState = acceptState;
        this.rejectState = rejectState;
        this.tape = tape;
        this.transitions = transitions;
    }
    
    
    /**
     * Runs the TM testing a user input string and iterating through the string
     * following all defined transitions and moving the TM head as necessary.
     * Returns true if the input ends and the state is an accepted state, and
     * false otherwise.
     * 
     * @return 
     */
    public boolean accept() {
        String state = startState;
        boolean accepted = state.equals(acceptState);
        
        while (!accepted) {
            char currChar = tape.getCells().get(tape.getHeadPosition());

            boolean found = false;
            int j = 0;
            
            // find transition with matching startState and input char
            while ((!found) && (j < transitions.size())) {
                Transition t = transitions.get(j);
                if ((t.getFromState().equals(state)) && (t.getRead() == currChar)) {
                    
                    // found a match so write new character
                    tape.getCells().set(tape.getHeadPosition(), t.getWrite());
                    
                    // now change head position (++ if right, -- if left)
                    int pos = tape.getHeadPosition();
                    switch (t.getDirection()) {
                        case 'R':
                            tape.setHeadPosition(++pos);
                            break;
                        case 'L':
                            tape.setHeadPosition(--pos);
                            break;
                        default:
                            // error, TM not properly defined
                            return false;
                    }
                    // head moved past last char to blank, so add blank
                    // to avoid index out of bounds
                    if (tape.getHeadPosition() == tape.getCells().size()) {
                        tape.getCells().add(' ');
                    }
                    
                    // found transition, so move to new state
                    if ((state = t.getToState()).equals(rejectState)) {
                        // is reject state, so no accepted
                        return false;
                    }
                    // transition found and not reject state, so exit loop
                    found = true;
                }
                j++;
            }
            accepted = state.equals(acceptState);
            if (!found) {
                // no defined transition so not accepted, error in TM
                return false;
            }
            
        }
        return accepted;
    }
}
