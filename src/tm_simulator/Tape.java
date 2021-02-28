package tm_simulator;

import java.util.*;

/**
 * Class representing the tape of a Turing machine
 * 
 * @author MrMutart
 */
public class Tape {
    private ArrayList<Character> cells;
    private int headPosition;

    /**
     * Default Constructor
     * 
     */
    public Tape() {
        cells = new ArrayList<>();
        headPosition = 0;
    }

    
    /* Getters and Setters */
    
    public ArrayList<Character> getCells() {
        return cells;
    }

    public void setCells(ArrayList<Character> cells) {
        this.cells = cells;
    }

    public int getHeadPosition() {
        return headPosition;
    }

    public void setHeadPosition(int headPosition) {
        this.headPosition = headPosition;
    }
    
    /**
     * Function to initialize each cell in the tape ArrayList
     * based on input String
     * 
     * @param input the user String
     */
    public void initialize(String input) {
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            cells.add(c);
        }
    }
}
